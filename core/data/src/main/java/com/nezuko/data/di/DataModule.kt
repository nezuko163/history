package com.nezuko.data.di

import com.nezuko.data.impl.AuthRepositoryImpl
import com.nezuko.data.impl.DuelRepositoryImpl
import com.nezuko.data.impl.MatchmakingRepositoryImpl
import com.nezuko.data.impl.QuestionRepositoryImpl
import com.nezuko.data.impl.RemoteStorageRepositoryImpl
import com.nezuko.data.impl.UserProfileRepositoryImpl
import com.nezuko.domain.repository.AuthRepository
import com.nezuko.domain.repository.DuelRepository
import com.nezuko.domain.repository.MatchmakingRepository
import com.nezuko.domain.repository.QuestionRepository
import com.nezuko.domain.repository.RemoteStorageRepository
import com.nezuko.domain.repository.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindAuthRepo(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindUserProfileRepo(impl: UserProfileRepositoryImpl): UserProfileRepository

    @Binds
    @Singleton
    fun bindRemoteStorageRepo(impl: RemoteStorageRepositoryImpl): RemoteStorageRepository

    @Binds
    @Singleton
    fun bindMatchmakingSearchRepo(impl: MatchmakingRepositoryImpl): MatchmakingRepository

    @Binds
    @Singleton
    fun bindQuestionRepo(impl: QuestionRepositoryImpl): QuestionRepository

    @Binds
    @Singleton
    fun bindDuelRepo(impl: DuelRepositoryImpl): DuelRepository
}