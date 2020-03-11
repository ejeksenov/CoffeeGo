package kz.coffee.go.presentation.addCashback

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kz.coffee.go.R

class AddCashbackFragment : Fragment() {

    companion object {
        fun newInstance() =
            AddCashbackFragment()
    }

    private lateinit var viewModel: AddCashbackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_cashback_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddCashbackViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
