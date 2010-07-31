package edu.colstate.cs.webcam.smacktest;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.Jingle;

import edu.colstate.cs.webcam.Friend;
import edu.colstate.cs.webcam.WebcamServiceImpl;
import edu.colstate.cs.webcam.WebcamServiceListener;



public class WebcamServiceTester implements WebcamServiceListener {

    private WebcamServiceImpl mServiceImpl = null;
    private FileOutputStream audioFileOutputStream = null;

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
    	// Create thread for playing audio
       	Runnable playback = new Runnable()
    	{
			public void run() {
		        File file = new File("/user/kurtn/audio1.pcm");
		        byte byteArray[] = new byte[1024];
		        FileInputStream fis = new FileInputStream(file);
		        BufferedInputStream bis = new BufferedInputStream(fis);

		    	int iNumBytesRead = bis.read(byteArray, 0, 1024);
		        while (iNumBytesRead > 0)
		        {
		        	// send read audio data
		            mServiceImpl.sendAudioData(byteArray, iNumBytesRead);

		            // read next bytes
		        	iNumBytesRead = bis.read(byteArray, 0, 1024);
		        }
			}
       	};

       	Thread t = new Thread(playback);
       	t.start();
      	
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
        
        // Setup output file (for recording audio)
        // ::TODO:: generate random number, and allow for configurable directory
        File outputAudioFile = new File("/home/kurtn/output-call.pcm");
        audioFileOutputStream = new FileOutputStream(outputAudioFile);
        
                        
        // ::TODO:: remove test code
        acceptCall(friend);
    }
    
    public void acceptCall(Friend friend)
    {
        mServiceImpl.acceptCall(friend);
    }
    
    
    public void receiveAudioData(byte audioData[], int bufLen)
    {
    	// write to audio file
    	if (audioFileOutputStream != null)
    	{
    		audioFileOutputStream.write(audioData);
    		audioFileOutputStream.flush();
      	}
    	
        String strData = new String(audioData);
        System.out.println(strData);
    }
    
}
§