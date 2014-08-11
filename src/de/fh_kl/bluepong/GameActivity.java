package de.fh_kl.bluepong;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.game.GameEngine;
import de.fh_kl.bluepong.util.BluetoothService;

/**
 * Activity the game is running in
 */
public class GameActivity extends Activity implements SurfaceHolder.Callback, Constants {
	
	SurfaceView sv;
	GameEngine gameEngine;
    SharedPreferences preferences;

    BluetoothService bluetoothService;
    	
	int gameMode;
	
	String playerNames[];	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

       setContentView(R.layout.activity_game);
       
       Intent intent = getIntent();
       gameMode = intent.getIntExtra(GAME_MODE, 0);
       
       if(gameMode == TOURNAMENT_MODE || gameMode == TOURNAMENT_MODE_AI){
    	   playerNames = intent.getStringArrayExtra(PLAYER_NAMES);
       }

       sv = (SurfaceView) findViewById(R.id.TrainingView);

       sv.getHolder().addCallback(this);

       preferences = PreferenceManager.getDefaultSharedPreferences(this);

	}

    @Override
    protected void onStart() {
        super.onStart();

        if (gameMode == BLUETOOTH_MODE) {
            bluetoothService = BluetoothService.getInstance();

            bluetoothService.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (gameMode == BLUETOOTH_MODE) {
            bluetoothService.stop();
        }
    }

    // wait until view is created to start game engine
    @Override
	public void surfaceCreated(SurfaceHolder holder) {

        if (gameMode == BLUETOOTH_MODE) {
            double displayRatio[] = bluetoothService.syncDisplaySize(sv.getWidth(), sv.getHeight());

            bluetoothService.syncPreferences(preferences);

            gameEngine = new GameEngine(this, sv, preferences, gameMode, displayRatio);
        } else if (gameMode >= TOURNAMENT_MODE) {
            gameEngine = new GameEngine(this, sv, preferences, gameMode, playerNames);
        } else {
            gameEngine = new GameEngine(this, sv, preferences, gameMode);
        }
		sv.setOnTouchListener(gameEngine);
	}

    /**
     * callback for tournament mode
     * @param winner winner of this game
     */
	public void endRound(int winner){
		Intent endIntent = new Intent();
		endIntent.putExtra(WINNER, winner);
		setResult(RESULT_OK, endIntent);
		finish();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@Override
	public void onBackPressed() {
        if(gameMode >= TOURNAMENT_MODE) {
            endRound(gameEngine.checkForWinner());
            gameEngine.stop();

        } else {
            gameEngine.stop();

            if (gameMode == BLUETOOTH_MODE) {
                setResult(RESULT_OK);
                finish();
            }
        }
		super.onBackPressed();
	}

    @Override
    protected void onPause() {
        if (gameEngine != null) {
            gameEngine.pause();
        }
        super.onPause();
    }
}
