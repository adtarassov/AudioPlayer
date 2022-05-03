package com.adtarassov.audioplayer.ui.sreen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.adtarassov.audioplayer.R.id
import com.adtarassov.audioplayer.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainActivityViewModel by viewModels()
  private val appBarConfiguration =
    AppBarConfiguration(setOf(id.audio_list_fragment, id.profile_fragment, id.voice_fragment))

  private lateinit var binding: ActivityMainBinding
  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)
    setupNavigation()
    lifecycleScope.launchWhenCreated {
      viewModel.viewStates().filterNotNull().collect { state -> bindViewState(state) }
    }
    lifecycleScope.launchWhenCreated {
      viewModel.viewActions().filterNotNull().collect { action -> bindViewAction(action) }
    }
  }

  private fun setupNavigation() {
    val navHostFragment = supportFragmentManager.findFragmentById(id.nav_host_fragment) as NavHostFragment
    navController = navHostFragment.navController
    navController.addOnDestinationChangedListener(viewModel.onFragmentChangedListener)
    binding.bottomNavigationView.setupWithNavController(navController)
    setupActionBarWithNavController(navController, appBarConfiguration)
  }

  private fun bindViewAction(action: MainActivityAction) {
    when (action) {
      is MainActivityAction.OnNavigationChange -> {
//        binding.bottomNavigationView.menu.forEach { item ->
//          if (action.destination == item.itemId) {
//            item.isChecked = true
//          }
//        }
      }
    }
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