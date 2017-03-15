package com.zhangchi.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class JBCCClientTest{
	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testJBCCClient(){
		String host="192.168.0.91:2181,192.168.0.92:2181,192.168.0.93:2181";
		String path="/tdng_dw";
		String ABCName="ABC";
		String TBCName="TBC";
		String BCName="ABC";
		Map<String,String> tableDef=new HashMap<>();
		tableDef.put("studentid","Integer");
		tableDef.put("studentname","String");
		Map<String,Object> transact=new HashMap<>();
		transact.put("studentid",100);
		transact.put("studentname","hello");
		String condition="studentid=100";
		JBCCClient test_JBCCClient = JBCCClient.getInstance();
		test_JBCCClient.initializeClient(host,path);
		JBCCResult test_JBCCResult=test_JBCCClient.openSyncConnection();
		List<JBCCResult> test_ListJBCCResult=test_JBCCClient.createTBC(TBCName,tableDef);
		test_ListJBCCResult=test_JBCCClient.createTBC(TBCName,tableDef);
		test_ListJBCCResult=test_JBCCClient.createABC(ABCName,tableDef);
		test_ListJBCCResult=test_JBCCClient.createTBC(TBCName,tableDef);
		test_JBCCResult=test_JBCCClient.fastSelectFromBC(BCName,condition);
		test_JBCCResult=test_JBCCClient.fastSelectFromBC(BCName,condition);
		test_JBCCResult=test_JBCCClient.closeSyncConnection();
	}

}
