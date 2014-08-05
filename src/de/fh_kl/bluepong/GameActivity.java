package de.fh_kl.bluepong;

import views.GameView;
import de.fh_kl.bluepong.drawables.Paddle;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.os.Build;

public class GameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);
        
//        GameView gv = (GameView) findViewById(R.id.TrainingView);
        
//        Canvas canvas = new Canvas();
//       
//        new Paddle(100, 20, 1).draw(canvas);
//        
//        sv.draw(canvas);
	}
	
}
