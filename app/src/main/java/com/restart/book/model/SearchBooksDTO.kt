package com.restart.book.model

import com.google.gson.annotations.SerializedName

data class SearchBooksDTO (
    val total: Int,
    @SerializedName("items") val booksList: List<Book>
  )