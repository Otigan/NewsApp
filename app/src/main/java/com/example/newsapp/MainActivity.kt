package com.example.newsapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


private lateinit var binding: ActivityMainBinding
private lateinit var navController: NavController
private lateinit var appBarConfiguration: AppBarConfiguration


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        /*Toast.makeText(
            this,
            Locale.getDefault().country.toString(),
            Toast.LENGTH_SHORT
        ).show()*/

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()


        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.headlinesFragment, R.id.newsFragment, R.id.settingsFragment)
        )

        setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavBar.setupWithNavController(navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}