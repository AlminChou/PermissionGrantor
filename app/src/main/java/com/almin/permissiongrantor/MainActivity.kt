package com.almin.permissiongrantor

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.almin.permissiongrantor.permission.Permission
import com.almin.permissiongrantor.permission.PermissionGrantListener
import com.almin.permissiongrantor.permission.PermissionGrantor
import com.almin.permissiongrantor.permission.PermissionsGrantListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var permissionGrantor: PermissionGrantor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionGrantor = PermissionGrantor.with(this).build()

        btn_get_permission.setOnClickListener{
            permissionGrantor.handlePermission(Permission.calendar(), object: PermissionGrantListener {
                override fun onPermissionGrantFailed() {
                    println("onPermissionGrantFailed")
                }

                override fun onPermissionGrantSuccess() {
                    println("onPermissionGrantSuccess")
                }
            })
        }



        btn_get_permissions.setOnClickListener {
            permissionGrantor.handlePermissions(Permission.calendar(), Permission.storage(), permissionsGrantListener = object : PermissionsGrantListener {
                override fun onPermissionGrantFailed(permission: String) {
                    when(permission){
                        android.Manifest.permission.WRITE_CALENDAR -> println("  Failed    $permission")
                        android.Manifest.permission.READ_EXTERNAL_STORAGE -> println("  Failed    $permission")
                    }
                }

                override fun onPermissionsGrantSuccess(permission: String) {
                    when(permission){
                        android.Manifest.permission.WRITE_CALENDAR -> println("  Success    $permission")
                        android.Manifest.permission.READ_EXTERNAL_STORAGE -> println("  Success    $permission")
                    }
                }
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionGrantor.onRequestPermissionsResult(requestCode,permissions,grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
