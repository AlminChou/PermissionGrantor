package com.almin.permissiongrantor.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.util.SparseArray
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Almin on 2018/7/11.
 */
class DefaultGrantStrategy(private val permissionDelegate: PermissionDelegate) : GrantStrategy{

    companion object {
        private const val MULTI_PERMISSION_REQUEST_CODE = 1616
    }

    private val atomicInteger = AtomicInteger(30)

    private val permissionGrantListenerArray = SparseArray<PermissionGrantListener>()
    private var permissionsGrantListener: PermissionsGrantListener? = null


    // for single permission
    override fun handlePermission(permission: Permission, permissionGrantListener: PermissionGrantListener) {
        val requestCode = if(permission.requestCode == -1) permission.requestCode else atomicInteger.getAndIncrement()
        if(permissionDelegate.hasSelfPermissions(permission.name)){
            permissionGrantListener.onPermissionGrantSuccess()
        }else{
            permissionGrantListenerArray.put(requestCode, permissionGrantListener)
            permissionDelegate.requestPermissions(arrayOf(permission.name), requestCode)
        }
    }

    // fo multi - permission
    override fun handlePermissions(permissions: Array<Permission>, permissionsGrantListener: PermissionsGrantListener) {
        this.permissionsGrantListener = permissionsGrantListener

        val requestCode = MULTI_PERMISSION_REQUEST_CODE

        val requestPermissions = ArrayList<String>().toMutableList()

        for(permission in permissions){
            if(permissionDelegate.hasSelfPermissions(permission.name)){
                permissionsGrantListener.onPermissionsGrantSuccess(permission.name)
            }else{
                requestPermissions.add(permission.name)
            }
        }

        if(requestPermissions.size > 0){
            permissionDelegate.requestPermissions(requestPermissions.toTypedArray(), requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(permissions.isNotEmpty()){
            if(requestCode == MULTI_PERMISSION_REQUEST_CODE){
                handleMultiPermissionsResult(requestCode,permissions,grantResults)
            }else{
                handleSinglePermissionResult(requestCode,permissions,grantResults)
            }
        }
    }

    private fun handleMultiPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        var needHandleNeverAskCount = 0
        for (permission in permissions) {
            when (grantResults[permissions.indexOf(permission)]) {
                PackageManager.PERMISSION_GRANTED -> permissionsGrantListener!!.onPermissionsGrantSuccess(permission)
                PackageManager.PERMISSION_DENIED -> {
                    permissionsGrantListener!!.onPermissionGrantFailed(permission)
                    if (!permissionDelegate.shouldShowRequestPermissionRationale(*permissions)) {
                        //never ask
                        needHandleNeverAskCount++
                    }
                }
            }
        }
        if(needHandleNeverAskCount > 0) {
            val toastMessage = if(needHandleNeverAskCount>1) "${needHandleNeverAskCount}个" else null
            navigateToAppSettingPage("${toastMessage}权限已被拒绝且不再询问, 请手动打开权限后重试")
        }
        permissionsGrantListener = null
    }

    private fun handleSinglePermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        with(permissionGrantListenerArray.get(requestCode)){
            when(grantResults[0]){
                PackageManager.PERMISSION_GRANTED -> onPermissionGrantSuccess()
                PackageManager.PERMISSION_DENIED -> {
                    onPermissionGrantFailed()
                    val show = permissionDelegate.shouldShowRequestPermissionRationale(*permissions)
                    if(show){
                        //next time will ask again
                    }else{
                        //never ask
                        navigateToAppSettingPage("权限已被拒绝且不再询问, 请手动打开权限后重试")
                    }
                }
            }
            permissionGrantListenerArray.remove(permissionGrantListenerArray.indexOfValue(this))
        }
    }


    private fun navigateToAppSettingPage(message: String){
        AlertDialog.Builder(permissionDelegate.getContext())
                .setTitle("警告")
                .setMessage(message)
                .setPositiveButton("去手动授权") { dialog, which ->
                    val intent =  Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse("package:${permissionDelegate.getContext().packageName}")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    permissionDelegate.getContext().startActivity(intent)
                }
                .setNegativeButton("取消", null)
                .show()
    }
}