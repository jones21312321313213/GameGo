package cit.edu.gamego


import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cit.edu.gamego.extensions.setupAndLoad
import com.bumptech.glide.Glide
import android.view.animation.AnimationUtils
import androidx.core.text.HtmlCompat
import cit.edu.gamego.data.SingleGameResponse
import cit.edu.gamego.databinding.ActivityReviewPageBinding
import cit.edu.gamego.extensions.FavoritesDataHolder
import cit.edu.gamego.extensions.toast
import cit.edu.gamego.data.ApiClient
import cit.edu.gamego.data.GameApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class reviewPageActivity : Activity() {

    private lateinit var webView: WebView  // ✅ Declare webView properly

    private lateinit var binding: ActivityReviewPageBinding
    private var isLiked = false
    private var trailer: String = ""
    private var abc: String? = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_review_page)
        val gameImg = findViewById<ImageView>(R.id.game_pic_rp)
        val gameTitle = findViewById<TextView>(R.id.game_title_rp)
        val gameRev = findViewById<TextView>(R.id.review_content)
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
                it.getDoubleExtra("ratings", 0.0).let { ratings ->
                    binding.ratingsTvRp.text = ratings.toString()
                }
                trailer = it.getStringExtra("trailer") ?: ""
            } else {
                // GUID is available, fetch data from the API
                fetchGameDetails(guid)
            }
        }




        // ✅ Initialize WebView
        webView = findViewById(R.id.game_webview)
        webView.setupAndLoad(trailer)

        // temp
//        gameRev.text = when {
//            gameTitle.text.toString().trim().equals("Ye Quest", ignoreCase = true) ->
//                "This game explores how Kanye takes inspiration from controversial figures to make music."
//            gameTitle.text.toString().trim().equals("Black Myth Wukong", ignoreCase = true) ->
//                "An action RPG where players control a monkey warrior inspired by Journey to the West."
//            gameTitle.text.toString().trim().equals("Monster Hunter: World", ignoreCase = true) ->
//                "Embark on the ultimate hunting experience, tracking and battling massive creatures."
//            gameTitle.text.toString().trim().equals("Helldivers 2", ignoreCase = true) ->
//                "A cooperative third-person shooter where players defend Super Earth from alien threats."
//            gameTitle.text.toString().trim().equals("DOTA 2", ignoreCase = true) ->
//                "A competitive MOBA where two teams of five heroes battle to destroy the enemy’s Ancient."
//            gameTitle.text.toString().trim().equals("League of Legends", ignoreCase = true) ->
//                "A fast-paced MOBA where champions with unique abilities fight to control Summoner’s Rift."
//            gameTitle.text.toString().trim().equals("Counter Strike 2", ignoreCase = true) ->
//                "A tactical first-person shooter where teams compete in bomb defusal and hostage rescue modes."
//            gameTitle.text.toString().trim().equals("God of War: Ragnarok", ignoreCase = true) ->
//                "A mythological action-adventure game following Kratos and Atreus on their Norse journey."
//            gameTitle.text.toString().trim().equals("Valorant", ignoreCase = true) ->
//                "A tactical shooter where agents with unique abilities battle in intense 5v5 matches."
//            gameTitle.text.toString().trim().equals("Elden Ring", ignoreCase = true) ->
//                "An open-world action RPG where players explore the Lands Between and battle formidable foes."
//            else -> "No review available."
//        }

        binding.backRp.setOnClickListener {
            finish()
        }

        binding.heart.setOnClickListener {
            if (isLiked) {
                binding.heart.setImageResource(R.drawable.baseline_favorite_border_24)
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
                        binding.gameTitleRp.text = it.name ?: "Unknown Game"

                        // Description
                        val plainText = HtmlCompat.fromHtml(it.description ?: "No description", HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                        binding.reviewContent.text = plainText

                        // Rating
                        val ratingText = it.original_game_rating?.joinToString { r -> r.name } ?: "N/A"
                        binding.ratingsTvRp.text = ratingText

                        // Image
                        it.image?.medium_url?.let { url ->
                            Glide.with(this@reviewPageActivity).load(url).into(binding.gamePicRp)
                            abc = url
                        }

                        // Optional: log more
                        val platform = it.platforms?.joinToString{p -> p.name}?: "None"
                        binding.platformText.text = platform
                        val devs = it.developers?.joinToString { d -> d.name } ?: "Unknown"
                        val genres = it.genres?.joinToString { g -> g.name } ?: "None"
                        binding.genreText.text = genres
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