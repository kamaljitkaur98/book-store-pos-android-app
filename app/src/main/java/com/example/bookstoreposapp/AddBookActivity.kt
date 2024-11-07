package com.example.bookstoreposapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstoreposapp.fragment.NavFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddBookActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.add_book_activity)

        val fragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .addToBackStack(null)
            .commit()

        //Feel like we dont need this back button as buttom navbar is already there

//        val addBookButton: ImageButton = findViewById(R.id.button_back)
//
//        addBookButton.setOnClickListener {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }

        val addInventoryButton : MaterialButton = findViewById(R.id.button_add_inventory)

        val bookName: TextInputEditText = findViewById(R.id.edit_text_book_name)
        val bookDescription: TextInputEditText = findViewById(R.id.edit_text_book_description)
        val bookISBN: TextInputEditText = findViewById(R.id.edit_text_isbn)
        val bookPrice: TextInputEditText = findViewById(R.id.edit_text_price)

        addInventoryButton.setOnClickListener{
            val name = bookName.text.toString()
            val description = bookDescription.text.toString()
            val isbn = bookISBN.text.toString()
            val price = bookPrice.text.toString()
            //TODO("Logic to send data to backend API")
        }

    }
}