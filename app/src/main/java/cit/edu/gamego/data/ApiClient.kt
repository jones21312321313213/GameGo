package cit.edu.gamego.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://www.giantbomb.com/api/"

    // Retrofit client for making API calls
    val api: GiantBombApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Using Gson to parse the response
            .build()
            .create(GiantBombApi::class.java)
    }


}
