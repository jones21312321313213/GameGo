package cit.edu.gamego.helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.R
import cit.edu.gamego.data.Game
import com.bumptech.glide.Glide
import android.util.Patterns

class GameRecyclerViewAdapter(
    private val context: Context,
    private val listOfGame: List<Game>,
    private val onClick: (Game) -> Unit,
    private val isAlternativeLayout: Boolean = false // Flag for layout selection
) : RecyclerView.Adapter<GameRecyclerViewAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo: ImageView = view.findViewById(R.id.photo_irg)
        val name: TextView = view.findViewById(R.id.title_irg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutId = if (isAlternativeLayout) {
            R.layout.item_devs_fav_game
        } else {
            R.layout.items_favorites
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = listOfGame[position]

        // Check if the medium_url is a valid URL
        val url = item.photo?.medium_url
        if (url != null && Patterns.WEB_URL.matcher(url).matches()) {

            Glide.with(holder.itemView.context)
                .load(url)
                .into(holder.photo)
        } else {

            holder.photo.setImageResource(item.photo?.medium_url!!.toInt())
        }

        holder.name.text = item.name

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }


    override fun getItemCount(): Int = listOfGame.size
}
