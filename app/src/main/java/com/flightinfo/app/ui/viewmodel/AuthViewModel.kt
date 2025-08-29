package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.User
import com.flightinfo.app.repository.UserRepository
import com.flightinfo.app.utils.AuthManager
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<User>>(Resource.Idle())
    val loginState: StateFlow<Resource<User>> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<Resource<User>>(Resource.Idle())
    val registerState: StateFlow<Resource<User>> = _registerState.asStateFlow()

    init {
        // 初始化时从AuthManager获取当前用户状态
        viewModelScope.launch {
            authManager.currentUser.collect { user ->
                if (user != null) {
                    _currentUser.value = user
                    _isLoggedIn.value = true
                } else {
                    _currentUser.value = null
                    _isLoggedIn.value = false
                }
            }
        }
    }

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            try {
                val result = userRepository.loginUser(email, password)
                result.fold(
                    onSuccess = { user ->
                        _loginState.value = Resource.Success(user)
                        authManager.login(user)
                    },
                    onFailure = { exception ->
                        _loginState.value = Resource.Error(exception.message ?: "Login failed")
                    },
                )
            } catch (e: Exception) {
                _loginState.value = Resource.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(
        email: String,
        password: String,
        firstName: String = "",
        lastName: String = "",
        phoneNumber: String = "",
    ) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading()
            try {
                val result = userRepository.registerUser(email, password, firstName, lastName, phoneNumber)
                result.fold(
                    onSuccess = { user ->
                        _registerState.value = Resource.Success(user)
                        authManager.login(user)
                    },
                    onFailure = { exception ->
                        _registerState.value = Resource.Error(exception.message ?: "Registration failed")
                    },
                )
            } catch (e: Exception) {
                _registerState.value = Resource.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            authManager.logout()
            _loginState.value = Resource.Idle()
            _registerState.value = Resource.Idle()
        }
    }

    fun clearLoginState() {
        _loginState.value = Resource.Idle()
    }

    fun clearRegisterState() {
        _registerState.value = Resource.Idle()
    }

    fun updateCurrentUser(user: User) {
        authManager.updateUser(user)
    }
}
