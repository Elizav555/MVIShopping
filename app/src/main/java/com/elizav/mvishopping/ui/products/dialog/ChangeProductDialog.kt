package com.elizav.mvishopping.ui.products.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.elizav.mvishopping.R
import com.elizav.mvishopping.databinding.DialogChangeProductBinding
import com.elizav.mvishopping.ui.products.ProductsListFragment

class ChangeProductDialog(
    private val productOldName: String? = null,
    private val position: Int? = null
) : DialogFragment() {
    private lateinit var binding: DialogChangeProductBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogChangeProductBinding.inflate(layoutInflater)

        with(binding) {
            return activity?.let {
                var submitBtnText = getString(R.string.add_product)
                productOldName?.let { name ->
                    etName.setText(name)
                    submitBtnText = getString(R.string.change_name)
                }
                val dialog = AlertDialog.Builder(it).setView(root)
                    .setPositiveButton(submitBtnText) { _, _ ->
                        (parentFragment as ProductsListFragment).changeProductName(
                            etName.text.toString().trim(), position
                        )
                    }
                    .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        dialog?.cancel()
                    }.create()
                etName.addTextChangedListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled =
                        etName.text.toString().isNotBlank()
                }
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled =
                        etName.text.toString().isNotBlank()
                }
                dialog
            } ?: throw IllegalStateException(getString(R.string.activity_error))
        }
    }
}