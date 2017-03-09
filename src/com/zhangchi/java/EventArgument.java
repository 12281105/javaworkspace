package com.zhangchi.java;

public class EventArgument {
	private String type;
	private String argname;
	
	public EventArgument(String type,String argname) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.argname = argname;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getArgname() {
		return argname;
	}
	public void setArgname(String argname) {
		this.argname = argname;
	}
	
}
