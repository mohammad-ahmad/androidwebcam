package edu.colstate.cs.mediarelay;

import java.net.Socket;

public class MediaRelaySocket {

	private Socket clientSocket = null;
	
	public MediaRelaySocket(Socket clientSocket) {
		// TODO Auto-generated constructor stub
		this.clientSocket = clientSocket;
	}

	public Socket getSocket() {
		// TODO Auto-generated method stub
		return clientSocket;
	}

}
