package edu.colstate.cs.webcam;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

public class Friend 
{
	// ::TODO:: do we need to export other XMPP Statuses
	enum EStatus
	{
		STATUS_AVAILABLE,
		STATUS_UNAVAILABLE
	}
	
	private RosterEntry rosterEntry = null;
	private Roster roster = null;
	private String emailAddress = null;

	public Friend(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
	
	public Friend(Roster roster, RosterEntry rosterEntry)
	{
		this.roster = roster;
		this.rosterEntry = rosterEntry;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}
	
	public String getUser()
	{
		// return email address, with /Smack appended
		if (rosterEntry == null)
		{
			return getEmailAddress() + "/Smack";
		}
		
		return rosterEntry.getUser();
	}
	
	public String getName()
	{
		return rosterEntry.getName();
	}
	
	public EStatus getStatus() 
	{
		EStatus status = EStatus.STATUS_UNAVAILABLE;
		
		if (roster.getPresence(getUser()).getType() == Presence.Type.available)
		{
			status = EStatus.STATUS_AVAILABLE;
		}
		
		return status;
	}
	
	
	
}
