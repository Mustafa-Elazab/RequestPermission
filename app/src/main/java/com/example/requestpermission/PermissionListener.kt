package com.example.requestpermission

interface PermissionListener {

    fun   isPermissionGranted(isGranted : Boolean)

    fun   shouldShowRationaleInfo()
}