package com.almin.permissiongrantor.permission

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityCompat
import java.lang.ref.WeakReference

/**
 * Created by Almin on 2018/7/13.
 */
class ActivityPermissionDelegate(activity: Activity) : PermissionDelegate(){
    private val activityWeakReference = WeakReference<Activity>(activity)

    override fun getContext(): Context = activityWeakReference.get()!!

    override fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activityWeakReference.get()!!,permissions,requestCode)
    }

    override fun shouldShowRequestPermissionRationale(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activityWeakReference.get()!!, permission)) {
                return true
            }
        }
        return false
    }
}