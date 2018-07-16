package com.almin.permissiongrantor.permission

/**
 * Created by Almin on 2018/7/6.
 */
interface PermissionGrantListener{
    fun onPermissionGrantSuccess()
    fun onPermissionGrantFailed()
}