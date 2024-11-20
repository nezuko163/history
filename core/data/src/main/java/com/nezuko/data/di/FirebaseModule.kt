package com.nezuko.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {
    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance().apply { this.setLogLevel(Logger.Level.DEBUG) }
    }
}