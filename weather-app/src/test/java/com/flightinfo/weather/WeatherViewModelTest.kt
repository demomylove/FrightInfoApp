package com.flightinfo.weather

import com.flightinfo.weather.data.*
import com.flightinfo.weather.ui.WeatherViewModel
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val clock = MutableClock(Instant.parse("2026-12-31T15:59:00Z"))
    private val a = City(1, "北京", emptyList(), 39.9, 116.4, null)
    private val b = a.copy(id = 2, name = "上海")
    private val repository = FakeRepository()
    private lateinit var model: WeatherViewModel
    @Before fun setup() { Dispatchers.setMain(dispatcher); model = WeatherViewModel(repository, clock) }
    @After fun cleanup() { model.onBackground(); Dispatchers.resetMain() }
    private fun snapshot(city: City, partial: Boolean = false) = WeatherSnapshot(city, clock.instant(), today(clock),
        today(clock).atTime(23, 45), if (partial) null else 10.0, 0,
        targetDates(today(clock)).map { DayWeather(it, 0, 20.0, 5.0) }, null, null)

    @Test fun blankNeverCallsNetworkAndInvalidSearchCanRecover() = runTest(dispatcher) {
        model.search("  "); runCurrent(); assertEquals(0, repository.searches.size)
        model.search("不存在"); runCurrent(); repository.searches[0].second.complete(emptyList()); runCurrent()
        assertTrue(model.state.value.searchMessage.contains("未找到"))
        model.search("北京"); runCurrent(); repository.searches[1].second.complete(listOf(a)); runCurrent()
        assertEquals(listOf(a), model.state.value.cities)
    }
    @Test fun obsoleteSearchCannotReplaceNewCandidates() = runTest(dispatcher) {
        model.search("北京"); runCurrent(); model.search("上海"); runCurrent()
        repository.searches[1].second.complete(listOf(b)); runCurrent()
        repository.searches[0].second.complete(listOf(a)); runCurrent()
        assertEquals(listOf(b), model.state.value.cities)
    }
    @Test fun cityChangeClearsOldSnapshotAndLateSuccessCannotOverwriteB() = runTest(dispatcher) {
        model.select(a); runCurrent(); repository.loads[0].second.complete(snapshot(a)); runCurrent()
        model.refresh(); runCurrent(); model.select(b); runCurrent()
        assertNull(model.state.value.snapshot)
        repository.loads[2].second.complete(snapshot(b)); runCurrent()
        repository.loads[1].second.complete(snapshot(a)); runCurrent()
        assertEquals(b, model.state.value.snapshot!!.city)
        assertFalse(model.state.value.loading)
    }
    @Test fun obsoleteFailureAndFinallyCannotEndNewLoading() = runTest(dispatcher) {
        model.select(a); runCurrent(); model.select(b); runCurrent()
        repository.loads[0].second.completeExceptionally(WeatherFailure(WeatherFailure.Kind.NETWORK)); runCurrent()
        assertTrue(model.state.value.loading)
        assertNull(model.state.value.weatherError)
        repository.loads[1].second.complete(snapshot(b)); runCurrent()
        assertEquals(b, model.state.value.snapshot!!.city)
    }
    @Test fun refreshIsNewRequestFailureRetainsStaleThenPartialReplacesWholeSnapshot() = runTest(dispatcher) {
        model.select(a); runCurrent(); repository.loads[0].second.complete(snapshot(a)); runCurrent()
        model.refresh(); model.refresh(); runCurrent(); assertEquals(2, repository.loads.size)
        repository.loads[1].second.completeExceptionally(WeatherFailure(WeatherFailure.Kind.RATE_LIMIT)); runCurrent()
        assertTrue(model.state.value.stale); assertFalse(model.state.value.loading)
        model.refresh(); runCurrent(); repository.loads[2].second.complete(snapshot(a, partial = true)); runCurrent()
        assertNull(model.state.value.snapshot!!.temperature)
        assertFalse(model.state.value.snapshot!!.complete)
        assertFalse(model.state.value.stale)
        model.refresh(); runCurrent(); repository.loads[3].second.complete(snapshot(a)); runCurrent()
        assertTrue(model.state.value.snapshot!!.complete)
    }
    @Test fun requestAcrossMidnightIsDiscardedAndOnlyOneReplacementStarts() = runTest(dispatcher) {
        model.select(a); runCurrent()
        clock.now = Instant.parse("2026-12-31T16:01:00Z")
        repository.loads[0].second.complete(snapshot(a)); runCurrent()
        model.checkDate(); model.checkDate(); runCurrent()
        assertEquals(2, repository.loads.size)
        assertNull(model.state.value.snapshot)
        repository.loads[1].second.complete(snapshot(a)); runCurrent()
        assertEquals("2027-01-02", model.state.value.snapshot!!.days.first().date.toString())
    }
    @Test fun foregroundMidnightAndBackgroundResumeFilterOldDatesAndDeduplicate() = runTest(dispatcher) {
        model.select(a); runCurrent(); repository.loads[0].second.complete(snapshot(a)); runCurrent()
        model.onForeground(); runCurrent()
        clock.now = Instant.parse("2026-12-31T16:00:00Z")
        advanceTimeBy(60000); runCurrent()
        assertEquals(2, repository.loads.size)
        assertEquals(6, model.state.value.snapshot!!.days.size)
        repository.loads[1].second.complete(snapshot(a)); runCurrent()
        model.onBackground()
        clock.now = Instant.parse("2027-01-01T16:00:00Z")
        model.onForeground(); runCurrent(); model.checkDate(); runCurrent()
        assertEquals(3, repository.loads.size)
        repository.loads[2].second.completeExceptionally(WeatherFailure(WeatherFailure.Kind.NETWORK)); runCurrent()
        model.checkDate(); runCurrent(); assertEquals(3, repository.loads.size)
        model.onBackground()
    }
    @Test fun searchAndInitialWeatherErrorsRecover() = runTest(dispatcher) {
        model.search("北京"); runCurrent()
        repository.searches[0].second.completeExceptionally(WeatherFailure(WeatherFailure.Kind.SERVICE)); runCurrent()
        assertTrue(model.state.value.searchFailed); assertFalse(model.state.value.searching)
        model.search("北京"); runCurrent(); repository.searches[1].second.complete(listOf(a)); runCurrent()
        model.select(a); runCurrent(); repository.loads[0].second.completeExceptionally(WeatherFailure(WeatherFailure.Kind.NETWORK)); runCurrent()
        assertFalse(model.state.value.loading); assertNull(model.state.value.snapshot)
        model.refresh(); runCurrent(); repository.loads[1].second.complete(snapshot(a)); runCurrent()
        assertTrue(model.state.value.snapshot!!.complete)
    }
    private class FakeRepository : WeatherRepository {
        val loads = mutableListOf<Pair<City, CompletableDeferred<WeatherSnapshot>>>()
        val searches = mutableListOf<Pair<String, CompletableDeferred<List<City>>>>()
        // Deliberately ignores cancellation: the generation guard must still protect the UI.
        override suspend fun load(city: City): WeatherSnapshot {
            val result = CompletableDeferred<WeatherSnapshot>(); loads += city to result
            return withContext(NonCancellable) { result.await() }
        }
        override suspend fun search(query: String): List<City> {
            val result = CompletableDeferred<List<City>>(); searches += query to result
            return withContext(NonCancellable) { result.await() }
        }
    }
    private class MutableClock(var now: Instant) : Clock() {
        override fun instant() = now
        override fun getZone(): ZoneId = ZoneId.of("America/New_York")
        override fun withZone(zone: ZoneId): Clock = object : Clock() {
            override fun instant() = now
            override fun getZone() = zone
            override fun withZone(other: ZoneId) = this@MutableClock.withZone(other)
        }
    }
}
