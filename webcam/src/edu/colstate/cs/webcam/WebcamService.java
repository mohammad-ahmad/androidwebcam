package edu.colstate.cs.webcam;

import java.util.List;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

//import android.app.NotificationManager;
import android.app.Service;
//import android.content.Context;
import android.content.Intent;
//import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
//import android.util.Log;

public class WebcamService extends Service {

    public class WebcamServiceBinder extends Binder {
    	WebcamService getService() {
            return WebcamService.this;
        }
    }	
	
	private final IBinder mBinder = new WebcamServiceBinder();
	private XMPPConnection connection = null;
	
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return mBinder;
	}

	@Override
	public void onCreate( ) 
	{
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent,startId);
	}

	@Override
	public void onDestroy( ) 
	{
		if (connection != null)
		{
			logout();
		}
		
		super.onDestroy();
	}
	
	
	// method to login
    boolean login(String userName, String password, String serverHost)
    {
        try
        {
        	if (connection == null)
        	{
        		connection = new XMPPConnection(serverHost);
        	}
        	connection.connect();
        	connection.login(userName, password);
    
        	// cleanup 
        	Message message = new Message();
        	message.setTo("kurtn2@22ndcenturysoftware.com");
        	message.setSubject("Test");
        	message.setBody("Test message");
        	message.setType(Message.Type.headline);
        	connection.sendPacket(message);      
        }
        catch (XMPPException ex)
        {
        	ex.printStackTrace();
        }
    	
    	return false;
    }

	// method to get friends (and their status)
    List<Friend> getFriendList()
    {
    	return null;
    }
    
    // method to send text message to friend
    void sendTextMessage(Friend friend, String messageText)
    {
    	// ::TODO:: use Friend object 
    	
    	Message message = new Message();
    	message.setTo("kurtn2@22ndcenturysoftware.com");
    	message.setSubject("Test");
    	message.setBody(messageText);
    	message.setType(Message.Type.headline);
    	connection.sendPacket(message);      
    	
    }
    
    // method to logout
    boolean logout()
    {
    	boolean bReturnCode = true;
    	
    	try
    	{
	    	if (connection != null)
	    	{
	    		connection.disconnect();
	    		connection = null;
	    	}
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    		bReturnCode = false;
    	}
    	
    	return bReturnCode;
    }

    // Notification methods
    void notifyFriendJoin(Friend friend)
    {
    	
    }
    
    void notifyFriendLeave(Friend friend)
    {
    }
    
    void notifyMessageReceived(Friend friend, String message)
    {
    }
	
}
