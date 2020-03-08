package kz.coffee.go.presentation.editProfile

import android.Manifest.permission
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import kz.coffee.go.R
import kz.coffee.go.databinding.EditProfileFragmentBinding
import kz.coffee.go.domain.User
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
        var user = User("", 0.0, "", "")
    }

    private val REQUEST_CAMERA = 1

    private val viewModel: EditProfileViewModel by viewModel()
    private lateinit var binding: EditProfileFragmentBinding

    private var imageUri: Uri? = null
    private var userName = ""
    private val citiesArray: Array<String> by lazy { resources.getStringArray(R.array.cities_kz) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.edit_profile_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(binding.root.context, R.layout.spinner_item, citiesArray)
        binding.spEditProfuleCities.adapter = adapter

        onGetUserData()

        binding.spEditProfuleCities.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    user.city = citiesArray[position]
                }

            }

        binding.btnEditProfileBack.setOnClickListener {
            this.findNavController().popBackStack()
        }

        binding.btnEditProfileSave.setOnClickListener {
            if (onCheckField()) {
                if (imageUri != null)
                    onSaveImageToStorage()
                else
                    onUpdateUserData()
            }
        }

        binding.tvEditProfileChangeAvatar.setOnClickListener {
            if (checkPermission())
                onOpenImagePicker()
            else
                requestPermission()
        }
    }

    private fun onUpdateUserData() {
        viewModel.editProfile(user).observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Loading -> {
                    showLoading(binding.progressBar)
                }
                is Resource.Success -> {
                    hideLoading(binding.progressBar)
                    showMessage(R.string.succeed_change, binding.root.context)
                    this.findNavController().popBackStack()
                }
                is Resource.Failure -> {
                    hideLoading(binding.progressBar)
                    showError(it.throwable.message, binding.root)
                }
            }
        })
    }

    private fun onSaveImageToStorage() {
        viewModel.saveImageToStorage(imageUri!!).observe(viewLifecycleOwner, Observer {
                when(it) {
                    is Resource.Loading -> {
                        showLoading(binding.progressBar)
                    }
                    is Resource.Success -> {
                        hideLoading(binding.progressBar)
                        user.imageUrl = it.data
                        onUpdateUserData()
                    }
                    is Resource.Failure -> {
                        hideLoading(binding.progressBar)
                        showError(it.throwable.message, binding.root)
                    }
                }
        })
    }

    private fun onCheckField(): Boolean {
        userName = binding.edtEditProfileName.text.toString()
        if (userName.isBlank() || userName.length < 3) {
            binding.edtEditProfileName.error = resources.getString(R.string.enter_fullname)
            return false
        }
        return true
    }

    private fun onOpenImagePicker() {
        Pix.start(this, 100)
    }

    private fun onGetUserData() {
        viewModel.getUserData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    showLoading(binding.progressBar)
                }
                is Resource.Success -> {
                    hideLoading(binding.progressBar)
                    val userMap = it.data
                    user = User(
                        fullName = userMap.fullName,
                        cashback = userMap.cashback,
                        city = userMap.city,
                        imageUrl = userMap.imageUrl
                    )
                    onBindToView()
                }
                is Resource.Failure -> {
                    hideLoading(binding.progressBar)
                    showError(it.throwable.message, binding.root)
                }
            }
        })
    }

    private fun onBindToView() {
        Glide.with(binding.root).load(user.imageUrl).error(R.drawable.ic_broken_image_gray)
            .placeholder(R.drawable.ic_account_circle_gray).centerInside().apply(
                RequestOptions.circleCropTransform()
            ).into(binding.ivEditProfileAvatar)
        binding.edtEditProfileName.setText(user.fullName)

        val city = user.city
        if (!city.isNullOrBlank()) {
            val index = citiesArray.indexOf(city)
            binding.spEditProfuleCities.setSelection(index)
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context?.applicationContext!!,
            permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(permission.CAMERA),
            REQUEST_CAMERA
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onOpenImagePicker()
                } else {
                    Toast.makeText(
                        context?.applicationContext,
                        "Permission Denied, You cannot access and camera",
                        Toast.LENGTH_LONG
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permission.CAMERA)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                                    requestPermissions(
                                        arrayOf(permission.CAMERA),
                                        REQUEST_CAMERA
                                    )
                                }
                            )
                            return
                        }
                    }
                }
                return
            }
        }
    }

    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val returnValue =
                data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            val imageUriStr = returnValue[0]
            if (imageUriStr != null) {
                val imageUri =
                    Uri.fromFile(File(imageUriStr))
                Glide.with(binding.root.context).load(imageUri)
                    .placeholder(R.drawable.ic_account_circle_gray).apply(
                    RequestOptions.circleCropTransform()
                ).into(binding.ivEditProfileAvatar)
                binding.ivEditProfileAvatar.setImageURI(imageUri)
                this.imageUri = imageUri
            }
        }
    }


}
