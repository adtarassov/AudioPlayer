package com.adtarassov.audioplayer.ui.sreen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.adtarassov.audioplayer.R.id
import com.adtarassov.audioplayer.databinding.ActivityMainBinding
import com.adtarassov.audioplayer.utils.player.AudioManager
import com.adtarassov.audioplayer.utils.AudioListType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var navController: NavController

  private val onFragmentChangedListener = NavController.OnDestinationChangedListener {
      _, _, arguments ->
    val args= arguments ?: return@OnDestinationChangedListener
    val listType = AudioListType.typeById(args[AudioListType.BUNDLE_KEY] as Int)
    binding.toolbar.subtitle = AudioListType.getToolbarSubtitleByType(listType)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)
    val navHostFragment = supportFragmentManager.findFragmentById(id.nav_host_fragment) as NavHostFragment
    navController = navHostFragment.navController
    appBarConfiguration = AppBarConfiguration(setOf(id.mainScreenFragment))
    navController.addOnDestinationChangedListener(onFragmentChangedListener)
    setupActionBarWithNavController(navController, appBarConfiguration)
  }

  override fun onDestroy() {
    navController.removeOnDestinationChangedListener(onFragmentChangedListener)
    super.onDestroy()
  }

  override fun onSupportNavigateUp(): Boolean {
    binding.toolbar.subtitle = ""
    return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
  }
}