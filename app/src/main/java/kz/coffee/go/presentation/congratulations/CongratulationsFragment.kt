package kz.coffee.go.presentation.congratulations

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import kz.coffee.go.R
import kz.coffee.go.databinding.CongratulationsFragmentBinding
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.presentation.main.MainActivity
import kz.coffee.go.presentation.main.MainActivity.Companion.startFragment
import kz.coffee.go.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CongratulationsFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            CongratulationsFragment()
    }

    private val viewModel: CongratulationsViewModel by viewModel()
    private lateinit var binding: CongratulationsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.congratulations_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle: Bundle? = arguments
        if (bundle != null) {
            val price = bundle.getString(PRODUCT_PRICE)
            val cashback = bundle.getString(PRODUCT_CASHBACK)
            val cafeteriaId = bundle.getString(CAFETERIA_ID)
            if (!price.isNullOrBlank() && !cashback.isNullOrBlank() && !cafeteriaId.isNullOrBlank()) {
                binding.tvCongratulationsYouPaidQuantity.text = price
                binding.tvCongratulationsCashback.text = "кэшбэк +$cashback"
                viewModel.getCafeteriaById(cafeteriaId).observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Loading -> {
                            showLoading(binding.progressBar)
                        }
                        is Resource.Success -> {
                            hideLoading(binding.progressBar)
                            val cafeteria = it.data
                            val cafeteriaLogo = cafeteria.logoUrl
                            val cafeteriaName = cafeteria.name
                            Glide.with(binding.root).load(cafeteriaLogo)
                                .placeholder(R.drawable.ic_image_gray_34dp)
                                .error(R.drawable.ic_broken_image_gray).centerCrop().apply(
                                RequestOptions.circleCropTransform()
                            ).into(binding.ivCongratulationsCafeteriaLogo)
                            binding.tvCongratulationsCafeteriaName.text = cafeteriaName
                        }
                        is Resource.Failure -> {
                            hideLoading(binding.progressBar)
                            showError(it.throwable.message, binding.root)
                        }
                    }
                })
            }
        }

        binding.btnCongratulationsBack.setOnClickListener {
            this.findNavController().popBackStack(startFragment, false)
            (activity as MainActivity).showBottomNavigation()
        }
    }


}
