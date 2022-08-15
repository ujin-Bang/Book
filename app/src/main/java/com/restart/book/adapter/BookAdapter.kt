package com.restart.book.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.restart.book.databinding.ItemBookBinding
import com.restart.book.model.Book
import java.text.DecimalFormat

class BookAdapter: ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {

    inner class BookItemViewHolder(private val binding:ItemBookBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(bookModel: Book){
            binding.titleTextView.text = bookModel.title
            binding.authorTextView.text = bookModel.author
            binding.descriptionTextView.text = bookModel.description

            val priceInt = bookModel.price.toInt()
            val commaPri = DecimalFormat("#,###")
            val resultPri = commaPri.format(priceInt)
            binding.priceTextView.text = "${resultPri}원"

            Glide.with(binding.coverImageView.context).load(bookModel.image).into(binding.coverImageView)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object { //이미 할당된 아이템과, 내용이 같으면 중복을 막고 값이 표시되지 않게 하는 변수 생성.
        val diffUtil = object : DiffUtil.ItemCallback<Book>(){
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.title == newItem.title //Book의 컨텐트중 타이틀이 같으면
            }

        }
    }
}