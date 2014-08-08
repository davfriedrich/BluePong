package de.fh_kl.bluepong;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.game.GameEngine;
import de.fh_kl.bluepong.util.RelativeSizeProvider;

public class GameActivity extends Activity implements SurfaceHolder.Callback, Constants {
	
	SurfaceView sv;
	GameEngine gameEngine;
    SharedPreferences preferences;
	
	int gameMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

       setContentView(R.layout.activity_game);
       
       Intent intent = getIntent();
       gameMode = intent.getIntExtra(GAME_MODE, 0);

       sv = (SurfaceView) findViewById(R.id.TrainingView);

       sv.getHolder().addCallback(this);

       preferences = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		gameEngine = new GameEngine(this, sv, preferences, gameMode);

		sv.setOnTouchListener(gameEngine);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		gameEngine.stop();
		super.onBackPressed();
	}
	
	
	
}
