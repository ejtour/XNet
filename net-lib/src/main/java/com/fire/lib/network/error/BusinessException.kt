package com.fire.lib.network.error

import java.lang.RuntimeException

/**
 * 业务的错误，错误码+ 错误String
 */
class  BusinessException(val code: Int, message: String?) : RuntimeException(message)