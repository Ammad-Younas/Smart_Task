package com.madi.smarttask.feature_auth.di

import com.google.firebase.auth.FirebaseAuth
import com.madi.smarttask.core.data.preferences.SettingsDataStore
import com.madi.smarttask.feature_auth.data.repository.AuthRepositoryImpl
import com.madi.smarttask.feature_auth.domain.repository.AuthRepository
import com.madi.smarttask.feature_auth.domain.usecase.AuthenticateWithGoogleUseCase
import com.madi.smarttask.feature_auth.domain.usecase.LoginUseCase
import com.madi.smarttask.feature_auth.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        settingsDataStore: SettingsDataStore
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, settingsDataStore)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAuthenticateWithGoogleUseCase(repository: AuthRepository): AuthenticateWithGoogleUseCase {
        return AuthenticateWithGoogleUseCase(repository)
    }
}
