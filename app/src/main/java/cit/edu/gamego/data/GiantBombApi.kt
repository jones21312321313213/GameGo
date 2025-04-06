package cit.edu.gamego.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Define API interface for Retrofit
interface GiantBombApi {
    @GET("games")
    fun getGames(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10,
        @Query("sort") sort: String = "original_release_date:desc"
    ): Call<GameApiResponse>

}
