package com.fire.lib.base

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.fire.lib.network.bean.NetState
import com.fire.lib.utils.DialogStateManager
import com.fire.lib.utils.LogUtils
import com.fire.lib.utils.NetworkStateManager
import java.lang.reflect.ParameterizedType

/**
 * ViewModelFragment基类，自动把ViewModel注入Fragment
 */
abstract class LazyFragment<VM : BaseViewModel> : Fragment() {

    lateinit var mViewModel: VM
    private var mFragmentTag = this.javaClass.simpleName

    private val handler = Handler()

    //是否第一次加载
    private var isFirst: Boolean = true

    private var dialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtils.d(mFragmentTag, " : onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        LogUtils.d(mFragmentTag, " : onCreateView")
        return createLayoutView(inflater, container, savedInstanceState)
    }

    /**
     * Fragment执行onViewCreated后触发的方法
     */
    open fun initData() {
        LogUtils.d(mFragmentTag, " : initData")
    }

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtils.d(mFragmentTag, " : onViewCreated")
        isFirst = true
        mViewModel = createViewModel()
        initView(savedInstanceState)
        initData()
        createObserver()
        DialogStateManager.instance.mDialogStateCallback.observe(this) { message ->
            showDialogWi(message)
        }
    }

    /**
     * TODO 设置仅一次弹窗
     */

    private fun showDialogWi(message: String) {
        if (lifecycle.currentState == Lifecycle.State.RESUMED) {
            if (dialog == null) {

//                    MaterialDialog(requireContext()).show {
//                    positiveButton(text = "确定")
//                    negativeButton(text = "取消")
//                    message(text = message)

                dialog = MaterialDialog(requireContext())
                    .cancelable(true)
                    .cancelOnTouchOutside(false)
                    .cornerRadius(12f)
//                        .customView(R.layout.layout_custom_progress_dialog_view)
                    .lifecycleOwner(this)
            }
            dialog?.show {
                positiveButton(text = "确定")
                negativeButton(text = "取消")
                message(text = message)
            }

        }

    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 当前Fragment绑定的视图布局
     */
    @LayoutRes
    abstract fun layoutId(): Int

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * 获取rootView
     */
    abstract fun createLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

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

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            // 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿
            handler.postDelayed({
                lazyLoadData()

                //在Fragment中，只有懒加载过了才能开启网络变化监听
                NetworkStateManager.instance.mNetworkStateCallback.observe(
                    viewLifecycleOwner,
                    Observer {
                        //不是首次订阅时调用方法，防止数据第一次监听错误
                        if (!isFirst) {
                            onNetworkStateChanged(it)
                        }
                    })
                isFirst = false
            }, 300)
        }
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}
}