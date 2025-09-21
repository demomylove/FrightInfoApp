package com.flightinfo.app.api

import com.flightinfo.app.data.model.ConflictResolution
import com.flightinfo.app.data.model.DeviceInfo
import com.flightinfo.app.data.model.FamilyAccount
import com.flightinfo.app.data.model.FamilyMember
import com.flightinfo.app.data.model.Itinerary
import com.flightinfo.app.data.model.SharePermission
import com.flightinfo.app.data.model.SharedItinerary
import com.flightinfo.app.data.model.SyncConflict
import com.flightinfo.app.data.model.SyncDataType
import com.flightinfo.app.data.model.SyncMetadata
import com.flightinfo.app.data.model.SyncRecord
import com.flightinfo.app.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CloudSyncApi {

    // 用户认证和设备管理
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshRequest: RefreshTokenRequest): Response<AuthResponse>

    @GET("devices")
    suspend fun getDevices(): Response<List<DeviceInfo>>

    @POST("devices")
    suspend fun registerDevice(@Body deviceInfo: DeviceInfo): Response<DeviceInfo>

    @DELETE("devices/{deviceId}")
    suspend fun unregisterDevice(@Path("deviceId") deviceId: String): Response<Unit>

    // 数据同步
    @GET("sync/metadata")
    suspend fun getSyncMetadata(@Query("dataTypes") dataTypes: List<SyncDataType>): Response<Map<String, SyncMetadata>>

    @POST("sync/upload")
    suspend fun uploadSyncRecords(@Body records: List<SyncRecord>): Response<SyncUploadResponse>

    @GET("sync/download")
    suspend fun downloadSyncRecords(
        @Query("since") since: Long,
        @Query("dataTypes") dataTypes: List<SyncDataType>,
    ): Response<List<SyncRecord>>

    @POST("sync/resolve-conflict")
    suspend fun resolveConflict(@Body resolution: ConflictResolutionRequest): Response<Unit>

    // 家庭账户管理
    @POST("family/create")
    suspend fun createFamilyAccount(@Body request: CreateFamilyRequest): Response<FamilyAccount>

    @POST("family/join")
    suspend fun joinFamilyAccount(@Body request: JoinFamilyRequest): Response<FamilyMember>

    @GET("family/{familyId}")
    suspend fun getFamilyAccount(@Path("familyId") familyId: String): Response<FamilyAccountDetail>

    @GET("family/{familyId}/members")
    suspend fun getFamilyMembers(@Path("familyId") familyId: String): Response<List<FamilyMember>>

    @PUT("family/{familyId}/members/{memberId}")
    suspend fun updateFamilyMember(
        @Path("familyId") familyId: String,
        @Path("memberId") memberId: String,
        @Body member: FamilyMember,
    ): Response<FamilyMember>

    @DELETE("family/{familyId}/members/{memberId}")
    suspend fun removeFamilyMember(
        @Path("familyId") familyId: String,
        @Path("memberId") memberId: String,
    ): Response<Unit>

    @DELETE("family/{familyId}/leave")
    suspend fun leaveFamilyAccount(@Path("familyId") familyId: String): Response<Unit>

    // 行程共享
    @POST("share/itinerary")
    suspend fun shareItinerary(@Body request: ShareItineraryRequest): Response<SharedItinerary>

    @GET("share/itineraries")
    suspend fun getSharedItineraries(): Response<List<SharedItinerary>>

    @PUT("share/itineraries/{shareId}")
    suspend fun updateSharedItinerary(
        @Path("shareId") shareId: String,
        @Body request: UpdateShareRequest,
    ): Response<SharedItinerary>

    @DELETE("share/itineraries/{shareId}")
    suspend fun unshareItinerary(@Path("shareId") shareId: String): Response<Unit>

    @GET("share/itineraries/{shareId}/data")
    suspend fun getSharedItineraryData(@Path("shareId") shareId: String): Response<Itinerary>
}

data class LoginRequest(
    val email: String,
    val password: String,
    val deviceId: String,
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val deviceId: String,
)

data class RefreshTokenRequest(
    val refreshToken: String,
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User,
    val expiresIn: Long,
)

data class SyncUploadResponse(
    val successCount: Int,
    val failedRecords: List<String>,
    val conflicts: List<SyncConflict>,
)

data class ConflictResolutionRequest(
    val conflictId: String,
    val resolution: ConflictResolution,
    val resolvedData: String?,
)

data class CreateFamilyRequest(
    val name: String,
    val description: String? = null,
)

data class JoinFamilyRequest(
    val inviteCode: String,
    val name: String,
)

data class FamilyAccountDetail(
    val account: FamilyAccount,
    val members: List<FamilyMember>,
    val sharedItineraries: List<SharedItinerary>,
)

data class ShareItineraryRequest(
    val itineraryId: String,
    val shareWith: List<String>? = null,
    val familyAccountId: String? = null,
    val permissions: List<SharePermission>,
    val expiresAt: Long? = null,
    val message: String? = null,
)

data class UpdateShareRequest(
    val permissions: List<SharePermission>,
    val expiresAt: Long? = null,
    val isActive: Boolean,
)
