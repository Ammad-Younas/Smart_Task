package com.madi.smarttask.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.madi.smarttask.R
import com.madi.smarttask.core.util.Resource
import com.madi.smarttask.core.util.SimpleResource
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
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
        } catch (_: Exception) {
            Resource.Error(UiText.unknownError())
        }
    }
}
