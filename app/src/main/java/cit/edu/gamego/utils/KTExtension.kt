package cit.edu.gamego.extensions

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cit.edu.gamego.R
import cit.edu.gamego.data.Game
import cit.edu.gamego.reviewPageActivity
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import cit.edu.gamego.LoginActivity
import cit.edu.gamego.data.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import cit.edu.gamego.data.GameApiResponse
import cit.edu.gamego.data.GameResult
import cit.edu.gamego.data.GiantBombGame
import cit.edu.gamego.data.Image
import cit.edu.gamego.data.ReviewListResponse
import cit.edu.gamego.data.SingleGameResponse
import com.bumptech.glide.Glide


//This is for API call
@SuppressLint("NotifyDataSetChanged")
fun Call<GameApiResponse>.enqueueGameList(
    lifecycleOwner: LifecycleOwner, // Pass the LifecycleOwner (Fragment or Activity)
    list: MutableList<Game>,
    onNotify: () -> Unit
) {
    this.enqueue(object : Callback<GameApiResponse> {
        override fun onResponse(
            call: Call<GameApiResponse>,
            response: Response<GameApiResponse>
        ) {
            if (response.isSuccessful) {
                val games = response.body()?.results
                Log.d("API", "Fetched ${games?.size} games")
                if (games != null) {
                    list.clear()
                    list.addAll(games.map { game ->
                        Game(
                            guid = game.guid,
                            name = game.name ?: "Unknown",
                            date = game.original_release_date,
                            rating = "1.1",
                            photo = game.image,
                            gameTrailer = """
                                <iframe width="100%" height="100%" src="https://www.youtube.com/embed/pnSsgRJmsCc?si=Fy9aZVKwThO7lKAi" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
                            """.trimIndent(),
                            description = "",
                            isLiked = false,
                            platform = emptyList(),
                            developer = "",
                            genre = emptyList(),
                            theme = emptyList(),
                            franchise = emptyList(),
                            publishers = emptyList(),
                            alias = "",
                        )
                    })

                    // ✅ Safely run the UI update within the lifecycleOwner's lifecycleScope
                    lifecycleOwner.lifecycleScope.launchWhenStarted {
                        onNotify() // Trigger the UI update
                    }
                }
            } else {
                Log.e("API", "Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<GameApiResponse>, t: Throwable) {
            Log.e("API", "Failed: ${t.message}")
        }
    })
}



fun String.extractGuidFromUrl(): String? {
    return this.trimEnd('/').split("/").lastOrNull()
}

// this is for videos since their URL is like https://giantbomb/videos/(video guid)/(randomnumber)
// we do not want to get the random number
fun String.extractGuidFromShowUrl(): String? {
    val segments = this.trimEnd('/').split("/")
    return if (segments.size >= 2) {
        val maybeGuid = segments[segments.size - 2]
        if (maybeGuid.matches(Regex("\\d+-\\d+"))) maybeGuid else null
    } else {
        null
    }
}


fun Context.showConfirmation(message: String) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.activity_logout_popout)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    // Set popup width and height
    val widthInPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 400f, resources.displayMetrics
    ).toInt()
    val heightInPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics
    ).toInt()

    dialog.window?.setLayout(widthInPx, heightInPx)

    val btnYes = dialog.findViewById<Button>(R.id.yesLogout_id)
    val btnNo = dialog.findViewById<Button>(R.id.noLogout_id)
    val msg = dialog.findViewById<TextView>(R.id.message)

    msg.text = message
    dialog.show()

    btnYes.setOnClickListener {
        Toast.makeText(this, "Logged out", Toast.LENGTH_LONG).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    btnNo.setOnClickListener {
        dialog.dismiss()
    }
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.setupAndLoad(trailer: String, fallbackImageView: ImageView, super_url: String?) {
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.cacheMode = WebSettings.LOAD_NO_CACHE
    settings.mediaPlaybackRequiresUserGesture = false

    Log.d("setUpANDLOAD URL", "URL: $trailer")

    webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            evaluateJavascript(
                """(function() {
                    var videos = document.getElementsByTagName('video');
                    var iframes = document.getElementsByTagName('iframe');
                    return videos.length > 0 || iframes.length > 0;
                })();"""
            ) { result ->
                val hasVideo = result == "true"

                if (hasVideo) {
                    this@setupAndLoad.visibility = View.VISIBLE
                    fallbackImageView.visibility = View.GONE
                } else {
                    this@setupAndLoad.visibility = View.GONE
                    fallbackImageView.visibility = View.VISIBLE

                    val safeContext = fallbackImageView.context
                    if (safeContext is Activity && !safeContext.isFinishing && !safeContext.isDestroyed) {
                        if (!super_url.isNullOrBlank()) {
                            Glide.with(safeContext)
                                .load(super_url)
                                .centerCrop()
                                .into(fallbackImageView)
                        } else {
                            fallbackImageView.setImageResource(R.drawable.ye)
                        }
                    }
                }
            }
        }
    }
    loadUrl(trailer)
}

// both will go from curent act/fragment to reviewPageActivity
fun Context.moreWithGlide(game: Game) {
    val intent = Intent(this, reviewPageActivity::class.java).apply {
        putExtra("guid", game.guid)
    }
    startActivity(intent)
}

fun Fragment.moreWithGlideFragment(game: Game) {
    val intent = Intent(requireContext(), reviewPageActivity::class.java).apply {
        putExtra("guid", game.guid)
    }
    startActivity(intent)
}

// this will show the images,name of the games in the seach view
 fun GameResult.toGame(): Game {
    return Game(
        guid = this.guid,
        name = this.name,
        photo = Image(
            this.image?.medium_url ?: "",
            this.image?.super_url ?: ""
        )
    )
}
fun EditText.isTextNullOrEmpty(): Boolean {
    return this.text.isNullOrEmpty()
}


fun Context.toast(message: CharSequence) {
    return Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun showDeleteDialog(position: Int) {
   print("skibidi")
}

// used in fetchGame details or api calls that will individually get games GUID and make them as a game
fun createGame(title: String?,
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
        date = date ?: "Unknown Date",
        rating = ratings ?: "0.0",
        gameTrailer =  "",
        photo = Image("",""),
        description = desc,
        isLiked = isLiked,
        platform = platform?.split(",") ?: emptyList(),
        genre = genre?.split(",") ?: emptyList(),
        theme = theme?.split(",") ?: emptyList(),
        franchise = franchise?.split(",") ?: emptyList(),
        publishers = publishers?.split(",")?: emptyList(),
        developer =  developer,
        alias = alias,
    )
}

// temp
object FavoritesDataHolder {
    var title: String = ""
    var releaseDate: String = ""
    var imageRes: String = ""
    var rating: String = ""
    val isLiked: Boolean = true
}







