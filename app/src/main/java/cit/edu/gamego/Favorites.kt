package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.AppCache

import cit.edu.gamego.data.Game
import cit.edu.gamego.data.Image
import cit.edu.gamego.data.SingleGameResponse
import cit.edu.gamego.extensions.moreWithGlide
import cit.edu.gamego.helper.GameRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/// continue to fetch guid from firebase
class Favorites : Activity() {

    private lateinit var recyclerView: RecyclerView
    private val favoriteGames: MutableList<Game> = mutableListOf()
    private lateinit var recyclerViewAdapter: GameRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)



        recyclerView = findViewById(R.id.recyclerViewFav)
        recyclerView.layoutManager = LinearLayoutManager(this)


//        val name = FavoritesDataHolder.title
//        val rating = FavoritesDataHolder.rating
//        val img = FavoritesDataHolder.imageRes ?: R.drawable.ye.toString()


        recyclerViewAdapter = GameRecyclerViewAdapter(
            this,
            favoriteGames,
            onClick ={game->
                moreWithGlide(game)
            }
        )
        recyclerView.adapter = recyclerViewAdapter

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val dbRef = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
                .getReference("Users")
                .child(uid)
                .child("favorites")

            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (favSnapshot in snapshot.children) {
                            val guid = favSnapshot.getValue(String::class.java)
                            if (!guid.isNullOrEmpty()) {
                                fetchGameDetails(guid)
                            }
                        }
                    } else {
                        Log.w("FIREBASE", "No favorites found for UID: $uid")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FIREBASE", "DB read failed: ${error.message}")
                }
            })
        } else {
            Log.e("AUTH", "No user is logged in")
            startActivity(Intent(this@Favorites, LoginActivity::class.java))
        }

        //favoriteGames.add( Game(name,date,1.1,Image(R.drawable.ye.toString())))

        val btnBack = findViewById<ImageView>(R.id.fav_back)
        btnBack.setOnClickListener {
            finish()
        }
    }


    //add thse to an extension later

    // only gets guid,name,image
    fun fetchGameDetails(guid: String) {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val call = ApiClient.api.getGameByGuid(guid, apiKey)

        call.enqueue(object : Callback<SingleGameResponse> {
            override fun onResponse(call: Call<SingleGameResponse>, response: Response<SingleGameResponse>) {
                val game = response.body()?.results
                if (response.isSuccessful && game != null) {
                    val simpleGame = Game(
                        name = game.name ?: "Unknown Game",
                        date = "",
                        rating = "",
                        photo = game.image ?: Image("", ""),
                        developer = "",
                        guid = guid,
                        platform = emptyList(),
                        genre = emptyList(),
                        theme = emptyList(),
                        franchise = emptyList(),
                        publishers = emptyList(),
                        alias = ""
                    )
                    favoriteGames.add(simpleGame)
                    recyclerViewAdapter.notifyItemInserted(favoriteGames.size - 1) // Important! Update adapter
                } else {
                    Log.w("DETAILS", "Failed response or null body for GUID: $guid")
                }
            }
            override fun onFailure(call: Call<SingleGameResponse>, t: Throwable) {
                Log.e("DETAILS", "Network failure for GUID $guid: ${t.message}")
            }
        })
    }


    private fun abc(game: Game){

    }

    private fun showDeleteDialog(position: Int) {
        // Implement delete logic
    }


}