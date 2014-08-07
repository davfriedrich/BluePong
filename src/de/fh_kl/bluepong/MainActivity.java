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

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_main);
		
		Typeface team401 = Typeface.createFromAsset(getAssets(), "fonts/Team401.ttf");
        TextView gameTitle = (TextView) findViewById(R.id.gameTitle);
		Button onePlayerButton = (Button) findViewById(R.id.onePlayerButton);
		Button twoPlayerButton = (Button) findViewById(R.id.twoPlayerButton);
		Button tournamentButton = (Button) findViewById(R.id.tournamentButton);
		Button settingsButton = (Button) findViewById(R.id.settingsButton);


        Animation animation = new AlphaAnimation(1, 0.33f);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        gameTitle.setTypeface(team401);
		onePlayerButton.setTypeface(team401);
        onePlayerButton.startAnimation(animation);
		twoPlayerButton.setTypeface(team401);
        twoPlayerButton.startAnimation(animation);
		tournamentButton.setTypeface(team401);
        tournamentButton.startAnimation(animation);
		settingsButton.setTypeface(team401);
        settingsButton.startAnimation(animation);
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
