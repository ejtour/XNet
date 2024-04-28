package com.fire.lib.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import com.fire.lib.network.R
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Rationale
import com.yanzhenjie.permission.RequestExecutor
import com.yanzhenjie.permission.runtime.Permission


object PermissionHelp {

    fun with(
        activity: Activity,
        permissions: Array<String>,
        onGrantedClick: (List<String>) -> Unit = {},
        onDeniedClick: (List<String>) -> Unit = {}
    ) {

        AndPermission.with(activity)
            .runtime()
            .permission(permissions) //权限组 存储
            //.rationale(DefaultRationale()) //拒绝一次后重试
            .onGranted(Action { permission ->
                onGrantedClick(permission)
            }) //权限调用成功后的回调
            .onDenied(Action { permissions ->
                onDeniedClick(permissions)
            }) //权限调用失败后的回调
            .start()
    }


    class DefaultRationale : Rationale<List<String>> {

        override fun showRationale(
            context: Context,
            permissions: List<String>,
            executor: RequestExecutor
        ) {
            val permissionNames: List<String> = Permission.transformText(context, permissions)
            val message = context.getString(
                R.string.message_permission_rationale,
                TextUtils.join("\n", permissionNames)
            )
            AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage(message)
                .setNegativeButton("取消") { dialog: DialogInterface?, which: Int -> executor.cancel() }
                .setPositiveButton("确定") { dialog: DialogInterface?, which: Int -> executor.execute() }
                .create()
                .show()
        }

    }


}
