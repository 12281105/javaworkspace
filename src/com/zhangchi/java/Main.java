package com.zhangchi.java;

import java.util.LinkedList;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		DGraph<String> mDG = new ListDGraph<String>();
		ReadInitConf readInit = new ReadInitConf();
    	readInit.readInit("./confinfo/DGraph.init", mDG);
    	LinkedList<LinkedList<Edge<String>>> result = mDG.DFS_ALL_LOOP_Travel("1");
    	Random random = new Random();
    	int index = random.nextInt(result.size());
    	LinkedList<Edge<String>> eventpath = result.get(index);
		GenerateTestCode<String> generateTestCode = new GenerateTestCode<>(eventpath);
		generateTestCode.generateCode();
	}
}
