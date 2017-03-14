package com.zhangchi.java;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ListDGraphTest3 {

    DGraph<String> mDG = new ListDGraph<String>();
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAll() {
    	
        Utils.log("===============add v=================");
        
        mDG.add("1");
        mDG.add("2");
        mDG.add("3");
        mDG.add("4");
        /*
        mDG.add("5");
        mDG.add("6");
        mDG.add("7");
        mDG.add("8");
        */

        Utils.log("===============add edge=================");
        
        mDG.add(new Edge<String>("1", "2",1));
        mDG.add(new Edge<String>("2", "3",2));
        mDG.add(new Edge<String>("3", "4",3));
        mDG.add(new Edge<String>("3", "2",4));
        mDG.add(new Edge<String>("1", "3",5));
        //mDG.add(new Edge<String>("1", "3",6));
        //mDG.add(new Edge<String>("3", "2",6));
        //mDG.add(new Edge<String>("3", "3",7));
        //mDG.add(new Edge<String>("4", "4",8));
        /*
        mDG.add(new Edge<String>("4", "6",6));
        mDG.add(new Edge<String>("4", "7",7));
        mDG.add(new Edge<String>("5", "4",8));
        mDG.add(new Edge<String>("5", "6",9));
        mDG.add(new Edge<String>("5", "7",10));
        mDG.add(new Edge<String>("6", "7",11));
        mDG.add(new Edge<String>("6", "8",12));
        mDG.add(new Edge<String>("7", "6",13));
        mDG.add(new Edge<String>("7", "8",14));
        */
        /*
        LinkedList<LinkedList<String>> circle = new LinkedList<LinkedList<String>>();
    	
        mDG.checkCircles(circle);
        
        for(LinkedList<String> pathList : circle){
        	System.out.println(pathList.toString());
        }
        */
        LinkedList<LinkedList<Edge<String>>> result = mDG.DFS_ALL_LOOP_Travel("1");
        for (LinkedList<Edge<String>> linkedList : result) {
			for (Edge<String> edge : linkedList) {
				System.out.print(edge.getEventnum()+" ");
			}
			System.out.println();
		}
        
    }
    
}
