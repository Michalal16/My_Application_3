package com.example.myapplication
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log

class ConnectivityReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("TAG", "Connectivity Receiver is ready to use!")
        if(context != null)
            isConnectedOrConnecting(context)

    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val allNetworkInfo = connMgr.allNetworkInfo
        for (networkInfo in allNetworkInfo) {
            Log.d(TAG, "Is connected: ${networkInfo?.isConnected}")
            Log.d(TAG, "Type: ${networkInfo?.type} ${ConnectivityManager.TYPE_WIFI}")
        }

        return false
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}