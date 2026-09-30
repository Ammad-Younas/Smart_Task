package com.madi.smarttask.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
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
import com.madi.smarttask.feature_auth.util.AuthConstants
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
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            Resource.Error(UiText.StringResource(R.string.error_invalid_credentials))
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                AuthConstants.ERROR_USER_NOT_FOUND -> Resource.Error(UiText.StringResource(R.string.error_user_not_found))
                AuthConstants.ERROR_WRONG_PASSWORD, AuthConstants.ERROR_INVALID_CREDENTIAL -> Resource.Error(UiText.StringResource(R.string.error_invalid_credentials))
                AuthConstants.ERROR_TOO_MANY_REQUESTS -> Resource.Error(UiText.StringResource(R.string.error_too_many_requests))
                else -> Resource.Error(UiText.StringResource(R.string.error_login_failed))
            }
        } catch (_: Exception) {
            Resource.Error(UiText.StringResource(R.string.error_login_failed))
        }
    }

    override suspend fun register(email: String, username: String, password: String): SimpleResource {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = result.user
            if (user != null) {
                val trimmedUsername = username.trim()
                if (trimmedUsername.isNotBlank()) {
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
                firebaseAuth.signOut()
                Resource.Success(Unit)
            } else {
                Resource.Error(UiText.StringResource(R.string.check_your_internet))
            }
        } catch (_: FirebaseAuthUserCollisionException) {
            Resource.Error(UiText.StringResource(R.string.error_user_already_exists))
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                AuthConstants.ERROR_EMAIL_ALREADY_IN_USE -> Resource.Error(UiText.StringResource(R.string.error_user_already_exists))
                AuthConstants.ERROR_INVALID_EMAIL -> Resource.Error(UiText.StringResource(R.string.error_invalid_email))
                else -> Resource.Error(UiText.StringResource(R.string.error_registration_failed))
            }
        } catch (_: Exception) {
            Resource.Error(UiText.StringResource(R.string.error_registration_failed))
        }
    }

    override suspend fun updateProfile(username: String, newPassword: String?): SimpleResource {
        return try {
            val user = firebaseAuth.currentUser
                ?: return Resource.Error(UiText.unknownError())

            val trimmedUsername = username.trim()
            if (trimmedUsername.isNotBlank()) {
                settingsDataStore.saveUserName(trimmedUsername)
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(trimmedUsername)
                    .build()
                user.updateProfile(profileUpdates).await()

                val userMap = mapOf(
                    "uid" to user.uid,
                    "username" to trimmedUsername,
                    "email" to (user.email ?: "")
                )
                try {
                    FirebaseDatabase.getInstance().getReference("users").child(user.uid).setValue(userMap).await()
                } catch (_: Exception) { }
            }

            if (!newPassword.isNullOrBlank()) {
                user.updatePassword(newPassword.trim()).await()
            }

            Resource.Success(Unit)
        } catch (_: Exception) {
            Resource.Error(UiText.unknownError())
        }
    }

    override suspend fun logout(): SimpleResource {
        return try {
            firebaseAuth.signOut()
            settingsDataStore.saveUserName("")
            Resource.Success(Unit)
        } catch (_: Exception) {
            Resource.Error(UiText.unknownError())
        }
    }
}
