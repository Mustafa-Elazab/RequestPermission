package com.example.requestpermission

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


class HomeFragment : Fragment(), PermissionListener {

    private lateinit var permissionHelper: PermissionHelper

    private lateinit var single: Button
    private lateinit var multi: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        single = view.findViewById(R.id.button)
        multi = view.findViewById(R.id.mbutton)

        permissionHelper = PermissionHelper(context = this, this)
        single.setOnClickListener {
            permissionHelper.checkForPermissions(android.Manifest.permission.CAMERA)
        }
        multi.setOnClickListener {
            permissionHelper.checkForMultiplePermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }


    }

    override fun isPermissionGranted(isGranted: Boolean) {
        if (isGranted) {
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
           //do any thing want
        }
    }





    override fun shouldShowRationaleInfo() {
        val dialogBuilder = AlertDialog.Builder(requireContext())


        dialogBuilder.setMessage("Camera permission is Required")

            .setCancelable(false)

            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                permissionHelper.launchPermissionDialog(Manifest.permission.CAMERA)
            })

            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })


        val alert = dialogBuilder.create()

        alert.setTitle("AlertDialogExample")

        alert.show()
    }

}