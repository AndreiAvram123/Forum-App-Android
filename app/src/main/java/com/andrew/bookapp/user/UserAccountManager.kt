package com.andrew.bookapp.user

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andrew.bookapp.R
import com.andrew.bookapp.models.User
import com.andrew.dataLayer.interfaces.dao.UserDao
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserAccountManager @Inject constructor(private val sharedPreferences: SharedPreferences,
                                             @ApplicationContext private val context: Context,
                                             private val userDao: UserDao) {


    fun getToken() = sharedPreferences.getStringNotNull(R.string.key_token)

    val continueUserFlow = MutableLiveData(false)


    suspend fun saveUserAndToken(user: User, token: String) {
        persistToken(token)
        userDao.insertUser(user)
        continueUserFlow.postValue(true)
    }

    private fun persistToken(token: String) {
        sharedPreferences.edit {
            putString(context.getString(R.string.key_token),token)
        }
    }

    fun deleteUserFromMemory() {
        sharedPreferences.edit {
            putString(context.getString(R.string.key_token),"")
        }
    }


    private fun SharedPreferences.getStringNotNull(keyID: Int
    ): String {
        val value = getString(context.getString(keyID), "unknown")
        value?.let { return it }
        return "Unknown"
    }


    private fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        action(editor)
        editor.apply()
    }

}