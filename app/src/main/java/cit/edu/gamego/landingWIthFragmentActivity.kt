package cit.edu.gamego

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class landingWIthFragmentActivity : AppCompatActivity() {

    private lateinit var mainLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_with_fragment);
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
                startActivity(
                    Intent(this,LoginActivity::class.java)
                )
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