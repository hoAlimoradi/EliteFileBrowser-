package com.alimoradi.elitefilebrowser.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.alimoradi.elitefilebrowser.R
import com.alimoradi.elitefilebrowser.main.ApplicationGraph

class PermissionActivity :
    AppCompatActivity() {

    private val userAction = createUserAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_permission)
        findViewById<View>(R.id.activity_permission_allow).setOnClickListener {
            userAction.onPermissionAllowClicked()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_CODE) {
            return
        }
        if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            finish()
        } else {
            showSnackbar("This app needs this permission to work", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_OPEN_DIRECTORY && resultCode == Activity.RESULT_OK) {
            finish()
        } else {
            showSnackbar("This app needs this permission to work", com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
        }
    }

    private fun showSnackbar(@StringRes text: Int, duration: Int) {
        com.google.android.material.snackbar.Snackbar.make(window.decorView, text, duration).show()
    }

    private fun showSnackbar(text: String, duration: Int) {
        com.google.android.material.snackbar.Snackbar.make(window.decorView, text, duration).show()
    }

    private fun createScreen() = object : PermissionContract.Screen {

        override fun requestStoragePermission() {
            ActivityCompat.requestPermissions(
                this@PermissionActivity,
                PERMISSIONS,
                REQUEST_CODE)
        }

        override fun requestScopedStoragePermission() {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, REQUEST_CODE_OPEN_DIRECTORY)
        }
    }

    private fun createUserAction(): PermissionContract.UserAction {
        val screen = createScreen()
        val fileScopedStorageManager = ApplicationGraph.getFileScopedStorageManager()
        return PermissionPresenter(
            screen,
            fileScopedStorageManager
        )
    }

    companion object {

        private val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val REQUEST_CODE = 26
        private const val REQUEST_CODE_OPEN_DIRECTORY = 1

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, PermissionActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }
    }
}
