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
        @Query("offset") offset: Int = 0
    ): Call<GameApiResponse>

    @GET("games")
    fun getGamesByPlatform(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 15,
        @Query("offset") offset: Int = 0,
        @Query("filter") filter: String // id number of Ps4 in giantbomb
    ): Call<GameApiResponse>


    @GET("games")
    fun getGamesByTheme(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 15,
        @Query("offset") offset: Int = 0,
        @Query("filter") filter: String // e.g., "themes:1"
    ): Call<GameApiResponse>

    // used to get specified game data
    @GET("game/{guid}/")
    fun getGameByGuid(
        @Path("guid") guid: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("field_list") fieldList: String = "name,description,image,original_game_rating,developers,similar_games,themes,videos,genres,platforms,images"
    ): Call<SingleGameResponse>


    @GET("videos/{videoGuid}/")  // Modify to match your API structure
    fun getVideoDetails(
        @Path("videoGuid") videoGuid: String,  // Extract the GUID from the URL dynamically
        @Query("api_key") apiKey: String,
        @Query("format") format: String
    ): Call<SingleVideoResponse>


    @GET("reviews/")
    fun getPopularReviews(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 5,
        @Query("field_list") fields: String = "game,guid"
    ): Call<ReviewListResponse>
}
