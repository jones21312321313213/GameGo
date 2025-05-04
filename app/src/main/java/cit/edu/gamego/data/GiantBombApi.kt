package cit.edu.gamego.data

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Define API interface for Retrofit
interface GiantBombApi {
    @GET("games/")
    fun getGames(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("filter") filter: String? = null,
        @Query("field_list") fieldList: String = "name,image,guid,themes,genres",
        @Query("limit") limit: Int = 50,
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


//        @GET("games")
//        suspend fun getGamesByPlatform(
//            @Query("api_key") apiKey: String,
//            @Query("format") format: String = "json",
//            @Query("limit") limit: Int = 15,
//            @Query("offset") offset: Int = 0,
//            @Query("filter") filter: String
//        ): Response<GameApiResponse> // Return a Response object, not Call






    // used to get specified game data
    @GET("game/{guid}/")
    fun getGameByGuid(
        @Path("guid") guid: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("field_list") fieldList: String = "name,description,image,original_game_rating,developers,similar_games,themes,videos,genres,platforms,images,original_release_date"
    ): Call<SingleGameResponse>


    @GET("reviews/")
    fun getPopularReviews(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 5,
        @Query("offset") offset: Int = 0,
        @Query("field_list") fields: String = "game,guid"
    ): Call<ReviewListResponse>

    @GET("search/")
    fun searchGames(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("query") query: String,
        @Query("resources") resources: String = "game"
    ): Call<SearchResponse>


    @GET("games")
    fun advanceSearchGames(
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("filter") filter: String,
        @Query("field_list") fieldList: String = "name,image,guid",
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): Call<GameApiResponse>


    @GET("games")
    suspend fun getGamesByCombinedFilters(
        @Query("api_key") apiKey: String,
        @Query("filter") filter: String,
        @Query("limit") limit: Int = 15
    ): Response<GameApiResponse>

}
