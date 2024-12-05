package com.example.bookstoreposapp.API.add

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstoreposapp.MainActivity
import com.example.bookstoreposapp.R
import com.example.bookstoreposapp.adapters.RetrofitInstance
import com.example.bookstoreposapp.fragment.NavFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal


class AddBookActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.add_book_activity)

        val fragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .commit()
        val addInventoryButton: MaterialButton = findViewById(R.id.button_add_inventory)

        val bookName: TextInputEditText = findViewById(R.id.edit_text_book_name)
        val bookDescription: TextInputEditText = findViewById(R.id.edit_text_book_description)
        val bookISBN: TextInputEditText = findViewById(R.id.edit_text_isbn)
        val bookPrice: TextInputEditText = findViewById(R.id.edit_text_price)

        bookISBN.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val isbn = bookISBN.text.toString()
                if (isbn.isNotBlank()) {
                    fetchBookDetailsByISBN(isbn, bookName, bookDescription, bookPrice)
                }
            }
        }
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        addInventoryButton.setOnClickListener {
            val name = bookName.text.toString()
            val description = bookDescription.text.toString()
            val isbn = bookISBN.text.toString()
            val price = bookPrice.text.toString()
            if (isbn.isNotEmpty() && price.isNotEmpty()) {
                val price = BigDecimal(price).toDouble()
                makePurchase(isbn, price)
            } else {
                Toast.makeText(this, "ISBN and Price cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun fetchBookDetailsByISBN(
        isbn: String,
        bookName: TextInputEditText,
        bookDescription: TextInputEditText,
        bookPrice: TextInputEditText
    ) {
        val call = RetrofitInstance.api.getBookByISBN(isbn)

        call.enqueue(object : Callback<ApiBookDetailByIsbn> {
            override fun onResponse(
                call: Call<ApiBookDetailByIsbn>,
                response: Response<ApiBookDetailByIsbn>
            ) {
                if (response.isSuccessful) {
                    val bookDetails = response.body()
                    if (bookDetails != null) {
                        // Populate fields with book details
                        bookName.setText(bookDetails.title)
                        bookDescription.setText("Authors: ${bookDetails.authors}, Edition: ${bookDetails.edition}")
                        bookPrice.setText(bookDetails.basePrice.toString())
                    } else {
                        Toast.makeText(this@AddBookActivity, "No book found for this ISBN", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AddBookActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiBookDetailByIsbn>, t: Throwable) {
                Toast.makeText(this@AddBookActivity, "Failed to fetch book details: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun makePurchase(isbn: String, price: Double) {
        RetrofitInstance.api.buyBook(isbn, price).enqueue(object : Callback<PurchaseResponse> {
            override fun onResponse(call: Call<PurchaseResponse>, response: Response<PurchaseResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val purchaseResponse = response.body()!!
                    navigateToPurchaseDetails(purchaseResponse)
                } else {
                    Toast.makeText(this@AddBookActivity, "Purchase failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PurchaseResponse>, t: Throwable) {
                Log.e("AddBookActivity", "Error: ${t.message}")
                Toast.makeText(this@AddBookActivity, "Error making purchase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToPurchaseDetails(purchaseResponse: PurchaseResponse) {
        val intent = Intent(this, PurchaseDetailsActivity::class.java).apply {
            putExtra("id", purchaseResponse.id)
            putExtra("message", purchaseResponse.message)
            putExtra("transactionId", purchaseResponse.transactionId)
        }
        startActivity(intent)
    }
}

