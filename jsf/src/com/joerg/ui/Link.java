package com.joerg.ui;

public enum Link {
	HOME("home"),
	PROGRAM("program"),
	PLACE("place"),
	ARRIVE("arrive"),
	SLEEP("sleep"),
	REGISTER("register");
	
	private String linkName;
	
	Link(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkName() {
		return linkName;
	}
}
