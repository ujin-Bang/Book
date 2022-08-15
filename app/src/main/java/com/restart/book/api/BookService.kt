package com.restart.book.api

import com.restart.book.model.SearchBooksDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BookService {
    @GET("/v1/search/book.json")
    fun getSearchBook(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") pw: String,
        @Query("query") keyword: String,
        @Query("display") display: Int
        ): Call<SearchBooksDTO>

}