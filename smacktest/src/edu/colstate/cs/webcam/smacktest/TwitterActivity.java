package edu.colstate.cs.webcam.smacktest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TwitterActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	protected int currentSpinnerSelection = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.twitter);
        
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
        
        final Spinner spinner = (Spinner) findViewById( R.id.twitterSpinner) ;
        ArrayAdapter<?> adapter = ArrayAdapter. createFromResource(this,
        	    R. array. uploadfrequency, android. R. layout. simple_spinner_item) ;
        	adapter. setDropDownViewResource(android. R. layout.simple_spinner_dropdown_item) ;
        
        spinner.setAdapter(adapter)	;
        populateWidgets();

        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                int arg2, long arg3) {
            	currentSpinnerSelection = arg2;
            }
            
            public void onNothingSelected(AdapterView<?> a)
            {
            	
            }
        });
            
        
	}

	 protected void populateWidgets(){
	    	// do work here to populate widgets
	    	// checkbox
		 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		 
		 String strUserID = settings.getString("twitterloginid", "");
		 String strPassword = settings.getString("twitterpassword", "");

		 int frequency = settings.getInt("twitterfrequency", 0);
		 currentSpinnerSelection = frequency;
	     Spinner spinner = (Spinner) findViewById( R.id.twitterSpinner) ;
	     spinner.setSelection(frequency);
	    
	     
		 
	     EditText userID = (EditText) findViewById(R.id.twitterUserID);
	     userID.setText(strUserID);
	     
	     EditText password = (EditText) findViewById(R.id.twitterPassword);
	     
	     password.setText(strPassword);
	    }

	    protected void persistWidgetData(){
			 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			 SharedPreferences.Editor editor = settings.edit();
			 
		     EditText userID = (EditText) findViewById(R.id.twitterUserID);
			 editor.putString("twitterloginid", userID.getText().toString());

		     EditText password = (EditText) findViewById(R.id.twitterPassword);
			 editor.putString("twitterpassword", password.getText().toString());

		     Spinner spinner = (Spinner) findViewById( R.id.twitterSpinner) ;
		     editor.putInt("twitterfrequency", currentSpinnerSelection);

		     
		     
		     editor.commit();
		  	    }	
	
}
