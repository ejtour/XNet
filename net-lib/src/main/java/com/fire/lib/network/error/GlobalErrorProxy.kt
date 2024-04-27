package com.fire.lib.network.error

import com.fire.lib.network.core.HttpResponseCallAdapterFactory

/**
 * 全局错误的代理
 */
class GlobalErrorProxy() : IGlobalError, HttpResponseCallAdapterFactory.ErrorHandler {

    private var globalError: IGlobalError? = null
    fun initGlobalError(globalError: IGlobalError) {

        this.globalError = globalError;

    }

    override fun onFailure(throwable: BusinessException) {
        globalError?.onFailure(throwable)
    }
}