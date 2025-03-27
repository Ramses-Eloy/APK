package com.dbsnetwork.iptv

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
//Fragments imports
import com.dbsnetwork.iptv.MoviesFragment
import com.dbsnetwork.iptv.TVShowsFragment
import com.dbsnetwork.iptv.FavoritesFragment
import com.dbsnetwork.iptv.AccountFragment
import com.dbsnetwork.iptv.SettingsFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "DBS Network IPTV"

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)

        // Setup drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Setup navigation view
        navigationView.setNavigationItemSelectedListener(this)

        // Load header image if present
        val headerView = navigationView.getHeaderView(0)
        val logoImageView = headerView.findViewById<ImageView>(R.id.header_logo)
        Glide.with(this)
            .load(R.drawable.dbs_logo)
            .into(logoImageView)

        // Set default fragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation item clicks
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
                toolbar.title = "Home"
            }
            R.id.nav_tv_channels -> {
                replaceFragment(ChannelsFragment())
                toolbar.title = "TV Channels"
            }
            R.id.nav_movies -> {
                replaceFragment(MoviesFragment())
                toolbar.title = "Movies"
            }
            R.id.nav_tv_shows -> {
                replaceFragment(TVShowsFragment())
                toolbar.title = "TV Shows"
            }
            R.id.nav_favorites -> {
                replaceFragment(FavoritesFragment())
                toolbar.title = "Favorites"
            }
            R.id.nav_account -> {
                replaceFragment(AccountFragment())
                toolbar.title = "My Account"
            }
            R.id.nav_settings -> {
                replaceFragment(SettingsFragment())
                toolbar.title = "Settings"
            }
            R.id.nav_support -> {
                // Launch support activity or dialog
                toolbar.title = "Support"
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}