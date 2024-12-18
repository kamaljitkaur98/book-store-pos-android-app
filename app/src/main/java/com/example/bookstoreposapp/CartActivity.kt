package com.example.bookstoreposapp

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreposapp.DAO.CartDao
import com.example.bookstoreposapp.adapters.BookRVAdapter
import com.example.bookstoreposapp.adapters.CartAdapter
import com.example.bookstoreposapp.fragment.NavFragment
import com.example.bookstoreposapp.model.CartViewModel

class CartActivity: AppCompatActivity() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CartViewModel
    private var recyclerViewState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_cart)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)


        if (savedInstanceState == null) {
            val fragment = NavFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.bottom_nav_fragment, fragment)
                .commit()
        }

        recyclerView = findViewById(R.id.recycler_view)

        val proceedButton: Button = findViewById(R.id.proceedToCheckout)
        val backButton: ImageButton = findViewById(R.id.backButton)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator().apply {
            addDuration = 300
            removeDuration = 300
        }
        cartAdapter = CartAdapter(emptyList(), this)
        viewModel.allCartItems.observe(this) { cartItems ->
            cartAdapter.updateData("initial", 0, cartItems)
        }
        recyclerView.adapter = cartAdapter

        proceedButton.setOnClickListener {
            viewModel.getCartItemCount().observe(this) { count ->
                if (count == 0) {
                    Toast.makeText(this, "No items to checkout", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, CheckoutActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        }

        backButton.setOnClickListener {
            println("Press registered")
            onBackPressed()
        }
    }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            outState.putParcelable("recycler_state", recyclerViewState)
        }

        override fun onRestoreInstanceState(savedInstanceState: Bundle) {
            super.onRestoreInstanceState(savedInstanceState)
            recyclerViewState = savedInstanceState.getParcelable("recycler_state")
            if (recyclerViewState != null) {
                recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            }
        }
    }