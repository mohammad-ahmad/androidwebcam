package edu.colstate.cs.webcam;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.Jingle;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class WebcamService extends Service implements WebcamServiceListener {


	public class WebcamServiceBinder extends Binder {
    	WebcamService getService() {
            return WebcamService.this;
        }
    }	
	
	private WebcamServiceImpl mServiceImpl = null;
	
	private final IBinder mBinder = new WebcamServiceBinder();
	
	@Override
	public IBinder onBind(Intent intent) 
	{
		return mBinder;
	}

	@Override
	public void onCreate( ) 
	{
		super.onCreate();
		mServiceImpl = new WebcamServiceImpl(this);
	}
	
	@Override
	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent,startId);
	}

	@Override
	public void onDestroy( ) 
	{
		if (mServiceImpl.connection != null)
		{
			mServiceImpl.logout();
			mServiceImpl = null;
		}
		
		super.onDestroy();
	}
	
	
	// method to login
    public boolean login(String userName, String password, String serverHost)
    {
    	return mServiceImpl.login(userName, password, serverHost);
    }

	// method to get friends (and their status)
    ArrayList<Friend> getFriendList()
    {
    	return mServiceImpl.getFriendList();
    }
    
    // method to send text message to friend
    void sendTextMessage(Friend friend, String messageText)
    {
    	 mServiceImpl.sendTextMessage(friend, messageText);
     }
    
    // method to logout
    boolean logout()
    {
    	return mServiceImpl.logout();
    }

    // Notification methods
    public void notifyFriendListChanged()
    {
    	Log.i("edu.colstate.cs.webcam.WebcamService", "Friend List changed");
    	
        // send intent
        Intent intent = new Intent("edu.colstate.cs.webcam.FRIEND_LIST_CHANGED");
        sendBroadcast(intent);
    	
    }
        
    public void notifyMessageReceived(Message msg)
    {
    	String fromUser = msg.getFrom();
    	String message = msg.getBody();

    	Log.i("edu.colstate.cs.webcam.WebcamService", "Msg from = " + fromUser + ",Contents=" + message);
 	
        Intent intent = new Intent("edu.colstate.cs.webcam.MESSAGE_RECEIVED");
        
        intent.putExtra("edu.colstate.cs.webcam.fromuser", fromUser);
        intent.putExtra("edu.colstate.cs.webcam.message", message);
        sendBroadcast(intent);
    }
    
	
	// Audio call methods
	public void callFriend(Friend friend)
	{
		mServiceImpl.callFriend(friend);
	}
	
	public void receiveCallRequest(Jingle jingle)
	{
		// ::TODO:: send notification  (for now automatically accept the call, for test purposes)
		ArrayList<Friend> friendList = mServiceImpl.getFriendList();
		
		Friend friend = friendList.get(0);
	    for (Friend aFriend : friendList)
	    {
	       	if (aFriend.getUser().equals(jingle.getFrom()))
	       	{
	       		friend = aFriend;
	       	}
	    }
	       				
		// ::TODO:: remove test code
		acceptCall(friend);
	}
	
	public void acceptCall(Friend friend)
	{
		mServiceImpl.acceptCall(friend);
	}
	
	public void callEstablished(Jingle jingle)
	{
		// ::TODO:: send notification that call was established
		
		
		// ::TODO:: remove test code
//		String strTestData = "This is a test";
		
//		mServiceImpl.sendAudioData(strTestData.getBytes(), strTestData.getBytes().length);
	}
	
	public void rejectCall(Friend friend)
	{
		mServiceImpl.rejectCall(friend);
	}
	
	public void sendAudioData(byte audioData[], int bufLen)
	{
		mServiceImpl.sendAudioData(audioData, bufLen);
	}

	public void receiveAudioData(byte audioData[], int bufLen)
	{
		String strData = new String(audioData, bufLen);
		System.out.println(strData);
		
		// ::TODO:: send notification that audio data was received
	}
	
}
