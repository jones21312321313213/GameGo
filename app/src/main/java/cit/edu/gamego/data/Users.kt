package cit.edu.gamego.data

data class Users(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val listOfLikedGames: List<String> = emptyList()
){

}
