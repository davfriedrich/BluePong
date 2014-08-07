package de.fh_kl.bluepong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
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

        TextView gameTitle = (TextView) findViewById(R.id.gameTitle);
        TextView twoPlayerTitle = (TextView) findViewById(R.id.twoPlayerTitle);
		Button multitouchButton = (Button) findViewById(R.id.multitouchButton);
		Button bluetoothButton =(Button) findViewById(R.id.bluetoothButton);

        Animation animation = new AlphaAnimation(1, 0.33f);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        gameTitle.setTypeface(team401);
        twoPlayerTitle.setTypeface(team401);
		multitouchButton.setTypeface(team401);
        multitouchButton.startAnimation(animation);
		bluetoothButton.setTypeface(team401);
        bluetoothButton.startAnimation(animation);
	}
	
	public void startMultitouchGame(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GAME_MODE, MULTITOUCH_MODE);
		
		startActivity(intent);
	}
}
