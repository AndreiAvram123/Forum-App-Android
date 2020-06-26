package com.example.dataLayer.interfaces

import com.example.dataLayer.models.PostDTO
import com.example.dataLayer.models.SerializeImage
import com.example.dataLayer.models.ServerResponse
import com.example.dataLayer.models.serialization.SerializePost
import retrofit2.http.*

interface PostRepositoryInterface {

    @GET("/api/recentPosts")
    suspend fun fetchRecentPosts(): ArrayList<PostDTO>

    @GET("/api/posts/page/{lastPostID}")
    suspend fun fetchNextPagePosts(@Path("lastPostID")lastPostID: Int): List<PostDTO>


    @GET("/api/post/{id}")
    suspend fun fetchPostByID(@Path("id") postID: Int): PostDTO

    @GET("/api/user/{id}/favoritePosts")
    suspend fun fetchUserFavoritePosts(@Path("id") userID: Int): ArrayList<PostDTO>


    @GET("/api/user/{id}/posts")
    suspend fun fetchMyPosts(@Path("id") userID: Int): ArrayList<PostDTO>

    @DELETE("/api/user/{userID}/removeFromFavorites/{postID}")
    suspend fun removePostFromFavorites(@Path("userID") userID: Int, @Path("postID") postID: Int): ServerResponse

    @POST("/api/user/{userID}/addToFavorites/{postID}")
    suspend fun addPostToFavorites(@Path("postID") postID: Int, @Path("userID") userID: Int)

    @POST("/api/posts/uploadImage")
    suspend fun uploadImage(@Body serializeImage: SerializeImage): ServerResponse

    @POST("/api/posts/create")
    suspend fun uploadPost(@Body uploadPost: SerializePost): ServerResponse


}