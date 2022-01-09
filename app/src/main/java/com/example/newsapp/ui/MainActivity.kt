package com.example.newsapp.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.di.LocaleHelperEntryPoint
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun attachBaseContext(newBase: Context) {
        val localeHelper =
            EntryPointAccessors.fromApplication(
                newBase, LocaleHelperEntryPoint::class.java,
            ).localeHelper
        super.attachBaseContext(localeHelper.onAttach(newBase))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()


        /*navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.headlinesFragment -> {

                    val country =
                        sharedPreferences.getString("country", Locale.getDefault().country)

                    val loc = Locale("", country)

                    //Toast.makeText(this, loc.displayCountry, Toast.LENGTH_SHORT).show()
                    arguments?.putString("country", loc.displayCountry)
                }
            }

        }*/

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.headlinesFragment, R.id.searchNewsFragment, R.id.settingsFragment)
        )

        setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavBar.setupWithNavController(navController)


    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}