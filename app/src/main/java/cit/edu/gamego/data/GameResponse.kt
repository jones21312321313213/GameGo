package cit.edu.gamego.data

// API Response for games
data class GameApiResponse(
    val results: List<GiantBombGame>
)

// Single Game Data
data class GiantBombGame(
    val name: String?,
    val original_release_date: String?,
    val image: Image?,
    val original_game_rating: List<Rating>?
)

// Image class for storing the image URL
data class Image(
    val medium_url: String
) {
    fun toInt(): Int {
        return this.toInt()
    }
}

// Rating class to handle game ratings
data class Rating(
    val name: String
)
