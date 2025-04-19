package cit.edu.gamego

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.Image
import cit.edu.gamego.extensions.extractGuidFromUrl
import cit.edu.gamego.helper.GameRecyclerViewAdapterwGlide

class GameMediaFragment : Fragment() {
    private val listOfImages = mutableListOf<Game>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameRecyclerViewAdapterwGlide

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_media, container, false)

        val tempImageList: List<String> =
            arguments?.getString("images")?.split(",")?.filter { it.isNotBlank() } ?: emptyList()

        recyclerView = view.findViewById(R.id.horizontalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        // Populate listOfImages with Game objects containing the image URLs
        tempImageList.forEach { img ->
            // Create a Game object with the necessary data
            val game = Game(
                name = "",
                description = "",
                rating = "",
                photo = Image(img, "")
            )

            listOfImages.add(game)
        }

        // Initialize the adapter
        // Change the layout of this later
        adapter = GameRecyclerViewAdapterwGlide(
             requireContext(),
            listOfImages,
            onClick = { /* Handle click action */ },
            isAlternativeLayout = true
        )

        recyclerView.adapter = adapter

        return view
    }
}
