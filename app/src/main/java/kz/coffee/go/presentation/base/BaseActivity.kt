package kz.coffee.go.presentation.base

import android.view.View
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import kz.coffee.go.utils.EMPTY_STRING
import kz.coffee.go.utils.gone
import kz.coffee.go.utils.snackbar
import kz.coffee.go.utils.visible

abstract class BaseActivity: AppCompatActivity() {

    fun showError(@StringRes errorMessage: Int, rootView: View) = snackbar(errorMessage, rootView)

    fun showError(errorMessage: String?, rootView: View) = snackbar(errorMessage ?: EMPTY_STRING, rootView)

    fun showLoading(progressBar: ProgressBar) = progressBar.visible()

    fun hideLoading(progressBar: ProgressBar) = progressBar.gone()
}