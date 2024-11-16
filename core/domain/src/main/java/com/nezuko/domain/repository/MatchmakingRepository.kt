package com.nezuko.domain.repository

import com.nezuko.domain.model.QuestionModel
import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface MatchmakingRepository {
    val currentRoom: StateFlow<RoomModel?>
    val isSearching: StateFlow<Boolean>
    val questions: StateFlow<List<QuestionModel>?>

    suspend fun startSearch(
        user: UserProfile,
        onRoomCreated: (room: RoomModel) -> Unit,
        onGameEnd: (room: RoomModel) -> Unit
    )

    suspend fun stopSearch(
        user: UserProfile,
        onStopSearch: () -> Unit,
    )

    suspend fun endGame(): RoomModel?
}