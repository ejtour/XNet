package com.fire.lib.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fire.lib.network.R
import com.fire.lib.network.bean.NetState
import com.fire.lib.utils.NetworkStateManager
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())

        mViewModel = createViewModel()

        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            onNetworkStateChanged(it)
        })
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    @LayoutRes
    abstract fun layoutId(): Int

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 获取当前类绑定的泛型ViewModel-clazz
     */
    @Suppress("UNCHECKED_CAST")
    private fun <VM> getVmClazz(obj: Any): VM {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
    }
}