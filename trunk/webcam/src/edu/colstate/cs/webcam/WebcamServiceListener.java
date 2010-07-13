package edu.colstate.cs.webcam;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.Jingle;

public interface WebcamServiceListener {
    public void notifyFriendListChanged();
    public void notifyMessageReceived(Message msg);
    
	public void receiveCallRequest(Jingle jingle);
	public void callEstablished(Jingle jingle);
	public void receiveAudioData(byte audioData[], int bufLen);


}
