package com.example.myapplication.ui.screen.auth

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CredentialUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class EmailLoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application.applicationContext)
    private val _uiState = MutableStateFlow(CredentialUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(value: String) = _uiState.update { it.copy(email = value, error = null) }
    fun updatePassword(value: String) = _uiState.update { it.copy(password = value, error = null) }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Nhập email và mật khẩu hợp lệ.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.login(state.email.trim(), state.password)
                .onSuccess { onSuccess() }
                .onFailure { error -> _uiState.update { it.copy(error = error.message) } }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}

class CreatePasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application.applicationContext)
    private val _uiState = MutableStateFlow(CredentialUiState())
    val uiState = _uiState.asStateFlow()

    fun updatePassword(value: String) = _uiState.update { it.copy(password = value, error = null) }

    fun completeRegistration(sessionId: String, onSuccess: () -> Unit) {
        val password = _uiState.value.password
        if (password.length < 8) {
            _uiState.update { it.copy(error = "Mật khẩu cần ít nhất 8 ký tự.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.completeRegistration(sessionId, password)
                .onSuccess { onSuccess() }
                .onFailure { error -> _uiState.update { it.copy(error = error.message) } }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
