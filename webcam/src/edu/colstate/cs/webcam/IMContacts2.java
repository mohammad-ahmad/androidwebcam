package edu.colstate.cs.webcam;
 
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.*;
import android.widget.*;
 
public class IMContacts2 extends ListActivity {
 
	private Cursor cur;
    public static final String TAG = "IMContacts2";
    
    protected void onCreate(Bundle savedInstanceState) {
    	
    	Log.d(TAG, "Activity State: onCreate() 1");
 	         
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.list);
 		

 		// Get cursor for IM contacts
 		String protocol = "Our Protocol";
 		cur = this.managedQuery(ContactsContract.Data.CONTENT_URI, 
 				new String[] {ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Im.DATA, ContactsContract.CommonDataKinds.Im.TYPE}, 
 				ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.Data.DATA6 + " = ?", new String[]{ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE, protocol}, null);
 		startManagingCursor(cur);
 		
 		// Desired columns to be bound
 		String[] columns = {ContactsContract.CommonDataKinds.Im.DATA, ContactsContract.CommonDataKinds.Im.TYPE};
 		int[] to = new int[] {R.id.name_entry};
 		SimpleCursorAdapter cAdapter = new SimpleCursorAdapter(this, R.layout.list_entry, cur, columns, to);	
 		setListAdapter(cAdapter);
 
 		// launch preferences dialog if required
 		SharedPreferences settings = getSharedPreferences(PreferencesActivity.PREFS_NAME, 0);
  		String strUserID = settings.getString("loginid", "");
 		String strPassword = settings.getString("password", "");
 		String strServerHost = settings.getString("serverhost", "");

 		if (strUserID.equals(""))
 		{
 			Intent intent = new Intent(this, PreferencesActivity.class);
 			startActivityForResult(intent, 1);
  		}
 		else
 		{
 	        WebcamApp.getApp(this).sendLoginRequest(strUserID, strPassword, strServerHost);
  		}
    }
    
    

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// If we are called from here, this means we returned from the Preferences Dialog
		
		// ::TODO:: check return value (for now login in user)
 		SharedPreferences settings = getSharedPreferences(PreferencesActivity.PREFS_NAME, 0);
  		String strUserID = settings.getString("loginid", "");
 		String strPassword = settings.getString("password", "");
 		String strServerHost = settings.getString("serverhost", "");
	    WebcamApp.getApp(this).sendLoginRequest(strUserID, strPassword, strServerHost);
	}



	public void onListButtonClick(View v) throws Exception {
    	// get the row the clicked button is in
    	LinearLayout vwParentRow = (LinearLayout)v.getParent();
    	Button btnChild = (Button)vwParentRow.getChildAt(0);
    	TextView tvChild = (TextView)vwParentRow.getChildAt(1);
    	btnChild.setText("Connecting..."); 	
    	Log.d(TAG, "onListButtonClick: click handled for contact " + tvChild.getText());
    	// email address is in tvChild.getText()
    	
    	Friend friend = new Friend(tvChild.getText().toString());
        WebcamApp.getApp(this).callFriend(friend);
        
        // ::TODO:: after call is connected, change to screen allowing user to disconnect/stop call,
        // or change the GUI button from Connecting... to Disconnected
        
    	
    	
    }
        
 }