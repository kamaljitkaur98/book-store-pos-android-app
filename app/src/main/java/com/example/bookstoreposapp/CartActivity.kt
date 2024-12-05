package com.example.bookstoreposapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreposapp.DAO.CartDao
import com.example.bookstoreposapp.adapters.BookRVAdapter
import com.example.bookstoreposapp.adapters.CartAdapter
import com.example.bookstoreposapp.adapters.RetrofitInstance
import com.example.bookstoreposapp.database.CartDatabase
import com.example.bookstoreposapp.fragment.NavFragment
import com.example.bookstoreposapp.model.CartViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CartActivity: AppCompatActivity() {

    private lateinit var cartAdapter: CartAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.user_cart)
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

        val fragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .commit()

        recyclerView = findViewById(R.id.recycler_view)

        val proceedButton: Button = findViewById(R.id.proceedToCheckout)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(emptyList(), this)
        viewModel.allCartItems.observe(this) { cartItems ->
            cartAdapter.updateData("initial", 0, cartItems)
        }
        recyclerView.adapter = cartAdapter

        proceedButton.setOnClickListener{
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
}