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
import com.example.bookstoreposapp.adapters.RetrofitInstance
import com.example.bookstoreposapp.database.CartDatabase
import com.example.bookstoreposapp.model.CartItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutActivity : AppCompatActivity() {

    var totalAmount = 0.00
    val buyingBackList = mutableListOf<CartItem>()
    val sellingList = mutableListOf<CartItem>()

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

        val paymentButton: Button = findViewById(R.id.proceedToPayment)

        backButton.setOnClickListener {
            onBackPressed() // Navigate back
        }

        paymentButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch{
                handlePayment()
            }
        }
    }

    private suspend fun handlePayment() {
        val nameInput: TextInputEditText = findViewById(R.id.user_name)
        val emailInput: TextInputEditText = findViewById(R.id.user_phone_number)
        val phoneInput: TextInputEditText = findViewById(R.id.user_email)

        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val phone = phoneInput.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields before proceeding", Toast.LENGTH_SHORT).show()
        } else {
                val isSuccess = handlePurchase()
                if(isSuccess){
                    showSuccessPopup()
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateTotalAndSeparateItems(cartItems: List<CartItem>) : Double {
        totalAmount = 0.0
        buyingBackList.clear()
        sellingList.clear()
        for (item in cartItems) {
            when (item.availability) {
                true -> {
                    // Add amount for new items
                    totalAmount += item.discountedPrice.toDouble()
                    sellingList.add(item)
                }
                false -> {
                    // Subtract amount for old items
                    totalAmount -= item.discountedPrice.toDouble()
                    buyingBackList.add(item)
                }
            }
        }
        return totalAmount;
    }

    private fun showSuccessPopup() {
        val dialog = Dialog(this, R.style.PauseDialog)
        dialog.setContentView(R.layout.popup_success)

        val successMessage = dialog.findViewById<TextView>(R.id.successMessage)
        val successIcon = dialog.findViewById<ImageView>(R.id.successIcon)
        successMessage.text = "Purchase Successful"
        successIcon.setImageResource(R.drawable.ic_check_circle)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        dialog.show()
    }


    private suspend fun handlePurchase(): Boolean {
        var isPaymentSuccessful = true

        // Running on the IO dispatcher for network calls
        withContext(Dispatchers.IO) {
            val apiService = RetrofitInstance.api
            var transactionId = ""

            // Handle new cart items
            if (sellingList.isNotEmpty()) {
                sellingList.forEach { item ->
                    val call = apiService.sellBook(item.itemId)
                    val response = call.execute()
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        transactionId = apiResponse?.transactionId ?: ""
                        cartDao.deleteCartItem(item)
                    } else {
                        isPaymentSuccessful = false
                        println("Error: ${response.body()}")
                    }
                }
            }

            // Handle old cart items
            if (buyingBackList.isNotEmpty()) {
                buyingBackList.forEach { item ->
                    val call = apiService.buyBack(item.itemId)
                    println(item)
                    val response = call.execute()
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        transactionId = apiResponse?.transactionId ?: ""
                        cartDao.deleteCartItem(item)
                    } else {
                        isPaymentSuccessful = false
                        println("Error: ${response}")
                    }
                }
            }
        }

        return isPaymentSuccessful
    }
}
