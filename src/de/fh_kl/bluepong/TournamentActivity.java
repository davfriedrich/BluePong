package de.fh_kl.bluepong;

import de.fh_kl.bluepong.constants.Constants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TournamentActivity extends Activity implements Constants{
	
	TextView textView;
	EditText textField;
	Button button;
	int count, counter;
	String player[];
	int state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_tournament);
		
		Typeface team401 = Typeface.createFromAsset(getAssets(), "fonts/Team401.ttf");
		
		textView = (TextView) findViewById(R.id.tournamentViewTextView);
		textField = (EditText) findViewById(R.id.tounamentViewTextField);
		button= (Button) findViewById(R.id.tournamentViewButton);
		
		button.setTypeface(team401);
		textView.setTypeface(team401);
		textField.setTypeface(team401);
		
		button.setText("OK");		
		textView.setText("Geben Sie eine Spieleranzahl ein:");
		textField.setInputType(InputType.TYPE_CLASS_PHONE);
		
		state = 0;		
	}
	
	public void click(View v){
		switch(state){
			case 0:
				try{
					count = Integer.parseInt(textField.getText().toString());
				}catch(NumberFormatException e){
					count = 0;
				}
				if(count < 2){
					textView.setText("Mindesten 2 Spieler notwendig");
					textField.setText("");
				}else{
					textView.setText("Geben sie den Namen des 1. Spielers ein");
					textField.setText("");
					textField.setInputType(InputType.TYPE_CLASS_TEXT);
					state = 1;
					counter = 0;
					player = new String[count];
				}
				break;
			case 1:
				String tmpName = textField.getText().toString();
				if(!tmpName.equals("")){
					player[counter] = tmpName;
					counter++;
					textView.setText("Geben sie den Namen des " + (counter + 1) + ". Spielers ein");
					textField.setText("");
					if(count == counter){
						state = 2;
						textView.setText("");
						textField.setVisibility(View.GONE);
						button.setText("Spiel beginnen");
					}
				}
				break;
			case 2:
				Intent intent = new Intent(this,TournamentOverviewActivity.class);
				intent.putExtra(TOURNAMENT_PLAYER, player);
				startActivity(intent);
				break;
		}
		
	}
}
