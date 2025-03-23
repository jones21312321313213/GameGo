package cit.edu.gamego

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfilePictureActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_picture);


//        val button_profilepic_back = findViewById<ImageView>(R.id.profilepic_back_Id);
//        button_profilepic_back.setOnClickListener{
//            Log.e("Dont u", "absolute cinema");
//            Toast.makeText(this,"avasd",Toast.LENGTH_LONG).show();
//            val intent = Intent(this,activity_landing::class.java);
//            startActivity(intent);
//        }

        //pop up of confirmation logout
        val btnEdit = findViewById<Button>(R.id.edit_Id);
        btnEdit.setOnClickListener{
            startActivity(
                Intent(this,EditProfilePicture::class.java)
            )
        }
//
//        btnEdit.setOnClickListener{
//            val message = "Are you sure you want to logout?"
//            showLogOutConfirmation(message);
//        }

        val btnEditProfilePic = findViewById<ImageView>(R.id.pp_editinfo_Id);
        btnEditProfilePic.setOnClickListener{
            val intent = Intent(this,EditProfilePicture::class.java);

            startActivity(intent);
        }
    }
}