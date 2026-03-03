package com.codepath.lab6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.codepath.lab6.databinding.ActivityMainBinding
import kotlinx.serialization.json.Json

// Helper function for JSON parsing (used by your fragments)
fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Switch fragments when a bottom nav item is tapped
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_parks -> {
                    replaceFragment(ParksFragment())
                    true
                }
                R.id.menu_campgrounds -> {
                    replaceFragment(CampgroundFragment())
                    true
                }
                else -> false
            }
        }

        // Default tab so screen isn't blank on launch
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.menu_parks
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, fragment)
            .commit()
    }
}