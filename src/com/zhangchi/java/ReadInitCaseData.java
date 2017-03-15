package com.zhangchi.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

public class ReadInitCaseData {
	public void readInit(String path,LinkedList<String> codeList){
		FileReader fReader = null;
		BufferedReader bReader = null;
		try {
			fReader = new FileReader(path);
			bReader = new BufferedReader(fReader);
			String bufferline;
			StringBuffer stringBuffer = new StringBuffer();
			
			while ((bufferline=bReader.readLine())!=null) {
				stringBuffer.setLength(0);
				if (bufferline.startsWith("String")) {    //String 类型
					stringBuffer.append(bufferline.replace('@', ' ')).append(";\n");
					codeList.add(stringBuffer.toString());
				}
				else if (bufferline.startsWith("Map")){   //Map 类型
					stringBuffer.append(bufferline.substring(0,bufferline.indexOf('=')+1).replace('@', ' '));
					stringBuffer.append("new HashMap<>();\n");
					String temp = bufferline.substring(bufferline.indexOf('=')+1).replace('{', '\0').replace('}', '\0').trim();
					String[] strs = temp.split(",");
					for (String string : strs) {
						stringBuffer.append(bufferline.substring(bufferline.indexOf('@')+1,bufferline.indexOf('='))).append(".put(").append(string.replace(':', ',')).append(");\n");
					}
					codeList.add(stringBuffer.toString());
				}
				else{
					
				}
			}
			
		} 
		catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		finally {
			
			//在返回之前关闭流
			try {
				if (bReader!=null) {
					bReader.close();
				}
				if(fReader!=null){
					fReader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
