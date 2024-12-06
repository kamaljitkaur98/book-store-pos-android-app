import com.example.bookstoreposapp.API.ApiResponseBook
import com.example.bookstoreposapp.API.add.ApiBookDetailByIsbn
import com.example.bookstoreposapp.API.add.PurchaseResponse
import com.example.bookstoreposapp.API.checkout.ApiSellResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("api/inventory/all")
    fun getBooks(): Call<List<ApiResponseBook>>

    @GET("api/inventory/isbn/{isbn}")
    fun getBookByISBN(@Path("isbn") isbn: String): Call<ApiBookDetailByIsbn>

    @POST("api/inventory/buy/{isbn}/{price}")
    fun buyBook(@Path("isbn") isbn: String, @Path("price") price: Double): Call<PurchaseResponse>

    @POST("api/inventory/sell/{id}")
    fun sellBook(@Path("id") isbn: String): Call<ApiSellResponse>

    @POST("api/inventory/buyback/{id}")
    fun buyBack(@Path("id") isbn: String): Call<ApiSellResponse>
}
