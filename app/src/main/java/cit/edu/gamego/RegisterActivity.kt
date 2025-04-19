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
// <><><><><><><><><><><><><><Game>


        binding.registerBtnId.setOnClickListener {
            username = binding.registerUsernameId.text.toString()
            password = binding.registerPassId.text.toString()
            email = binding.registerEmailId.text.toString()
            cpass = binding.registerConfirmpassId.text.toString()

            // Validation
            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || cpass.isEmpty()) {
                toast("Fill out the form completely")
                return@setOnClickListener
            }

            if (cpass != password) {
                toast("Passwords do not match")
                return@setOnClickListener
            }// wow
            // Save to Firebase
            val users = Users(username, password, email)
            database =  FirebaseDatabase.getInstance("https://gamego-5912c-default-rtdb.asia-southeast1.firebasedatabase.app")

            reference = database.getReference("Users")

            reference.child(username).setValue(users).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Clear form
                    binding.registerUsernameId.setText("")
                    binding.registerConfirmpassId.setText("")
                    binding.registerPassId.setText("")
                    binding.registerEmailId.setText("")

                    // Notify and redirect
                    toast("Registration successful")

                    // Send to LoginActivity with extras
                    startActivity(
                        Intent(this, LoginActivity::class.java).apply {
                            putExtra("username", username)
                            putExtra("password", password)
                            putExtra("email", email)
                        }
                    )
                    finish()
                } else {
                    Log.e("Firebase Error", "Error: ${task.exception?.message}")
                    toast("Registration failed. Try again.")
                }
            }
        }


    }

}