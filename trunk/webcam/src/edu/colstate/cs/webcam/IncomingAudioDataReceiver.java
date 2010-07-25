package edu.colstate.cs.webcam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class IncomingAudioDataReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle extras = intent.getExtras();
		
		byte audioData[] = extras.getByteArray("audiodata");
		int bufLen = extras.getInt("audiodatalen");
				
		WebcamApp app = WebcamApp.getApp(context);
		app.receiveAudioData(audioData, bufLen);
		

	}

}
