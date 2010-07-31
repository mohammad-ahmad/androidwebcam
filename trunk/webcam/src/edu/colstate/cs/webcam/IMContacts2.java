package edu.colstate.cs.webcam;
 
import android.app.ListActivity;
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

 		// ::TODO:: display dialog box, if UserID/Password not stored in Properties
        WebcamApp.getApp(this).sendLoginRequest("kurtn", "coolit12", "22ndcenturysoftware.com");

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