package kz.coffee.go.presentation.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kz.coffee.go.R
import kz.coffee.go.databinding.ProfileFragmentBinding
import kz.coffee.go.domain.User
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var binding: ProfileFragmentBinding

    private val settingsList: MutableList<String> by lazy { context?.resources?.getStringArray(R.array.settings)?.toMutableList()!! }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsersData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideLoading(binding.progressBar)
                    val user = it.data
                    onBindDataToView(user)
                }
                is Resource.Loading -> {
                    showLoading(binding.progressBar)
                }
                is Resource.Failure -> {
                    hideLoading(binding.progressBar)
                    showError(it.throwable.message, binding.root)
                }
            }
        })

        binding.rvProfileSettings.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )
        val adapter = ProfileSettingsAdapter(settingsList)
        binding.rvProfileSettings.adapter = adapter

        adapter.onItemClick = {
            onManageSettingsItemClick(it)
        }

        binding.tvProfileEdit.setOnClickListener {

        }

        binding.btnProfileSignOut.setOnClickListener {
            onAlertSignOut()
        }

    }

    private fun onManageSettingsItemClick(position: Int) {
        when (position) {
            0 -> {

            }
            1 -> {

            }
            2 -> onOpenAgreementUrl()
            3 -> onShareApp()
            4 -> onAlertContactUs()
        }
    }

    private fun onAlertContactUs() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage(resources.getString(R.string.contact_us_text))
        alertDialogBuilder.setPositiveButton(R.string.call) { dialog, _ ->
            onCallToUs()
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton(R.string.write) { dialog, _ ->
            onSendEmail()
            dialog.dismiss()
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }

    private fun onAlertSignOut() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage(resources.getString(R.string.sign_out_question))
        alertDialogBuilder.setPositiveButton(R.string.yes) { dialog, _ ->
            viewModel.signOut()
            dialog.dismiss()
            this.findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.show()
    }

    private fun onBindDataToView(user: User) {
        val imageUrl = user.imageUrl
        if (!imageUrl.isNullOrBlank())
            Glide.with(binding.root.context).load(imageUrl).centerInside().placeholder(R.drawable.ic_account_circle_gray).apply(
                RequestOptions.circleCropTransform()
            ).into(binding.ivProfileAvatar)
        else
            Glide.with(binding.root.context).load(R.drawable.ic_account_circle_gray).apply(
                RequestOptions.circleCropTransform()
            ).into(binding.ivProfileAvatar)

        binding.tvProfileName.text = user.fullName
        binding.tvProfileBalance.text = user.cashback.toString()
    }


    private fun onOpenAgreementUrl() {
        val agreementUrl =
            "https://app.termly.io/document/terms-and-conditions/5ac5d493-8d8d-4a26-839e-f9a40c5364f6"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(agreementUrl)
        startActivity(i)
    }

    private fun onShareApp() {
        val sendIntent = Intent()
        val packageName = /*context?.packageName!!*/"kz.nextstep.coffeego"
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Скачивайте CoffeeGo: https://play.google.com/store/apps/details?id=${packageName}"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    private fun onCallToUs() {
        val phoneNumber = "+77010374040"
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun onSendEmail() {
        val email = "alima.seytkhan@gmail.com"
        val uri = Uri.parse("mailto:$email")
            .buildUpon()
            .appendQueryParameter("subject", "")
            .appendQueryParameter("body", "")
            .build()
        val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(Intent.createChooser(emailIntent, ""))
    }


}
