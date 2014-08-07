package de.fh_kl.bluepong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void onePlayer(View v){
		Intent intent = new Intent(this, OnePlayerActivity.class);
		startActivity(intent);
	}
	
	public void twoPlayer(View v){
		Intent intent = new Intent(this, TwoPlayerActivity.class);
		startActivity(intent);
	}

	public void tournamentMode(View v){	
		Intent intent = new Intent(this, TournamentActivity.class);
		startActivity(intent);
	}

    public void showSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
