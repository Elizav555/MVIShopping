package com.elizav.mvishopping.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.elizav.mvishopping.R

object ShowDialog {
    fun showDialog(context: Context, params: DialogParams) {
        val dialog = AlertDialog.Builder(context)
            .setTitle(params.title)
            .setMessage(params.message)
            .setPositiveButton(
                params.submitBtnText,
                params.submitOnClickListener
            )
            .setNegativeButton(R.string.cancel, params.cancelOnClickListener)
            .create()
        dialog.show()
    }
}