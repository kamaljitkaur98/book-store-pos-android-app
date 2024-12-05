package com.example.bookstoreposapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.bookstoreposapp.database.CartDatabase
import com.example.bookstoreposapp.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CartRepository
    val allCartItems: LiveData<List<CartItem>>

    init {
        val cartDao = CartDatabase.getDatabase(application).cartDao()
        repository = CartRepository(cartDao)
        allCartItems = repository.allCartItems
    }

    fun insertCartItem(cartItem: CartItem) = viewModelScope.launch {
        repository.insertCartItem(cartItem)
    }

    fun deleteCartItem(cartItem: CartItem) = viewModelScope.launch {
        repository.deleteCartItem(cartItem)
    }

    fun clearCart() = viewModelScope.launch {
        repository.clearCart()
    }
}