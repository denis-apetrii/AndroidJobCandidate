package app.storytel.candidate.com.ui

import android.content.Context
import app.storytel.candidate.com.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * Show an alert dialog with error message
 * @param context
 * @param message Display message text
 * @param buttonMessage Dialog button text
 * @param onclick Code block for handling on dialog button click
 */
fun showErrorDialog(context:Context, message: String, buttonMessage: Int, onclick: () -> Unit) {

    val dialog = MaterialAlertDialogBuilder(context).apply {
        setTitle(R.string.error_title)
        setMessage(message)

        setPositiveButton(buttonMessage) { dialog, _ ->
            dialog.dismiss()
            onclick()
        }

        setCancelable(false)
    }.create()
    dialog.show()
}
