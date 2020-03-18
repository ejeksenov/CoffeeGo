package kz.coffee.go.presentation.cafeteria

import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kz.coffee.go.R
import kz.coffee.go.databinding.CafeteriaFragmentBinding
import kz.coffee.go.domain.cafeteria.Cafeteria
import kz.coffee.go.domain.cafeteria.Product
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.utils.CAFETERIA_ID
import kz.coffee.go.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel


class CafeteriaFragment : BaseFragment() {

    companion object {
        fun newInstance() = CafeteriaFragment()
    }

    private val viewModel: CafeteriaViewModel by viewModel()
    private lateinit var binding: CafeteriaFragmentBinding
    private var isScrollUp = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.cafeteria_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments
        if (bundle != null) {
            val cafeteriaId = bundle.getString(CAFETERIA_ID)
            if (!cafeteriaId.isNullOrBlank())
                viewModel.getCafeteriaById(cafeteriaId).observe(viewLifecycleOwner, Observer {
                    when (it) {
                        is Resource.Loading -> {
                            showLoading(binding.progressBar)
                        }
                        is Resource.Success -> {
                            hideLoading(binding.progressBar)
                            onBindDataToView(it.data)
                        }
                        is Resource.Failure -> {
                            hideLoading(binding.progressBar)
                            showError(it.throwable.message, binding.root)
                        }
                    }
                })
        }

        binding.btnCafeteriaBack.setOnClickListener {
            this.findNavController().popBackStack()
        }

    }

    private fun onBindDataToView(cafeteria: Cafeteria) {
        val address = cafeteria.address
        if (!address.isNullOrBlank())
            binding.tvCafeteriaAddress.text = address

        val imageUrl = cafeteria.imageUrl
        Glide.with(binding.root).load(imageUrl).placeholder(R.drawable.ic_image_gray_200dp)
            .error(R.drawable.ic_broken_image_gray).centerCrop().into(binding.ivCafeteriaImage)

        val logoUrl = cafeteria.logoUrl
        Glide.with(binding.root).load(logoUrl).placeholder(R.drawable.ic_image_gray_34dp)
            .error(R.drawable.ic_broken_image_gray).apply(
                RequestOptions.circleCropTransform()
            ).centerCrop().into(binding.ivCafeteriaLogo)

        val cafeteriaName = cafeteria.name
        if (!cafeteriaName.isNullOrBlank())
            binding.tvCafeteriaName.text = cafeteriaName

        val workingTime = cafeteria.workingTime
        if (!workingTime.isNullOrBlank())
            binding.tvCafeteriaWorkingTime.text = workingTime

        val productsList = cafeteria.products
        onSetProductsListAdapter(productsList)

        val phoneNumbers = cafeteria.phoneNumber
        onSetPhoneNumbersListAdapter(phoneNumbers)

    }

    private fun onSetPhoneNumbersListAdapter(phoneNumbers: MutableList<String>) {
        val adapter = TextListAdapter(phoneNumbers)
        binding.rvCafeteriaPhoneNumbers.adapter = adapter
        binding.rvCafeteriaPhoneNumbers.addItemDecoration(
            DividerItemDecoration(
                binding.root.context,
                RecyclerView.VERTICAL
            )
        )
        adapter.onItemClick = {
            onPhoneCall(it)
        }
    }

    private fun onSetProductsListAdapter(productsList: MutableList<Product>) {
        val list: MutableList<String> = ArrayList()
        for (product in productsList) {
            val productStr = " - ${product.name} - ${product.price} тг"
            list.add(productStr)
        }
        val productAdapter = TextListAdapter(list)
        binding.rvCafeteriaProducts.addItemDecoration(
            DividerItemDecoration(
                binding.root.context,
                RecyclerView.VERTICAL
            )
        )
        binding.rvCafeteriaProducts.adapter = productAdapter
    }

    private fun onPhoneCall(it: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$it")
        startActivity(intent)
    }


}
