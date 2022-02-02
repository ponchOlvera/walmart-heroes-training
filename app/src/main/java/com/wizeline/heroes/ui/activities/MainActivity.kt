package com.wizeline.heroes.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.wizeline.heroes.R
import com.wizeline.heroes.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {

        setSupportActionBar(binding.toolbar)
        val hostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment? ?: return

        val navController = hostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.characters_dest, R.id.search_dest))
        binding.bottomNavView.setupWithNavController(hostFragment.navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.host_fragment).navigateUp(appBarConfiguration)
    }

    fun updateTitle(title: String){
        binding.toolbar.title = title
    }
}
