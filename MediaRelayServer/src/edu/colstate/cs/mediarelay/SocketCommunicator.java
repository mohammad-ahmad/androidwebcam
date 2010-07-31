package edu.colstate.cs.mediarelay;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SocketCommunicator implements Runnable {

	Socket fromSocket;
	Socket toSocket;
	
	public  SocketCommunicator(Socket fromSocket, Socket toSocket)
	{
		this.fromSocket = fromSocket;
		this.toSocket = toSocket;
	}
	
	@Override
	public void run() {
		
		
		try
		{
			InputStream dis = fromSocket.getInputStream();
			OutputStream dos = toSocket.getOutputStream();
			byte byteArray[] = new byte[4097];
		
			while (fromSocket.isConnected())
			{
				int numBytes = dis.read(byteArray, 0, 4096);
					
					if (numBytes > 0)
					{
						byteArray[numBytes] = 0;
						dos.write(byteArray, 0, numBytes);
						dos.flush();
						
						System.out.println("Relayed" + numBytes);
					}
					
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

}
