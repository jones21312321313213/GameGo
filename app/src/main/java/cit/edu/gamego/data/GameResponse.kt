package cit.edu.gamego.data

// API Response for games
data class GameApiResponse(
    val results: List<GiantBombGame>
)

// used in getting the popular games using api
data class ReviewListResponse(
    val results: List<GiantBombReview>
)

data class GiantBombReview(
    val guid: String,
    val game: GameInfo?
)

data class GameInfo(
    val name: String?,
    val site_detail_url: String?
)

// Single Game Data
data class GiantBombGame(
    val guid: String, // <-- Add this line to get GUID
    val name: String?,
    val original_release_date: String?,
    val image: Image?,
    val original_game_rating: List<Rating>?
)

// used in getting the details of a single game
data class SingleGameResponse(
    val results: GameDetails
)

data class SingleVideoResponse(
    val results:  List<VideoDetails>
)

data class ImageResponse(
    val results: List<Image>
)
data class VideoDetails(
    val api_detail_url: String?,
    val deck: String?,
    val embed_player: String?, // <- This is the URL you want for WebView
    val guid: String?,
    val id: Int?,
    val length_seconds: Int?,
    val name: String?,
    val site_detail_url: String?,
)
// general
data class GameDetails(
    val name: String?,
    val description: String?,
    val developers: List<Company>?,
    val genres: List<Genre>?,
    val franchises: List<Franchise>?,
    val image: Image?,
    val original_game_rating: List<Rating>?,
    val original_release_date: String?,
    val people: List<Person>?,
    val platforms: List<Platform>?,
    val publishers: List<Company>?,
    val videos: List<Video>?,
    val themes: List<Theme>?,
    val similar_games: List<SimilarGame>?,
    val aliases: String?,
    val images: List<Image>?
)



data class SimilarGame(
    val id: Int?,
    val name: String?,
    val api_detail_url: String?,
    val site_detail_url: String?
)
data class Company(
    val name: String
)
data class Genre(
    val name: String
)
data class Franchise(
    val name: String
)
data class Person(
    val name: String
)
data class Platform(
    val name: String
)
data class Video(
    val name: String,
    val site_detail_url: String
)
data class Theme(
    val name: String
)
data class Image(
    val medium_url: String,
    val super_url: String
)
data class Rating(
    val name: String
)
