package de.fh_kl.bluepong;

import de.fh_kl.bluepong.constants.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TwoPlayerActivity extends Activity implements Constants {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_player);
	}
	
	public void startMultitouchGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GAME_MODE, MULTITOUCH_MODE);
		
		startActivity(intent);
	}
}
