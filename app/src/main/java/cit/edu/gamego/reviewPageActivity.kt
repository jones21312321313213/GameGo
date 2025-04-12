package cit.edu.gamego


import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import cit.edu.gamego.data.SingleGameResponse
import cit.edu.gamego.databinding.ActivityReviewPageBinding
import cit.edu.gamego.extensions.FavoritesDataHolder
import cit.edu.gamego.extensions.toast
import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.Image
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class reviewPageActivity : AppCompatActivity() {

    private lateinit var webView: WebView  // ✅ Declare webView properly

    private lateinit var binding: ActivityReviewPageBinding
    private var isLiked = false
    private var trailer: String = ""
    private var abc: String? = ""
    private var bundle = Bundle()
    private var bundle2 = Bundle()
    private var bundle3 = Bundle()
    private lateinit var tabLayout: TabLayout
    private lateinit var frameLayout: FrameLayout
    private var gg: Game? = null
    private var gomen: Game? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_review_page)
        val gameImg = findViewById<ImageView>(R.id.game_pic_rp)
        val gameTitle = findViewById<TextView>(R.id.game_title_rp)

        val rating = findViewById<TextView>(R.id.ratings_tv_rp)
        //val back = findViewById<ImageView>(R.id.back_rp)

        binding = ActivityReviewPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val zoomInAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        val zoomOutAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_out)

        binding.gamePicRp.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                binding.heart.setImageResource(R.drawable.baseline_favorite_24)
                binding.heart.startAnimation(zoomInAnim)
                binding.insideHeard.startAnimation(zoomInAnim)
                binding.insideHeard.startAnimation(zoomOutAnim)
                isLiked = true
            }
        })

        var abc: String? = ""

        intent?.let {
            val guid = it.getStringExtra("guid") // Get the GUID
            if (guid.isNullOrBlank()) {
                // No GUID, use local data from the Intent
                it.getStringExtra("title")?.let { title ->
                    binding.gameTitleRp.text = title
                }
                it.getStringExtra("imageRes")?.let { imageRes ->
                    // Use local image resource (assuming it's an integer)
                    binding.gamePicRp.setImageResource(imageRes.toInt())
                }
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
        // temp
        val title = binding.gameTitleRp.text.toString().trim()
        val gameRev = when {
            title.equals("Ye Quest", ignoreCase = true) ->
                "This game explores how Kanye takes inspiration from controversial figures to make music."
            title.equals("Black Myth Wukong", ignoreCase = true) ->
                "An action RPG where players control a monkey warrior inspired by Journey to the West."
            title.equals("Monster Hunter: World", ignoreCase = true) ->
                "Embark on the ultimate hunting experience, tracking and battling massive creatures."
            title.equals("Helldivers 2", ignoreCase = true) ->
                "A cooperative third-person shooter where players defend Super Earth from alien threats."
            title.equals("DOTA 2", ignoreCase = true) ->
                "A competitive MOBA where two teams of five heroes battle to destroy the enemy’s Ancient."
            title.equals("League of Legends", ignoreCase = true) ->
                "A fast-paced MOBA where champions with unique abilities fight to control Summoner’s Rift."
            title.equals("Counter Strike 2", ignoreCase = true) ->
                "A tactical first-person shooter where teams compete in bomb defusal and hostage rescue modes."
            title.equals("God of War: Ragnarok", ignoreCase = true) ->
                "A mythological action-adventure game following Kratos and Atreus on their Norse journey."
            title.equals("Valorant", ignoreCase = true) ->
                "A tactical shooter where agents with unique abilities battle in intense 5v5 matches."
            title.equals("Elden Ring", ignoreCase = true) ->
                "An open-world action RPG where players explore the Lands Between and battle formidable foes."
            else -> " "
        }
        gg?.description = gameRev
        bundle.putString("review", gameRev)

        ////////////////////////////////////////////////
        // Initialize the TabLayout and FrameLayout
        tabLayout = findViewById(R.id.tabLayout)
        frameLayout = findViewById(R.id.frameLayout)

        // Add tabs to TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("About"))
        tabLayout.addTab(tabLayout.newTab().setText("Media"))
        tabLayout.addTab(tabLayout.newTab().setText("Details"))
        // Load the first fragment by default
        loadFragment(GameDescriptionFragment(),bundle)


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

        // Handle Tab selection
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Replace the fragment based on tab selection
                if (tab != null) {
                    when (tab.position) {
                        0 -> loadFragment(GameDescriptionFragment(),bundle)  // First tab
                        1 -> loadFragment(GameMediaFragment(),bundle) // Second tab
                        2 -> loadFragment(GameDetailsFragment(),bundle2)
                    }
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                // Handle tab unselection if necessary
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
                // Handle tab reselection if necessary
            }
        })

        // ✅ Initialize WebView
        webView = findViewById(R.id.game_webview)
        webView.setupAndLoad(trailer)


        binding.backRp.setOnClickListener {
            finish()
        }

        binding.heart.setOnClickListener {
            if (isLiked) {
                binding.heart.setImageResource(R.drawable.baseline_favorite_border_24)
                toast("removed game from favorites")
            } else {
                binding.heart.setImageResource(R.drawable.baseline_favorite_24)
                binding.insideHeard.startAnimation(zoomInAnim)
                binding.insideHeard.startAnimation(zoomOutAnim)

                FavoritesDataHolder.title =  binding.gameTitleRp.text.toString()
                FavoritesDataHolder.releaseDate = "21"
                FavoritesDataHolder.imageRes = abc.toString()
                toast("added game to favorites")
            }

            binding.heart.startAnimation(zoomInAnim)
            isLiked = !isLiked
        }

    }

    private fun createGame(title: String?,
                           date: String?,
                           ratings: String?,
                           desc: String?,
                           isLiked: Boolean,
                           platform: String?,
                           genre: String?,
                           theme: String?,
                           franchise: String?,
                           publishers: String?,
                           developer: String?, alias: String?): Game {
        return Game(
            guid = "",
            name = title ?: "Unknown Game",
            date = date ?: "Unknown Date", // Set a default value for date or pass from the Intent if needed
            rating = ratings ?: "0.0", // Default to "0.0" if no rating is provided
            gameTrailer =  "",
            photo = Image(""),
            description = desc,
            isLiked = isLiked,
            platform = platform?.split(",") ?: emptyList(),
            genre = genre?.split(",") ?: emptyList(),
            theme = theme?.split(",") ?: emptyList(),
            franchise = franchise?.split(",") ?: emptyList(),
            publishers = publishers?.split(",")?: emptyList(),
            developer =  developer,
            alias = alias
        )
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
        val apiKey = BuildConfig.Giant_Bomb_API_KEY
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
                            Glide.with(this@reviewPageActivity).load(url).into(binding.gamePicRp)
                            abc = url
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
                        // Franchise
                        val franchises = it.franchises?.joinToString { f -> f.name } ?: "None"
                        //Publishers
                        val publishers = it.publishers?.joinToString{p -> p.name} ?: "None"
                        // Alias
                        val alias = it.aliases ?: "None"
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
                        Log.d("DETAILS", "Devs: $devs | Genres: $genres")
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