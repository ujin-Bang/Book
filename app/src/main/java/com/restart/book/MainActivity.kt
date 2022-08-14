package com.restart.book

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.restart.book.api.BookService
import com.restart.book.model.SearchBooksDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val id = "oMT5idYoCg8lB7YaH6Y5"
    val pw = "RxzWCVLpg3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getSearchBook(id,pw,"Love").enqueue(object : Callback<SearchBooksDTO>{
            override fun onResponse(
                call: Call<SearchBooksDTO>,
                response: Response<SearchBooksDTO>
            ) {//성공 처리
                if (response.isSuccessful.not()){
                    Log.e(TAG,"북서치 실패")
                    return
                }
                response.body()?.let{
                    Log.d(TAG, it.toString())

                    it.booksList.forEach {
                        Log.d(TAG, it.toString())
                    }
                }
            }

            override fun onFailure(call: Call<SearchBooksDTO>, t: Throwable) {
               //실패처리
                Log.d(TAG,"북서치 서버요청 실패")
            }

        })
    }

    companion object{
        private const val TAG = "MainActivity"
    }
}