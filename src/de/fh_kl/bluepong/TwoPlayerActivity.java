package de.fh_kl.bluepong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import de.fh_kl.bluepong.constants.Constants;

public class TwoPlayerActivity extends Activity implements Constants {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_two_player);
		
		Typeface team401 = Typeface.createFromAsset(getAssets(), "fonts/Team401.ttf");
		
		Button multitouchButton = (Button) findViewById(R.id.multitouchButton);
		Button bluetoothButton =(Button) findViewById(R.id.bluetoothButton);
		
		multitouchButton.setTypeface(team401);
		bluetoothButton.setTypeface(team401);
	}
	
	public void startMultitouchGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GAME_MODE, MULTITOUCH_MODE);
		
		startActivity(intent);
	}
}
