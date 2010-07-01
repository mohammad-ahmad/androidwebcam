package edu.colstate.cs.webcam.smacktest;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.os.Bundle;

public class SmackTestActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try
        {
        	XMPPConnection connection = new XMPPConnection("22ndcenturysoftware.com");
        	connection.connect();
        	connection.login("kurtn", "coolit12");
    
        	Message message = new Message();
        	message.setTo("kurtn2@22ndcenturysoftware.com");
        	message.setSubject("Test");
        	message.setBody("Test message");
        	message.setType(Message.Type.headline);
        	connection.sendPacket(message);      
        	connection.disconnect();
        }
        catch (XMPPException ex)
        {
        	ex.printStackTrace();
        }
        
    }
}