package com.thedreamsanctuary.dreamguest.admin;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Ban implements Comparable<Ban>, Serializable{
	private static final long serialVersionUID = -2985427290358604648L;
	private final UUID playerUUID;
	private final String name;
	private final String by;
	private final String reason;
	private final Date when;
	
	public Ban(UUID playerUUID, String name, String by, String reason){
		this.playerUUID = playerUUID;
		this.name = name;
		this.by = by;
		this.reason = reason;
		this.when = new Date();
	}
	
	public UUID getUUID(){
		return playerUUID;
	}

	public String getName() {
		return name;
	}

	public String getBy() {
		return by;
	}

	public String getReason() {
		return reason;
	}

	public Date getWhen() {
		return when;
	}

	public int compareTo(Ban o) {
		if(this.getWhen().before(o.getWhen())){
			return -1;
		}else if(this.getWhen().after(o.getWhen())){
			return 1;
		}else{
			return 0;
		}
	}

}
