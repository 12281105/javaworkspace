package com.zhangchi.java;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;


public class GenerateTestCode<V>{
	LinkedList<Edge<V>> testPath = null;
	
	public GenerateTestCode(LinkedList<Edge<V>> testPath) {
		// TODO Auto-generated constructor stub
		this.testPath = testPath;
	}
	
	public void generateCode(){
		Map<String,String> typedict = new HashMap<>();
    	StringBuffer stringBuffer = new StringBuffer();
    	stringBuffer.append("JBCCClient myJBCCClient = JBCCClient.getInstance();\n");
    	typedict.put("JBCCClient", "myJBCCClient");
    	for (Edge<V> edge : testPath) {
    		TriggerEvent triggerEvent = edge.getTriggerEvent();
    		String retprefix = "";
    		if(!typedict.containsKey(triggerEvent.getRetResult())){
    			typedict.put(triggerEvent.getRetResult(), "test_"+triggerEvent.getRetResult().replaceAll("<|>|,", ""));
    			retprefix = triggerEvent.getRetResult()+" ";
    		}
    		else{
    			
    		}
    		if(triggerEvent.getRetResult().equals("NULL")){
    			stringBuffer.append(typedict.get(triggerEvent.getObjectName())+"."+triggerEvent.getEventName());
    		}
    		else{
    			stringBuffer.append(retprefix+typedict.get(triggerEvent.getRetResult())+"="+typedict.get(triggerEvent.getObjectName())+"."+triggerEvent.getEventName());
    		}
    		if(triggerEvent.getInputArg()==null){
    			stringBuffer.append("();\n");
    		}
    		else{
    			stringBuffer.append("(");
    			for (EventArgument eventArgument : triggerEvent.getInputArg()) {
    				stringBuffer.append(eventArgument.getArgname()+",");
    			}
    			stringBuffer.deleteCharAt(stringBuffer.length()-1);
    			stringBuffer.append(");\n");
    		}
    		
			//stringBuffer.append(triggerEvent.getRetResult()+" "+typedict.get(triggerEvent.getRetResult())+"="+triggerEvent.getObjectName()+"."+triggerEvent.getEventName()+"("+triggerEvent.getInputArg()+")\n");
		}
    	System.out.println(stringBuffer.toString());
	}
	
}
