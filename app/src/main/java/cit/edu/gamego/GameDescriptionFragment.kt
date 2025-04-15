package cit.edu.gamego

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class GameDescriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_game, container, false)
        val d = view.findViewById<TextView>(R.id.about_game_tv)

        // Get the review or description from arguments
        val review = arguments?.getString("review")
        val description = arguments?.getString("desc")

        // Show review if it's not blank, otherwise show description
        d.text = if (review.isNullOrBlank()) {
            description ?: "No Description Available"
        } else {
            review
        }

        return view
    }
}
