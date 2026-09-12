package com.madi.smarttask.feature_name.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.madi.smarttask.feature_name.domain.repository.UserRepository
import com.madi.smarttask.core.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey(Constants.KEY_USER_NAME)
    }

    override fun getUserName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_NAME]
        }
    }

    override suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }
}
