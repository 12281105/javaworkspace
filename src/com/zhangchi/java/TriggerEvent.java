package com.zhangchi.java;

import java.util.LinkedList;

public class TriggerEvent {
	private String objectName;
	private String eventName;
	private String retResult;
	private LinkedList<EventArgument> inputArg;
	
	public TriggerEvent(String objectName,String eventName,String retResult,LinkedList<EventArgument> inputArg) {
		// TODO Auto-generated constructor stub
		this.objectName = objectName;
		this.eventName = eventName;
		this.inputArg = inputArg;
		this.retResult = retResult;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public LinkedList<EventArgument> getInputArg() {
		return inputArg;
	}

	public void setInputArg(LinkedList<EventArgument> inputArg) {
		this.inputArg = inputArg;
	}

	public String getRetResult() {
		return retResult;
	}

	public void setRetResult(String retResult) {
		this.retResult = retResult;
	}
	
	
}
