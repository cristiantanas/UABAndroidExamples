package org.uab.android.multimedia.mediarecorder;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	ToggleButton	recordAudio;
	ToggleButton	playAudio;
	
	TextView		what;
	
	MediaPlayer		mediaPlayer;
	MediaRecorder	mediaRecorder;
	AudioManager	audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		recordAudio = (ToggleButton) findViewById(R.id.audioRecording);
		playAudio = (ToggleButton) findViewById(R.id.audioPlaying);
		
		what = (TextView) findViewById(R.id.doingWhat);
		
		// Set up record button
		recordAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				// Enable or disable the play button
				playAudio.setEnabled(!isChecked);
				
				// Start/stop recording
				onRecordPressed(isChecked);
			}

			
		});
		
		// Set up play button
		playAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				// Enable or disable the record button
				recordAudio.setEnabled(!isChecked);
				
				// Start/stop playing
				onPlayPressed(isChecked);
			}
		});
		
		// Get a reference to the AudioManager
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		
		// Request audio focus
		audioManager.requestAudioFocus(
				audioFocusChangeListener, 		// Audio focus listener
				AudioManager.STREAM_MUSIC, 		// Audio focus for music playback
				AudioManager.AUDIOFOCUS_GAIN	// Audio focus time period unknown
				);
	}
	
	@Override
	protected void onPause() {
		
		// Release recording and playback resources, if necessary
		stopPlaying();
		
		if ( mediaRecorder != null ) {
			mediaRecorder.release();
			mediaRecorder = null;
		}
		
		super.onPause();
	}
	
	/**
	 * Determines whether the MediaPlayer should start or stop playing.
	 * 
	 * @param shouldStartPlaying State of the play toggle button
	 */
	protected void onPlayPressed(boolean shouldStartPlaying) {
		
		if (shouldStartPlaying) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}
	
	/**
	 * Initializes the MediaPlayer for audio playback.
	 */
	private void startPlaying() {
		
		what.setText("Playing audio.");
		
		mediaPlayer = new MediaPlayer();
		
		try {
			// Set the audio file that should be reproduced
			mediaPlayer.setDataSource(getFilesDir() + "/audio.m4a");
			
			// Set a listener to perform some action when the audio is done playing
			mediaPlayer.setOnCompletionListener(audioCompletionListener);
			
			// Prepare the MediaPlayer resources. Note: This operation is SYNCHRONOUS
			mediaPlayer.prepare();
			
			// Start playing
			mediaPlayer.start();
		} catch (IOException e) {
			Log.e("MULTIMEDIA_MEDIA_RECORDER", "Couldn't prepare and start MediaPlayer");
		}
	}
	
	/**
	 * Stops the MediaPlayer from audio playback and releases its resources.
	 */
	private void stopPlaying() {
		
		if ( mediaPlayer != null ) {
			
			if ( mediaPlayer.isPlaying() ) {
				mediaPlayer.stop();
			}
			
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	/**
	 * Determines whether the MediaRecorder should start or stop recording.
	 * 
	 * @param shouldStartRecording State of the record toggle button
	 */
	private void onRecordPressed(boolean shouldStartRecording) {
		
		if (shouldStartRecording) {
			startRecording();
		} else {
			stopRecording();
		}
	}
	
	/**
	 * Initializes the MediaRecorder for audio recording.
	 */
	private void startRecording() {
		
		what.setText("Recording audio.");
		
		mediaRecorder = new MediaRecorder();
		
		// Set the MediaRecorder audio source
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		
		// Set the audio's output format
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		
		// Set the audio encoder to use to record the audio
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		// Set the file where the audio should be save on internal storage
		mediaRecorder.setOutputFile(getFilesDir() + "/audio.m4a");
		
		// Prepare the MediaRecorder resources
		try {
			mediaRecorder.prepare();
		} catch (IOException e) {
			Log.e("MULTIMEDIA_MEDIA_RECORDER", "Couldn't prepare and start MediaRecorder");
		}

		// Start recording
		mediaRecorder.start();
	}
	
	/**
	 * Stops the MediaRecorder from recording and releases its resources
	 */
	private void stopRecording() {
		
		if ( mediaRecorder != null ) {
			
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}
	
	OnAudioFocusChangeListener audioFocusChangeListener = new OnAudioFocusChangeListener() {
		
		@Override
		public void onAudioFocusChange(int focusChange) {
			
			// If the audio focus has been lost, abandon the audio focus for this 
			// Activity and stop the MediaPlayer if playing audio.
			if ( focusChange == AudioManager.AUDIOFOCUS_LOSS ) {
				
				audioManager.abandonAudioFocus(audioFocusChangeListener);
				if ( mediaPlayer != null && mediaPlayer.isPlaying() ) {
					
					stopPlaying();
				}
			}
		}
	};
	
	OnCompletionListener audioCompletionListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			
			// If the audio has finished playing set the start/stop button to 
			// its initial state
			playAudio.toggle();
			what.setText("");
		}
	};
}
