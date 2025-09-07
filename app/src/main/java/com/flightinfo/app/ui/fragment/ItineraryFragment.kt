package com.flightinfo.app.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flightinfo.app.R
import com.flightinfo.app.ui.viewmodel.ItineraryViewModel
import com.flightinfo.app.utils.CalendarSyncManager
import com.flightinfo.app.utils.DeepLinkHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItineraryFragment : Fragment() {

    private val viewModel: ItineraryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_itinerary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.createSampleItineraryIfEmpty(System.currentTimeMillis())

        val timeline = view.findViewById<LinearLayout>(R.id.timelineContainer)
        val syncBtn = view.findViewById<Button>(R.id.syncCalendarButton)

        viewLifecycleOwner.lifecycle.addObserver(object : androidx.lifecycle.DefaultLifecycleObserver {})

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.itineraries.collect { list ->
                timeline.removeAllViews()
                val inflater = LayoutInflater.from(requireContext())
                list.forEach { iti ->
                    val item = inflater.inflate(R.layout.item_timeline, timeline, false)
                    item.findViewById<TextView>(R.id.titleText).text = iti.title
                    item.findViewById<TextView>(R.id.timeText).text =
                        "${formatTime(iti.startTime)} - ${formatTime(iti.endTime)}"

                    item.findViewById<Button>(R.id.checkInButton).setOnClickListener {
                        val ok = DeepLinkHelper.openCheckIn(requireContext(), iti.checkInUrl)
                        if (!ok) Toast.makeText(requireContext(), "无法打开值机链接", Toast.LENGTH_SHORT).show()
                    }
                    item.findViewById<Button>(R.id.boardingPassButton).setOnClickListener {
                        val ok = DeepLinkHelper.openBoardingPass(requireContext(), iti.boardingPassUrl)
                        if (!ok) Toast.makeText(requireContext(), "无法打开登机牌链接", Toast.LENGTH_SHORT).show()
                    }
                    timeline.addView(item)
                }
            }
        }

        syncBtn.setOnClickListener {
            ensureCalendarPermissions {
                val mgr = CalendarSyncManager(requireContext())
                val current = viewModel.itineraries.value.firstOrNull()
                val ok = current?.let { mgr.syncItinerary(it) } ?: false
                Toast.makeText(requireContext(), if (ok) "已同步到日历" else "同步失败或无权限", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ensureCalendarPermissions(onGranted: () -> Unit) {
        val needed = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        val notGranted = needed.any {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }
        if (notGranted) {
            requestPermissions(needed, REQ_CAL)
        } else {
            onGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CAL) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                view?.findViewById<Button>(R.id.syncCalendarButton)?.performClick()
            } else {
                Toast.makeText(requireContext(), "需要日历权限以同步", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun formatTime(ms: Long): String {
        val fmt = java.text.SimpleDateFormat("MM-dd HH:mm", java.util.Locale.getDefault())
        return fmt.format(java.util.Date(ms))
    }

    companion object {
        private const val REQ_CAL = 2001
    }
}
