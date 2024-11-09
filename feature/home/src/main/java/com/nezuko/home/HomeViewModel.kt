package com.nezuko.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import com.nezuko.domain.repository.MatchmakingRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val matchmakingRepository: MatchmakingRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {
    val me = userProfileRepository.me
    fun startSearch(
        userProfile: UserProfile,
        onRoomCreated: (room: RoomModel) -> Unit,
        onGameEnd: (room: RoomModel) -> Unit
    ) {
        viewModelScope.launch {
            matchmakingRepository.startSearch(
                user = userProfile,
                onRoomCreated = onRoomCreated,
                onGameEnd = onGameEnd
            )
        }
    }
}