package cit.edu.gamego

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProfilePicFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_profile_picture, container, false)



        // Edit button logic
        val btnEdit = view.findViewById<Button>(R.id.edit_Id)
        btnEdit.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfilePicture::class.java))
        }

        // Edit profile picture button logic
        val btnEditProfilePic = view.findViewById<ImageView>(R.id.pp_editinfo_Id)
        btnEditProfilePic.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfilePicture::class.java))
        }

        return view
    }
}
