package edu.colstate.cs.mediarelay;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MediaRelayServer {

	private static HashMap<String, MediaRelaySocket> mapMediaRelaySocket = new HashMap<String,MediaRelaySocket>();
	
	private static String readLine(Socket clientSocket)
	{
		String strLine = null;
		
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			strLine = br.readLine();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return strLine;
	}

	private static String getStringData(String strCommandLine, int iIndex)
	{
		StringTokenizer tokenizer = new StringTokenizer(strCommandLine);

		int iCounter = 0;
		
		while (iCounter <= iIndex && tokenizer.hasMoreTokens())
		{
			if (iCounter == iIndex)
			{
				return tokenizer.nextToken();
			}
			// advance counter/token
			iCounter++;
			tokenizer.nextToken();
		}
		
		return null;
	}

	
	private static String getCommand(String strCommandLine)
	{
		return getStringData(strCommandLine, 0);
	}
	
	private static String getCommandData(String strCommandData)
	{
		return getStringData(strCommandData, 1);
	}
	
	public static void main(String args[])
	{
		try
		{
			ServerSocket s = new ServerSocket(15000);
			
			while (true)
			{
				Socket clientSocket = s.accept();
				clientSocket.setSoTimeout(0);
				
				String strLine = readLine(clientSocket);
				String strCommand = getCommand(strLine);
				String strData = getCommandData(strLine);
				
				if (strCommand.equals("IDENTITY"))
				{
					System.out.println("Identity: " + strData);
					MediaRelaySocket mediaRelaySocket = new MediaRelaySocket(clientSocket);
					mapMediaRelaySocket.put(strData, mediaRelaySocket);
				}
				else if (strCommand.equals("IDENTITYJOIN"))
				{
					System.out.println("Identity: " + strData);
					MediaRelaySocket mediaRelaySocket = new MediaRelaySocket(clientSocket);
					mapMediaRelaySocket.put(strData, mediaRelaySocket);

					String strClient1 = getJoinClient(strLine, 1);
					String strClient2 = getJoinClient(strLine, 2);

					System.out.println("Join: " + strClient1 + "," + strClient2);
					MediaRelaySocket socket1 = mapMediaRelaySocket.get(strClient1);
					MediaRelaySocket socket2 = mapMediaRelaySocket.get(strClient2);
					
					SocketCommunicator communicator1 = new SocketCommunicator(socket1.getSocket(), socket2.getSocket());
					
					Thread t1 = new Thread(communicator1);
					t1.start();

					SocketCommunicator communicator2 = new SocketCommunicator(socket2.getSocket(), socket1.getSocket());
					
					Thread t2 = new Thread(communicator2);
					t2.start();
					
					
				}
				else if (strCommand.equals("JOIN"))
				{
					String strClient1 = getJoinClient(strLine, 1);
					String strClient2 = getJoinClient(strLine, 2);

					System.out.println("Join: " + strClient1 + "," + strClient2);
					
					MediaRelaySocket socket1 = mapMediaRelaySocket.get(strClient1);
					MediaRelaySocket socket2 = mapMediaRelaySocket.get(strClient2);
					
					SocketCommunicator communicator1 = new SocketCommunicator(socket1.getSocket(), socket2.getSocket());
					
					Thread t1 = new Thread(communicator1);
					t1.start();

					SocketCommunicator communicator2 = new SocketCommunicator(socket2.getSocket(), socket1.getSocket());
					
					Thread t2 = new Thread(communicator2);
					t2.start();
					
				}
			}
			
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			
		}
	}

	private static String getJoinClient(String strLine, int i) {
		return getStringData(strLine, i);
	}
	
}
