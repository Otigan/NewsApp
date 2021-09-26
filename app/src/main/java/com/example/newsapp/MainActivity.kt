package com.example.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NewsApp)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.headlinesFragment -> {

                    val country =
                        sharedPreferences.getString("country", Locale.getDefault().country)

                    val loc = Locale("", country)

                    //Toast.makeText(this, loc.displayCountry, Toast.LENGTH_SHORT).show()
                    arguments?.putString("country", loc.displayCountry)
                }
            }

        }


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.headlinesFragment,
                R.id.favouriteNewsFragment,
                R.id.newsFragment,
                R.id.settingsFragment
            )
        )

        setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(navController, appBarConfiguration)

        val args = Bundle()

        args.putString("country", Locale.getDefault().country.toString())


        binding.bottomNavBar.setupWithNavController(navController)


    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}