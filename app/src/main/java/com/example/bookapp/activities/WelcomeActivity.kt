package com.example.bookapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.bookapp.R
import com.example.bookapp.dagger.AppComponent
import com.example.bookapp.dagger.DaggerAppComponent
import com.example.bookapp.dagger.MyApplication
import com.example.bookapp.fragments.AuthenticationFragment
import com.example.bookapp.models.User
import com.example.bookapp.user.UserAccountManager
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.interfaces.dao.UserDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class WelcomeActivity : AppCompatActivity(), AuthenticationFragment.FragmentCallback {

    private var googleSignInAccount: GoogleSignInAccount? = null
    private val viewModelUser: ViewModelUser by viewModels()

    private val requestCodeGoogleSignIn = 1


    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var userAccountManager: UserAccountManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_activity_welcome)
        val appComponent: AppComponent = DaggerAppComponent.factory().create(applicationContext, viewModelUser.viewModelScope)
        (application as MyApplication).appComponent = appComponent

        appComponent.inject(this)
        appComponent.inject(viewModelUser)

        userAccountManager.user.observe(this, Observer {
            if (it.userID != 0) {
                startMainActivity()
            }
        })
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                googleSignInAccount = task.getResult(ApiException::class.java)
                googleSignInAccount?.let {
                    viewModelUser.loginWithGoogle(it.id!!, it.displayName!!, it.email!!).observe(this, Observer { user ->
                        if (user != null) {
                            lifecycleScope.launch {
                                userAccountManager.saveUserInMemory(user)
                                runOnUiThread { startMainActivity() }
                            }
                        }
                    })
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
                e.printStackTrace()
                Snackbar.make(findViewById(R.id.container_activity_welcome), "Authentication Failed for google.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun saveUserInMemory(user: User) {

        startMainActivity()

    }


    override fun loginWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()
        val signInIntent = GoogleSignIn.getClient(this, gso).signInIntent;
        startActivityForResult(signInIntent, requestCodeGoogleSignIn)
    }

}