package com.example.myapplication.ui.screen.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OtpUiState(
    val otp: String = "",
    val secondsRemaining: Int = 45,
    val isLoading: Boolean = false,
    val error: String? = null
)

class OtpViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application.applicationContext)
    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState = _uiState.asStateFlow()
    private var countdownJob: Job? = null
    private var email = ""
    private var sessionId = ""

    init {
        startCountdown()
    }

    fun updateOtp(value: String) {
        if (value.length <= 6 && value.all { it.isLetterOrDigit() }) {
            _uiState.update { it.copy(otp = value.uppercase(), error = null) }
        }
    }

    fun initialize(email: String, sessionId: String) {
        if (this.sessionId.isNotBlank()) return
        this.email = email
        this.sessionId = sessionId
    }

    fun verifyOtp(onVerified: (String) -> Unit) {
        val code = _uiState.value.otp
        if (code.length != 6 || sessionId.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.verifyRegistrationOtp(sessionId, code)
                .onSuccess { response ->
                    if (response.verified) onVerified(sessionId)
                    else _uiState.update { it.copy(error = "Mã OTP chưa được xác minh.") }
                }
                .onFailure { error -> _uiState.update { it.copy(error = error.message) } }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun resend() {
        if (email.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.sendRegistrationOtp(email)
                .onSuccess { response ->
                    sessionId = response.registrationSessionId
                    _uiState.update { it.copy(otp = "", secondsRemaining = 45) }
                    startCountdown()
                }
                .onFailure { error -> _uiState.update { it.copy(error = error.message) } }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (_uiState.value.secondsRemaining > 0) {
                delay(1_000)
                _uiState.update { it.copy(secondsRemaining = (it.secondsRemaining - 1).coerceAtLeast(0)) }
            }
        }
    }
}
