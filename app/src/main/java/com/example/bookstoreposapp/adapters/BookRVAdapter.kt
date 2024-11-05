package com.example.bookstoreposapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreposapp.BookData
import com.example.bookstoreposapp.R
import org.w3c.dom.Text

class BookRVAdapter(var bookList: List<BookData>) :
    RecyclerView.Adapter<BookRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo : ImageView = itemView.findViewById(R.id.bookImage)
        val title: TextView = itemView.findViewById(R.id.titleText)
        val status: TextView = itemView.findViewById(R.id.status)
        val originalPrice: TextView = itemView.findViewById(R.id.originalPrice)
        val discountedPrice: TextView = itemView.findViewById(R.id.discountedPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_card_item, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.title.text = bookList[position].title
        holder.status.text = bookList[position].status
        holder.originalPrice.text = bookList[position].originalPrice
        holder.discountedPrice.text = bookList[position].discountedPrice
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(bookList : List<BookData>){
        this.bookList = bookList;
        notifyDataSetChanged()
    }
}