package kz.coffee.go.presentation.main

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kz.coffee.go.R
import kz.coffee.go.databinding.ActivityMainBinding
import kz.coffee.go.presentation.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = nav_host_fragment as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)
        val navController = navHostFragment.navController

        val destination: Int
        if(mainViewModel.checkUserSignedIn()) {
            destination = R.id.homeFragment
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            destination = R.id.loginFragment
            binding.bottomNavigationView.visibility = View.GONE
        }
        navGraph.startDestination = destination
        navController.graph = navGraph

    }


    fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}
