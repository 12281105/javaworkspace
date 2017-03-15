package com.zhangchi.java;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;


public class GenerateTestCode<V>{
	LinkedList<Edge<V>> testPath = null;
	String dstpath = "./src/com/zhangchi/java/JBCCClientTest.java";
	
	public GenerateTestCode(LinkedList<Edge<V>> testPath) {
		// TODO Auto-generated constructor stub
		this.testPath = testPath;
	}
	
	public void generateCode(){
		StringBuffer stringBuffer = new StringBuffer();
		ReadInitCaseData initCaseData = new ReadInitCaseData();
		LinkedList<String> codeList = new LinkedList<String>();
		initCaseData.readInit("./confinfo/CaseData.init", codeList);
		//变量初始化代码
		for (String string : codeList) {
			stringBuffer.append(string);
		}
		
		//接口调用代码
		Map<String,String> typedict = new HashMap<>();
    	
    	stringBuffer.append("JBCCClient test_JBCCClient = JBCCClient.getInstance();\n");
    	typedict.put("JBCCClient", "test_JBCCClient");
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
    	
		}
    	writeToFile(stringBuffer.toString());
	}
	
	public void writeToFile(String bodycode){
		StringBuffer resultBuffer = new StringBuffer();
		//所在的包名
		resultBuffer.append("package com.zhangchi.java;\n\n");
		
		//导入的类名，暂时未实现
		resultBuffer.append(
		"import org.junit.After;\n"
		+ "import org.junit.Before;\n"
		+ "import org.junit.Test;\n"
		+ "import java.util.HashMap;\n"
		+ "import java.util.Map;\n\n");
		
		//类的主体部分
		resultBuffer.append("public class JBCCClientTest{\n");
		
		resultBuffer.append("\t@Before\n"
		+ "\tpublic void setUp() throws Exception {\n\n"
		+"\t}\n\n");
		
		resultBuffer.append("\t@After\n"
		+"\tpublic void tearDown() throws Exception {\n\n"
	    +"\t}\n\n");
		
		resultBuffer.append("\t@Test\n"
				+ "\tpublic void testJBCCClient(){\n");
		
		int index = 0;
		int start = 0;
		while (true) {
			start = index;
			index = bodycode.indexOf('\n', index);
			if(index==-1){
				break;
			}
			resultBuffer.append("\t\t").append(bodycode.substring(start, index+1));
			index++;
		}
		resultBuffer.append("\t}\n\n");
		
		resultBuffer.append("}\n");
		//System.out.println(resultBuffer.toString());
		
		//将代码写入java文件
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(dstpath);
			fileWriter.write(resultBuffer.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
