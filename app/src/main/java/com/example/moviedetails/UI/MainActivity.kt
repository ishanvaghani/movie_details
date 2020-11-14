package com.example.moviedetails.UI

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moviedetails.R
import com.example.moviedetails.ViewModel.OtherViewModel
import com.example.moviedetails.ViewModel.TvShowViewModel
import com.example.moviedetails.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val otherViewModel: OtherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNav.setupWithNavController(navController)

        otherViewModel.apply {
            readyGuestSessionId()
            getGuestSessionId().observe(this@MainActivity, {
                val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("GuestSessionId", it.guestSessionId)
                editor.apply()
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}