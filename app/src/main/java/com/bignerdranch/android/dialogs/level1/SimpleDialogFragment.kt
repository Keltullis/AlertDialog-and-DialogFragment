package com.bignerdranch.android.dialogs.level1

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.bignerdranch.android.dialogs.R
import com.bignerdranch.android.dialogs.showToast


class SimpleDialogFragment:DialogFragment() {

    //В бандл скалдывается индекс нажатой кнопки
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener { _,which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
        }
        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setIcon(R.drawable.ic_baseline_local_fire_department_24)
            .setTitle(R.string.default_alert_title)
            .setMessage(R.string.default_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG,"Dialog dismissed")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        showToast(R.string.dialog_cancelled)
    }

    companion object{
        val TAG = SimpleDialogFragment::class.java.simpleName
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        const val KEY_RESPONSE = "RESPONSE"
    }
}

// Всё как обычно,но теперь диалог это фрагмент
// Диалог фрагмент не изчезает после поворота экрана