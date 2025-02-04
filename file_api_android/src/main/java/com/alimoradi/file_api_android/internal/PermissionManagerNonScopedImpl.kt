package com.alimoradi.file_api_android.internal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.alimoradi.file_api_android.PermissionManager
import com.alimoradi.file_api_android.PermissionRequestAddOn

internal class PermissionManagerNonScopedImpl(
    private val context: Context,
    private val permissionRequestAddOn: PermissionRequestAddOn
) : PermissionManager {

    override fun hasStoragePermission(): Boolean {
        return checkStoragePermission(context)
    }

    override fun requestStoragePermissionIfRequired(): Boolean {
        val hasPermission = hasStoragePermission()
        if (!hasPermission) {
            permissionRequestAddOn.requestStoragePermission()
            return true
        }
        return false
    }

    companion object {

        /**
         * Check a permission.
         *
         * @param context The current [Context].
         * @return True if all the permissions are [PackageManager.PERMISSION_GRANTED].
         */
        @JvmStatic
        fun checkStoragePermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val checkSelfPermission = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
            return true
        }
    }
}
