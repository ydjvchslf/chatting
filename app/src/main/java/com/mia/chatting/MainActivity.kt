package com.mia.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mia.chatting.databinding.ActivityMainBinding
import com.mia.chatting.util.DebugLog

class MainActivity : AppCompatActivity() {

    private val logTag = MainActivity::class.simpleName ?: ""
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DebugLog.i(logTag, "onCreate-()")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // 다크모드 비활성화
        navController = Navigation.findNavController(this, R.id.nav_host)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> hideBottomNavi(true)
                else -> hideBottomNavi(false)
            }
        }
    }

    private fun hideBottomNavi(state: Boolean){
        if (state) binding.bottomNav.visibility = View.GONE else binding.bottomNav.visibility = View.VISIBLE
    }
}