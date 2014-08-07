package de.fh_kl.bluepong;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.util.TournamentPlayer;

public class TournamentOverviewActivity extends Activity implements Constants{
	
	int count;
	TournamentPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_tournament_overview);
		
		Typeface team401 = Typeface.createFromAsset(getAssets(), "fonts/Team401.ttf");
		
		Intent intent = getIntent();
		String tmp[] = intent.getStringArrayExtra(TOURNAMENT_PLAYER);
		
		count = tmp.length;
		player = new TournamentPlayer();
		
		Random r = new Random(count);
		for(int i = 0; i < count; i++){
			player.insert(tmp[i], r.nextInt(count*2));
		}
	}
}
