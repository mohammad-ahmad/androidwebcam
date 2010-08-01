package edu.colstate.cs.webcam;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class PreferencesActivity extends Activity {

	public static final String PREFS_NAME = "WebcamPrefsFile";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.preferences);
        
        // setup handler for Ok button
        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   persistWidgetData();
	           setResult(RESULT_OK);
	           finish();
           }
        });
        // setup handler for the Cancel button
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
 	           setResult(RESULT_CANCELED);
 	           finish();
            }
         });
        
        populateWidgets();

            
        
	}

	 protected void populateWidgets(){
	    	// do work here to populate widgets
	    	// checkbox
		 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		 
		 String strUserID = settings.getString("loginid", "");
		 String strPassword = settings.getString("password", "");
		 String strServerHost = settings.getString("serverhost", "22ndcenturysoftware.com");

	     EditText userID = (EditText) findViewById(R.id.twitterUserID);
	     userID.setText(strUserID);
	     
	     EditText password = (EditText) findViewById(R.id.twitterPassword);
	     
	     password.setText(strPassword);

	     EditText serverhost = (EditText) findViewById(R.id.serverHost);
	     serverhost.setText(strServerHost);
	    }

	    protected void persistWidgetData(){
			 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			 SharedPreferences.Editor editor = settings.edit();
			 
		     EditText userID = (EditText) findViewById(R.id.twitterUserID);
			 editor.putString("loginid", userID.getText().toString());

		     EditText password = (EditText) findViewById(R.id.twitterPassword);
			 editor.putString("password", password.getText().toString());

		     EditText serverhost = (EditText) findViewById(R.id.serverHost);
		     editor.putString("serverhost", serverhost.getText().toString());
		     
		     
		     editor.commit();
		  	    }	
	
}
