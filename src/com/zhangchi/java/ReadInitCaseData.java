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
				if (bufferline.startsWith("String")) {
					stringBuffer.append(bufferline.replace('@', ' ')).append(";");
					retCodeList.add(stringBuffer.toString());
				}
				else if (bufferline.startsWith("Map")){
					stringBuffer.append(bufferline.substring(0,bufferline.indexOf('@')))
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
