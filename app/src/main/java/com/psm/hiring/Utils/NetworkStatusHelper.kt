package com.psm.hiring.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import android.net.NetworkInfo

import android.content.Intent

import java.util.HashSet

import android.content.BroadcastReceiver


class NetworkStateReceiver : BroadcastReceiver() {
    protected var listeners: MutableSet<NetworkStateReceiverListener>
    protected var connected: Boolean?
    override fun onReceive(context: Context, intent: Intent) {
        if (intent == null || intent.extras == null) return
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = manager.activeNetworkInfo
        if (ni != null && ni.state == NetworkInfo.State.CONNECTED) {
            connected = true
        } else if (intent.getBooleanExtra(
                ConnectivityManager.EXTRA_NO_CONNECTIVITY,
                java.lang.Boolean.FALSE
            )
        ) {
            connected = false
        }
        notifyStateToAll()
    }

    private fun notifyStateToAll() {
        for (listener in listeners) notifyState(listener)
    }

    private fun notifyState(listener: NetworkStateReceiverListener?) {
        if (connected == null || listener == null) return
        if (connected == true) listener.networkAvailable() else listener.networkUnavailable()
    }

    fun addListener(l: NetworkStateReceiverListener) {
        listeners.add(l)
        notifyState(l)
    }

    fun removeListener(l: NetworkStateReceiverListener) {
        listeners.remove(l)
    }

    interface NetworkStateReceiverListener {
        fun networkAvailable()
        fun networkUnavailable()
    }

    init {
        listeners = HashSet()
        connected = null
    }
}