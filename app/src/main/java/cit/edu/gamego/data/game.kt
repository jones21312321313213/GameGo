package cit.edu.gamego.data

import cit.edu.gamego.R


data class Game(
    var name: String = "",
    var date: String = "",
    var rating:Double,
    var photo: Int = R.drawable.aaa,
    val gameTrailer: String = ""

)

// todo add platform compatablilit, rating ,genres ,theme,gamemodes