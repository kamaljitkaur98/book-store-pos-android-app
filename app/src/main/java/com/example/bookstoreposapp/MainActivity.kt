package com.example.bookstoreposapp

import com.example.bookstoreposapp.R
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstoreposapp.adapters.BookRVAdapter
import com.example.bookstoreposapp.fragment.NavFragment
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var bookList = ArrayList<BookData>()
    private lateinit var bookRVAdapter: BookRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        searchView = findViewById(R.id.search_view)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        addDataToList()

        bookRVAdapter = BookRVAdapter(bookList)
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

    private fun addDataToList(){
        bookList.add(BookData("Android Programming", 1, "New", "29.99", "29.99"))
        bookList.add(BookData("Kotlin Programming", 1, "New", "29.99", "29.99"))
        bookList.add(BookData("Android Programming", 1, "New", "29.99", "29.99"))
        bookList.add(BookData("Android Programming", 1, "New", "29.99", "29.99"))
        bookList.add(BookData("Android Programming", 1, "New", "29.99", "29.99"))
    }

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
}