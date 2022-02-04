package com.wizeline.heroes.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.wizeline.heroes.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        checkAppIsBlocked()
    }

    private fun checkAppIsBlocked() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                val isAppBlocked: Boolean = remoteConfig[IS_APP_BLOCKED_KEY].asBoolean()
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.i(TAG, "Fetch and activate updated: $updated with value: $isAppBlocked")
                } else {
                    Log.e(TAG, "Fetch failed")
                }

                if (isAppBlocked) {
                    Toast.makeText(
                        this,
                        "The app is currently blocked, you cannot use it.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
    }

    companion object {
        private const val TAG = "MainActivity"

        // Remote Config keys
        private const val IS_APP_BLOCKED_KEY = "isAppBlocked"
    }
}