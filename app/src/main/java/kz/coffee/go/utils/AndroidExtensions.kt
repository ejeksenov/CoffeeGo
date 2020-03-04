package kz.coffee.go.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.coffee.go.data.coroutine.CoroutineContextProvider
import kotlin.coroutines.CoroutineContext

inline fun <T> LiveData<T>.subscribe(owner: LifecycleOwner, crossinline onDataReceived: (T) -> Unit) =
    observe(owner, Observer { onDataReceived(it) })

fun snackbar(@StringRes message: Int, rootView: View) = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()

fun snackbar(message: String, rootView: View) = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show()

fun showToast(@StringRes message: Int, context: Context) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun showToast(message: String, context: Context) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun View.visible() {
  visibility = View.VISIBLE
}

fun View.gone() {
  visibility = View.GONE
}

fun checkEmailValidity(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun View.hideKeyboard() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

inline fun View.onClick(crossinline onClick: () -> Unit) {
  setOnClickListener { onClick() }
}

inline fun ViewModel.launch(
    coroutineContext: CoroutineContext = CoroutineContextProvider().main,
    crossinline block: suspend CoroutineScope.() -> Unit): Job {
  return viewModelScope.launch(coroutineContext) { block() }
}