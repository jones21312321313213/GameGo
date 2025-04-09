package cit.edu.gamego.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Define API interface for Retrofit
interface GiantBombApi {
    @GET("games")
    fun getGames(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 15,
        @Query("sort") sort: String = "original_release_date:asc"
    ): Call<GameApiResponse>

    @GET("game/{guid}/")
    fun getGameByGuid(
        @Path("guid") guid: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("field_list") fieldList: String = "name,description,image,original_game_rating,developers,similar_games,themes,videos,genres,platforms"
    ): Call<SingleGameResponse>


}
