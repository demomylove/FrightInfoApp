package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Update
import com.flightinfo.app.data.model.FamilyAccount
import com.flightinfo.app.data.model.FamilyMember
import com.flightinfo.app.data.model.Itinerary
import com.flightinfo.app.data.model.SharedItinerary
import com.flightinfo.app.data.model.SyncDataType
import com.flightinfo.app.data.model.SyncRecord
import com.flightinfo.app.data.model.SyncStatus
import com.flightinfo.app.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncRecordDao {
    @Query("SELECT * FROM sync_records WHERE status = :status ORDER BY createdAt ASC")
    suspend fun getPendingSyncRecords(status: SyncStatus = SyncStatus.PENDING): List<SyncRecord>

    @Query("SELECT * FROM sync_records WHERE dataType = :dataType AND entityId = :entityId")
    suspend fun getSyncRecordsByEntity(dataType: SyncDataType, entityId: String): List<SyncRecord>

    @Query("SELECT * FROM sync_records WHERE status = :status")
    fun getSyncRecordsByStatusFlow(status: SyncStatus): Flow<List<SyncRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncRecord(record: SyncRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncRecords(records: List<SyncRecord>)

    @Update
    suspend fun updateSyncRecord(record: SyncRecord)

    @Delete
    suspend fun deleteSyncRecord(record: SyncRecord)

    @Query("DELETE FROM sync_records WHERE status = :status AND createdAt < :before")
    suspend fun deleteOldSyncRecords(status: SyncStatus, before: Long)

    @Query("UPDATE sync_records SET status = :newStatus WHERE id = :recordId")
    suspend fun updateSyncRecordStatus(recordId: String, newStatus: SyncStatus)
}

@Dao
interface FamilyAccountDao {
    @Query("SELECT * FROM family_accounts WHERE isActive = 1")
    suspend fun getAllActiveFamilyAccounts(): List<FamilyAccount>

    @Query("SELECT * FROM family_accounts WHERE id = :id")
    suspend fun getFamilyAccountById(id: String): FamilyAccount?

    @Query("SELECT * FROM family_accounts WHERE ownerId = :ownerId AND isActive = 1")
    suspend fun getFamilyAccountsByOwner(ownerId: String): List<FamilyAccount>

    @Query("SELECT * FROM family_accounts WHERE inviteCode = :inviteCode AND isActive = 1")
    suspend fun getFamilyAccountByInviteCode(inviteCode: String): FamilyAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyAccount(account: FamilyAccount)

    @Update
    suspend fun updateFamilyAccount(account: FamilyAccount)

    @Query("UPDATE family_accounts SET isActive = 0 WHERE id = :id")
    suspend fun deactivateFamilyAccount(id: String)

    @Delete
    suspend fun deleteFamilyAccount(account: FamilyAccount)
}

@Dao
interface FamilyMemberDao {
    @Query("SELECT * FROM family_members WHERE familyAccountId = :familyId AND isActive = 1")
    suspend fun getFamilyMembers(familyId: String): List<FamilyMember>

    @Query("SELECT * FROM family_members WHERE userId = :userId AND isActive = 1")
    suspend fun getFamilyMembershipsByUser(userId: String): List<FamilyMember>

    @Query("SELECT * FROM family_members WHERE id = :id")
    suspend fun getFamilyMemberById(id: String): FamilyMember?

    @Query("SELECT * FROM family_members WHERE familyAccountId = :familyId AND userId = :userId")
    suspend fun getFamilyMember(familyId: String, userId: String): FamilyMember?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMember(member: FamilyMember)

    @Update
    suspend fun updateFamilyMember(member: FamilyMember)

    @Query("UPDATE family_members SET isActive = 0 WHERE id = :id")
    suspend fun deactivateFamilyMember(id: String)

    @Delete
    suspend fun deleteFamilyMember(member: FamilyMember)

    @Query("DELETE FROM family_members WHERE familyAccountId = :familyId AND userId = :userId")
    suspend fun removeFamilyMember(familyId: String, userId: String)
}

@Dao
interface SharedItineraryDao {
    @Query("SELECT * FROM shared_itineraries WHERE sharedWith = :userId AND isActive = 1")
    suspend fun getSharedItinerariesForUser(userId: String): List<SharedItinerary>

    @Query("SELECT * FROM shared_itineraries WHERE sharedBy = :userId AND isActive = 1")
    suspend fun getItinerariesSharedByUser(userId: String): List<SharedItinerary>

    @Query("SELECT * FROM shared_itineraries WHERE familyAccountId = :familyId AND isActive = 1")
    suspend fun getSharedItinerariesForFamily(familyId: String): List<SharedItinerary>

    @Query("SELECT * FROM shared_itineraries WHERE itineraryId = :itineraryId AND isActive = 1")
    suspend fun getSharesForItinerary(itineraryId: String): List<SharedItinerary>

    @Query("SELECT * FROM shared_itineraries WHERE id = :id")
    suspend fun getSharedItineraryById(id: String): SharedItinerary?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSharedItinerary(sharedItinerary: SharedItinerary)

    @Update
    suspend fun updateSharedItinerary(sharedItinerary: SharedItinerary)

    @Query("UPDATE shared_itineraries SET isActive = 0 WHERE id = :id")
    suspend fun deactivateSharedItinerary(id: String)

    @Delete
    suspend fun deleteSharedItinerary(sharedItinerary: SharedItinerary)

    @Query("DELETE FROM shared_itineraries WHERE itineraryId = :itineraryId")
    suspend fun deleteAllSharesForItinerary(itineraryId: String)

    @Query("DELETE FROM shared_itineraries WHERE expiresAt < :now AND expiresAt IS NOT NULL")
    suspend fun deleteExpiredShares(now: Long = System.currentTimeMillis())
}

data class FamilyAccountWithMembers(
    @Embedded val account: FamilyAccount,
    @Relation(
        parentColumn = "id",
        entityColumn = "familyAccountId",
    )
    val members: List<FamilyMember>,
)

data class SharedItineraryWithDetails(
    @Embedded val sharedItinerary: SharedItinerary,
    @Relation(
        parentColumn = "itineraryId",
        entityColumn = "id",
    )
    val itinerary: Itinerary?,
    @Relation(
        parentColumn = "sharedBy",
        entityColumn = "id",
    )
    val sharedByUser: User?,
)
