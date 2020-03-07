package kz.coffee.go.presentation.changeEmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import kz.coffee.go.R
import kz.coffee.go.databinding.ChangeEmailFragmentBinding
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.utils.Resource
import kz.coffee.go.utils.checkEmailValidity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangeEmailFragment : BaseFragment() {

    companion object {
        fun newInstance() = ChangeEmailFragment()
    }

    private val viewModel: ChangeEmailViewModel by viewModel()
    private lateinit var binding: ChangeEmailFragmentBinding

    private var email: String = ""
    private var newEmail: String = ""
    private var password: String = ""

    private val currentEmailText: String by lazy { resources.getString(R.string.current_email_text) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.change_email_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = viewModel.getCurrentEmail()
        binding.tvChangeEmailCurrent.text = "$currentEmailText\n$email"

        binding.btnChangeEmailBack.setOnClickListener {
            this.findNavController().popBackStack()
        }

        binding.btnChangeEmailSave.setOnClickListener {
            if (onCheckFields())
                viewModel.reauthenticateUser(password).observe(viewLifecycleOwner, Observer {
                    when(it) {
                        is Resource.Loading -> {
                            showLoading(binding.progressBar)
                        }
                        is Resource.Success -> {
                            hideLoading(binding.progressBar)
                            onChangeEmailObserve()
                        }
                        is Resource.Failure -> {
                            hideLoading(binding.progressBar)
                            showError(it.throwable.message, binding.root)
                        }
                    }
                })
        }

    }

    private fun onChangeEmailObserve() {
        viewModel.changeEmail(newEmail).observe(viewLifecycleOwner, Observer {
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
        newEmail = binding.edtChangeEmailNew.text.toString()
        password = binding.edtChangeEmailCurrentPassword.text.toString()
        if (newEmail.isBlank() || !checkEmailValidity(newEmail)) {
            binding.edtChangeEmailNew.error = resources.getString(R.string.enter_valid_email)
            return false
        }
        if (password.isBlank()) {
            binding.edtChangeEmailCurrentPassword.error = resources.getString(R.string.enter_password)
            return false
        }
        return true
    }

}
