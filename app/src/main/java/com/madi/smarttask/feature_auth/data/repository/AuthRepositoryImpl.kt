package com.madi.smarttask.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.madi.smarttask.R
import com.madi.smarttask.core.util.Resource
import com.madi.smarttask.core.util.SimpleResource
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.domain.repository.AuthRepository
import com.madi.smarttask.feature_name.domain.usecase.NameUseCases
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val nameUseCases: NameUseCases
) : AuthRepository {

    override suspend fun login(email: String, password: String): SimpleResource {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
            if (result.user != null) {
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
            if (result.user != null) {
                if (username.isNotBlank()) {
                    nameUseCases.saveUserName(username.trim())
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

    override suspend fun authenticateWithGoogle(idToken: String): SimpleResource {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            if (result.user != null) {
                val displayName = result.user?.displayName
                if (!displayName.isNullOrBlank()) {
                    nameUseCases.saveUserName(displayName)
                }
                Resource.Success(Unit)
            } else {
                Resource.Error(UiText.StringResource(R.string.check_your_internet))
            }
        } catch (e: Exception) {
            val uiText = e.localizedMessage?.let { UiText.DynamicString(it) }
                ?: UiText.StringResource(R.string.error_google_sign_in_failed)
            Resource.Error(uiText)
        }
    }
}
