package org.uab.android.multimedia.mediaplayerservice;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AudioPlayerService extends Service {
	
	static final String LOG_TAG = "MULTIMEDIA_MEDIA_PLAYER_SERVICE_AUDIO_PLAYER_SERVICE";

	// MediaPlayer object to reproduce an audio file 
	MediaPlayer		mediaPlayer;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		// Create and return an instance of AudioPlayerServiceBinder
		
		AudioPlayerServiceBinder serviceBinder = new AudioPlayerServiceBinder();
		return serviceBinder;
	}
	
	@Override
	public void onDestroy() {
		
		// Release any resources associated with the MediaPlayer
		stop();
		super.onDestroy();
	}
	
	public void play(Uri audioFile) {
		
		if ( mediaPlayer != null ) {
			
			// If the MediaPlayer has already been initialized and been paused 
			// try to resume the audio playback
			try {
				
				mediaPlayer.start();
				
			} catch (IllegalStateException e) {
				Log.e(LOG_TAG, "MediaPlayer has receive start in an invalid state.");
			}
		}
		else {
		
			mediaPlayer = new MediaPlayer();

			try {

				// Set the audio file that should be reproduced
				mediaPlayer.setDataSource(getBaseContext(), audioFile);

				// Prepare the MediaPlayer resources. Note: This operation is SYNCHRONOUS
				mediaPlayer.prepare();
				
				// Start playing
				mediaPlayer.start();

			} catch (IOException e) {
				Log.e(LOG_TAG, "Couldn't prepare and start MediaPlayer");
			}
		}
	}
	
	public void pause() {
		
		// If the MediaPlayer is playing audio, pause it
		if ( mediaPlayer != null && mediaPlayer.isPlaying() ) {
			mediaPlayer.pause();
		}
	}
	
	public void stop() {
		
		// If the MediaPlayer is playing audio, stop it and then release its resources
		if ( mediaPlayer != null ) {
			
			if ( mediaPlayer.isPlaying() ) {
				mediaPlayer.stop();
			}
			
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	public boolean isPlaying() {
		
		if ( mediaPlayer != null && mediaPlayer.isPlaying() ) {
			
			return true;
		}
		
		return false;
	}

	
	public class AudioPlayerServiceBinder extends Binder {
		
		// Return a reference to the AudioPlayerService class
		public AudioPlayerService getServiceInstance() {
			return AudioPlayerService.this;
		}
	}
}
