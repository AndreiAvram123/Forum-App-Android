package com.example.dataLayer.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookapp.AppUtilities
import com.example.bookapp.models.Post
import com.example.dataLayer.PostDatabase
import com.example.dataLayer.dataMappers.PostMapper
import com.example.dataLayer.interfaces.PostRepositoryInterface
import com.example.dataLayer.interfaces.RoomPostDao
import com.example.dataLayer.models.PostDTO
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@InternalCoroutinesApi
class PostRepository(private val application: Application) {

    private var nextPageToFetch: Int = 1;


    var currentFetchedPost: MutableLiveData<Post> = MutableLiveData()

    private val favoritePosts: MutableLiveData<ArrayList<Post>> by lazy {
        MutableLiveData<ArrayList<Post>>()
    }

    private val myPosts: MutableLiveData<ArrayList<Post>> by lazy {
        MutableLiveData<ArrayList<Post>>()
    }
    private val repositoryInterface: PostRepositoryInterface by lazy {
        AppUtilities.getRetrofit().create(PostRepositoryInterface::class.java)
    }

    val newFetchedPosts = MutableLiveData<ArrayList<Post>>()
    private val postDao: RoomPostDao = PostDatabase.getDatabase(application).postDao()
    val recentPosts: LiveData<List<Post>> by lazy {
        postDao.getRecentPosts();
    }

    suspend fun fetchPostByID(id: Long, userID: String = "") {
        try {
            currentFetchedPost.value = PostMapper.mapDtoObjectToDomainObject(repositoryInterface.fetchPostByID(id, userID))
        } catch (e: java.lang.Exception) {
            currentFetchedPost.value = Post.buildNullSafeObject();
            e.printStackTrace()

        }
    }

    /**
     * This method should be called when the favorite posts
     * data is requested
     * The method decided weather it should fetch the data from
     * cache or from the source
     *
     * @param userID
     * @return
     */
    fun fetchFavoritePosts(userID: String?): MutableLiveData<ArrayList<Post>> {
        //start fetching the other data on the other thread
        repositoryInterface.fetchFavoritePostsByUserID(userID, true).enqueue(object : Callback<ArrayList<PostDTO>> {
            override fun onResponse(call: Call<ArrayList<PostDTO>>, response: Response<ArrayList<PostDTO>>) {
                favoritePosts.value = response.body()?.let {
                    PostMapper.mapDTONetworkToDomainObjects(it)
                }
            }

            override fun onFailure(call: Call<ArrayList<PostDTO>>, t: Throwable) {}
        })
        return favoritePosts;
    }

    fun fetchMyPosts(userID: String?): MutableLiveData<ArrayList<Post>> {
        repositoryInterface.fetchMyPosts(userID, true).enqueue(object : Callback<ArrayList<PostDTO>> {
            override fun onResponse(call: Call<ArrayList<PostDTO>>, response: Response<ArrayList<PostDTO>>) {
                myPosts.value = response.body()?.let { PostMapper.mapDTONetworkToDomainObjects(it) }
            }

            override fun onFailure(call: Call<ArrayList<PostDTO>>, t: Throwable) {}
        })
        return myPosts
    }

    suspend fun addPostToFavorites(postID: Long, userID: String) = repositoryInterface.addPostToFavorites(postID, userID)


    fun deletePostFromFavorites(postID: Long, userID: String?) {
        //  repositoryInterface.deletePostFromFavorites(postID,userID);
    }


    suspend fun fetchNextPagePosts() {
        try {
            val fetchedData: ArrayList<PostDTO> = repositoryInterface.fetchPostByPage(nextPageToFetch);
            nextPageToFetch++
            postDao.insertPosts(PostMapper.mapDTONetworkToDomainObjects(fetchedData));
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    suspend fun fetchPostFirstPage() {
        if (nextPageToFetch == 1) {
            postDao.removeOldFetchedData()
            fetchNextPagePosts()
        }

    }
}

