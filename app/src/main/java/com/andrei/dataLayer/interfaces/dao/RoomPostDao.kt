package com.andrei.dataLayer.interfaces.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.andrei.kit.models.Post
import com.andrei.dataLayer.models.UserWithFavoritePosts
import com.andrei.dataLayer.models.UserWithFavoritePostsCrossRef
import com.andrei.dataLayer.models.UserWithPosts


@Dao
interface RoomPostDao {

    @Query("SELECT * FROM post ORDER BY postID DESC")
    fun getCachedPosts(): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM user WHERE userID = :userID LIMIT 1")
    fun getFavoritePosts(userID: String): LiveData<UserWithFavoritePosts>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFavoritePosts(usersWithFavoritePostsCrossRef: List<UserWithFavoritePostsCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoritePost(usersWithFavoritePostsCrossRef: UserWithFavoritePostsCrossRef)

    @Delete
    suspend fun deletePostFromFavorites(usersWithFavoritePostsCrossRef: UserWithFavoritePostsCrossRef)


    @Query("SELECT * FROM user WHERE userID = :userID")
    fun getAllUserPosts(userID: String): LiveData<UserWithPosts>

    @Query("SELECT * FROM post WHERE postID = :postID LIMIT 1")
    fun getPostByID(postID: Int): LiveData<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<Post>)

    @Query("DELETE FROM post")
    suspend fun removeCachedData()


    @Query("SELECT * FROM user WHERE userID = :userID LIMIT 1")
    suspend fun getFavoritePostsTest(userID: String): UserWithFavoritePosts


}