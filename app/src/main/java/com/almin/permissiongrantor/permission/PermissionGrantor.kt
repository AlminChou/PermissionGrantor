package com.almin.permissiongrantor.permission

import android.app.Activity
import android.support.v4.app.Fragment


/**
 * Created by Almin on 2018/7/6.
 *
 * permissions have been defined in SDK, so you can find your permission code in Mainfest.class when you need.
 */
class PermissionGrantor private constructor(private val grantStrategy: GrantStrategy){

    // for single permission
    fun handlePermission(permission: Permission, permissionGrantListener: PermissionGrantListener) {
        grantStrategy.handlePermission(permission,permissionGrantListener)
    }

    // fo multi - permission
    fun handlePermissions(vararg permissions: Permission, permissionsGrantListener: PermissionsGrantListener) {
        grantStrategy.handlePermissions(permissions.toMutableList().toTypedArray(),permissionsGrantListener)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        grantStrategy.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }


    class Builder(private val permissionDelegate: PermissionDelegate){
        private var grantStrategy: GrantStrategy? = null

        fun build(): PermissionGrantor {
            if (this.grantStrategy == null) {
                this.grantStrategy = DefaultGrantStrategy(permissionDelegate)
            }

            return PermissionGrantor(this.grantStrategy!!)
        }

        fun strategy(grantStrategy: GrantStrategy): Builder {
            this.grantStrategy = grantStrategy
            return this
        }
    }

    companion object {
        fun with(activity: Activity): Builder {
            return Builder(ActivityPermissionDelegate(activity))
        }

        fun with(fragment: Fragment): Builder {
            return Builder(FragmentPermissionDelegate(fragment))
        }
    }
}