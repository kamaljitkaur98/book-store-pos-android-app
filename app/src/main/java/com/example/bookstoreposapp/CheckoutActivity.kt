package com.example.bookstoreposapp

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout_activity) // Replace with your actual XML file name

        // Toolbar Views
        val backButton: ImageButton = findViewById(R.id.backButton)
        val titleText: TextView = findViewById(R.id.text_title)

        // Input Fields
        val nameInputLayout: TextInputLayout = findViewById(R.id.input_layout_user_name)
        val nameInput: TextInputEditText = findViewById(R.id.user_name)

        val emailInputLayout: TextInputLayout = findViewById(R.id.input_layout_user_email)
        val emailInput: TextInputEditText = findViewById(R.id.user_phone_number)

        val phoneInputLayout: TextInputLayout = findViewById(R.id.input_layout_user_phone_number)
        val phoneInput: TextInputEditText = findViewById(R.id.user_email)

        // Total Section
        val totalLabel: TextView = findViewById(R.id.totalLabel)
        val totalAmount: TextView = findViewById(R.id.totalAmount)

        // Email Receipt Checkbox
        val emailReceiptCheckbox: CheckBox = findViewById(R.id.emailReceiptCheckbox)

        // Payment Buttons
        val cashButton: Button = findViewById(R.id.cashButton)
        val cardButton: Button = findViewById(R.id.cardButton)

        // Bottom Navigation Fragment (if needed for interaction)
        // val bottomNavFragment = supportFragmentManager.findFragmentById(R.id.bottom_nav_fragment)

        // Set up Click Listeners
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
        // Logic for handling payment method selection
        when (paymentMethod) {
            "Cash" -> {
                // Handle cash payment
                Toast.makeText(this, "Payment Successful using Cash", Toast.LENGTH_SHORT)
            }
            "Card" -> {
                // Handle card payment
                Toast.makeText(this, "Payment Successful using Card", Toast.LENGTH_SHORT)
            }
        }
    }
}
