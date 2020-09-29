package com.andrew.dataLayer.repositories

import android.net.ConnectivityManager
import androidx.lifecycle.liveData
import com.andrew.bookapp.user.UserAccountManager
import com.andrew.dataLayer.dataMappers.UserMapper
import com.andrew.dataLayer.interfaces.UserRepositoryInterface
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(private val repo: UserRepositoryInterface,
                                         private val connectivityManager: ConnectivityManager,
                                         private val userAccountManager: UserAccountManager) {


    suspend fun loginWithGoogle(idToken: String, displayName: String, email: String) {
        try {
            val serverResponse = repo.fetchGoogleUser(idToken)
            if (serverResponse.userDTO != null && serverResponse.token != null) {
                userAccountManager.saveUserAndToken(UserMapper.mapToDomainObject(serverResponse.userDTO), serverResponse.token)
            } else {
                createGoogleAccount(idToken, displayName, email)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun createGoogleAccount(idToken: String, displayName: String, email: String) {
        try {
            val response = repo.createGoogleAccount(idToken, displayName, email)
            if (response.userDTO != null && response.token != null) {
                userAccountManager.saveUserAndToken(UserMapper.mapToDomainObject(response.userDTO), response.token)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            createGoogleAccount(idToken, displayName, email)
        }
    }

    fun fetchSearchSuggestions(query: String) = liveData {
        if (connectivityManager.activeNetwork != null) {
            try {
                val fetchedSuggestions = repo.fetchSuggestions(query)
                emit(fetchedSuggestions.map { UserMapper.mapToDomainObject(it) })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun login(username: String, password: String) = liveData {
        emit(OperationStatus.ONGOING)
        try {
            val response = repo.login(username, password)
            if (response.userDTO != null && response.token != null) {
                userAccountManager.saveUserAndToken(UserMapper.mapToDomainObject(response.userDTO)
                        , response.token)
                emit(OperationStatus.FINISHED)
            } else {
                emit(OperationStatus.FAILED)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        emit(OperationStatus.FAILED)
    }

    suspend fun register(username: String, email: String, password: String) = repo.register(username, email, password)
}