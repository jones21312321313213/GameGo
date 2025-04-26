package cit.edu.gamego.helper
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cit.edu.gamego.R
import cit.edu.gamego.data.Game
import cit.edu.gamego.extensions.setupAndLoad
import com.bumptech.glide.Glide

class GameListAdapter(
    val context: Context,
    val listOfGame: List<Game>,
    val onClickMore: (game: Game) -> Unit,
    val onClickItem: (game: Game) -> Unit,
    val onLongPress: (position: Int) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = listOfGame.size

    override fun getItem(position: Int): Any = listOfGame[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_listview_game,
            parent,
            false
        )

        val photo = view.findViewById<ImageView>(R.id.imageview_photo)
        val name = view.findViewById<TextView>(R.id.textview_name)
        val more = view.findViewById<ImageView>(R.id.iv_more)

        val game = listOfGame[position]

        // Handle the image loading logic here
        val imageStr = game.photo?.medium_url ?: "placeholder"

        if (imageStr.startsWith("http")) {
            // Load from URL using Glide
            Glide.with(context)
                .load(imageStr)
                .placeholder(R.drawable.ye) // Show placeholder while loading
                .error(R.drawable.ye) // Show error placeholder if image fails to load
                .into(photo)
        } else {
            // If the imageStr is not a URL, it could be a local drawable name
            val resId = context.resources.getIdentifier(imageStr, "drawable", context.packageName)
            if (resId != 0) {
                photo.setImageResource(resId) // Load from drawable
            } else {
                photo.setImageResource(R.drawable.ye) // Fallback if the drawable doesn't exist
            }
        }

        name.text = "${game.name} ${game.date}"

        more.setOnClickListener {
            onClickMore(game)
        }

        view.setOnClickListener {
            onClickItem(game)
        }

        view.setOnLongClickListener {
            onLongPress(position)
            true
        }

        return view
    }
}
