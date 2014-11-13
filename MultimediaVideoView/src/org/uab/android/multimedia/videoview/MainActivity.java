package org.uab.android.multimedia.videoview;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends Activity {
	
	VideoView 			videoViewer;
	
	MediaController 	videoViewerController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get a reference to the VideoView
		videoViewer = (VideoView) findViewById(R.id.videoView1);
		
		// Add a Media controller to allow forward/reverse/pause/resume 
		videoViewerController = new MediaController(this, true);
		videoViewerController.setEnabled(false);
		
		videoViewer.setMediaController(videoViewerController);
		videoViewer.setVideoURI(Uri.parse(
				"android.resource://org.uab.android.multimedia.videoview/raw/moon"
				));
		
		// Add an OnPreparedListener to enable the MediaController once the video is ready
		videoViewer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				
				videoViewerController.setEnabled(true);
			}
		});
	}
	
	@Override
	protected void onPause() {
		
		// Clean up and release resources
		
		if ( videoViewer != null && videoViewer.isPlaying() ) {
			videoViewer.stopPlayback();
		}
		
		videoViewer = null;
		
		super.onPause();
	}
}
