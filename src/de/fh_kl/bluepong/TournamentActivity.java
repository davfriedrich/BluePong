package de.fh_kl.bluepong;

import java.util.Random;

import android.text.Html;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import de.fh_kl.bluepong.constants.Constants;
import de.fh_kl.bluepong.util.TournamentPlayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TournamentActivity extends Activity implements Constants{
	
	TextView textView;
	EditText textField;
	Button button;
    Animation animation;
	int number, counter;
	String playerStringArray[];
	int state;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_tournament);
		
		textView = (TextView) findViewById(R.id.tournamentViewTextView);
		textField = (EditText) findViewById(R.id.tounamentViewTextField);
		button= (Button) findViewById(R.id.tournamentViewButton);

        animation = new AlphaAnimation(1, 0.33f);
        animation.setDuration(500);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

		button.setText(R.string.tournamentActivityButtonOkString);
		textView.setText(R.string.tournamentActivityPlayerNumberSetText);
		textField.setInputType(InputType.TYPE_CLASS_PHONE);
		
		state = 0;		
	}
	
	public void click(View v){
		switch(state){
			case 0:
				try{
					number = Integer.parseInt(textField.getText().toString());
				}catch(NumberFormatException e){
					number = 0;
				}
				if(number < 2){
					textView.setText(R.string.tournamentActivityPlayerNumberSetErrorText);
					textField.setText("");
				}else{
					String textViewString = getString(R.string.tournamentActivitySetPlayerNameText) + " 1";
					textView.setText(textViewString);
					textField.setText("");
					textField.setInputType(InputType.TYPE_CLASS_TEXT);
					state = 1;
					counter = 0;
					playerStringArray = new String[number];
				}
				break;
			case 1:
				String tmpName = textField.getText().toString();
				if(!tmpName.equals("")){
					playerStringArray[counter] = tmpName;
					counter++;
					String textViewString = getString(R.string.tournamentActivitySetPlayerNameText) + " " + (counter + 1);
					textView.setText(textViewString);
					textField.setText("");
					if(number == counter){
						state = 2;
						textView.setText("");
						textField.setVisibility(View.GONE);
						
						InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		                in.hideSoftInputFromWindow(textField.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		                
						button.setText(R.string.tournamentActivityStartTournamentButtonString);
                        button.startAnimation(animation);
					}
				}
				break;
			case 2:
				startTournamentOverview();
                button.clearAnimation();
				break;
		}
		
	}
	
	
	
	
	int count;
	TournamentPlayer player;
	int playerCounter;
	
	String currentPlayer1, currentPlayer2;
	boolean aiMode;
	int lastWinnerIndex;
	
	TextView playerTextView, nextRoundTextView;
	Button startGameButton;
	
	private void startTournamentOverview() {
		setContentView(R.layout.activity_tournament_overview);
		
		playerTextView = (TextView) findViewById(R.id.tournamentOverviewNextPlayerTextView);
		nextRoundTextView = (TextView) findViewById(R.id.tournamentOververviewNextGameTextView);
		startGameButton = (Button) findViewById(R.id.tournamentOverviewStartGameButton);
        startGameButton.startAnimation(animation);
		
		count = playerStringArray.length;
		player = new TournamentPlayer();
		
		Random r = new Random(System.currentTimeMillis());
		for(int i = 0; i < count; i++){
			player.insert(playerStringArray[i], r.nextInt(count*2));
		}
		
		playerCounter = 0;
		
		getPlayer();
	}
	
	public void getPlayer(){
		String[] tmpPlayer = player.getNext();
		currentPlayer1 = tmpPlayer[0];
		currentPlayer2 = tmpPlayer[1];
        String vs;
		if(currentPlayer2 == null){
			currentPlayer2 = "AI";
			aiMode = true;
            vs = currentPlayer1 + "<br><font color='red'> " + getString(R.string.tournamentOverviewVersus) + " </font><br>" + getString(R.string.aritifialIntelligence);
        }else{
            aiMode = false;
//            String vs = currentPlayer1 + "<font color='" + getResources().getColor(R.color.bluepongBlue) + "'> vs </font>" + currentPlayer2;
            vs = currentPlayer1 + "<br><font color='red'> " + getString(R.string.tournamentOverviewVersus) + " </font><br>" + currentPlayer2;
		}
        playerTextView.setText(Html.fromHtml(vs), TextView.BufferType.SPANNABLE);
		playerCounter += 2;
	}
	
	public void prepareNextRound(){
		player.markAsLoser(lastWinnerIndex);
		if(playerCounter >= count){
			count = count - player.clean();
			playerCounter = 0;
		}
		if(count == 1){
			startGameButton.setVisibility(View.INVISIBLE);
			nextRoundTextView.setText(R.string.tournamentOverviewWinnerText);
			playerTextView.setText(player.getWinner());
		}

		if(count == 0){
			startGameButton.setVisibility(View.INVISIBLE);
			nextRoundTextView.setText(R.string.tournamentOverviewWinnerText);
			playerTextView.setText(getString(R.string.aritifialIntelligence));
		}
		if(count > 1){
			getPlayer();
		}
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
        startGameButton.clearAnimation();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		lastWinnerIndex = data.getIntExtra(WINNER, -1);
		if(lastWinnerIndex == -1){
			playerTextView.setText(R.string.tournamentOverviewErrorGameEndText);
		}else{
			prepareNextRound();
		}
	}	
}
