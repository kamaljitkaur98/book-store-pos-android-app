package com.example.bookstoreposapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstoreposapp.BookData
import com.example.bookstoreposapp.BookDetailActivity
import com.example.bookstoreposapp.CheckoutActivity
import com.example.bookstoreposapp.MainActivity
import com.example.bookstoreposapp.R
import com.example.bookstoreposapp.adapters.BookRVAdapter.BookViewHolder
import com.example.bookstoreposapp.database.CartDatabase
import com.example.bookstoreposapp.model.CartItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter (private var cartItems: List<CartItem>, private val context: Context) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var totalAmount = 0.00
    val newCartItems = mutableListOf<CartItem>()
    val oldCartItems = mutableListOf<CartItem>()

    val db = CartDatabase.getDatabase(context)
    val cartDao = db.cartDao()

    fun updateData(event: String, position: Int, newItems: List<CartItem>) {
        cartItems = newItems
        calculateTotalAndSeparateItems(cartItems)
        if(event == "delete"){
            notifyItemRemoved(position)
        }else{
            notifyDataSetChanged()
        }
    }

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleText)
        val status: TextView = itemView.findViewById(R.id.status)
        val originalPrice: TextView = itemView.findViewById(R.id.originalPrice)
        val discountedPrice: TextView = itemView.findViewById(R.id.discountedPrice)
        val retailStatus: TextView = itemView.findViewById(R.id.retailingText)

        val deleteButton: ImageView = itemView.findViewById(R.id.deleteIcon)

        val cardView: CardView = itemView.findViewById(R.id.cart_card_view)

        init {
            // All on-click listeners go here
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.title.text = cartItems[position].itemName
        holder.status.text = "Owners: ${cartItems[position].status}"
        holder.originalPrice.text = cartItems[position].originalPrice
        holder.discountedPrice.text = cartItems[position].discountedPrice
        holder.retailStatus.text = if (cartItems[position].status == "New") "Retailing" else "BuyBack"

        when (cartItems[position].status) {
            "New" -> holder.cardView.setCardBackgroundColor(context.getColor(R.color.new_book))
            "Old" -> holder.cardView.setCardBackgroundColor(context.getColor(R.color.used_book))
        }
        holder.deleteButton.setOnClickListener {
            GlobalScope.launch {
                cartDao.deleteCartItem(cartItems[position])
            }
            val updatedCartItems = cartDao.getAllCartItems().value
            if (updatedCartItems != null) {
                (context as Activity).runOnUiThread {
                    updateData("delete", position, updatedCartItems)
                }
            }

        }
    }

    fun calculateTotalAndSeparateItems(cartItems: List<CartItem>) {
        totalAmount = 0.0
        newCartItems.clear()
        oldCartItems.clear()
        for (item in cartItems) {
            when (item.status) {
                "New" -> {
                    // Add amount for new items
                    totalAmount += item.discountedPrice.toDouble()
                    newCartItems.add(item)
                }
                "Old" -> {
                    // Subtract amount for old items
                    totalAmount -= item.discountedPrice.toDouble()
                    oldCartItems.add(item)
                }
            }
        }

        (context as Activity).runOnUiThread {
            val totalAmountTextView = (context).findViewById<TextView>(R.id.total_amount)
            val formattedAmount = String.format("%.2f", totalAmount)
            totalAmountTextView.text = formattedAmount
        }
    }
}