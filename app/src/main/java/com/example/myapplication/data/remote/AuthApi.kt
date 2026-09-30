package com.example.myapplication.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class SendEmailOtpRequest(
    val email: String,
    val deviceId: String
)

data class SendEmailOtpResponse(
    val registrationSessionId: String,
    val expiresIn: Double,
    val debugOtp: String? = null
)

data class VerifyRegistrationOtpRequest(
    val registrationSessionId: String,
    val code: String,
    val deviceId: String
)

data class VerifyRegistrationOtpResponse(
    val verified: Boolean,
    val expiresIn: Double
)

data class CompleteRegistrationRequest(
    val registrationSessionId: String,
    val password: String,
    val deviceId: String
)

data class EmailLoginRequest(
    val email: String,
    val password: String,
    val deviceId: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: String
)

interface AuthApi {
    @POST("api/auth/register/start")
    suspend fun sendRegistrationOtp(@Body request: SendEmailOtpRequest): SendEmailOtpResponse

    @POST("api/auth/register/verify-otp")
    suspend fun verifyRegistrationOtp(@Body request: VerifyRegistrationOtpRequest): VerifyRegistrationOtpResponse

    @POST("api/auth/register/complete")
    suspend fun completeRegistration(@Body request: CompleteRegistrationRequest): AuthResponse

    @POST("api/auth/email/login")
    suspend fun loginWithEmail(@Body request: EmailLoginRequest): AuthResponse
}
