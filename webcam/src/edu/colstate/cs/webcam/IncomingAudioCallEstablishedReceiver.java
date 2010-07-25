package edu.colstate.cs.webcam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class IncomingAudioCallEstablishedReceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {

		// notify application object that the call is established 
		WebcamApp app = WebcamApp.getApp(context);
		app.incomingCallEstablised();
		
	}

	
}
