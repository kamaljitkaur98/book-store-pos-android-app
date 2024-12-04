package com.example.bookstoreposapp

import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bookstoreposapp.fragment.NavFragment

class BookDetailActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.book_details_activity)
        val book = intent.getSerializableExtra("bookData") as BookData
        book?.let { populateBookDetails(it) }

        val fragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .addToBackStack(null)
            .commit()

        val backButton: ImageButton = findViewById(R.id.backButton)
//        backButton.setOnClickListener {
//            onBackPressed()
//        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Youtube Video Uploaded
        val youtubeWebView: WebView = findViewById(R.id.youtubeWebView)
        val videoURL = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/V2KCAfHjySQ?si=jAxh0SCxk4sRTYa7\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
        youtubeWebView.loadData(videoURL,"text/html", "utf-8")
        youtubeWebView.settings.javaScriptEnabled = true
        youtubeWebView.webChromeClient = WebChromeClient()
    }



    private fun populateBookDetails(book: BookData) {
        Glide.with(this)
            .load(book.image) // URL of the image
            .placeholder(R.drawable.default_book_image) // Optional: Placeholder image
            .error(R.drawable.default_book_image) // Optional: Error image if loading fails
            .into(findViewById(R.id.bookImage)) // Target ImageView
        findViewById<TextView>(R.id.bookTitle).text = book.title
        findViewById<TextView>(R.id.originalPrice).text = book.originalPrice
        findViewById<TextView>(R.id.ownershipStatus).text = book.status
        findViewById<TextView>(R.id.description).text = "Sample Description"
        findViewById<TextView>(R.id.isbnInfo).text = "ISBN: ${book.isbn}"
        findViewById<TextView>(R.id.uuidInfo).text = "UUID: ${book.id}"
    }
}