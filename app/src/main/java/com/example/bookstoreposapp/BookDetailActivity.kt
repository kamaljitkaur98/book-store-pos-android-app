package com.example.bookstoreposapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstoreposapp.fragment.NavFragment

class BookDetailActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.book_details_activity)

        val fragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .addToBackStack(null)
            .commit()

        val backButton: ImageButton = findViewById(R.id.backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}