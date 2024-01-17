package com.example.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.notes.databinding.ActivityDashboardBinding
import com.google.android.material.navigation.NavigationView

class DashboardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.navBar.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        if (savedInstanceState == null) {
            val navController = findNavController(R.id.nav_bar)
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.notesbook
                ), drawerLayout
            )

            // Установка кнопки "вверх" и бокового меню
            setupActionBarWithNavController(navController, appBarConfiguration)
            // Связывание NavigationView с NavController
            navView.setupWithNavController(navController)
        }
    }

    // Обработка нажатия на кнопку "вверх"
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_bar)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
