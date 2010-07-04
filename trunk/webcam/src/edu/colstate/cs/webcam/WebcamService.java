package edu.colstate.cs.webcam;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class WebcamService extends Service implements RosterListener, PacketListener {


	public class WebcamServiceBinder extends Binder {
    	WebcamService getService() {
            return WebcamService.this;
        }
    }	
	
	private final IBinder mBinder = new WebcamServiceBinder();
	private XMPPConnection connection = null;
	private ArrayList<Friend> friendList = null;
	private Roster roster = null;
	
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

        // If connected add listener and return true
        if (connection != null && connection.isConnected())
        {
           	// Register packet listener
            PacketFilter filter = new PacketTypeFilter(Message.class);
            connection.addPacketListener(this, filter);        	

    		// get current roster and create listener
        	roster = connection.getRoster();
           	roster.addRosterListener(this);
               
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
    		
        	Collection<RosterEntry> entries = roster.getEntries();
           	for (RosterEntry entry : entries)
        	{
        		Friend friend = new Friend(roster, entry);
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
    void notifyFriendListChanged()
    {
    	Log.i("edu.colstate.cs.webcam.WebcamService", "Friend List changed");
    	
        // send intent
        Intent intent = new Intent("edu.colstate.cs.webcam.FRIEND_LIST_CHANGED");
        sendBroadcast(intent);
    	
    }
        
    void notifyMessageReceived(Message msg)
    {
    	String fromUser = msg.getFrom();
    	String message = msg.getBody();

    	Log.i("edu.colstate.cs.webcam.WebcamService", "Msg from = " + fromUser + ",Contents=" + message);
 	
        Intent intent = new Intent("edu.colstate.cs.webcam.MESSAGE_RECEIVED");
        
        intent.putExtra("edu.colstate.cs.webcam.fromuser", fromUser);
        intent.putExtra("edu.colstate.cs.webcam.message", message);
        sendBroadcast(intent);
    }
    
	// Roster callback methods
    @Override
	public void entriesAdded(Collection<String> addresses) {
    	
    	// As there is no constructor in Smack library that takes a string
    	// and creates a RosterEntry object (we need to rebuild the entire list)
       	friendList.clear();
    	Collection<RosterEntry> entries = roster.getEntries();
       	for (RosterEntry entry : entries)
    	{
    		Friend friend = new Friend(roster, entry);
    		friendList.add(friend);    		
    	}
       	
       	notifyFriendListChanged();
	}

	@Override
	public void entriesDeleted(Collection<String> addresses) {

       	for (String entry : addresses)
    	{
       		for (Friend friend : friendList)
       		{
       			if (friend.getUser().equals(entry))
       			{
       				friendList.remove(friend);
       			}
       		}
    	}
       	
       	notifyFriendListChanged();
	}

	@Override
	public void entriesUpdated(Collection<String> addresses) {
       	notifyFriendListChanged();
		
	}

	@Override
	public void presenceChanged(Presence presence) {
       	notifyFriendListChanged();
	}

	// Packet Listener method
	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Message)
		{
			Message msg = (Message)packet;
			notifyMessageReceived(msg);
		}
	}

	
}
