package edu.colstate.cs.webcam;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MediaRelaySocket implements Runnable {
	
	Socket socket = null;
	MediaAudioListener listener = null;
	InputStream is = null;
	
    public MediaRelaySocket(String strHostName, int iPort)
    {
    	try
    	{
    		// establish socket and set read timeout to 0
    		socket = new Socket(strHostName, iPort);
			socket.setSoTimeout(0);

    		is = socket.getInputStream();

    		// create thread to read data
    		Thread t = new Thread(this);
    		t.start();
    	}
    	catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    public void joinCall(String strFrom, String strTo)
    {
		try
		{
			OutputStream os = socket.getOutputStream();
			String strPeerData = "IDENTITYJOIN " + strFrom + " " + strTo + "\n";
			os.write(strPeerData.getBytes());
			os.flush();
			
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
    	
    }
    
    public void setIdentity(String strPeer)
    {
		try
		{
			OutputStream os = socket.getOutputStream();
			String strPeerData = "IDENTITY " + strPeer + "\n";
			os.write(strPeerData.getBytes());
			os.flush();
			
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
    	
    	
    }
    
    public void setPeer(String strPeer)
    {
		try
		{
			OutputStream os = socket.getOutputStream();
			String strPeerData = "PEER " + strPeer;
			os.write(strPeerData.getBytes());
			
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
    	
    }
    
    public void setAudioListener(MediaAudioListener listener)
    {
    	this.listener = listener;
    }
    
	public void sendAudioData(byte audioData[], int bufLen)
	{
		try
		{
			OutputStream os = socket.getOutputStream();
			os.write(audioData, 0, bufLen);
			
			// flush to force send
			os.flush();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			
			// ::TODO:: pass error on up chain
		}
	}

	@Override
	public void run() {
		
		byte byteArray[] = new byte[4097];
		int numBytesRead = 0;
		while (!socket.isClosed() && numBytesRead != -1)
		{
			try
			{
				numBytesRead = is.read(byteArray, 0, 4096);
				if (numBytesRead > 0)
				{
					byteArray[numBytesRead] = 0;
					
					listener.receiveAudioData(byteArray, numBytesRead);
				}
				
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
			
		}
		
	}

}
