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
import cit.edu.gamego.LoginActivity
import cit.edu.gamego.R
import cit.edu.gamego.data.Game
import cit.edu.gamego.reviewPageActivity
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.annotation.SuppressLint
import android.util.Log
import android.widget.ImageView
import cit.edu.gamego.data.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import cit.edu.gamego.data.GameApiResponse
import cit.edu.gamego.data.GiantBombGame
import cit.edu.gamego.data.Image
import cit.edu.gamego.data.ReviewListResponse
import cit.edu.gamego.data.SingleGameResponse


//This is for API call
@SuppressLint("NotifyDataSetChanged")
fun Call<GameApiResponse>.enqueueGameList(
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
                    onNotify()
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
fun WebView.setupAndLoad(trailer: String, fallbackImageView: ImageView) {
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.cacheMode = WebSettings.LOAD_NO_CACHE
    settings.mediaPlaybackRequiresUserGesture = false

    webViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            evaluateJavascript(
                """(function() {
                    var videos = document.getElementsByTagName('video');
                    return videos.length > 0;
                })();"""
            ) { result ->
                val hasVideo = result == "true"

                if (hasVideo) {
                    this@setupAndLoad.visibility = View.VISIBLE
                    fallbackImageView.visibility = View.GONE
                } else {
                    this@setupAndLoad.visibility = View.GONE
                    fallbackImageView.visibility = View.VISIBLE
                }
            }
        }
    }

    loadDataWithBaseURL(null, trailer, "text/html", "utf-8", null)
}


fun EditText.isTextNullOrEmpty(): Boolean {
    return this.text.isNullOrEmpty()
}


fun Context.toast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun showDeleteDialog(position: Int) {
   print("skibidi")
}

// temp
object FavoritesDataHolder {
    var title: String = ""
    var releaseDate: String = ""
    var imageRes: String = ""
    var rating: String = ""
    val isLiked: Boolean = true
}







