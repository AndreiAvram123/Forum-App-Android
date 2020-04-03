package com.example.dataLayer.interfaces;

import com.example.bookapp.models.Comment;
import com.example.dataLayer.models.CommentDTO;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentsRepositoryInterface {

    @GET("RestfulRequestHandler.php")
    Call<ArrayList<Comment>> fetchCommentsByPostID(@Query("postID") long postID, @Query("comments") boolean fetchComments);

    @POST("RestfulRequestHandler.php")
    Call<Comment> uploadComment(@Query("uploadComment") boolean uploadComment, @Body CommentDTO comment);


}