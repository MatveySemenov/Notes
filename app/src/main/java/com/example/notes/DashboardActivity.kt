package com.example.notes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.notes.UI.about.nav_about
import com.example.notes.UI.settings.nav_settings
import com.example.notes.databinding.ActivityDashboardBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity(), UserDataChangeListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // ставить тему при открытии приложения
        getThemeOpenApp()


        val appPreferences = AppPreferences(this)
        appPreferences.setUserDataChangeListener(this)

        val (userName, userEmail) = appPreferences.getUserData()
        getUINavHeaderMain(userName,userEmail)

        //фрагменты для которых мы показываем боковое меню
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.notes_book,
                R.id.nav_archives,
                R.id.nav_trash,
                R.id.nav_settings,
                R.id.nav_about,
                R.id.nav_exit
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //фрагменты для которых не показываем боковое меню
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in setOf(
                    R.id.loading_app,
                    R.id.sign_in,
                    R.id.sign_up
                )
            ) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                supportActionBar?.hide()
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                supportActionBar?.show()
            }
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                //Обработчик нажатия на элемент меню "Настройки"
                R.id.nav_settings -> {
                    startActivity(Intent(this, nav_settings::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                // Обработчик нажатия на элемент меню "О приложении"
                R.id.nav_about -> {
                    startActivity(Intent(this, nav_about::class.java))
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }

                R.id.nav_exit -> {
                    FirebaseAuth.getInstance().signOut() //выйти из аккаунта
                    navController.navigate(R.id.action_notes_book_to_sign_in)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }

            }
            false
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun getUINavHeaderMain(userName: String?, userEmail: String?) {
        val headerView = binding.navView.getHeaderView(0)
        val navHeaderName = headerView.findViewById<TextView>(R.id.nav_header_name)
        navHeaderName.text = userName
        val navHeaderEmail = headerView.findViewById<TextView>(R.id.nav_header_gmail)
        navHeaderEmail.text = userEmail
    }

    //Темы
    private fun getThemeOpenApp(){
        // Инициализируйте тему здесь, до загрузки активности
        val isDarkTheme = getSavedThemeState()
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isDarkTheme", false)
    }
}