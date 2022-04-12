package com.adtarassov.audioplayer.ui.sreen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.adtarassov.audioplayer.R.id
import com.adtarassov.audioplayer.databinding.ActivityMainBinding
import com.adtarassov.audioplayer.utils.AudioListType
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainActivityModel by viewModels()

  private lateinit var binding: ActivityMainBinding
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var navController: NavController
  private lateinit var bottomNavigationView: BottomNavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)

    val navHostFragment = supportFragmentManager.findFragmentById(id.nav_host_fragment) as NavHostFragment
    appBarConfiguration = AppBarConfiguration(setOf(id.home_fragment, id.profile_fragment))
    bottomNavigationView = binding.bottomNavigationView
    navController = navHostFragment.navController
    navController.addOnDestinationChangedListener(viewModel.onFragmentChangedListener)

    bottomNavigationView.setupWithNavController(navController)
    setupActionBarWithNavController(navController, appBarConfiguration)

    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
  }

  private fun bindViewAction(action: MainActivityAction) {
  }

  private fun bindViewState(state: MainActivityViewState) {
  }

  override fun onDestroy() {
    navController.removeOnDestinationChangedListener(viewModel.onFragmentChangedListener)
    super.onDestroy()
  }

  override fun onSupportNavigateUp(): Boolean {
    return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
  }
}