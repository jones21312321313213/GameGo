package cit.edu.gamego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import cit.edu.gamego.extensions.showConfirmation
import com.google.android.material.navigation.NavigationView
import android.widget.ImageView

class landingWIthFragmentActivity : AppCompatActivity() {

    private lateinit var mainLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private var et_uname: String? = null
    private var et_upass: String? = null
    private var et_uemail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_with_fragment)

        // Load the initial fragment
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, landingFragment())
                .commit()
        }

        // Retrieve intent data from login or previous activity
        intent?.let {
            et_uname = it.getStringExtra("username")
            et_upass = it.getStringExtra("password")
            et_uemail = it.getStringExtra("email")
        }

        val fav = findViewById<ImageView>(R.id.faves)
        fav.setOnClickListener {
            startActivity(Intent(this, Favorites::class.java))
        }

        mainLayout = findViewById(R.id.main)
        navView = findViewById(R.id.nav_view)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, mainLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        mainLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.nav_logout) {
                showConfirmation("Are you sure you want to log out")
                return@setNavigationItemSelectedListener true
            }
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.nav_home -> landingFragment()
                R.id.nav_profile -> ProfilePicFragment().apply {
                    arguments = Bundle().apply {
                        putString("username", et_uname)
                        putString("password", et_upass)
                        putString("email", et_uemail)
                    }
                }
                R.id.nav_about_devs -> developerFragment()
                R.id.nav_settings -> settingsFragment()
                else -> landingFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()

            mainLayout.closeDrawers()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == Activity.RESULT_OK) {
            val newUsername = data?.getStringExtra("new_username")
            val newPassword = data?.getStringExtra("new_password")
            val newEmail = data?.getStringExtra("new_email")

            // Reload the ProfilePicFragment with updated data
            val fragment = ProfilePicFragment().apply {
                arguments = Bundle().apply {
                    putString("username", newUsername)
                    putString("password", newPassword)
                    putString("email", newEmail)
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        private const val REQUEST_CODE_EDIT_PROFILE = 100
    }
}
