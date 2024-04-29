package com.fire.lib.utils

import com.fire.lib.base.EventLiveData

class DialogStateManager private constructor() {

    val mDialogStateCallback = EventLiveData<String>()

    companion object {
        val instance: DialogStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DialogStateManager()
        }
    }
}