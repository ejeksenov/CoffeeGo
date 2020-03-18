package kz.coffee.go.presentation.qrScan

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.fragment.findNavController
import com.google.zxing.Result
import kz.coffee.go.R
import kz.coffee.go.databinding.ScanQrCodeFragmentBinding
import kz.coffee.go.presentation.base.BaseFragment
import kz.coffee.go.presentation.main.MainActivity
import kz.coffee.go.utils.CAFETERIA_QR_CODE
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler

class ScanQrCodeFragment : BaseFragment(), ResultHandler {

    companion object {
        fun newInstance() = ScanQrCodeFragment()
    }

    private var isFlashOn = false

    private val zXingScannerView by lazy { ZXingScannerView(context) }
    private lateinit var binding: ScanQrCodeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.scan_qr_code_fragment, container, false)
        zXingScannerView.setAutoFocus(true)
        binding.zxScannerView.addView(zXingScannerView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnScanQrCodeCancel.setOnClickListener {
            this.findNavController().popBackStack()
            (activity as MainActivity).showBottomNavigation()
        }
        binding.btnScanQrCodeFlash.setOnClickListener {
            isFlashOn = !isFlashOn
            zXingScannerView.flash = isFlashOn
            if (isFlashOn) binding.btnScanQrCodeFlash.setImageResource(R.drawable.ic_flash_off_white) else binding.btnScanQrCodeFlash.setImageResource(
                R.drawable.ic_flash_on_white
            )
        }
    }

    override fun onResume() {
        super.onResume()
        onStartCamera()
    }

    private fun onStartCamera() {
        zXingScannerView.setResultHandler(this)
        zXingScannerView.startCamera()
    }

    override fun onStop() {
        super.onStop()
        zXingScannerView.stopCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.zxScannerView.removeView(zXingScannerView)
    }

    override fun handleResult(rawResult: Result?) {
        val qrCodeText = rawResult?.text
        if (qrCodeText?.contains(",")!! && qrCodeText.split(",").size >= 3) {
            val arg = bundleOf(CAFETERIA_QR_CODE to qrCodeText)
            this.findNavController()
                .navigate(R.id.action_scanQrCodeFragment_to_addCashbackFragment, arg)
        } else
            onAlertSimpleDialog(resources.getString(R.string.error_qr_code), binding.root.context)
    }

    private fun onAlertSimpleDialog(
        message: String?,
        context: Context?
    ) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            onStartCamera()
        }
        alertDialog.show()
    }

}
