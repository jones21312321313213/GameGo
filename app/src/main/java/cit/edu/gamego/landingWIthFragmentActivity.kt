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

        var et_uname: String? = null
        var et_upass: String? = null
        var et_uemail: String? = null


        intent?.let{
            it.getStringExtra("username")?.let{
                et_uname = it
            }
            it.getStringExtra("password")?.let{
                et_upass = it
            }

            it.getStringExtra("email")?.let{
                et_uemail = it
            }
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
            }else if(menuItem.itemId == R.id.nav_profile){

            }
            val fragment: Fragment = when (menuItem.itemId) {
                R.id.nav_home -> landingFragment()
                R.id.nav_profile -> pwdProfilePicFragment(et_uname.toString(),et_upass.toString(),et_uemail.toString())
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

    private fun pwdProfilePicFragment(name: String, pass: String,email: String): ProfilePicFragment {
        val fragment = ProfilePicFragment()
        val bundle = Bundle().apply {
            putString("username", name)
            putString("password", pass)
            putString("email", email)
        }
        fragment.arguments = bundle
        return fragment
    }

}