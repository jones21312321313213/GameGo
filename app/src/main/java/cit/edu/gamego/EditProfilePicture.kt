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
import androidx.appcompat.app.AppCompatActivity

class EditProfilePicture : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_picture)

        val name = findViewById<EditText>(R.id.edit_name_Id)
        val email = findViewById<EditText>(R.id.edit_email_Id)
        val phone = findViewById<EditText>(R.id.edit_phoneNumber_Id)
        val newPass = findViewById<EditText>(R.id.newpass_Id)
        val confirmPass = findViewById<EditText>(R.id.edit_confirmpass_Id)

        val btnBack = findViewById<ImageView>(R.id.edit_pfp_back_Id)
        btnBack.setOnClickListener {
            finish()
        }

        val btnSave = findViewById<Button>(R.id.edit_save_Id)
        btnSave.setOnClickListener {
            val message = "Are you sure you want to save changes?"
            showSaveConfirmationPopUp(message, name.text.toString(), newPass.text.toString(), email.text.toString(),phone.text.toString())
        }
    }

    private fun showSaveConfirmationPopUp(message: String, name: String, pass: String, email: String,phone:String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.activity_logout_popout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val widthInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400f, resources.displayMetrics).toInt()
        val heightInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200f, resources.displayMetrics).toInt()
        dialog.window?.setLayout(widthInPx, heightInPx)

        val btnYes = dialog.findViewById<Button>(R.id.yesLogout_id)
        val btnNo = dialog.findViewById<Button>(R.id.noLogout_id)
        val msg = dialog.findViewById<TextView>(R.id.message)

        msg.text = message
        dialog.show()

        btnYes.setOnClickListener {
            Toast.makeText(this, "Saved changes", Toast.LENGTH_LONG).show()

            // Send data back to LandingWithFragmentActivity
            val resultIntent = Intent().apply {
                putExtra("new_username", name)
                putExtra("new_password", pass)
                putExtra("new_email", email)
                putExtra("new_phone",phone)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // Close activity after saving
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
    }
}
