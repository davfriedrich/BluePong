package de.fh_kl.bluepong;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * User: #empty
 * Date: 07.08.14
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}