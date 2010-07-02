package edu.colstate.cs.webcam;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // call method on App tier, that calls method on Service tier
        WebcamApp.getApp(this).sendLoginRequest("kurtn", "coolit12", "22ndcenturysoftware.com");
    }
}