package cit.edu.gamego

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import cit.edu.gamego.extensions.showConfirmation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditProfilePicture : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_picture);


        // todo pass the data from this act to the profilepicFragmenrt
        val name = findViewById<EditText>(R.id.edit_name_Id)
        val email = findViewById<EditText>(R.id.edit_email_Id)
        val phone = findViewById<EditText>(R.id.edit_phoneNumber_Id)
        val newPass = findViewById<EditText>(R.id.newpass_Id)
        val confirmPass = findViewById<EditText>(R.id.edit_confirmpass_Id)




        val btnBack = findViewById<ImageView>(R.id.edit_pfp_back_Id);
        btnBack.setOnClickListener{
           // val intent = Intent(this,ProfilePictureActivity::class.java);
//            val message = "Saves have not been save are you sure\n do you want to save changes?"
//            showSaveConfirmationPopUp(message);
            //startActivity(intent);
            finish()
        }

        val btnSave = findViewById<Button>(R.id.edit_save_Id);
        btnSave.setOnClickListener{
            val message = "Are you sure you want to save changes?"
            sendDatatoFragment(name.text.toString(),newPass.text.toString(),email.text.toString())
            showSaveConfirmationPopUp(message);
        }
    }

    private fun sendDatatoFragment(name: String, pass: String,email: String): ProfilePicFragment {
        val fragment = ProfilePicFragment()
        val bundle = Bundle().apply {
            putString("new_username", name)
            putString("new_password", pass)
            putString("new_email", email)
        }
        fragment.arguments = bundle
        return fragment
    }

    private fun showSaveConfirmationPopUp(message:String){
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
//            Toast.makeText(this,"saved changes", Toast.LENGTH_LONG).show();
//            val intent = Intent(this,ProfilePictureActivity::class.java);
//            startActivity(intent);
            finish()
        }

        btnNo.setOnClickListener{
            dialog.dismiss();
        }
    }
}