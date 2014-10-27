package org.uab.android.persistentdata.sharedpreferences;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final int 		MAX_RANGE = 1000;
	private static final String		HIGH_SCORE_KEY = "HIGH_SCORE";
	
	TextView		highScoreTextView;
	TextView		currentScoreTextView;
	Button			playButton;
	Button			resetButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the high score TextView
		highScoreTextView = (TextView) findViewById(R.id.highScoreTv);
		
		//TODO Update the high score based on the saved high score
		
		// Get a reference to the current game score TextView
		currentScoreTextView = (TextView) findViewById(R.id.currentScoreTv);
		
		playButton = (Button) findViewById(R.id.playBtn);
		playButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Play a game where we get and display a random number
				Random randomGenerator = new Random();
				int value = randomGenerator.nextInt(MAX_RANGE);
				
				// Update the current game score
				currentScoreTextView.setText(String.valueOf(value));
				
				//TODO Check whether the current score is greater than the high score
				//		and update the high score accordingly
			}
		});
		
		resetButton = (Button) findViewById(R.id.resetBtn);
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Set current game and high score to 0
			}
		});
	}
}
