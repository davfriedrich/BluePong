package de.fh_kl.bluepong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class OnePlayerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_player);
	}
	
	public void startGameActivity(View v) {
		Intent i = new Intent(this, GameActivity.class);
		
		startActivity(i);
	}
}
