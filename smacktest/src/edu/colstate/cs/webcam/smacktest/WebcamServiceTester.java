package edu.colstate.cs.webcam.smacktest;



import java.util.ArrayList;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.Jingle;

import edu.colstate.cs.webcam.Friend;
import edu.colstate.cs.webcam.WebcamServiceImpl;
import edu.colstate.cs.webcam.WebcamServiceListener;



public class WebcamServiceTester implements WebcamServiceListener {

	private WebcamServiceImpl mServiceImpl = null;

	public static void main(String args[])
	{
		WebcamServiceTester clsTester = new WebcamServiceTester();
		clsTester.login("kurtn2", "coolit12", "22ndcenturysoftware.com");
		
		while (true)
		{
		
		try
		{
			Thread.sleep(60000);
		}
		catch (Exception ex)
		{
			
		}
		}
		
	}
	
    public WebcamServiceTester()
    {
    	mServiceImpl = new WebcamServiceImpl(this);
    }

	// method to login
    public boolean login(String userName, String password, String serverHost)
    {
    	return mServiceImpl.login(userName, password, serverHost);
    }

    
    public void notifyFriendListChanged()
    {
    	
    }
    public void notifyMessageReceived(Message msg)
    {
    	
    }
    
	public void callEstablished(Jingle jingle)
	{
		String strData = new String("Testing123");

		// ::TODO:: send audio file over
		mServiceImpl.sendAudioData(strData.getBytes(), strData.getBytes().length);
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
	
	
	public void receiveAudioData(byte audioData[], int bufLen)
	{
		String strData = new String(audioData);
		System.out.println(strData);
	}
	
}
