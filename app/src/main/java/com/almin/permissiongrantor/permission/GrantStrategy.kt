package com.almin.permissiongrantor.permission

/**
 * Created by Almin on 2018/7/10.
 */
interface GrantStrategy{
    // for single - permission
    fun handlePermission(permission: Permission, permissionGrantListener: PermissionGrantListener)
    // for multi - permission
    fun handlePermissions(permissions: Array<Permission>, permissionsGrantListener: PermissionsGrantListener)
    // handle result
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
}