package com.restart.book.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.restart.book.databinding.ItemHistoryBinding
import com.restart.book.model.History

class HistoryAdapter(val historyDeleteClickedListener: (String) -> Unit) :
    ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {

    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyModel: History) {
            binding.historyKeywordTextView.text = historyModel.keyword

            binding.historyKeywordDeleteButton.setOnClickListener {
                historyDeleteClickedListener(historyModel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object { //이미 할당된 아이템과, 내용이 같으면 중복을 막고 값이 표시되지 않게 하는 변수 생성.
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem

            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}

