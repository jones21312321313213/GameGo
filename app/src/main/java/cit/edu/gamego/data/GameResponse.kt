package cit.edu.gamego.data

// API Response for games
data class GameApiResponse(
    val results: List<GiantBombGame>
)

// Single Game Data
data class GiantBombGame(
    val guid: String, // <-- Add this line to get GUID
    val name: String?,
    val original_release_date: String?,
    val image: Image?,
    val original_game_rating: List<Rating>?
)

data class SingleGameResponse(
    val results: GameDetails
)

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
    val similar_games: List<Game>?
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
    val medium_url: String
)
data class Rating(
    val name: String
)
