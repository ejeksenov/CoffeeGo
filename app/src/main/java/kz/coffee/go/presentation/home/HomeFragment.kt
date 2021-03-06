package kz.coffee.go.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration

import kz.coffee.go.R
import kz.coffee.go.databinding.HomeFragmentBinding
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.utils.CAFETERIA_ID
import kz.coffee.go.utils.ManagingSharedPrefClass
import kz.coffee.go.utils.Resource
import kz.coffee.go.utils.SHARED_PREFS_CITY_NAME
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModel()
    private lateinit var binding: HomeFragmentBinding

    private val citiesArray: Array<String> by lazy { resources.getStringArray(R.array.cities_kz) }
    private val cafeteriasListAdapter: CafeteriaListAdapter by lazy { CafeteriaListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCitiesSpinnerSetAdapter()
        val dividerItemDecoration = DividerItemDecoration(binding.root.context, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(binding.root.context, R.drawable.custom_divider)!!)
        binding.rvHomeCafeteriaList.addItemDecoration(dividerItemDecoration)
        binding.rvHomeCafeteriaList.adapter = cafeteriasListAdapter
        cafeteriasListAdapter.onItemClick = {
            val arg = bundleOf(CAFETERIA_ID to it)
            this.findNavController().navigate(R.id.action_homeFragment_to_cafeteriaFragment, arg)
        }
    }

    private fun onCitiesSpinnerSetAdapter() {
        val adapter = ArrayAdapter(binding.root.context, R.layout.spinner_item, citiesArray)
        binding.spHomeCitiesList.adapter = adapter

        binding.spHomeCitiesList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val cityName = citiesArray[position]
                onGetCafeteriasList(cityName)
                ManagingSharedPrefClass.saveSharedSetting(binding.root.context, SHARED_PREFS_CITY_NAME, cityName)
            }
        }

        val cityName = ManagingSharedPrefClass.readSharedSetting(binding.root.context, SHARED_PREFS_CITY_NAME, "Алматы")
        val index = citiesArray.indexOf(cityName)
        binding.spHomeCitiesList.setSelection(index)
    }

    private fun onGetCafeteriasList(city: String) {
        viewModel.getCafeteriasListByCity(city).observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> {
                    showLoading(binding.progressBar)
                }
                is Resource.Success -> {
                    hideLoading(binding.progressBar)
                    cafeteriasListAdapter.updateList(it.data)
                }
                is Resource.Failure -> {
                    hideLoading(binding.progressBar)
                    showError(it.throwable.message, binding.root)
                }
            }
        })
    }

}
