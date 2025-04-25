package cit.edu.gamego

import android.app.Activity
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import cit.edu.gamego.extensions.toast

//continue this; fetching data would still not work 
class ProfilePicFragment : Fragment() {
    private var currentPassword: String = ""
    private var isHidden = true
    private val auth = FirebaseAuth.getInstance()

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

        val currentUser = auth.currentUser

        if (currentUser != null) {
            val uid = currentUser.uid
            val dbRef = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
                .getReference("Users")
                .child(uid)

            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").getValue(String::class.java) ?: ""
                        val emailVal = snapshot.child("email").getValue(String::class.java) ?: ""
                        val password = snapshot.child("password").getValue(String::class.java) ?: ""

                        // Assign to views
                        currentPassword = password
                        usern1.text = username
                        name.text = username
                        email1.text = emailVal
                        email.text = emailVal
                        pass.text = "*".repeat(password.length)
                    } else {
                        Log.e("FIREBASE", "User data not found for UID: $uid")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FIREBASE", "DB read failed: ${error.message}")
                }
            })
        } else {
            Log.e("AUTH", "No user is logged in")
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        show_pass.setOnClickListener {
            pass.text = if (isHidden) currentPassword else "*".repeat(currentPassword.length)
            isHidden = !isHidden
        }

        val btnEdit = view.findViewById<Button>(R.id.edit_Id)
        btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), EditProfilePicture::class.java)
            startActivity(intent)
        }

        return view
    }
}
