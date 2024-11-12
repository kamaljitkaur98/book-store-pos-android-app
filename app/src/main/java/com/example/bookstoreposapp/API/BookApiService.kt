import com.example.bookstoreposapp.API.ApiResponseBook
import retrofit2.Call
import retrofit2.http.GET

interface BookApiService {
    @GET("api/inventory")
    fun getBooks(): Call<List<ApiResponseBook>>
}
