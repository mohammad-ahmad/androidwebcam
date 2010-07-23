package edu.colstate.imcontacts;
 
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
 
public class IMContacts extends ListActivity {
 
	private Cursor cur;
    public static final String TAG = "ContactsAPI";
	

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
 		int[] to = new int[] {R.id.name_entry, R.id.type_entry};
 		SimpleCursorAdapter cAdapter = new SimpleCursorAdapter(this, R.layout.list_entry, cur, columns, to);
 		
 		setListAdapter(cAdapter);
      	}
 	
 }