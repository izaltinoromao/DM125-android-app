package com.izaltino.mytasks.fragment

import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.izaltino.mytasks.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class PreferenceFragment : PreferenceFragmentCompat() {

    companion object {
        const val DAILY_NOTIFICATION_KEY = "daily_notification"
        const val DATE_FORMAT_KEY = "date_format"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<SwitchPreferenceCompat>(DAILY_NOTIFICATION_KEY)?.onPreferenceChangeListener =
            OnPreferenceChangeListener { _, newValue ->
                if (newValue.toString().toBoolean()) {
                    Firebase.messaging.subscribeToTopic(DAILY_NOTIFICATION_KEY).addOnCompleteListener {
                        Log.e("fcm", "Tópico registrado.")
                    }
                } else {
                    Firebase.messaging.unsubscribeFromTopic(DAILY_NOTIFICATION_KEY).addOnCompleteListener {
                        Log.e("fcm", "Tópico desregistrado.")
                    }
                }

                true
            }

        findPreference<ListPreference>(DATE_FORMAT_KEY)?.setOnPreferenceChangeListener { _, newValue ->
            Log.d("Settings", "Date format changed to $newValue")
            true
        }
    }
}