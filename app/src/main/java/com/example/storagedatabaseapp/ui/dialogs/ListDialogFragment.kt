package com.example.storagedatabaseapp.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ListDialogFragment(
    private val title: Int,
    private val list: Int,
    private val itemCallback: (String) -> Unit,
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null) {
            throw IllegalStateException("Activity cannot be null")
        }
        isCancelable = false
        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setItems(list) { _, i ->
                itemCallback(resources.getStringArray(list)[i])
            }
            .create()
    }
}