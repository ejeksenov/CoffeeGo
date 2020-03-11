package kz.coffee.go.presentation.main

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kz.coffee.go.R
import kz.coffee.go.databinding.ActivityMainBinding
import kz.coffee.go.presentation.base.BaseActivity
import kz.coffee.go.utils.requestPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    private val navHostFragment by lazy { nav_host_fragment as NavHostFragment }
    private val graphInflater by lazy { navHostFragment.navController.navInflater }
    private val navGraph by lazy { graphInflater.inflate(R.navigation.nav_graph) }
    private val navController by lazy { navHostFragment.navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val destination: Int
        if (mainViewModel.checkUserSignedIn()) {
            destination = R.id.homeFragment
            binding.bottomNavigationView.visibility = View.VISIBLE
            binding.fab.show()
        } else {
            destination = R.id.loginFragment
            binding.bottomNavigationView.visibility = View.GONE
            binding.fab.hide()
        }
        navGraph.startDestination = destination
        navController.graph = navGraph

        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment.navController
        )

        binding.fab.setOnClickListener {
            if (kz.coffee.go.utils.checkPermission(this)) {
                hideBottomNavigation()
                when (navController.currentDestination?.id) {
                    R.id.homeFragment -> navController.navigate(R.id.action_homeFragment_to_scanQrCodeFragment)
                    R.id.profileFragment -> navController.navigate(R.id.action_profileFragment_to_scanQrCodeFragment)
                    R.id.cafeteriaFragment -> navController.navigate(R.id.action_cafeteriaFragment_to_scanQrCodeFragment)
                    R.id.changeEmailFragment -> navController.navigate(R.id.action_changeEmailFragment_to_scanQrCodeFragment)
                    R.id.changePasswordFragment -> navController.navigate(R.id.action_changePasswordFragment_to_scanQrCodeFragment)
                    R.id.editProfileFragment -> navController.navigate(R.id.action_editProfileFragment_to_scanQrCodeFragment)
                }
            } else
                requestPermission(this, 1)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (navController.currentDestination?.id != R.id.scanQrCodeFragment)
            showBottomNavigation()
    }


    fun showBottomNavigation() {
        if (binding.bottomNavigationViewLayout.visibility == View.GONE) {
            binding.bottomNavigationViewLayout.visibility = View.VISIBLE
            binding.fab.show()
        }
    }

    fun hideBottomNavigation() {
        binding.fab.hide()
        binding.bottomNavigationViewLayout.visibility = View.GONE
    }

    /*fun transformFloatingActionButton() {
        binding.bottomNavigationView.transform(binding.fab)
    }*/
}
