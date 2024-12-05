package com.example.bookstoreposapp

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstoreposapp.database.CartDatabase
import com.example.bookstoreposapp.model.CartItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CheckoutActivity : AppCompatActivity() {

    var totalAmount = 0.00
    val newCartItems = mutableListOf<CartItem>()
    val oldCartItems = mutableListOf<CartItem>()

    val db = CartDatabase.getDatabase(this)
    val cartDao = db.cartDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout_activity)

        cartDao.getAllCartItems().observe(this) { updatedCartItems ->
            if (updatedCartItems != null) {
                totalAmount = calculateTotalAndSeparateItems(updatedCartItems)
                val formattedAmount = String.format("%.2f", totalAmount)
                val totalAmountText: TextView = findViewById(R.id.totalAmount)
                totalAmountText.text = formattedAmount
            }
        }

        // Toolbar Views
        val backButton: ImageButton = findViewById(R.id.backButton)

        val cashButton: Button = findViewById(R.id.cashButton)
        val cardButton: Button = findViewById(R.id.cardButton)

        backButton.setOnClickListener {
            onBackPressed() // Navigate back
        }

        cashButton.setOnClickListener {
            // Handle Cash Button Click
            handlePayment("Cash")
        }

        cardButton.setOnClickListener {
            // Handle Card Button Click
            handlePayment("Card")
        }
    }

    private fun handlePayment(paymentMethod: String) {
        val nameInput: TextInputEditText = findViewById(R.id.user_name)
        val emailInput: TextInputEditText = findViewById(R.id.user_phone_number)
        val phoneInput: TextInputEditText = findViewById(R.id.user_email)

        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields before proceeding", Toast.LENGTH_SHORT).show()
        } else {
        when (paymentMethod) {
            "Cash" -> {
                // Handle cash payment
                showSuccessPopup()
                Toast.makeText(this, "Payment Successful using Cash", Toast.LENGTH_SHORT).show()
            }
            "Card" -> {
                // Handle card payment
                showSuccessPopup()
                Toast.makeText(this, "Payment Successful using Card", Toast.LENGTH_SHORT).show()
            }
            }
        }
    }

    fun calculateTotalAndSeparateItems(cartItems: List<CartItem>) : Double {
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
        return totalAmount;
    }

    private fun showSuccessPopup() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_success)

        val successMessage = dialog.findViewById<TextView>(R.id.successMessage)
        val successIcon = dialog.findViewById<ImageView>(R.id.successIcon)

        // Customize message or icon if needed
        successMessage.text = "Purchase Successful"
        successIcon.setImageResource(R.drawable.ic_check_circle)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true) // Make the dialog dismissible
        dialog.show()
    }


//    private fun handlePurchase() {
//        CoroutineScope(Dispatchers.IO).launch {
//            val apiService = RetrofitInstance.api
//            var totalAmount = 0.0
//            var lastTransactionId = ""
//
//            val deferredResponses = bookList.map { book ->
//                async {
//                    try {
//                        if (book.second == "AVAILABLE") {
//                            apiService.sellBook(book.first)
//                        } else {
//                            apiService.buyBack(book.first)
//                        }
//                    } catch (e: Exception) {
//                        null // Handle error gracefully
//                    }
//                }
//            }
//
//            val responses = deferredResponses.awaitAll()
//            responses.filterNotNull().forEach { response ->
//                totalAmount += response.amount
//                lastTransactionId = response.transactionId
//            }
//
//            withContext(Dispatchers.Main) {
//                loadNextActivity(totalAmount, lastTransactionId)
//            }
//        }
//    }

//    private fun loadNextActivity(totalAmount: Double, lastTransactionId: String) {
//        val intent = Intent(this, NextActivity::class.java)
//        intent.putExtra("totalAmount", totalAmount)
//        intent.putExtra("lastTransactionId", lastTransactionId)
//        startActivity(intent)
    }
