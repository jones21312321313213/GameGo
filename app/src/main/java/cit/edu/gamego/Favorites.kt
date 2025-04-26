package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.AppCache
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.Image
import cit.edu.gamego.data.SingleGameResponse
import cit.edu.gamego.extensions.FavoritesDataHolder
import cit.edu.gamego.helper.GameListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class Favorites : Activity() {

    private lateinit var favoriteGames: MutableList<Game>
    private lateinit var arrayAdapter: GameListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)


        val listView = findViewById<ListView>(R.id.listview)


        //val img = findViewById<ImageView>(R.id.fav_back)
//        var name: String = "Default Name"
//        var rating: Double = 0.0
//        var img: String = R.drawable.ye.toString()// fallback pic

        favoriteGames = mutableListOf(
            Game("YE Quest", "2030", "1.1", Image(R.drawable.ye.toString(),""), " ", "The visionary's journey through a surreal rap universe.", false, "game_yequest", listOf("PS5", "PC"), "Yeezy Interactive", listOf("Adventure", "Rhythm"), listOf("Hip-Hop", "Satire"), listOf("YE Series"),listOf("Yeezy Productions"), "YQ"),
            //Game("Helldivers", "2022",2.2, R.drawable.helldivers.toString()),
//            Game("Black Myth Wukong", "2024",3.3, R.drawable.bmw.toString()),
//            Game("Monster Hunter World", "2018",4.4, R.drawable.mhw.toString())
        )

        val name = FavoritesDataHolder.title
        val rating = FavoritesDataHolder.rating
        val img = FavoritesDataHolder.imageRes ?: R.drawable.ye.toString()

        //val releaseDate = FavoritesDataHolder.releaseDate

        favoriteGames.add(Game(name,"2030",rating,Image(img,"")))


        //favoriteGames.add( Game(name,date,1.1,Image(R.drawable.ye.toString())))


        arrayAdapter = GameListAdapter(this, favoriteGames,
            onClickMore = { game -> more(game) },
            onClickItem = { game ->
                // Do not add the same game again, just highlight/select it
                abc(game)
            },
            onLongPress = { position -> showDeleteDialog(position) })
        listView.adapter = arrayAdapter

        val btnBack = findViewById<ImageView>(R.id.fav_back)
        btnBack.setOnClickListener {
            finish()
        }
    }


    //add thse to an extension later
    private fun more(game: Game) {
//        startActivity(
//            Intent(this,reviewPageActivity::class.java).apply{
//                putExtra("title",game.name)
//                putExtra("imageRes",game.photo)
//            }
//        )
    }

    private fun abc(game: Game){

    }

    private fun showDeleteDialog(position: Int) {
        // Implement delete logic
    }


}