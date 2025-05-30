package cit.edu.gamego


import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.webkit.WebView
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cit.edu.gamego.extensions.setupAndLoad
import com.bumptech.glide.Glide
import android.view.animation.AnimationUtils
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import cit.edu.gamego.data.SingleGameResponse
import cit.edu.gamego.databinding.ActivityReviewPageBinding
import cit.edu.gamego.extensions.FavoritesDataHolder
import cit.edu.gamego.extensions.toast
import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.SingleVideoResponse
import cit.edu.gamego.extensions.createGame
import cit.edu.gamego.extensions.extractGuidFromShowUrl
import cit.edu.gamego.extensions.extractGuidFromUrl
import com.google.android.material.tabs.TabLayout

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.rejowan.cutetoast.CuteToast

class reviewPageActivity : AppCompatActivity() {


    private lateinit var binding: ActivityReviewPageBinding
    private var isLiked = false
    private var trailer: String = ""
    private var abc: String? = ""
    private var bundle = Bundle()
    private var bundle2 = Bundle()
    private var similarGamesBundle = Bundle()
    private var imagesBundle = Bundle()
    private lateinit var tabLayout: TabLayout
    private lateinit var frameLayout: FrameLayout
    private var gg: Game? = null
    private var gomen: Game? = null
    private lateinit var fallbackk: ImageView
    private var gameGuid: String = ""
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReviewPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

         fallbackk = binding.fallbackImage



        val zoomInAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        val zoomOutAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_out)

        binding.fallbackImage.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                binding.insideHeard.visibility = View.VISIBLE
                binding.heart.setImageResource(R.drawable.baseline_favorite_24)
                binding.insideHeard.bringToFront()
                binding.heart.startAnimation(zoomInAnim)
                binding.insideHeard.startAnimation(zoomInAnim)
                binding.insideHeard.startAnimation(zoomOutAnim)
                isLiked = true
            }
        })


        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val scrollUpButton = findViewById<Button>(R.id.scrollUpButton)
        val ratings = findViewById<LinearLayout>(R.id.ratings)

        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val rect = Rect()
            val isVisible = ratings.getLocalVisibleRect(rect)

            if (!isVisible) {

                scrollUpButton.bringToFront()
                scrollUpButton.invalidate()
                scrollUpButton.requestLayout()
                scrollUpButton.visibility = View.VISIBLE
            } else {

                scrollUpButton.visibility = View.GONE
            }
        }

        scrollUpButton.setOnClickListener {

            scrollView.post {
                scrollView.smoothScrollTo(0, scrollView.top)
            }
        }




        intent?.let {
            val guid = it.getStringExtra("guid") // Get the GUID
            gameGuid = guid.toString()
            if (guid.isNullOrBlank()) {
                // No GUID, use local data from the Intent
                it.getStringExtra("title")?.let { title ->
                    binding.gameTitleRp.text = title
                }
                //go back to this
//                it.getStringExtra("imageRes")?.let { imageRes ->
//                    binding.gamePicRp.setImageResource(imageRes.toString())
//                }
                it.getStringExtra("ratings" )?.let { ratings ->
                    binding.ratingsTvRp.text = ratings
                }
                it.getStringExtra("platform")?.let{platform->
                    binding.platformText.text = platform
                }
                it.getStringExtra("genre")?.let{genre->
                    binding.genreText.text = genre
                }

                trailer = it.getStringExtra("trailer") ?: ""
                gg = createGame(it.getStringExtra("title"),it.getStringExtra("date"), it.getStringExtra("ratings"), it.getStringExtra("desc"), it.getBooleanExtra("isLiked", false), it.getStringExtra("platform"), it.getStringExtra("genre"), it.getStringExtra("theme"), it.getStringExtra("franchise"),it.getStringExtra("publishers"),it.getStringExtra("developer"), it.getStringExtra("alias"))

            } else {
                // GUID is available, fetch data from the API
                fetchGameDetails(guid)
            }
        }



        ////////////////////////////////////////////////
        // Initialize the TabLayout and FrameLayout
        tabLayout = findViewById(R.id.tabLayout)
        frameLayout = findViewById(R.id.frameLayout)

        // Add tabs to TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("About"))
        tabLayout.addTab(tabLayout.newTab().setText("Media"))
        tabLayout.addTab(tabLayout.newTab().setText("Details"))
        tabLayout.addTab(tabLayout.newTab().setText("Similar Games"))
        //load the first fragment by default
        loadFragment(GameDescriptionFragment(),bundle)

        // for local games
        bundle2 = Bundle().apply {
            putString("name", binding.gameTitleRp.text.toString())
            putString("date", gg?.date)
            putString("ratings", gg?.rating)
            putString("description", gg?.description)
            gg?.let { putBoolean("isLiked", it.isLiked) }
            putString("platform", gg?.platform?.joinToString(", "))
            putString("genre", gg?.genre?.joinToString(", "))
            putString("theme", gg?.theme?.joinToString(", "))
            putString("developer",gg?.developer)
            putString("franchise", gg?.franchise?.joinToString(", "))
            putString("publishers", gg?.publishers?.joinToString(", "))
            putString("alias", gg?.alias)
        }

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Replace the fragment based on tab selection
                if (tab != null) {
                    when (tab.position) {
                        0 -> loadFragment(GameDescriptionFragment(),bundle)
                        1 -> loadFragment(GameMediaFragment(),imagesBundle)
                        2 -> loadFragment(GameDetailsFragment(),bundle2)
                        3 -> loadFragment(GameSimilarGamesFragment(),similarGamesBundle)
                    }
                }
            }
            override fun onTabUnselected(p0: TabLayout.Tab?) {
                //
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                //
            }
        })

        binding.backRp.setOnClickListener {
            finish()
        }


        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val dbRef = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
                .getReference("Users")
                .child(uid)
                .child("favorites")
            //checking if this game is already in favorites
            dbRef.get().addOnSuccessListener { snapshot ->
                val favoritesMap = snapshot.value as? Map<String, String> ?: emptyMap()
                val isGameLiked = favoritesMap.containsValue(gameGuid)

                if (isGameLiked) {
                    binding.heart.setImageResource(R.drawable.baseline_favorite_24) // Set heart to red
                    isLiked = true
                } else {
                    binding.heart.setImageResource(R.drawable.baseline_favorite_border_24) // Set heart to empty
                    isLiked = false
                }
            }.addOnFailureListener {
                toast("Failed to load favorites")
            }

            binding.heart.setOnClickListener {
                // fetch favorites reference from Firebase
                val favoritesRef = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
                    .getReference("Users")
                    .child(currentUser?.uid ?: return@setOnClickListener)
                    .child("favorites")

                favoritesRef.get().addOnSuccessListener { snapshot ->
                    // convert snapshot to a map of pushId to gameGuid
                    val favoritesMap = snapshot.value as? Map<String, String> ?: emptyMap()

                    if (isLiked) {
                        // UNHEART: remove game by finding the key with matching gameGuid
                        val keyToRemove = favoritesMap.entries.find { it.value == gameGuid }?.key

                        if (keyToRemove != null) {
                            favoritesRef.child(keyToRemove).removeValue()
                                .addOnSuccessListener {
                                    CuteToast.ct(this, "Removed from Favorites", CuteToast.LENGTH_SHORT, CuteToast.DELETE, true).show();
                                    binding.heart.setImageResource(R.drawable.baseline_favorite_border_24)
                                    binding.heart.startAnimation(zoomInAnim)
                                    isLiked = false
                                }
                                .addOnFailureListener {
                                    toast("Error removing favorite")
                                }
                        } else {
                            CuteToast.ct(this, "Favorite not found", CuteToast.LENGTH_SHORT, CuteToast.WARN, true).show();
                        }
                    } else {

                        // HEART: add game using Firebase's push() for automatic ID generation
                        val newFavoriteRef = favoritesRef.push()
                        newFavoriteRef.setValue(gameGuid)
                            .addOnSuccessListener {
                                CuteToast.ct(this, "Added to favorites", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                                binding.heart.setImageResource(R.drawable.baseline_favorite_24)
                                binding.insideHeard.startAnimation(zoomInAnim)
                                binding.insideHeard.startAnimation(zoomOutAnim)
                                binding.heart.startAnimation(zoomInAnim)
                                isLiked = true
                            }
                            .addOnFailureListener {
                                Log.e("FAVORITES", "Error adding favorite")

                            }
                    }
                }.addOnFailureListener {
                    Log.e("FAVORITES", "Failed to load into faav" )

                }
            }

        }
    }

    private fun loadFragment(fragment: Fragment, bundle: Bundle) {
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }

    abstract class DoubleClickListener : View.OnClickListener {
        private var lastClickTime: Long = 0
        companion object {
            private const val DOUBLE_CLICK_TIME_DELTA = 300
        }
        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v)
            }
            lastClickTime = clickTime
        }
        abstract fun onDoubleClick(v: View?)
    }

    private fun fetchGameDetails(guid: String) {
        val apiKey = BuildConfig.GIANT_BOMB_API_KEY
        val call = ApiClient.api.getGameByGuid(guid, apiKey)
        call.enqueue(object : Callback<SingleGameResponse> {
            override fun onResponse(call: Call<SingleGameResponse>, response: Response<SingleGameResponse>) {
                if (response.isSuccessful) {
                    val game = response.body()?.results
                    Log.d("DETAILS", "Fetched game: ${game?.name}")
                    game?.let {
                        //Name
                        binding.gameTitleRp.text = it.name ?: "Unknown Game"
                        // Description
                        val plainText = HtmlCompat.fromHtml(it.description ?: "No description", HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                        //binding.reviewContent.text = plainText
                        bundle.putString("desc",plainText)
                        //
                        loadFragment(GameDescriptionFragment(),bundle)
                        //
                        val date = it.original_release_date?: "Unknown Date"

                        // Rating
                        val ratingText = it.original_game_rating?.joinToString { r -> r.name } ?: "N/A"
                        binding.ratingsTvRp.text = ratingText
                        // Image
                        it.image?.medium_url?.let { url ->
                            Log.d("MEDIUM URL DETAILS","MEDIUM URL: $url")
                            Glide.with(this@reviewPageActivity).load(url).into(binding.fallbackImage)
                        }


                        // this part of the codes gets the backup image and the video for the reviw page
                        it.image?.super_url?.let{url ->
                            abc = url
                           Glide.with(this@reviewPageActivity).load(url).into(binding.fallbackImage)
                        }


                        // Platform
                        val platforms = it.platforms?.joinToString { p -> p.name } ?: "None"
                        binding.platformText.text = platforms
                        // Genre
                        val genres = it.genres?.joinToString { g -> g.name } ?: "None"
                        binding.genreText.text = genres
                        // Developer
                        val devs = it.developers?.joinToString { d -> d.name } ?: "Unknown"
                        // Theme
                        val themes = it.themes?.joinToString { t -> t.name } ?: "None"
                        binding.themeText.text = themes
                        // Franchise
                        val franchises = it.franchises?.joinToString { f -> f.name } ?: "None"
                        //Publishers
                        val publishers = it.publishers?.joinToString{p -> p.name} ?: "None"
                        // Alias
                        val alias = it.aliases ?: "None"
                        // Similar Games
                        val similarGames = game.similar_games?.map { it.site_detail_url } ?: emptyList()
                        // Images relating to the game
                        val images = game.images?.map{it.medium_url} ?: emptyList()

                        Log.d("SampleImage","Images: $images")
                        gomen = createGame(binding.gameTitleRp.text.toString(),date,"","",false,platforms,genres,themes,franchises,publishers,devs,alias)
                        bundle2 = Bundle().apply {
                            putString("name", binding.gameTitleRp.text.toString())
                            putString("date", gomen?.date)
                            putString("ratings", gomen?.rating)
                            putString("platform", gomen?.platform?.joinToString(", "))
                            putString("genre", gomen?.genre?.joinToString(", "))
                            putString("theme", gomen?.theme?.joinToString(", "))
                            putString("developer",gomen?.developer)
                            putString("franchise", gomen?.franchise?.joinToString(", "))
                            putString("publishers", gomen?.publishers?.joinToString(", "))
                            putString("alias", gomen?.alias)
                        }
                        //cache this
                        similarGamesBundle = Bundle().apply{
                            putString("similarGames", similarGames.joinToString(","))
                        }

                        imagesBundle = Bundle().apply{
                            putString("images",images.joinToString(","))
                        }

                    }
                } else {
                    Log.e("DETAILS", "Error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SingleGameResponse>, t: Throwable) {
                Log.e("DETAILS", "Failure: ${t.message}")
            }
        })
    }



}