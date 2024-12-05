package com.example.bookstoreposapp.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bookstoreposapp.model.CartItem

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): LiveData<List<CartItem>>

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
