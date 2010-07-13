package colstate.edu.UseContactApp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.util.Log;


public class UseContactApp extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent intent = new Intent(Intent.ACTION_PICK, Contacts.Phones.CONTENT_URI); 
        startActivityForResult(intent, 1); 

        
    }
    
    @Override 
    public void onActivityResult(int reqCode, int resultCode, Intent data) { 
      super.onActivityResult(reqCode, resultCode, data); 
     
      switch (reqCode) { 
        case (1) : 
          if (resultCode == Activity.RESULT_OK) { 
            Uri contactData = data.getData(); 
            Cursor c =  managedQuery(contactData, null, null, null, null); 
            if (c.moveToFirst()) { 
              String name = c.getString(c.getColumnIndexOrThrow(People.NAME)); 
              // TODO Whatever you want to do with the selected contact name. 
              String tag = "contacts program";
              Log.d(tag, name);
            } 
          } 
          break; 
      } 
    } 

}