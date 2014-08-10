package de.fh_kl.bluepong;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        addPreferencesFromResource(R.xml.preferences);
    }
}