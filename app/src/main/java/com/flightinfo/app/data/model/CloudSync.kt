package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

data class SyncMetadata(
    val version: Long = System.currentTimeMillis(),
    val deviceId: String,
    val lastSyncTime: Long = System.currentTimeMillis(),
    val checksum: String? = null,
)

@Entity(tableName = "sync_records")
@TypeConverters(SyncRecordConverters::class)
data class SyncRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val dataType: SyncDataType,
    val entityId: String,
    val action: SyncAction,
    val data: String,
    val metadata: SyncMetadata,
    val status: SyncStatus = SyncStatus.PENDING,
    val retryCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val syncedAt: Long? = null,
)

enum class SyncDataType {
    ITINERARY,
    BAGGAGE,
    TRACKED_FLIGHT,
    BOOKMARKED_FLIGHT,
    USER_PROFILE,
    FAMILY_MEMBER,
    SHARED_ITINERARY,
}

enum class SyncAction {
    CREATE,
    UPDATE,
    DELETE,
    SHARE,
    UNSHARE,
}

enum class SyncStatus {
    PENDING,
    SYNCING,
    SUCCESS,
    FAILED,
    CONFLICT,
}

@Entity(tableName = "family_accounts")
data class FamilyAccount(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val ownerId: String,
    val inviteCode: String = UUID.randomUUID().toString().take(8).uppercase(),
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true,
)

@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val familyAccountId: String,
    val userId: String,
    val userEmail: String,
    val userName: String,
    val role: FamilyRole = FamilyRole.MEMBER,
    val joinedAt: Long = System.currentTimeMillis(),
    val permissions: List<FamilyPermission> = listOf(),
    val isActive: Boolean = true,
)

enum class FamilyRole {
    OWNER,
    ADMIN,
    MEMBER,
}

enum class FamilyPermission {
    VIEW_ITINERARIES,
    EDIT_ITINERARIES,
    SHARE_ITINERARIES,
    MANAGE_MEMBERS,
    VIEW_BAGGAGE,
    EDIT_BAGGAGE,
}

@Entity(tableName = "shared_itineraries")
data class SharedItinerary(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val itineraryId: String,
    val sharedBy: String,
    val sharedWith: String,
    val familyAccountId: String?,
    val permissions: List<SharePermission> = listOf(),
    val sharedAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null,
    val isActive: Boolean = true,
)

enum class SharePermission {
    VIEW,
    EDIT,
    COMMENT,
    DOWNLOAD,
}

data class DeviceInfo(
    val deviceId: String,
    val deviceName: String,
    val platform: String = "Android",
    val appVersion: String,
    val lastSyncTime: Long,
    val isActive: Boolean = true,
)

data class SyncConflict(
    val id: String = UUID.randomUUID().toString(),
    val entityId: String,
    val dataType: SyncDataType,
    val localData: String,
    val remoteData: String,
    val localMetadata: SyncMetadata,
    val remoteMetadata: SyncMetadata,
    val detectedAt: Long = System.currentTimeMillis(),
    val resolved: Boolean = false,
)

enum class ConflictResolution {
    KEEP_LOCAL,
    KEEP_REMOTE,
    MERGE,
    MANUAL,
}

class SyncRecordConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromSyncMetadata(metadata: SyncMetadata): String {
        return gson.toJson(metadata)
    }

    @TypeConverter
    fun toSyncMetadata(metadataString: String): SyncMetadata {
        return gson.fromJson(metadataString, SyncMetadata::class.java)
    }

    @TypeConverter
    fun fromPermissionList(permissions: List<FamilyPermission>): String {
        return gson.toJson(permissions)
    }

    @TypeConverter
    fun toPermissionList(permissionsString: String): List<FamilyPermission> {
        val listType = object : TypeToken<List<FamilyPermission>>() {}.type
        return gson.fromJson(permissionsString, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromSharePermissionList(permissions: List<SharePermission>): String {
        return gson.toJson(permissions)
    }

    @TypeConverter
    fun toSharePermissionList(permissionsString: String): List<SharePermission> {
        val listType = object : TypeToken<List<SharePermission>>() {}.type
        return gson.fromJson(permissionsString, listType) ?: emptyList()
    }
}
