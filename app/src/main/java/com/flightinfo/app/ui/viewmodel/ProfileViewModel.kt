
package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.User
import com.flightinfo.app.repository.UserRepository
import com.flightinfo.app.utils.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager,
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun loadCurrentUser() {
        viewModelScope.launch {
            val userId = authManager.getCurrentUserId()
            if (userId != null) {
                val result = userRepository.getUserById(userId)
                result.onSuccess {
                    _user.postValue(it)
                }.onFailure {
                    // Handle error, e.g., post a null value or an error state
                    _user.postValue(null)
                }
            }
        }
    }
}
