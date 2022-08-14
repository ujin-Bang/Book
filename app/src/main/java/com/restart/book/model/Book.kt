package com.restart.book.model

import com.google.gson.annotations.SerializedName

data class Book(
    val title: String,
    val image: String,
    val author: String,
    @SerializedName("discount") val price: Int,
    val description: String
)
