package edu.colstate.cs.webcam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

//import android.app.NotificationManager;
import android.app.Service;
//import android.content.Context;
import android.content.Intent;
//import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
//import android.util.Log;

public class WebcamService extends Service implements RosterListener {


	public class WebcamServiceBinder extends Binder {
    	WebcamService getService() {
            return WebcamService.this;
        }
    }	
	
	private final IBinder mBinder = new WebcamServiceBinder();
	private XMPPConnection connection = null;
	private ArrayList<Friend> friendList = null;
	
	
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
        }
        catch (XMPPException ex)
        {
        	ex.printStackTrace();
        }

        // return true if connected
        if (connection != null && connection.isConnected())
        {
        	return true;
        }
   
        // assume failure if we reach here
        connection = null;
       	return false;
    }

	// method to get friends (and their status)
    ArrayList<Friend> getFriendList()
    {
    	if (friendList == null)
    	{
    		// create friend list
    		friendList = new ArrayList<Friend>();
    		
    		// get current roster and create listener
        	Roster roster = connection.getRoster();
           	roster.addRosterListener(this);
   
           	// add current logged in friends
        	Collection<RosterEntry> entries = roster.getEntries();
           	for (RosterEntry entry : entries)
        	{
        		Friend friend = new Friend(entry);
        		friendList.add(friend);    		
        	}
    	}
    	
    	return friendList;
    }
    
    // method to send text message to friend
    void sendTextMessage(Friend friend, String messageText)
    {
    	Message message = new Message();
    	message.setTo(friend.getUser());
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
    
	// Roster callback methods
    @Override
	public void entriesAdded(Collection<String> addresses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entriesDeleted(Collection<String> addresses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entriesUpdated(Collection<String> addresses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void presenceChanged(Presence presence) {
		// TODO Auto-generated method stub
		
	}

	
}
