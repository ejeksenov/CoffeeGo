package kz.coffee.go.presentation.base

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import kz.coffee.go.utils.*

abstract class BaseActivity: AppCompatActivity() {

    fun showError(@StringRes errorMessage: Int, rootView: View) = snackbar(errorMessage, rootView)

    fun showError(errorMessage: String?, rootView: View) = snackbar(errorMessage ?: EMPTY_STRING, rootView)

    fun showLoading(progressBar: ProgressBar) = progressBar.visible()

    fun hideLoading(progressBar: ProgressBar) = progressBar.gone()

    fun showMessage(@StringRes message: Int, context: Context) = showToast(message, context)

    fun showMessage(message: String, context: Context) = showToast(message, context)

}