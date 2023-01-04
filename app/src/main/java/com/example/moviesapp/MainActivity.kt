package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.moviesapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this activity
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        //Setup toolbar :
        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.fragment_host)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment))
        binding.toolbar.setupWithNavController(navController,appBarConfiguration)

        //To change text in toolBar in any destination :
        navController.addOnDestinationChangedListener{ _,destination,_ ->
            when (destination.id) {
                R.id.mainFragment -> mainToolbarText()
                R.id.detailFragment -> detailToolbarText()

            }
        }
    }

    // Fun To Change Text in Toolbar at main fragment:
    private fun mainToolbarText(){
        binding.toolbar.title = "Search Movies"
    }

    // Fun To Change Text in Toolbar at detail fragment:
    private fun detailToolbarText(){
        binding.toolbar.title = "Details"
    }



}