package cit.edu.gamego

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ProfilePicFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.profile_picture, container, false)

        val usern1 = view.findViewById<TextView>(R.id.username_tv)
        val email1 = view.findViewById<TextView>(R.id.email_tv)
        val name = view.findViewById<TextView>(R.id.name_tv)
        val email = view.findViewById<TextView>(R.id.email_view_tv)
        val pass = view.findViewById<TextView>(R.id.password_tv)
        val show_pass = view.findViewById<ImageView>(R.id.show_password)

        var passnotHidden: String?
        val passhid: String
        var isHidden = true

        // Get the original login data first
        var username = arguments?.getString("username")
        var emailText = arguments?.getString("email")
        var password = arguments?.getString("password")

        // Check if there is new data (edited profile) and update only if it's not null
        val newUsername = arguments?.getString("new_username")
        val newEmail = arguments?.getString("new_email")
        val newPassword = arguments?.getString("new_password")

        if (!newUsername.isNullOrEmpty()) username = newUsername
        if (!newEmail.isNullOrEmpty()) emailText = newEmail
        if (!newPassword.isNullOrEmpty()) password = newPassword

        // Set the data to views
        name.text = username
        usern1.text = username
        email1.text = emailText
        email.text = emailText
        pass.text = password

        // Hide password initially
        passnotHidden = pass.text.toString()
        pass.text = "*".repeat(passnotHidden.length)
        passhid = pass.text.toString()

        // Toggle password visibility
        show_pass.setOnClickListener {
            if (isHidden) {
                pass.text = passnotHidden
            } else {
                pass.text = passhid
            }
            isHidden = !isHidden
        }

        // Edit button logic
        val btnEdit = view.findViewById<Button>(R.id.edit_Id)
        btnEdit.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfilePicture::class.java))
        }

        return view
    }
}
