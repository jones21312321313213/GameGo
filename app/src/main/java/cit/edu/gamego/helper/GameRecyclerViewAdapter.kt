package cit.edu.gamego.helper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.R
import cit.edu.gamego.data.Game

class GameRecyclerViewAdapter(
    private val listOfGame: List<Game>,
    private val onClick:(Game) ->Unit
    ):RecyclerView.Adapter<GameRecyclerViewAdapter.ItemViewHolder>(){

        class ItemViewHolder(view: View):RecyclerView.ViewHolder(view){
            val photo =view.findViewById<ImageView>(R.id.photo_irg)
            val name = view.findViewById<TextView>(R.id.title_irg)
            val ratings = view.findViewById<TextView>(R.id.ratings_irg)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):GameRecyclerViewAdapter.ItemViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview_game,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GameRecyclerViewAdapter.ItemViewHolder,
        position: Int
    ) {
        val item = listOfGame[position]
        holder.photo.setImageResource(item.photo)
        holder.name.setText(item.name)
        holder.ratings.setText(item.rating.toString())

        holder.itemView.setOnClickListener{
            onClick(item)
        }
    }
    override fun getItemCount(): Int = listOfGame.size
}