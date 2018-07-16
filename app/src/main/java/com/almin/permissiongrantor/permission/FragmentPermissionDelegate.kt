package com.almin.permissiongrantor.permission

import android.content.Context
import android.support.v4.app.Fragment
import java.lang.ref.WeakReference

/**
 * Created by Almin on 2018/7/13.
 */
class FragmentPermissionDelegate(fragment: Fragment) : PermissionDelegate(){
    private val fragmentWeakReference = WeakReference<Fragment>(fragment)

    override fun getContext(): Context = fragmentWeakReference.get()!!.context!!

    override fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        fragmentWeakReference.get()!!.requestPermissions(permissions,requestCode)
    }

    override fun shouldShowRequestPermissionRationale(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (fragmentWeakReference.get()!!.shouldShowRequestPermissionRationale(permission)) {
                return true
            }
        }
        return false
    }
}