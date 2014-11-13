package org.uab.android.multimedia.mediaplayerservice;

import org.uab.android.multimedia.mediaplayerservice.AudioPlayerService.AudioPlayerServiceBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	static final String LOG_TAG = "MULTIMEDIA_MEDIA_PLAYER_SERVICE_MAIN_ACTIVITY";
	
	// Reference to the AudioPlayerService for playback control
	AudioPlayerService audioPlayerService;
	
	String		audioFilePath = "android.resource://org.uab.android.multimedia.mediaplayerservice/" + R.raw.chasing_eidolon__set_me_free;
	
	ImageButton		playButton;
	ImageButton		pauseButton;
	ImageButton		stopButton;
	
	TextView	songNameTv;
	TextView	mediaPlayerStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		songNameTv = (TextView) findViewById(R.id.songName);
		mediaPlayerStatus = (TextView) findViewById(R.id.mediaPlayerStatus);
		
		playButton = (ImageButton) findViewById(R.id.playButton);
		playButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mediaPlayerStatus.setText("Playing");
				songNameTv.setText(getResources().getResourceEntryName(R.raw.chasing_eidolon__set_me_free));
				audioPlayerService.play(Uri.parse(audioFilePath));
			}
		});
		
		pauseButton = (ImageButton) findViewById(R.id.pauseButton);
		pauseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mediaPlayerStatus.setText("Paused");
				audioPlayerService.pause();
			}
		});
		
		stopButton = (ImageButton) findViewById(R.id.stopButton);
		stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mediaPlayerStatus.setText("Stopped");
				songNameTv.setText("");
				audioPlayerService.stop();
			}
		});
		
		Intent audioPlayerIntent = new Intent(this, AudioPlayerService.class);
		bindService(
				audioPlayerIntent, 
				audioPlayerServiceConnection, 
				Context.BIND_AUTO_CREATE
				);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if ( !audioPlayerService.isPlaying() ) {
			
			unbindService(audioPlayerServiceConnection);
		}
		
	}
	
	ServiceConnection audioPlayerServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
			Log.i(LOG_TAG, "AudioPlayerService disconnected.");
			audioPlayerService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
			Log.i(LOG_TAG, "AudioPlayerService connected.");
			audioPlayerService = ((AudioPlayerServiceBinder) service).getServiceInstance();
		}
	};
}
