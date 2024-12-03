package com.example.bookstoreposapp

import BookApiService
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreposapp.API.ApiResponseBook
import com.example.bookstoreposapp.adapters.BookRVAdapter
import com.example.bookstoreposapp.adapters.RetrofitInstance
import com.example.bookstoreposapp.fragment.NavFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.Locale
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var bookList = ArrayList<BookData>()
    private lateinit var bookRVAdapter: BookRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        searchView = findViewById(R.id.search_view)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        //addDataToList()
        fetchBooks(RetrofitInstance.api)
        bookRVAdapter = BookRVAdapter(bookList, this)
        recyclerView.adapter = bookRVAdapter

        val fragment = NavFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.bottom_nav_fragment, fragment)
            .addToBackStack(null)
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
    }

//    private fun addDataToList(){
//        bookList.add(BookData("Android Programming", 1, "New", "29.99", "29.99"))
//        bookList.add(BookData("Kotlin Programming", 1, "New", "29.99", "29.99"))
//        bookList.add(BookData("Android Programming", 1, "New", "29.99", "29.99"))
//        bookList.add(BookData("Android Programming", 1, "New", "29.99", "29.99"))
//        bookList.add(BookData("Android Programming", 1, "New", "29.99", "29.99"))
//    }

    private fun filterList(query: String?){
        if(query != null){
            val filteredList = ArrayList<BookData>()
            for(i in bookList){
                if(i.title.toLowerCase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty()){
                Toast.makeText(this, "No Matching Books found", Toast.LENGTH_SHORT).show()
            }else{
                bookRVAdapter.setFilteredList(filteredList)
            }
        }
    }

    private fun fetchBooks(service: BookApiService) {
        service.getBooks().enqueue(object : Callback<List<ApiResponseBook>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<ApiResponseBook>>, response: Response<List<ApiResponseBook>>) {
                if (response.isSuccessful) {
                    Log.e("MainActivity", "Succcess: Building book list")
                    response.body()?.let {

                        it.forEach { apiBook ->
                            bookList.add(mapToBookData(apiBook))
                        }
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
            transactionCount = apiBook.transactionCount
        )
    }



}
