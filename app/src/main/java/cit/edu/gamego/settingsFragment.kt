package cit.edu.gamego

import android.content.Intent
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
        val edit_password = view.findViewById<ImageView>(R.id.edit_profilepic_settings)
        val edit_profilepicture = view.findViewById<ImageView>(R.id.edit_profilepic)
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

        edit_password.setOnClickListener{
            startActivity(
                Intent(requireContext(),EditProfilePicture::class.java)
            )
        }

        edit_profilepicture.setOnClickListener{
            startActivity(
                Intent(requireContext(),ChangeProfilePic::class.java)
            )
        }
        return view;
    }
}