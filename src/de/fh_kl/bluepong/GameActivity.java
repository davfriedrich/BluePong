package de.fh_kl.bluepong;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import de.fh_kl.bluepong.game.GameEngine;
import de.fh_kl.bluepong.util.RelativeSizeProvider;

public class GameActivity extends Activity implements SurfaceHolder.Callback{
	
	SurfaceView sv;
	GameEngine gameEngine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

       setContentView(R.layout.activity_game);
        
        sv = (SurfaceView) findViewById(R.id.TrainingView);
        
        sv.getHolder().addCallback(this);
        
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		gameEngine = new GameEngine(sv, new RelativeSizeProvider(sv.getWidth(), sv.getHeight()));
		
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
		super.onBackPressed();
		gameEngine.stop();
	}
	
	
	
}
