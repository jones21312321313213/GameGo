package cit.edu.gamego

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cit.edu.gamego.data.Game
import cit.edu.gamego.data.Image
import cit.edu.gamego.extensions.extractGuidFromUrl
import cit.edu.gamego.helper.GameMediaRecyclerViewAdapter
import cit.edu.gamego.helper.GameRecyclerViewAdapterwGlide
import com.bumptech.glide.Glide

class GameMediaFragment : Fragment() {
    private val listOfImages = mutableListOf<Game>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameMediaRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_media, container, false)

        val tempImageList: List<String> =
            arguments?.getString("images")?.split(",")?.filter { it.isNotBlank() } ?: emptyList()

        recyclerView = view.findViewById(R.id.horizontalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        tempImageList.forEach { img ->

            val game = Game(
                name = "",
                description = "",
                rating = "",
                photo = Image(img, img)
            )

            listOfImages.add(game)
        }

        adapter = GameMediaRecyclerViewAdapter(
             requireContext(),
            listOfImages,
            onClick = { game ->
                showBigImageDialog(game.photo?.super_url)
            }
        )
        recyclerView.adapter = adapter

        return view
    }

    private fun showBigImageDialog(imageUrl: String?) {
        if (imageUrl.isNullOrBlank()) return

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.image_popout, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.bigImageView)
        val closeButton = dialogView.findViewById<ImageView>(R.id.closeButton)

        Glide.with(requireContext())
            .load(imageUrl)
            .into(imageView)

        val dialog = android.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //animation on close
        closeButton.setOnClickListener {
            closeButton.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction {
                    closeButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction {
                            dialog.dismiss()
                        }
                        .start()
                }
                .start()
        }

        dialog.show()
    }



}
