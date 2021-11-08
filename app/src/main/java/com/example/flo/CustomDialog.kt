package com.example.flo

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class CustomDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    var delete = false

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.message_check)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val title = dialog.findViewById<TextView>(R.id.check_title)
        val NO=dialog.findViewById<TextView>(R.id.check_no_btn)
        val YES =dialog.findViewById<TextView>(R.id.check_yes_btn)

        NO.setOnClickListener {
            dialog.dismiss()
        }

        YES.setOnClickListener {
            delete= true
            onClickListener.onClicked(delete)
            delete=false
            dialog.dismiss()

        }

    }

    interface OnDialogClickListener
    {
        fun onClicked(delete:Boolean)
    }
}