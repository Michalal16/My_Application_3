package com.example.myapplication
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.R.layout.main_activity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(main_activity)
        val btn = findViewById<Button>(R.id.button2)
        btn.setOnClickListener{
            val url = URL("https://jsonplaceholder.typicode.com/posts")
            GlobalScope.launch {
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "GET" // optional default is GET
                    inputStream.bufferedReader().use {
                        it.lines().forEach { line -> Log.d("ACT", line)
                        }
                    }
                }
            }


            /*val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo;
            Log.d(TAG, "Is connected: ${networkInfo?.isConnected}")
            Log.d(TAG, "Type: ${networkInfo?.type} ${ConnectivityManager.TYPE_WIFI}")
*/

            val btn_contact = findViewById<Button>(R.id.button)
            btn_contact.setOnClickListener {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    READ_CONTACTS_PERMISSION_REQUEST_CODE
                )
                // this.readContacts()
            }

            registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    override fun onStop() {
        unregisterReceiver(ConnectivityReceiver())
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.readContacts()
            } else {
                val toast = Toast.makeText(this /* MyActivity */, "The app doesn't have access to contacts!", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }

    private fun readContacts() {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null && cursor.count > 0) {
            val curId = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val curName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val contactId = cursor.getString(curId)
                val displayName = cursor.getString(curName)
                Log.d(TAG, "Contact ${contactId} ${displayName}")
            }
        }
        cursor?.close()
    }

    companion object {
        const val READ_CONTACTS_PERMISSION_REQUEST_CODE = 2137
    }

}

