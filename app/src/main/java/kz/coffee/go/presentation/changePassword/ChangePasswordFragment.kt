package kz.coffee.go.presentation.changePassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.change_password_fragment.*

import kz.coffee.go.R
import kz.coffee.go.databinding.ChangePasswordFragmentBinding
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : BaseFragment() {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private val viewModel: ChangePasswordViewModel by viewModel()
    private lateinit var binding: ChangePasswordFragmentBinding

    private var password = ""
    private var newPassword = ""
    private var repeatPassword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.change_password_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChangePasswordBack.setOnClickListener {
            this.findNavController().popBackStack()
        }

        binding.btnChangePasswordSave.setOnClickListener {
            if (onCheckFields())
                viewModel.reauthenticateUser(password).observe(viewLifecycleOwner, Observer {
                    when(it) {
                        is Resource.Loading -> {
                            showLoading(binding.progressBar)
                        }
                        is Resource.Success -> {
                            hideLoading(binding.progressBar)
                            onChangePasswordObserve()
                        }
                        is Resource.Failure -> {
                            hideLoading(binding.progressBar)
                            showError(it.throwable.message, binding.root)
                        }
                    }
                })
        }

    }

    private fun onChangePasswordObserve() {
        viewModel.changePassword(password, newPassword).observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> {
                    showLoading(binding.progressBar)
                }
                is Resource.Success -> {
                    hideLoading(binding.progressBar)
                    this.findNavController().popBackStack()
                    showMessage(R.string.succeed_change, binding.root.context)
                }
                is Resource.Failure -> {
                    hideLoading(binding.progressBar)
                    showError(it.throwable.message, binding.root)
                }
            }
        })
    }

    private fun onCheckFields(): Boolean {
        password = binding.edtChangeCurrentPassword.text.toString()
        newPassword = binding.edtChangeNewPassword.text.toString()
        repeatPassword = binding.edtChangeRepeatNewPassword.text.toString()

        if (password.isBlank()) {
            binding.edtChangeCurrentPassword.error = resources.getString(R.string.enter_password)
            return false
        }
        if (newPassword.isBlank()) {
            binding.edtChangeNewPassword.error = resources.getString(R.string.enter_password)
            return false
        }
        if (repeatPassword.isBlank()) {
            binding.edtChangeRepeatNewPassword.error = resources.getString(R.string.enter_password)
            return false
        }
        if (newPassword.length < 6) {
            binding.edtChangeNewPassword.error = resources.getString(R.string.password_six_char)
            return false
        }
        if (newPassword != repeatPassword) {
            binding.edtChangeRepeatNewPassword.error = resources.getString(R.string.passwords_not_match)
            return false
        }

        return true
    }


}
