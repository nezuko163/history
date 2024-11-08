package com.nezuko.data.di

import com.nezuko.data.impl.AuthRepositoryImpl
import com.nezuko.data.impl.RemoteStorageRepositoryImpl
import com.nezuko.data.impl.UserProfileRepositoryImpl
import com.nezuko.domain.repository.AuthRepository
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

}