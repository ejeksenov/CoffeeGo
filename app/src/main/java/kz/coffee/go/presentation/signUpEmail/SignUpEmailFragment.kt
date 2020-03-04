package kz.coffee.go.presentation.signUpEmail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import kz.coffee.go.R
import kz.coffee.go.databinding.SignUpEmailFragmentBinding
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.presentation.main.MainActivity
import kz.coffee.go.utils.Resource
import kz.coffee.go.utils.checkEmailValidity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpEmailFragment : BaseFragment() {

    companion object {
        fun newInstance() = SignUpEmailFragment()
    }
    private val viewModel: SignUpEmailViewModel by viewModel()
    private lateinit var binding: SignUpEmailFragmentBinding

    private var emailStr = ""
    private var passwordStr = ""
    private var fullNameStr = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_email_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            if (onCheckFields())
                viewModel.createUserWithEmailAndPassword(emailStr, passwordStr, fullNameStr).observe(viewLifecycleOwner, Observer {
                    when(it) {
                        is Resource.Loading -> {
                            showLoading(binding.progressBar)
                        }
                        is Resource.Success -> {
                            hideLoading(binding.progressBar)
                            showMessage(resources.getString(R.string.succeed_signed_up), binding.root.context)
                            this.findNavController().navigate(R.id.action_signUpEmailFragment_to_homeFragment)
                            (activity as MainActivity).showBottomNavigation()
                        }
                        is Resource.Failure -> {
                            hideLoading(binding.progressBar)
                            showError(it.throwable.message, binding.root)
                        }
                    }
                })
        }

        binding.btnSignUpBack.setOnClickListener {
            this.findNavController().popBackStack()
        }
    }

    private fun onCheckFields(): Boolean {
        emailStr = binding.edtSignUpEmail.text.toString().trim()
        fullNameStr = binding.edtSignUpName.text.toString().trim()
        passwordStr = binding.edtSignUpPassword.text.toString().trim()

        if (emailStr.isBlank() || !checkEmailValidity(emailStr)) {
            binding.edtSignUpEmail.error = resources.getString(R.string.enter_valid_email)
            return false
        }

        if (fullNameStr.isBlank() || fullNameStr.length < 3) {
            binding.edtSignUpName.error = resources.getString(R.string.enter_fullname)
            return false
        }

        if (passwordStr.isBlank()) {
            binding.edtSignUpPassword.error = resources.getString(R.string.enter_password)
            return false
        }

        if (passwordStr.length < 6) {
            binding.edtSignUpPassword.error = resources.getString(R.string.password_six_char)
            return false
        }

        return true
    }

}
