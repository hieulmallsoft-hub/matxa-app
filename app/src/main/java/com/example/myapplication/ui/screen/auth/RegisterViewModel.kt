package com.example.myapplication.ui.screen.auth

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegisterUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application.applicationContext)
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun sendOtp(onSuccess: (String) -> Unit) {
        val email = _uiState.value.email.trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(error = "Vui lòng nhập email hợp lệ.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.sendRegistrationOtp(email)
                .onSuccess { response -> onSuccess(response.registrationSessionId) }
                .onFailure { error -> _uiState.update { it.copy(error = error.message) } }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
