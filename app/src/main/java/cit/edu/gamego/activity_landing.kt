package cit.edu.gamego

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast

class activity_landing : AppCompatActivity() {

    private lateinit var mainLayout: DrawerLayout
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)


        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnSearchClickListener {
            searchView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            searchView.requestLayout()
        }


        // Initialize Views
//        mainLayout = findViewById(R.id.main)
//        navView = findViewById(R.id.nav_view)
//        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
//
//        // Set Toolbar as ActionBar
//        setSupportActionBar(toolbar)
//
//        // Enable Drawer Toggle
//        val toggle = ActionBarDrawerToggle(
//            this, mainLayout, toolbar,
//            R.string.navigation_drawer_open, R.string.navigation_drawer_close
//        )
//        mainLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        // Handle Navigation Menu Item Clicks
//        navView.setNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.nav_home -> toast("Home selected")
//                R.id.nav_profile -> startActivity(
//                    Intent(this, ProfilePictureActivity::class.java)
//                )
//
//                R.id.nav_logout -> showLogOutConfirmation("Are you sure you want to log out?")
//
//                R.id.nav_settings -> startActivity(
//                    Intent(this, settings::class.java)
//                )
//
//                R.id.nav_about_devs -> startActivity(
//                    Intent(this, activity_developer::class.java)
//                )
//                // Add other menu items here
//            }
//            mainLayout.closeDrawers()
//            true
//        }


        // Goes to favorites/liked/saved
//        val btnAccount = findViewById<ImageView>(R.id.account_Id)
//        btnAccount.setOnClickListener {
//            startActivity(Intent(this, Favorites::class.java))
//        }
//    }
    }

    // Helper function to show a toast
    private fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
