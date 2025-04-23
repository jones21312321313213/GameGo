package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cit.edu.gamego.data.Users
import cit.edu.gamego.databinding.RegisterF3Binding
import cit.edu.gamego.extensions.isTextNullOrEmpty
import cit.edu.gamego.extensions.toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : Activity() {
    private lateinit var binding: RegisterF3Binding
    private var username:String = ""
    private var email:String = ""
    private var password:String = ""
    private var cpass:String =""
    public lateinit var database: FirebaseDatabase
    public lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.register_f3);
        binding = RegisterF3Binding.inflate(layoutInflater)
        setContentView(binding.root);
        val button_signUp = findViewById<TextView>(R.id.register_signup_Id);
        button_signUp.setOnClickListener {
            finish()
        }

//
//        val username= findViewById<EditText>(R.id.register_username_Id);
//        val password= findViewById<EditText>(R.id.register_pass_Id);
//        val email = findViewById<EditText>(R.id.register_email_Id)
//        val cpass = findViewById<EditText>(R.id.register_confirmpass_Id)
//        val btnRegister = findViewById<Button>(R.id.register_btn_Id);
// <


        binding.registerBtnId.setOnClickListener {
            username = binding.registerUsernameId.text.toString()
            password = binding.registerPassId.text.toString()
            email = binding.registerEmailId.text.toString()
            cpass = binding.registerConfirmpassId.text.toString()

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || cpass.isEmpty()) {
                toast("Fill out the form completely")
                return@setOnClickListener
            }

            if (cpass != password) {
                toast("Passwords do not match")
                return@setOnClickListener
            }

            val userData = Users(username, password, email)
            database = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_DB_URL)
            reference = database.getReference("Users")

            // You can either use `username` as the key (if you enforce uniqueness)
            // Or use push() to auto-generate a unique ID
            val newUserRef = reference.push()  // <-- unique key auto-generated
            newUserRef.setValue(userData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.registerUsernameId.setText("")
                    binding.registerConfirmpassId.setText("")
                    binding.registerPassId.setText("")
                    binding.registerEmailId.setText("")

                    toast("User registered successfully")

                    startActivity(Intent(this, LoginActivity::class.java).apply {
                        putExtra("username", username)
                        putExtra("password", password)
                        putExtra("email", email)
                    })
                    finish()
                } else {
                    Log.e("Firebase Error", "Data save failed: ${task.exception?.message}")
                    toast("Registration failed. Try again.")
                }
            }
        }

    }

}