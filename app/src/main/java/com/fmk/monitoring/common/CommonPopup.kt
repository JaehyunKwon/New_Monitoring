package com.fmk.monitoring.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.LinearLayout
import android.widget.TextView
import com.fmk.monitoring.R

object CommonPopup {

    /**
     * 확인, 취소 다이얼로그 (버튼 텍스트 변경 가능)
     * @param context
     * @param title
     * @return
     */
    fun showConfirmCancelDialog(
        context: Context,
        isCancelable: Boolean,
        title: String,
        msg: String,
        button1Txt: String?,
        button2Txt: String?,
        cancelListener: DialogInterface.OnClickListener?,
        confirmListener: DialogInterface.OnClickListener?
    ): Dialog? {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setNegativeButton(button1Txt, cancelListener)
        builder.setPositiveButton(button2Txt, confirmListener)
        dialog = builder.create()
        dialog.setCancelable(isCancelable)
        dialog.show()
        return dialog
    }
}