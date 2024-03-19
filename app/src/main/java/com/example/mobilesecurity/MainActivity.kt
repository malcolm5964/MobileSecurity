package com.example.mobilesecurity

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph
import androidx.navigation.compose.rememberNavController
import com.example.mobilesecurity.ui.theme.MobileSecurityTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState


class MainActivity : ComponentActivity() {
    private val REQUEST_READ_CONTACTS_PERMISSION = 1001
    private val REQUEST_WRITE_CONTACTS_PERMISSION = 1002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



//        // Check if permissions are granted, request if not
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS_PERMISSION)
//            Log.d("PERMISSION", "Read Contacts permission not granted")
//        }
//        else {
//            // Permission is granted
//            Log.d("PERMISSION", "Read Contacts permission granted")
//        }
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_CONTACTS), REQUEST_WRITE_CONTACTS_PERMISSION)
//            Log.d("PERMISSION", "Write Contacts permission not granted")
//        }
//        else {
//            // Permission is granted
//            Log.d("PERMISSION", "Write Contacts permission granted")
//        }

        setContent {
            MobileSecurityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestPermission(
                        onPermissionGranted = { Log.d("RequestPermission", "Granted") },
                        onPermissionDenied = { Log.d("RequestPermission", "Denied") },
                        onPermissionsRevoked = { Log.d("RequestPermission", "Revoked") }
                    )
                    //navController
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }

    /**
     * Composable function to request location permissions and handle different scenarios.
     *
     * @param onPermissionGranted Callback to be executed when all requested permissions are granted.
     * @param onPermissionDenied Callback to be executed when any requested permission is denied.
     * @param onPermissionsRevoked Callback to be executed when previously granted permissions are revoked.
     */
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestPermission(
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit,
        onPermissionsRevoked: () -> Unit
    ) {
        // Initialize the state for managing multiple location permissions.
        val permissionState = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
            )
        )

        // Use LaunchedEffect to handle permissions logic when the composition is launched.
        LaunchedEffect(key1 = permissionState) {
            // Check if all previously granted permissions are revoked.
            val allPermissionsRevoked =
                permissionState.permissions.size == permissionState.revokedPermissions.size

            // Filter permissions that need to be requested.
            val permissionsToRequest = permissionState.permissions.filter {
                !it.status.isGranted
            }

            // If there are permissions to request, launch the permission request.
            if (permissionsToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

            // Execute callbacks based on permission status.
            if (allPermissionsRevoked) {
                onPermissionsRevoked()
            } else {
                if (permissionState.allPermissionsGranted) {
                    onPermissionGranted()
                } else {
                    onPermissionDenied()
                }
            }
        }
    }
}