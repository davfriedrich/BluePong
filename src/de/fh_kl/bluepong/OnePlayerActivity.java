package de.fh_kl.bluepong;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import de.fh_kl.bluepong.constants.Constants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class OnePlayerActivity extends Activity implements Constants {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_one_player);
		

		Button trainingButton = (Button) findViewById(R.id.trainingButton);
		Button aiButton = (Button) findViewById(R.id.aiButton);
		Button highscoreButton = (Button) findViewById(R.id.highscoreButton);

        Animation animation = new AlphaAnimation(1, 0.33f);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        trainingButton.startAnimation(animation);
        aiButton.startAnimation(animation);
        highscoreButton.startAnimation(animation);
		
	}
	
	public void startTraining(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GAME_MODE, TRAINING_MODE);
		
		startActivity(intent);
	}

    public void startAiMatch(View v) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GAME_MODE, SINGLE_MODE);

		startActivity(intent);
	}
}
