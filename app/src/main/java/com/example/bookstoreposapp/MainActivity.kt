package com.example.bookstoreposapp

import BookApiService
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreposapp.API.ApiResponseBook
import com.example.bookstoreposapp.adapters.BookRVAdapter
import com.example.bookstoreposapp.adapters.RetrofitInstance
import com.example.bookstoreposapp.database.CartDatabase
import com.example.bookstoreposapp.fragment.NavFragment
import com.example.bookstoreposapp.model.CartItem
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.Locale
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var qrCodeButton: ImageButton
    private var bookList = ArrayList<BookData>()
    private lateinit var bookRVAdapter: BookRVAdapter
    private var allBooks = ArrayList<BookData>()
    private var displayList = ArrayList<BookData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        searchView = findViewById(R.id.search_view)
        qrCodeButton = findViewById(R.id.qr_code_button)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchBooks(RetrofitInstance.api)
        bookRVAdapter = BookRVAdapter(displayList, this)
        recyclerView.adapter = bookRVAdapter

        val fragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .commit()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        qrCodeButton.setOnClickListener {
            startQRScanner()
        }
    }


    private fun filterList(query: String?){
            val filteredList = ArrayList<BookData>()
    if(query != null && query.isNotEmpty()){
        allBooks.forEach {
            if (it.title.contains(query, ignoreCase = true) || it.isbn.contains(query, ignoreCase = true)
                || it.authors.contains(query, ignoreCase = true) || it.id.contains(query, ignoreCase = true)) {
                if (!it.availability) it.isVisible = true  // Only make unavailable books visible when they match the search
                filteredList.add(it)
                }
                }
                }
    bookRVAdapter.setFilteredList(filteredList)
            if(filteredList.isEmpty()){
                Toast.makeText(this, "No Matching Books found", Toast.LENGTH_SHORT).show()
            }
        }


    private fun fetchBooks(service: BookApiService) {
        service.getBooks().enqueue(object : Callback<List<ApiResponseBook>> {
            override fun onResponse(call: Call<List<ApiResponseBook>>, response: Response<List<ApiResponseBook>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                    allBooks.clear()
                        it.forEach { apiBook ->
                        allBooks.add(mapToBookData(apiBook))
//                            if(apiBook.availability){
//                                displayList.add(mapToBookData(apiBook))
//                            }
                        }
                        displayList.addAll(allBooks)
                        bookRVAdapter.notifyDataSetChanged()

                    }
                } else {
                    Log.e("MainActivity", "Error: ${response.errorBody()?.string()}")

                }
            }

            override fun onFailure(call: Call<List<ApiResponseBook>>, t: Throwable) {
                Log.e("MainActivity", "Failed to fetch books", t)
            }
        })
    }

    private fun mapToBookData(apiBook: ApiResponseBook): BookData {
        return BookData(
            title = apiBook.title,
            image = apiBook.imageURL?:"",
            status = if (apiBook.transactionCount > 0) "Old" else "New",
            originalPrice = "${apiBook.basePrice}",
            discountedPrice = "${apiBook.currentPrice}",
            isbn = apiBook.isbn,
            id = apiBook.id,
            transactionCount = apiBook.transactionCount,
            authors = apiBook.authors,
            edition = apiBook.edition,
            availability = apiBook.availability
        )
    }

    private fun startQRScanner() {
        IntentIntegrator(this).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            setPrompt("Scan a QR Code")
            setCameraId(0)
            setBeepEnabled(true)
            setOrientationLocked(true)
            initiateScan()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            allBooks.forEach {
                if (it.id == result.contents) {
                    it.availability = false
                    it.isVisible = true

                }
            }
            filterList(result.contents)
            Toast.makeText(this, "Scanned: ${result.contents}", Toast.LENGTH_SHORT).show()
        } else if (result != null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

}
