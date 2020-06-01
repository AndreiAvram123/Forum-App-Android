package com.example.dataLayer.models

import com.google.gson.annotations.SerializedName

data class ChatDTO(
        @SerializedName("id")
        val id: Int,
        @SerializedName("type")
        val type: String,
        @SerializedName("users")
        val users: List<UserDTO>
)