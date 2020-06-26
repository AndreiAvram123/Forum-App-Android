package com.example.dataLayer.models.serialization

import com.example.dataLayer.models.UserDTO
import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
        @SerializedName("user")
        val userDTO: UserDTO?,
        @SerializedName("token")
        val token: String?,
        @SerializedName("ErrorMessage")
        val errorMessage: String
)