package com.wizeline.heroes.ui

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.wizeline.heroes.R

object FragmentExtensionFunctions {

    var progressDialog: AlertDialog? = null

    fun Fragment.showDialog(context: Context?) {
        if (progressDialog == null) {
            val progressView =
                LayoutInflater.from(context).inflate(R.layout.progress_bar_dialog, null)
            progressDialog = AlertDialog.Builder(context)
                .setView(progressView)
                .setCancelable(false)
                .create()
        }
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.show()
    }

    fun Fragment.dismissDialog() {
        progressDialog?.dismiss()
    }

    fun Fragment.isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetworkCapabilities =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}