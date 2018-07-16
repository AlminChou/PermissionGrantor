package com.almin.permissiongrantor.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.support.v4.app.AppOpsManagerCompat
import android.support.v4.content.PermissionChecker
import android.support.v4.util.SimpleArrayMap

/**
 * Created by Almin on 2018/7/10.
 *
 * base on sdk version, we should use Compact class, such as ActivityCompact.java to handle permission.
 */
abstract class PermissionDelegate{


    companion object {
        // Map of dangerous permissions introduced in later framework versions.
        // Used to conditionally bypass permission-hold checks on older devices.
        private val MIN_SDK_PERMISSIONS = SimpleArrayMap<String, Int>(8).apply {
            put("com.android.voicemail.permission.ADD_VOICEMAIL", 14)
            put("android.permission.BODY_SENSORS", 20)
            put("android.permission.READ_CALL_LOG", 16)
            put("android.permission.READ_EXTERNAL_STORAGE", 16)
            put("android.permission.USE_SIP", 9)
            put("android.permission.WRITE_CALL_LOG", 16)
            put("android.permission.SYSTEM_ALERT_WINDOW", 23)
            put("android.permission.WRITE_SETTINGS", 23)
        }

    }


    abstract fun getContext():Context
    abstract fun requestPermissions(permissions: Array<String>, requestCode: Int)
    abstract fun shouldShowRequestPermissionRationale(vararg permissions: String): Boolean

    /**
     * Checks all given permissions have been granted.
     *
     * @param grantResults results
     * @return returns true if all permissions have been granted.
     */
    fun verifyPermissions(vararg grantResults: Int): Boolean {
        if (grantResults.isEmpty()) return false
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * Returns true if the permission exists in this SDK version
     *
     * @param permission permission
     * @return returns true if the permission exists in this SDK version
     */
    private fun permissionExists(permission: String): Boolean {
        // Check if the permission could potentially be missing on this device
        val minVersion = MIN_SDK_PERMISSIONS.get(permission)
        // If null was returned from the above call, there is no need for a device API level check for the permission
        // otherwise, we check if its minimum API level requirement is met
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion
    }

    /**
     * Returns true if the Activity or Fragment has access to all given permissions.
     *
     * @param context     context
     * @param permissions permission list
     * @return returns true if the Activity or Fragment has access to all given permissions.
     */
    fun hasSelfPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (permissionExists(permission) && !hasSelfPermission(permission)) {
                return false
            }
        }
        return true
    }

    /**
     * Determine context has access to the given permission.
     *
     *
     * This is a workaround for RuntimeException of Parcel#readException.
     *
     * @param context    context
     * @param permission permission
     * @return returns true if context has access to the given permission, false otherwise.
     * @see .hasSelfPermissions
     */
    private fun hasSelfPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && "Xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)) {
            return hasSelfPermissionForXiaomi(getContext(), permission)
        }
        try {
            return PermissionChecker.checkSelfPermission(getContext(), permission) == PackageManager.PERMISSION_GRANTED
        } catch (t: RuntimeException) {
            return false
        }

    }

    private fun hasSelfPermissionForXiaomi(context: Context, permission: String): Boolean {
        val permissionToOp = AppOpsManagerCompat.permissionToOp(permission)
                ?: // in case of normal permissions(e.g. INTERNET)
                return true
        val noteOp = AppOpsManagerCompat.noteOp(context, permissionToOp, Process.myUid(), context.packageName)
        return noteOp == AppOpsManagerCompat.MODE_ALLOWED && PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }


}