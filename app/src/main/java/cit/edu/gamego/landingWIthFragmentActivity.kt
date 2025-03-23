package cit.edu.gamego

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import cit.edu.gamego.extensions.showConfirmation
import com.google.android.material.navigation.NavigationView
import android.widget.ImageView
import cit.edu.gamego.extensions.showConfirmation

class landingWIthFragmentActivity : AppCompatActivity() {

    private lateinit var mainLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_with_fragment);

        // Para inig start ani kaay naa na ang contents sa activity_landing.xml
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, landingFragment())
                .commit()
        }

        val fav = findViewById<ImageView>(R.id.faves);
        fav.setOnClickListener{
            startActivity(
                Intent(this,Favorites::class.java)
            )
        }

        mainLayout = findViewById(R.id.main)
        navView = findViewById(R.id.nav_view)
        val toolbar: androidx.appcompat.widget.Toolbar =  findViewById(R.id.toolbar)

        // Set Toolbar as ActionBar
        setSupportActionBar(toolbar)

        // Enable Drawer Toggle
        val toggle = ActionBarDrawerToggle(
            this, mainLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        mainLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            if(menuItem.itemId == R.id.nav_logout){
                showConfirmation("Are you sure you want to log out");
                return@setNavigationItemSelectedListener true
            }
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.nav_home -> landingFragment()
                R.id.nav_profile -> ProfilePicFragment()
                R.id.nav_about_devs -> developerFragment()
                R.id.nav_settings -> settingsFragment()
                // Add other menu items here
                else -> landingFragment()
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
            mainLayout.closeDrawers()
            true
        }
    }
}