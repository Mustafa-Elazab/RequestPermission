package com.example.requestpermission

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHelper(context: Fragment, permissionListener: PermissionListener) {

    private var context: Fragment? = null
    private var permissionListener: PermissionListener? = null


    init {
        this.context = context
        this.permissionListener = permissionListener
    }

    private val requestPermissionListener =
        context.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                permissionListener.isPermissionGranted(true)
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private val requestMultiplePermissionsLauncher = context.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            Log.i("DEBUG", "${it.key} = ${it.value}")
            if (it.value) {
                permissionListener.isPermissionGranted(true)
            } else {
                Log.i("Permission: ", "Denied")
            }
        }
    }

    fun checkForPermissions(manifestPermission: String) {
        when {
            context?.requireContext()?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    manifestPermission
                )
            } == PackageManager.PERMISSION_GRANTED -> {
                println("Permission Granted....")
                permissionListener?.isPermissionGranted(true)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                context?.requireActivity() as Activity,
                manifestPermission
            ) -> {
                permissionListener?.isPermissionGranted(false)
                permissionListener?.shouldShowRationaleInfo()
            }
            else -> {
                launchPermissionDialog(manifestPermission)
                println("Final Else....")
            }
        }
    }

    private var isDenied: Boolean = false
    fun checkForMultiplePermissions(manifestPermissions: Array<String>) {
        for (permission in manifestPermissions) {
            context?.requireContext()?.let {
                if (ContextCompat.checkSelfPermission(
                        it,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    println("Permission Granted....")
                    permissionListener?.isPermissionGranted(true)
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context?.requireActivity() as Activity,
                        permission
                    )
                ) {
                    isDenied = true
                } else {
                    requestMultiplePermissionsLauncher.launch(manifestPermissions)
                }
            }
        }
        if (isDenied) {
            permissionListener?.isPermissionGranted(false)
            permissionListener?.shouldShowRationaleInfo()
        }
    }

    fun launchPermissionDialog(manifestPermission: String) {
        requestPermissionListener.launch(
            manifestPermission
        )
    }

    fun launchPermissionDialogForMultiplePermissions(manifestPermissions: Array<String>) {
        requestMultiplePermissionsLauncher.launch(manifestPermissions)
    }

}