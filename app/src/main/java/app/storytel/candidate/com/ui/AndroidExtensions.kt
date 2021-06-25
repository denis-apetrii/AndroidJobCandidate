package app.storytel.candidate.com.ui

import android.content.Context
import android.view.ContextMenu
import android.view.View
import app.storytel.candidate.com.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


/**
 * Show an alert dialog with error message
 * On neutral button click, dismiss dialog and refresh data
 */
fun showErrorDialog(context:Context, message: String, buttonMessage: Int, onclick: () -> Unit) {

    val dialog = MaterialAlertDialogBuilder(context).apply {
        setTitle(R.string.error_title)
        setMessage(message)

        setPositiveButton(buttonMessage) { dialog, _ ->
            dialog.dismiss()
            onclick()
        }
    }.create()
    dialog.show()
}
