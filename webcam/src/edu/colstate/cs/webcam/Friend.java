package edu.colstate.cs.webcam;

import org.jivesoftware.smack.RosterEntry;

public class Friend 
{
	private RosterEntry rosterEntry = null;

	public Friend(RosterEntry rosterEntry)
	{
		this.rosterEntry = rosterEntry;
	}

	public String getUser()
	{
		return rosterEntry.getUser();
	}
	
	public String getName()
	{
		return rosterEntry.getName();
	}
	
	
}
