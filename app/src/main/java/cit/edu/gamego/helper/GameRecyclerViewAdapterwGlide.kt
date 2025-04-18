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

class  GameRecyclerViewAdapterwGlide(
    private val context: Context,  // Pass the context to determine activity
    private val listOfGame: List<Game>,
    private val onClick: (Game) -> Unit,
    private val isAlternativeLayout: Boolean = false // Flag for layout selection
) : RecyclerView.Adapter<GameRecyclerViewAdapterwGlide.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo: ImageView = view.findViewById(R.id.photo_irg)
        val name: TextView = view.findViewById(R.id.title_irg)
        val ratings: TextView = view.findViewById(R.id.ratings_irg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutId = if (isAlternativeLayout) {
            R.layout.item_devs_fav_game // Use alternative layout
        } else {
            R.layout.item_recyclerview_game // Default layout
        }

        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = listOfGame[position]

        Glide.with(holder.itemView.context).load(item.photo?.medium_url).into(holder.photo)
//
//        val imageUrl = item.photo?.medium_url
//
//        if (!imageUrl.isNullOrBlank()) {
//            Glide.with(holder.itemView.context)
//                .load(imageUrl)
//                .placeholder(R.drawable.ye) // while loading
//                .error(R.drawable.aaa)  // if failed
//                .centerCrop()                        // for clean scaling
//                .into(holder.photo)
//        } else {
//            holder.photo.setImageResource(R.drawable.aaa)
//        }
        //holder.photo.setImageResource(item.photo.toInt())
        holder.name.text = item.name
        holder.ratings.text = item.rating.toString()

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = listOfGame.size
}
