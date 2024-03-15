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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph
import androidx.navigation.compose.rememberNavController
import com.example.mobilesecurity.ui.theme.MobileSecurityTheme


class MainActivity : ComponentActivity() {
    private val REQUEST_READ_CONTACTS_PERMISSION = 1001
    private val REQUEST_WRITE_CONTACTS_PERMISSION = 1002
    private val REQUEST_SEND_SMS_PERMISSION = 1003
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if permissions are granted, request if not
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS_PERMISSION)
            Log.d("PERMISSION", "Read Contacts permission not granted")
        }
        else {
            // Permission is granted
            Log.d("PERMISSION", "Read Contacts permission granted")
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_CONTACTS), REQUEST_WRITE_CONTACTS_PERMISSION)
            Log.d("PERMISSION", "Write Contacts permission not granted")
        }
        else {
            // Permission is granted
            Log.d("PERMISSION", "Write Contacts permission granted")
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), REQUEST_SEND_SMS_PERMISSION)
            Log.d("PERMISSION", "Send SMS permission not granted")
        }
        else {
            // Permission is granted
            Log.d("PERMISSION", "Send SMS permission granted")
        }

        setContent {
            MobileSecurityTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //navController
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}