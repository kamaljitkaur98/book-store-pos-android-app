package com.example.bookstoreposapp.API.add

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.example.bookstoreposapp.R
import com.example.bookstoreposapp.fragment.NavFragment

class PurchaseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.purchase_details_activity)
        val fragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .addToBackStack(null)
            .commit()
        val qrCodeImage: ImageView = findViewById(R.id.qr_code_image)
        val messageText: TextView = findViewById(R.id.message_text)
        val transactionIdText: TextView = findViewById(R.id.transaction_id_text)

        val id = intent.getStringExtra("id") ?: ""
        val message = intent.getStringExtra("message") ?: ""
        val transactionId = intent.getStringExtra("transactionId") ?: ""

        // Display message and transaction ID
        messageText.text = message
        transactionIdText.text = "Transaction ID: $transactionId"

        // Generate QR Code
        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(id, BarcodeFormat.QR_CODE, 400, 400)
            qrCodeImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
