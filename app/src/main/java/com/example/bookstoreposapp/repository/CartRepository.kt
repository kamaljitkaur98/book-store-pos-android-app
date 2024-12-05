package com.example.bookstoreposapp.repository

import androidx.lifecycle.LiveData
import com.example.bookstoreposapp.DAO.CartDao
import com.example.bookstoreposapp.model.CartItem

class CartRepository(private val cartDao: CartDao) {
    val allCartItems: LiveData<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun insertCartItem(cartItem: CartItem) {
        cartDao.insertCartItem(cartItem)
    }

    suspend fun deleteCartItem(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }
}