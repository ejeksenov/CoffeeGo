package kz.coffee.go.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kz.coffee.go.R
import kz.coffee.go.databinding.LoginFragmentBinding
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.presentation.main.MainActivity
import kz.coffee.go.utils.Resource
import kz.coffee.go.utils.checkEmailValidity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModel()
    private lateinit var binding: LoginFragmentBinding
    private var emailStr = ""
    private var passwordStr = ""

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoginSignIn.setOnClickListener {
            if (onCheckFields()) {
                viewModel.signInWithEmail(emailStr, passwordStr).observe(viewLifecycleOwner, Observer {
                    when(it) {
                        is Resource.Loading -> {
                            showLoading(binding.progressBar)
                        }
                        is Resource.Failure -> {
                            hideLoading(binding.progressBar)
                            showError(it.throwable.message, binding.root)
                        }
                        is Resource.Success -> {
                            hideLoading(binding.progressBar)
                            (activity as MainActivity).showBottomNavigation()
                            this.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                    }
                })
            }
        }

        binding.btnLoginSignUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_signUpEmailFragment)
        }
    }

    private fun onCheckFields(): Boolean {
        emailStr = binding.edtLoginEmail.text.toString().trim()
        passwordStr = binding.edtLoginPassword.text.toString().trim()

        binding.edtLoginEmail.error = null
        binding.edtLoginPassword.error = null

        if (emailStr.isBlank() || !checkEmailValidity(emailStr)) {
            binding.edtLoginEmail.error = resources.getString(R.string.enter_valid_email)
            return false
        }
        if (passwordStr.isBlank()) {
            binding.edtLoginPassword.error = resources.getString(R.string.enter_password)
            return false
        }
        if (passwordStr.length < 6) {
            binding.edtLoginPassword.error = resources.getString(R.string.password_six_char)
            return false
        }

        return true
    }


}
