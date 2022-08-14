package com.restart.book

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.restart.book.adapter.BookAdapter
import com.restart.book.api.BookService
import com.restart.book.databinding.ActivityMainBinding
import com.restart.book.model.SearchBooksDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getSearchBook(id, pw, "Love").enqueue(object : Callback<SearchBooksDTO> {
            override fun onResponse(
                call: Call<SearchBooksDTO>,
                response: Response<SearchBooksDTO>
            ) {//성공 처리
                if (response.isSuccessful.not()) {
                    Log.e(TAG, "북서치 실패")
                    return
                }
                response.body()?.let {
                    Log.d(TAG, it.toString())

                    it.booksList.forEach {
                        Log.d(TAG, it.toString())
                    }
                    adapter.submitList(it.booksList) //ListAdapter상속으로 submitList를 통해 현재 리스트가 리턴. 갱신되도록
                }
            }

            override fun onFailure(call: Call<SearchBooksDTO>, t: Throwable) {
                //실패처리
                Log.d(TAG, "북서치 서버요청 실패")
            }

        })
    }

    fun initBookRecyclerView(){

        adapter = BookAdapter()
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val id = "oMT5idYoCg8lB7YaH6Y5"
        private const val pw = "RxzWCVLpg3"
    }
}