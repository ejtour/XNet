package com.fire.lib.utils

import com.fire.lib.base.EventLiveData
import com.fire.lib.network.bean.NetState

class NetworkStateManager private constructor(){

    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy {
            NetworkStateManager()
        }
    }
}