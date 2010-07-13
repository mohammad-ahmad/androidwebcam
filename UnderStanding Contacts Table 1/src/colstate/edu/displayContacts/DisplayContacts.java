package colstate.edu.displayContacts;


import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

public class DisplayContacts extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView contactView = (TextView) findViewById(R.id.contactview);
        
        ContentResolver cr = getContentResolver();
        Cursor cur = managedQuery(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cur.moveToNext()) {

        String [] colNames = cur.getColumnNames();
        	contactView.append("Name: ");
        	
        	for(int x=0;x<colNames.length;x++){
        		contactView.append(colNames[x] + ' ');
        		try{
        			contactView.append(cur.getString(x) + '\n');
        		}catch(Exception e){
        			contactView.append("null\n");
        		}
        	}
        	contactView.append("\n");
        	}
        	}

    }
