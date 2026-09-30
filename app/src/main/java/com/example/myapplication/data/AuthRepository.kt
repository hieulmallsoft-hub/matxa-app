package com.example.myapplication.data

import android.content.Context
import android.provider.Settings
import com.example.myapplication.data.remote.ApiClient
import com.example.myapplication.data.remote.AuthResponse
import com.example.myapplication.data.remote.CompleteRegistrationRequest
import com.example.myapplication.data.remote.EmailLoginRequest
import com.example.myapplication.data.remote.SendEmailOtpRequest
import com.example.myapplication.data.remote.VerifyRegistrationOtpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthRepository(private val context: Context) {
    private val deviceId: String
        get() = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown-android-device"

    suspend fun sendRegistrationOtp(email: String) = request {
        ApiClient.authApi.sendRegistrationOtp(SendEmailOtpRequest(email, deviceId))
    }

    suspend fun verifyRegistrationOtp(sessionId: String, code: String) = request {
        ApiClient.authApi.verifyRegistrationOtp(
            VerifyRegistrationOtpRequest(sessionId, code, deviceId)
        )
    }

    suspend fun completeRegistration(sessionId: String, password: String) = request {
        ApiClient.authApi.completeRegistration(
            CompleteRegistrationRequest(sessionId, password, deviceId)
        ).also(::saveSession)
    }

    suspend fun login(email: String, password: String) = request {
        ApiClient.authApi.loginWithEmail(EmailLoginRequest(email, password, deviceId)).also(::saveSession)
    }

    private suspend fun <T> request(block: suspend () -> T): Result<T> = withContext(Dispatchers.IO) {
        runCatching { block() }.recoverCatching { throwable ->
            throw IllegalStateException(
                when (throwable) {
                    is HttpException -> "Yêu cầu không thành công (${throwable.code()}). Kiểm tra lại thông tin."
                    else -> "Không thể kết nối máy chủ. Vui lòng thử lại."
                }
            )
        }
    }

    private fun saveSession(response: AuthResponse) {
        context.getSharedPreferences("auth_session", Context.MODE_PRIVATE).edit()
            .putString("access_token", response.accessToken)
            .putString("refresh_token", response.refreshToken)
            .apply()
    }
}
