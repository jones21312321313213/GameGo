package cit.edu.gamego

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProfilePicFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_profile_picture, container, false)

        val usern1 = view.findViewById<TextView>(R.id.username_tv)
        val email1 = view.findViewById<TextView>(R.id.email_tv)
        val name = view.findViewById<TextView>(R.id.name_tv)
        val email = view.findViewById<TextView>(R.id.email_view_tv)
        val phone = view.findViewById<TextView>(R.id.phonenumber_tv)
        val pass = view.findViewById<TextView>(R.id.password_tv)


        arguments?.let{
            usern1.setText(it.getString("username"))
            email1.setText(it.getString("email"))
            email.setText(it.getString("email"))
            pass.setText(it.getString("password"))
        }


//        arguments?.let{
//            usern1.setText(it.getString("new_username"))
//            email1.setText(it.getString("new_email"))
//            email.setText(it.getString("new_email"))
//            pass.setText(it.getString("new_password"))
//        }


        // Edit button logic
        val btnEdit = view.findViewById<Button>(R.id.edit_Id)
        btnEdit.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfilePicture::class.java))
        }

        // Edit profile picture button logic
        val btnEditProfilePic = view.findViewById<ImageView>(R.id.pp_editinfo_Id)
        btnEditProfilePic.setOnClickListener {
            startActivity(
                Intent(requireContext(), EditProfilePicture::class.java).apply {
                    putExtra("username",usern1.toString())
                    putExtra("password",pass.toString())
                    putExtra("emai",email.toString())
                }
            )

        }

        return view
    }
}
