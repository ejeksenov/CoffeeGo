package kz.coffee.go.presentation.base

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

abstract class BaseFragment  : Fragment() {
    open fun showError(@StringRes errorMessage: Int, rootView: View) {
        (activity as BaseActivity).showError(errorMessage, rootView)
    }

    open fun showError(errorMessage: String?, rootView: View) {
        (activity as BaseActivity).showError(errorMessage, rootView)
    }

    open fun showLoading(progressBar: ProgressBar) {
        (activity as BaseActivity).showLoading(progressBar)
    }

    open fun hideLoading(progressBar: ProgressBar) {
        (activity as BaseActivity).hideLoading(progressBar)
    }

    open fun showMessage(@StringRes message: Int, context: Context) {
        (activity as BaseActivity).showMessage(message, context)
    }

    open fun showMessage(message: String, context: Context) {
        (activity as BaseActivity).showMessage(message, context)
    }
}