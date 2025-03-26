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

fun EditText.isTextNullOrEmpty(): Boolean {
    return this.text.isNullOrEmpty()
}


fun Context.toast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun List<Game>.filterGames(query: String?): List<Game> {
    if (query.isNullOrEmpty()) {
        return this
    }
    val searchQuery = query.lowercase()
    return this.filter {
        it.name.lowercase().contains(searchQuery) || it.date.contains(searchQuery)
    }
}

inline fun <reified T : Activity> Context.startActivityWithExtras(vararg extras: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    extras.forEach { (key, value) ->
        when (value) {
            is Int -> intent.putExtra(key, value)
            is String -> intent.putExtra(key, value)
            // Add other types as needed
        }
    }
    startActivity(intent)
}

fun showDeleteDialog(position: Int) {
   print("skibidi")
}


//private fun more(game: Game) {
//    startActivity(
//        Intent(requireContext(), reviewPageActivity::class.java).apply{
//            putExtra("title",game.name)
//            putExtra("imageRes",game.photo)
//        }
//    )
//}





