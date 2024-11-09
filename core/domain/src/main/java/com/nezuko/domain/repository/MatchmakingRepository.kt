package com.nezuko.domain.repository

import com.nezuko.domain.model.RoomModel
import com.nezuko.domain.model.UserProfile

interface MatchmakingRepository {
    suspend fun startSearch(
        user: UserProfile,
        onRoomCreated: (room: RoomModel) -> Unit,
        onGameEnd: (room: RoomModel) -> Unit
    )

    suspend fun endGame(): RoomModel?
}