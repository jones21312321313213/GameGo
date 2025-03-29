package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class ProfilePicFragment : Fragment() {
    private var currentPassword: String = ""
    private var isHidden = true

    // temp
    private val editProfileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val newUsername = data?.getStringExtra("new_username") ?: ""
            val newPassword = data?.getStringExtra("new_password") ?: ""
            val newEmail = data?.getStringExtra("new_email") ?: ""
            val newPhone = data?.getStringExtra("new_phone")?:""
            // ✅ Update the password variable
            if (newPassword.isNotEmpty()) {
                currentPassword = newPassword
            }

            // Update UI with new data
            view?.findViewById<TextView>(R.id.username_tv)?.text = newUsername
            view?.findViewById<TextView>(R.id.email_tv)?.text = newEmail
            view?.findViewById<TextView>(R.id.name_tv)?.text = newUsername
            view?.findViewById<TextView>(R.id.email_view_tv)?.text = newEmail
            view?.findViewById<TextView>(R.id.phonenumber_tv)?.text = newPhone
            view?.findViewById<TextView>(R.id.password_tv)?.text = "*".repeat(currentPassword.length) // ✅ Show hidden password by default
        }
    }

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

        // ✅ Get initial password and store it
        currentPassword = arguments?.getString("password") ?: ""

        val username = arguments?.getString("username") ?: ""
        val emailText = arguments?.getString("email") ?: ""

        name.text = username
        usern1.text = username
        email1.text = emailText
        email.text = emailText
        pass.text = "*".repeat(currentPassword.length) // ✅ Show hidden password initially

        // ✅ Toggle password visibility using latest password
        show_pass.setOnClickListener {
            pass.text = if (isHidden) currentPassword else "*".repeat(currentPassword.length)
            isHidden = !isHidden
        }

        val btnEdit = view.findViewById<Button>(R.id.edit_Id)
        btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditProfilePicture::class.java)
            editProfileLauncher.launch(intent)
        }
        return view
    }
}
