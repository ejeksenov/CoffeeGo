package kz.coffee.go.presentation.addCashback


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

import kz.coffee.go.R
import kz.coffee.go.databinding.AddCashbackFragmentBinding
import kz.coffee.go.domain.purchaseHistory.PurchaseHistory
import kz.coffee.go.domain.user.User
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.utils.CAFETERIA_QR_CODE
import kz.coffee.go.utils.Resource
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCashbackFragment : BaseFragment() {

    companion object {
        fun newInstance() =
            AddCashbackFragment()
    }

    private val viewModel: AddCashbackViewModel by viewModel()
    private lateinit var binding: AddCashbackFragmentBinding

    private var price = 0
    private var priceStr = ""
    private var cashback = 0
    private var cashbackPercent = 0
    private var cafeteriaId = ""
    private var cafeteriaName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.add_cashback_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtAddCashbackPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                priceStr = s.toString()
                if (priceStr.isNotBlank()) {
                    price = priceStr.toInt()
                    cashback = (price * cashbackPercent)/100
                    val cashbackTxt = "${cashbackPercent}% кэшбэк $cashback тг"
                    binding.tvAddCashBackQuantity.text = cashbackTxt
                }
            }

        })
        val bundle: Bundle? = arguments
        if (bundle != null) {
            val qrCodeData = bundle.getString(CAFETERIA_QR_CODE)?.split(",")!!
            if (qrCodeData.size >= 3) {
                cafeteriaId = qrCodeData[0]
                cafeteriaName = qrCodeData[1]
                cashbackPercent = qrCodeData[2].toInt()
            }

            binding.btnAddCashbackPay.setOnClickListener {
                if (onCheckFields())
                    viewModel.getUserById().observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is Resource.Loading -> {
                                showLoading(binding.progressBar)
                            }
                            is Resource.Success -> {
                                hideLoading(binding.progressBar)
                                val user = it.data
                                val newCashback = user.cashback?.plus(cashback)
                                user.cashback = newCashback
                                onAddCashback(user)
                            }
                            is Resource.Failure -> {
                                hideLoading(binding.progressBar)
                                showError(it.throwable.message, binding.root)
                            }
                        }
                    })
            }
        }
    }

    private fun onAddCashback(user: User) {
        viewModel.saveUserData(user).observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    showLoading(binding.progressBar)
                }
                is Resource.Success -> {
                    hideLoading(binding.progressBar)
                    onSaveToPurchaseHistory()
                }
                is Resource.Failure -> {
                    hideLoading(binding.progressBar)
                    showError(it.throwable.message, binding.root)
                }
            }
        })
    }

    private fun onSaveToPurchaseHistory() {
        val purchaseHistory = PurchaseHistory(null, cashback.toDouble(), price.toDouble(), null, null)
        viewModel.savePurchaseHistory(purchaseHistory, cafeteriaId).observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> {
                    showLoading(binding.progressBar)
                }
                is Resource.Success -> {
                    hideLoading(binding.progressBar)
                    showMessage(resources.getString(R.string.succeed_paid), binding.root.context)
                    this.findNavController().navigate(R.id.action_addCashbackFragment_to_congratulationsFragment)
                }
                is Resource.Failure -> {
                    hideLoading(binding.progressBar)
                    showError(it.throwable.message, binding.root)
                }
            }
        })
    }

    private fun onCheckFields(): Boolean {
        priceStr = binding.edtAddCashbackPrice.text.toString()
        if (priceStr.isBlank() || priceStr == "0" || priceStr == "0.0") {
            binding.edtAddCashbackPrice.error = resources.getString(R.string.enter_product_price)
            return false
        } else
            price = priceStr.toInt()
        return true
    }

}
