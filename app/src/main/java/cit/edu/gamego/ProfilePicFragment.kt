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
import com.bumptech.glide.Glide
import com.rejowan.cutetoast.CuteToast

//continue this; fetching data would still not work
class ProfilePicFragment : Fragment() {
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
        val savedGamesTv = view.findViewById<TextView>(R.id.saved_games)
        val pp = view.findViewById<ImageView>(R.id.profilePic)

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
                        val favoritesSnapshot = snapshot.child("favorites")

                        // y this? because when firebase tries to read the favorite it can be read as a map<String,bool> which is not the format in our firbase
                        val favorites: List<String> = if (favoritesSnapshot.value is Map<*, *>) {
                            val map = favoritesSnapshot.getValue(object : GenericTypeIndicator<Map<String, String>>() {})
                            map?.values?.toList() ?: emptyList()
                        } else if (favoritesSnapshot.value is List<*>) {
                            val list = favoritesSnapshot.getValue(object : GenericTypeIndicator<List<String>>() {})
                            list ?: emptyList()
                        } else {
                            emptyList()
                        }

                        val profilePicUrl = snapshot.child("profilePicUrl").getValue(String::class.java)

                        if (!profilePicUrl.isNullOrEmpty()) {
                            Glide.with(this@ProfilePicFragment)
                                .load(profilePicUrl)
                                .placeholder(R.drawable.mew)
                                .error(R.drawable.ye)
                                .circleCrop()
                                .into(pp)
                        }

                        usern1.text = username
                        name.text = username
                        email1.text = emailVal
                        email.text = emailVal

                        val savedCount = (favorites.size - 1).coerceAtLeast(0)
                        savedGamesTv.text = "$savedCount saved games"
                    } else {
                        Log.e("FIREBASE", "way uid: $uid")
                        CuteToast.ct(requireContext(), "no data found for this user", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FIREBASE", "DB read failed: ${error.message}")
                }
            })
        } else {
           Log.e("AUTH", "No user is logged in")

        }

        return view
    }
}

