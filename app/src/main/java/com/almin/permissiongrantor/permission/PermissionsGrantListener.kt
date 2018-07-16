package com.almin.permissiongrantor.permission

/**
 * Created by Almin on 2018/7/10.
 */
interface PermissionsGrantListener {
    fun onPermissionsGrantSuccess(permission: String)
    fun onPermissionGrantFailed(permission: String)
}