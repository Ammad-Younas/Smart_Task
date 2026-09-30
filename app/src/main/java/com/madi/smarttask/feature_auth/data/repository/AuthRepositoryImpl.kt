package com.madi.smarttask.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.madi.smarttask.R
import com.madi.smarttask.core.data.preferences.SettingsDataStore
import com.madi.smarttask.core.util.Resource
import com.madi.smarttask.core.util.SimpleResource
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val settingsDataStore: SettingsDataStore
) : AuthRepository {

    override suspend fun login(email: String, password: String): SimpleResource {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
            val user = result.user
            if (user != null) {
                val displayName = user.displayName
                if (!displayName.isNullOrBlank()) {
                    settingsDataStore.saveUserName(displayName)
                }
                Resource.Success(Unit)
            } else {
                Resource.Error(UiText.StringResource(R.string.check_your_internet))
            }
        } catch (_: FirebaseAuthInvalidUserException) {
            Resource.Error(UiText.StringResource(R.string.error_user_not_found))
        } catch (e: Exception) {
            val uiText = e.localizedMessage?.let { UiText.DynamicString(it) }
                ?: UiText.StringResource(R.string.error_login_failed)
            Resource.Error(uiText)
        }
    }

    override suspend fun register(email: String, username: String, password: String): SimpleResource {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = result.user
            if (user != null) {
                val trimmedUsername = username.trim()
                if (trimmedUsername.isNotBlank()) {
                    settingsDataStore.saveUserName(trimmedUsername)
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(trimmedUsername)
                        .build()
                    try {
                        user.updateProfile(profileUpdates).await()
                    } catch (_: Exception) { }

                    val userMap = mapOf(
                        "uid" to user.uid,
                        "username" to trimmedUsername,
                        "email" to email.trim()
                    )
                    try {
                        FirebaseDatabase.getInstance().getReference("users").child(user.uid).setValue(userMap).await()
                    } catch (_: Exception) { }
                }
                Resource.Success(Unit)
            } else {
                Resource.Error(UiText.StringResource(R.string.check_your_internet))
            }
        } catch (_: FirebaseAuthUserCollisionException) {
            Resource.Error(UiText.StringResource(R.string.error_user_already_exists))
        } catch (e: Exception) {
            val uiText = e.localizedMessage?.let { UiText.DynamicString(it) }
                ?: UiText.StringResource(R.string.error_registration_failed)
            Resource.Error(uiText)
        }
    }
}
