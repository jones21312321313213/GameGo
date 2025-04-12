package cit.edu.gamego.data

import cit.edu.gamego.R
import java.io.Serializable


data class Game(
    var name: String = "",
    var date: String? = "",
    var rating: String = "",
    var photo: Image?,
    val gameTrailer: String? = "",
    var description: String? = "",
    var isLiked: Boolean = false,
    val guid: String = "",
    val platform: List<String>? = emptyList(),
    val developer: String? = "",
    val genre: List<String>? = emptyList(),
    val theme: List<String>? = emptyList(),
    val franchise: List<String>? = emptyList(),
    val publishers: List<String>? = emptyList(),
    val alias: String? = ""
) : Serializable


// todo add platform compatablilit, rating ,genres ,theme,gamemodes