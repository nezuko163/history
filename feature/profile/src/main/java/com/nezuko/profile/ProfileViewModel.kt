package com.nezuko.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.repository.RemoteStorageRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val remoteStorageRepository: RemoteStorageRepository
) : ViewModel() {
    val me = userProfileRepository.me

    fun setUserAvatar(uri: Uri) {
        if (me.value.data == null) {
            Log.e(TAG, "setUserAvatar: me = null")
            return
        }
        viewModelScope.launch {
            val remoteUri = remoteStorageRepository.uploadPhotoUrl(uri)
            Log.i(TAG, "setUserAvatar: remoteUri - $remoteUri")
            if (remoteUri.isNotEmpty()) {
                userProfileRepository.setAvatarUrl(remoteUri)
                userProfileRepository.updateUserProfileById(me.value.data!!)
            }
        }
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}