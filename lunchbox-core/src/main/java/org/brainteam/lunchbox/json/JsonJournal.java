package org.brainteam.lunchbox.json;

import java.util.Date;

public class JsonJournal {
	
	private Date timestamp;
	private String text;
	private JsonOption user;
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public JsonOption getUser() {
		return user;
	}
	
	public void setUser(JsonOption user) {
		this.user = user;
	}

}
