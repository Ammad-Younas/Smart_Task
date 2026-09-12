package com.madi.smarttask.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.madi.smarttask.feature_name.data.repository.UserRepositoryImpl
import com.madi.smarttask.feature_name.domain.repository.UserRepository
import com.madi.smarttask.core.util.Constants
import com.madi.smarttask.feature_name.domain.usecase.GetUserName
import com.madi.smarttask.feature_name.domain.usecase.NameUseCases
import com.madi.smarttask.feature_name.domain.usecase.SaveUserName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DATASTORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository {
        return UserRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideNameUseCases(repository: UserRepository): NameUseCases {
        return NameUseCases(
            getUserName = GetUserName(repository),
            saveUserName = SaveUserName(repository)
        )
    }
}
