package org.brainteam.lunchbox.json;

import java.util.Date;

public class JsonSchedulerJob implements Comparable<JsonSchedulerJob> {

	private String key;
	private String group;
	private String name;
	private String description;
	private Date lastExecution;
	private Date nextExecution;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getLastExecution() {
		return lastExecution;
	}
	
	public void setLastExecution(Date lastExecution) {
		this.lastExecution = lastExecution;
	}
	
	public Date getNextExecution() {
		return nextExecution;
	}
	
	public void setNextExecution(Date nextExecution) {
		this.nextExecution = nextExecution;
	}
	
	@Override
	public int compareTo(JsonSchedulerJob o) {
		int group = getGroup().compareTo(o.getGroup());
		if (group != 0) {
			return group;
		}
		return getName().compareTo(o.getName());
	}
	
}
