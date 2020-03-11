package kz.coffee.go.presentation.congratulations

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kz.coffee.go.R

class CongratulationsFragment : Fragment() {

    companion object {
        fun newInstance() =
            CongratulationsFragment()
    }

    private lateinit var viewModel: CongratulationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.congratulations_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CongratulationsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
