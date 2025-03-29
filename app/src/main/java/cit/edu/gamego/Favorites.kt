package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cit.edu.gamego.data.Game
import cit.edu.gamego.helper.GameListAdapter

class Favorites : Activity() {

    private lateinit var favoriteGames: MutableList<Game>
    private lateinit var arrayAdapter: GameListAdapter
    private lateinit var date: String;
    private lateinit var name: String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)


        val listView = findViewById<ListView>(R.id.listview)


        //val img = findViewById<ImageView>(R.id.fav_back)
        intent?.let{
            it.getStringExtra("title")?.let{
                name = it
            }
//            it.getIntExtra("imageRes",0).let{imageResId->
//                if (imageResId != 0) {
//                    img.setImageResource(imageResId)
//                }
//            }
        }
        //favoriteGames.add( Game(name,date,R.drawable.ye))

        favoriteGames = mutableListOf(
            Game("ye quest", "2030",1.1, R.drawable.ye),
            Game("Helldivers", "2022",2.2, R.drawable.helldivers),
            Game("Black Myth Wukong", "2024",3.3, R.drawable.bmw),
            Game("Monster Hunter World", "2018",4.4, R.drawable.mhw)
        )




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
