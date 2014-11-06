package org.uab.android.threading.servicenothread;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class DownloadService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		try {
			URL url = new URL(
					"http://www.gencat.cat/opendata/recursos/equipaments/equipaments.rdf"
					);
			InputStream inputStream = url.openConnection().getInputStream();

			FileOutputStream fileOutputStream = 
					openFileOutput("resultat.xml", Context.MODE_PRIVATE);
			byte[] buffer = new byte[1024];
			while ( (inputStream.read(buffer) > -1) )
			{
				fileOutputStream.write(buffer);
			}

			fileOutputStream.close();
			inputStream.close();
			
			Toast.makeText(getBaseContext(), "Download complete!", Toast.LENGTH_SHORT)
				.show();
		}
		catch (Exception e) { 
			e.printStackTrace(); 
		}
		
		return START_NOT_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
