package cit.edu.gamego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class GameDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_details, container, false)

        val name = view?.findViewById<TextView>(R.id.gd_name_tv)
        val date = view?.findViewById<TextView>(R.id.gd_date_tv)
        val platform = view?.findViewById<TextView>(R.id.gd_platform_tv)
        val devs = view?.findViewById<TextView>(R.id.gd_developer_tv)
        val publisher = view?.findViewById<TextView>(R.id.gd_publisher_tv)
        val genre = view?.findViewById<TextView>(R.id.gd_genre_tv)
        val theme = view?.findViewById<TextView>(R.id.gd_theme_tv)
        val franchises = view?.findViewById<TextView>(R.id.gd_franchises_tv)

        arguments?.let { bundle ->
            name?.text = bundle.getString("name")
            date?.text = bundle.getString("date")
            platform?.text = bundle.getString("platform")
            devs?.text = bundle.getString("developer")
            publisher?.text = bundle.getString("publishers")
            genre?.text = bundle.getString("genre")
            theme?.text = bundle.getString("theme")
            franchises?.text = bundle.getString("franchise")
        }
        return view
    }
}