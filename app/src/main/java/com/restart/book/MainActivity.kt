package com.restart.book

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.restart.book.adapter.BookAdapter
import com.restart.book.api.BookService
import com.restart.book.databinding.ActivityMainBinding
import com.restart.book.model.Book
import com.restart.book.model.History
import com.restart.book.model.SearchBooksDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java
        "BookSearchDB"
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getSearchBook(id, pw, "Love", 100).enqueue(object : Callback<SearchBooksDTO> {
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

        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN){
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun search(keyword: String){

        bookService.getSearchBook(id,pw,keyword,100).enqueue(object : Callback<SearchBooksDTO>{
            override fun onResponse(
                call: Call<SearchBooksDTO>,
                response: Response<SearchBooksDTO>
            ) {

                saveSearchkeyword(keyword)
                if (response.isSuccessful.not()){
                    Log.e(TAG,response.message().toString())
                    return
                }
                response.body()?.let { it ->
                    Log.d(TAG,it.toString())

                    it.booksList.forEach {
                        Log.d(TAG, it.toString())
                    }
                    adapter.submitList(it.booksList) //리싸이클러뷰 갱신
                }
            }

            override fun onFailure(call: Call<SearchBooksDTO>, t: Throwable) {

            }

        })

    }

    private fun initBookRecyclerView(){

        adapter = BookAdapter()
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun showHistoryView(){
        Thread{
            val keywords = db.historyDao().getAll().reversed()
        }
        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryView(){
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchkeyword(keyword: String){
        Thread{
            db.historyDao().insertHistory(History(null,keyword))
        }.start()
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val id = "oMT5idYoCg8lB7YaH6Y5"
        private const val pw = "RxzWCVLpg3"
    }
}