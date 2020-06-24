package com.example.bookapp.dagger.modules

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookapp.dagger.MyApplication
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelPost
import com.example.dataLayer.interfaces.ChatRepositoryInterface
import com.example.dataLayer.interfaces.CommentRepoInterface
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.UserRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InternalCoroutinesApi
@InstallIn(ActivityComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun getRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("http://www.andreiram.co.uk/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun getPostRepository(retrofit: Retrofit): PostRepositoryInterface = retrofit.create(PostRepositoryInterface::class.java)

    @Provides
    fun getUserRepository(retrofit: Retrofit): UserRepositoryInterface = retrofit.create(UserRepositoryInterface::class.java)

    @Provides
    fun getChatRepository(retrofit: Retrofit): ChatRepositoryInterface = retrofit.create(ChatRepositoryInterface::class.java)

    @Provides
    fun getCommentsRepo(retrofit: Retrofit) = retrofit.create(CommentRepoInterface::class.java)


    @Provides
    fun getConnectivityManager(@ApplicationContext context: Context): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    fun coroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

}
