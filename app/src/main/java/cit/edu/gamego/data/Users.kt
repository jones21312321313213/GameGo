package cit.edu.gamego.data

data class Users(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val favorites: List<String> = emptyList()
){

}
