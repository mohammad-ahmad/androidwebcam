package edu.colstate.cs.webcam;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.Jingle;
import org.jivesoftware.smackx.provider.JingleProvider;

public class WebcamServiceImpl implements RosterListener, PacketListener, MediaAudioListener 
{
	protected XMPPConnection connection = null;
	protected ArrayList<Friend> friendList = null;
	protected Roster roster = null;
	protected MediaRelaySocket audioRelaySocket = null;
	protected WebcamServiceListener listener = null;

	public WebcamServiceImpl(WebcamServiceListener listener)
	{
		this.listener = listener;
	}
	
	// method to login
    public boolean login(String userName, String password, String serverHost)
    {
        try
        {
        	// Register Jingle provider
        	if (connection == null)
        	{
                ProviderManager providerManager = ProviderManager.getInstance();
                providerManager.addIQProvider("jingle", "urn:xmpp:tmp:jingle", new JingleProvider());
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
/*            PacketFilter messageFilter = new PacketTypeFilter(Message.class);
            PacketFilter jingleFilter = new PacketTypeFilter(Jingle.class);
        	OrFilter orFilter = new OrFilter();
        	orFilter.addFilter(messageFilter);
        	orFilter.addFilter(jingleFilter);
//            connection.addPacketListener(this, orFilter);
        
 */
        	// Accept packets of all types
        	connection.addPacketListener(this, null);

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
    public ArrayList<Friend> getFriendList()
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
    	listener.notifyFriendListChanged();
    }
        
    void notifyMessageReceived(Message msg)
    {
    	String fromUser = msg.getFrom();
    	String message = msg.getBody();
    	System.out.println("Msg from = " + fromUser + ",Contents=" + message);
 	
    	listener.notifyMessageReceived(msg);
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

	public void processMessagePacket(Packet packet)
	{
		Message msg = (Message)packet;
		
		if (msg.getBody().equals("webcam-call"))
		{
			Jingle jingle = new Jingle();
			jingle.setFrom(msg.getFrom());
			jingle.setTo(msg.getTo());
			
			receiveCallRequest(jingle);
		}
		else if (msg.getBody().equals("webcam-call-accept"))
		{
			Jingle jingle = new Jingle();
			jingle.setFrom(msg.getFrom());
			jingle.setTo(msg.getTo());
			
			callEstablished(jingle);
		}
		else if (msg.getBody().equals("webcam-call-terminate"))
		{
			// ::TODO:: implement
		}
		else
		{
			// regular text message
			notifyMessageReceived(msg);
		}
	}
	
	public void processJinglePacket(Packet packet)
	{
		Jingle jingle = (Jingle)packet;
		
		switch (jingle.getAction())
		{
			case SESSION_INITIATE:
				{
					receiveCallRequest(jingle);
				}
				break;
			case SESSION_ACCEPT:
			{
				// ::TODO:: implement
			}
			break;
			case SESSION_TERMINATE:
			{
				// ::TODO:: implement
			}
			break;
		}
	}
	
	// Packet Listener method
	@Override
	public void processPacket(Packet packet) {
		
		System.out.println(packet.toXML());
		
		if (packet instanceof Message)
		{
			processMessagePacket(packet);
		}
		
		if (packet instanceof Jingle)
		{
			processJinglePacket(packet);
		}

	}
	
	// Audio call methods
	public void callFriend(Friend friend)
	{
		// ::TODO:: check if Friend is online

		// KRN (TODO) -- Uncomment out when Jingle implementation is working
/*        
		Jingle jingle = new Jingle();
		jingle.setAction(JingleActionEnum.SESSION_INITIATE);
		jingle.setInitiator(connection.getUser());
		jingle.setTo(friend.getUser());

		connection.sendPacket(jingle);  
*/		
	   // Temporary workaround
	   // Send Text Message to other recipient - to send call request
	   sendTextMessage(friend, "webcam-call");
	}
	
	public void receiveCallRequest(Jingle jingle)
	{
		listener.receiveCallRequest(jingle);
	}
	
	public void acceptCall(Friend friend)
	{
		// ::TODO:: avoid hard-coding of Media Relay Server host/port
		
		// establish socket connection to Media Relay server
		audioRelaySocket = new MediaRelaySocket("22ndcenturysoftware.com", 15000);
		audioRelaySocket.setIdentity(connection.getUser());
		audioRelaySocket.setAudioListener(this);
		
		sendTextMessage(friend, "webcam-call-accept");
		
	}
	
	public void callEstablished(Jingle jingle)
	{
		// ::TODO:: avoid hard-coding of Media Relay Server host/port
		
		// establish socket connection to Media Relay server and indicate that
		// the server should should start relaying
		audioRelaySocket = new MediaRelaySocket("22ndcenturysoftware.com", 15000);
		audioRelaySocket.joinCall(connection.getUser(), jingle.getFrom());
		audioRelaySocket.setAudioListener(this);
		
		listener.callEstablished(jingle);
	}
	
	public void rejectCall(Friend friend)
	{
		// ::TODO:: implement
	}
	
	public void sendAudioData(byte audioData[], int bufLen)
	{
		audioRelaySocket.sendAudioData(audioData, bufLen);
	}

	public void receiveAudioData(byte audioData[], int bufLen)
	{
		String strData = new String(audioData);
		System.out.println(strData);
		listener.receiveAudioData(audioData, bufLen);
	}
}
