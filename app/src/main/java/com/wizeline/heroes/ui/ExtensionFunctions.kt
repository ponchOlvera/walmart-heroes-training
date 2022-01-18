package com.wizeline.heroes.ui

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.wizeline.heroes.R

object ExtensionFunctions {

    var progressDialog: AlertDialog? = null

    fun Fragment.showDialog(context: Context?){
        if (progressDialog == null){
            val progressView = LayoutInflater.from(context).inflate(R.layout.progress_bar_dialog, null)
            progressDialog = AlertDialog.Builder(context)
                .setView(progressView)
                .setCancelable(false)
                .create()
        }
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.show()
    }

    fun Fragment.dismissDialog(){
        progressDialog?.dismiss()
    }
}