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



        // should return to the main page but wala paman so go to login page (this is temp)
        val button_profilepic_back = findViewById<ImageView>(R.id.profilepic_back_Id);
        button_profilepic_back.setOnClickListener{
            Log.e("Dont u", "absolute cinema");
            Toast.makeText(this,"avasd",Toast.LENGTH_LONG).show();
            val intent = Intent(this,activity_landing::class.java);
            startActivity(intent);
        }

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

    private fun showLogOutConfirmation(message:String):Unit{
        val dialog = Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_logout_popout);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        //para ma change and width sa popup
        val widthInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            400f,
            resources.displayMetrics
        ).toInt()
        //para ma change and height sa popup
        val heightInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            200f,
            resources.displayMetrics
        ).toInt()

        dialog.window?.setLayout(
            widthInPx,  // Width
            heightInPx   // Height
        )

        val btnYes = dialog.findViewById<Button>(R.id.yesLogout_id);
        val btnNo = dialog.findViewById<Button>(R.id.noLogout_id);
        val msg = dialog.findViewById<TextView>(R.id.message);

        msg.text = message;
        dialog.show();
        btnYes.setOnClickListener{
            Toast.makeText(this,"logged out", Toast.LENGTH_LONG).show();
            val intent = Intent(this,activity_landing::class.java);
            startActivity(intent);
        }

        btnNo.setOnClickListener{
            dialog.dismiss();
        }

    }
}