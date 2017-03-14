package com.zhangchi.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class ReadInitConf implements ReadInit{

	@Override
	public boolean readInit(String path,DGraph<String> dGraph){
		// TODO Auto-generated method stub
		FileReader fReader = null;
		BufferedReader bReader = null;
		try {
			fReader = new FileReader(path);
			bReader = new BufferedReader(fReader);
			String bufferline;
			//�����ļ��е�һ���Ƕ�����Ϣ
			bufferline = bReader.readLine();
			if(bufferline!=null){
				String[] vertex = bufferline.split(" ");
				for (String string : vertex) {
					dGraph.add(string);
				}
			}
			//�����ļ�ʣ������Ǳ���Ϣ���¼���Ϣ��
			int count = 1;
			while ((bufferline=bReader.readLine())!=null) {
				if(bufferline.startsWith("#")){
					continue;
				}
				String edge = bufferline.substring(0, bufferline.indexOf(":"));
				String event = bufferline.substring(bufferline.indexOf(":")+1);
				//�����ַ���������edge����
				Edge<String> edgeobj = generateEdge(edge, event,count);
				dGraph.add(edgeobj);
				count++;
			}
			return true;
			
		} 
		catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		finally {
			
			//�ڷ���֮ǰ�ر���
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
		return false;
	}
	
	public Edge<String> generateEdge(String edge,String event,int count){
		Edge<String> edgeobj=null;
		//״̬��Ž���
		String[] vetx = edge.split("-");
		
		String[] ent = event.split(" ");
		//events����Ĵ�СӦ��Ϊ4
		//split���ڵ����⣺"a  a" ��ָ���������� ("a"\""\"a"),�и����ַ���
		String[] events = new String[4];
		int k=0;
		for (int i = 0; i < ent.length; i++) {
			if(ent[i].equals("")){
				continue;
			}
			events[k++] = ent[i];
		}
		if(k!=4 || vetx.length!=2){
			Utils.log(edge+":"+event+"====> missing or error arguement!");
			return edgeobj;
		}
		
		//��������б�
		LinkedList<EventArgument> inputlist;
		
		//û��������������
		if(events[3].equals("NULL")){
			inputlist = null;
		}
		else{
			String[] input = events[3].split(";");
			inputlist = new LinkedList<EventArgument>();
			
			//�����������
			for (String ins : input) {
				String args[] = ins.split("@");
				if(args.length == 1){
					System.out.println("---------------------------:"+input.length+":----------------------------");
				}
				EventArgument eventArgument = new EventArgument(args[0], args[1]);
				inputlist.add(eventArgument);
			}
		
		}
		//�¼������ߣ��¼������¼�����ֵ���¼��������
		TriggerEvent triggerEvent = new TriggerEvent(events[0], events[1], events[2], inputlist);
		
		edgeobj = new Edge<String>(vetx[0], vetx[1], count,triggerEvent);	
		
		return edgeobj;
	}

}
