package kz.coffee.go.presentation.qrScan

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kz.coffee.go.R

class ScanQrCodeFragment : Fragment() {

    companion object {
        fun newInstance() = ScanQrCodeFragment()
    }

    private lateinit var viewModel: ScanQrCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scan_qr_code_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScanQrCodeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
