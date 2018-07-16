# PermissionGrantor
## A library of handles Android's dynamic permissions





### how to use ：

##### 1.prepare work

* clone the library in your project 

* register the permission in the Mainfest.xml file when you need

  

##### 2.simple example

* create a PermissionGrantor instance  in Activity/Fragment , `PermissionGrantor.with(activity/fragment).build()`
* set up request permission result handle 
* choose the permission instance in the `Permission.kt`  you need
* use `fun handlePermission(permission: Permission, permissionGrantListener: PermissionGrantListener) `  or `fun handlePermission(permission: Permission, permissionGrantListener: PermissionGrantListener) `  to handle single request and multi-request

···kotlin 

```
class MainActivity : AppCompatActivity() {
    private lateinit var permissionGrantor: PermissionGrantor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionGrantor = PermissionGrantor.with(this).build()
        ...
    }
    
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionGrantor.onRequestPermissionsResult(requestCode,permissions,grantResults)
    	super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
   
}
```


```kotlin
// handle single permission request
permissionGrantor.handlePermission(Permission.calendar(), object: PermissionGrantListener{
       override fun onPermissionGrantFailed() {
                    
       }

       override fun onPermissionGrantSuccess() {
                    
       }
})
```

```kotlin
// handle multi-permission requset
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
```

end

just a study project.

If you have any questions, please leave a message to me.
