package cit.edu.gamego.data

object AppCache {

    // 🎮 Game cache: key = GUID
    val gameCache = mutableMapOf<String, Game>()

    // 📝 Review cache: key = review ID
    val reviewCache = mutableMapOf<String, ReviewListResponse>()

}
