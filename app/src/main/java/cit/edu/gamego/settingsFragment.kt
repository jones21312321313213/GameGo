package cit.edu.gamego

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class settingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_settings, container, false)
        

        val about_game = view.findViewById<ImageView>(R.id.about_the_game_settings)
        val about_devs = view.findViewById<ImageView>(R.id.about_devs)
        val edit_profile = view.findViewById<ImageView>(R.id.edit_profilepic_settings)

        about_game.setOnClickListener{
            startActivity(
                Intent(requireContext(),about_the_game::class.java)
            )
        }

        about_devs.setOnClickListener{
            startActivity(
                Intent(requireContext(),activity_developer::class.java)
            )
        }

        edit_profile.setOnClickListener{
            startActivity(
                Intent(requireContext(),EditProfilePicture::class.java)
            )
        }
        return view;
    }
}