package com.example.bookstoreposapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreposapp.BookData
import com.example.bookstoreposapp.BookDetailActivity
import com.example.bookstoreposapp.R


class BookRVAdapter(var bookList: List<BookData>, private val context: Context) :
    RecyclerView.Adapter<BookRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo : ImageView = itemView.findViewById(R.id.bookImage)
        val title: TextView = itemView.findViewById(R.id.titleText)
        val status: TextView = itemView.findViewById(R.id.status)
        val originalPrice: TextView = itemView.findViewById(R.id.originalPrice)
        val discountedPrice: TextView = itemView.findViewById(R.id.discountedPrice)

        val cardView: CardView = itemView.findViewById(R.id.book_card_view)

        init {
            cardView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val book = bookList[position]
                    val intent = Intent(context, BookDetailActivity::class.java)
                    intent.putExtra("bookData",book)
                    context.startActivity(intent)
                }
            }
        }
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
        holder.logo.setImageResource(bookList[position].image)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(bookList : List<BookData>){
        this.bookList = bookList;
        notifyDataSetChanged()
    }
}