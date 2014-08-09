package de.fh_kl.bluepong;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.util.TournamentPlayer;

public class TournamentOverviewActivity extends Activity implements Constants{
	
	int count;
	TournamentPlayer player;
	int playerCounter;
	
	String currentPlayer1, currentPlayer2;
	boolean aiMode;
	int lastWinnerIndex;
	
	Typeface team401;
	
	TextView playerTextView, nextRoundTextView;
	Button startGameButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_tournament_overview);
		
		playerTextView = (TextView) findViewById(R.id.tournamentOverviewNextPlayerTextView);
		nextRoundTextView = (TextView) findViewById(R.id.tournamentOververviewNextGameTextView);
		startGameButton = (Button) findViewById(R.id.tournamentOverviewStartGameButton);
		
		Intent callIntent = getIntent();
		String tmp[] = callIntent.getStringArrayExtra(TOURNAMENT_PLAYER);
		
		count = tmp.length;
		player = new TournamentPlayer();
		
		Random r = new Random(count);
		for(int i = 0; i < count; i++){
			player.insert(tmp[i], r.nextInt(count*2));
		}
		
		playerCounter = 0;
		
		getPlayer();
		
	}
	
	public void getPlayer(){
		String[] tmpPlayer = player.getNext();
		currentPlayer1 = tmpPlayer[0];
		currentPlayer2 = tmpPlayer[1];
		if(currentPlayer2 == null){
			currentPlayer2 = "AI";
			aiMode = true;
			playerTextView.setText(currentPlayer1 + " vs AI");
		}else{
			aiMode = false;
			playerTextView.setText(currentPlayer1 + " vs " + currentPlayer2);
		}
		playerCounter += 2;
	}
	
	public void startGame(View v){
		Intent gameIntent = new Intent(this,GameActivity.class);
		if(aiMode){
			gameIntent.putExtra(GAME_MODE, TOURNAMENT_MODE_AI);
		}else{
			gameIntent.putExtra(GAME_MODE, TOURNAMENT_MODE);
		}
		gameIntent.putExtra(PLAYER_NAMES, new String[] {currentPlayer1, currentPlayer2});
		startActivityForResult(gameIntent, 0);
	}
	
	public void prepareNextRound(){
		player.setWinner(lastWinnerIndex);
		if(playerCounter >= count){
			count = count - player.clean();
			playerCounter = 0;
		}
		if(count == 1){
			startGameButton.setVisibility(View.INVISIBLE);
			nextRoundTextView.setText("Der Gewinner ist:");
			playerTextView.setText(player.getWinner() + "!!!");
		}
		if(count == 0){
			startGameButton.setVisibility(View.INVISIBLE);
			nextRoundTextView.setText("Der Gewinner ist:");
			playerTextView.setText("Die AI!!!");
		}
		if(count > 1){
			getPlayer();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		lastWinnerIndex = data.getIntExtra(WINNER, -1);
		if(lastWinnerIndex == -1){
			playerTextView.setText("Das letzte Spiel muss wegen eines Fehlers wiederholt werden.");
		}else{
			prepareNextRound();
		}
	}
}
