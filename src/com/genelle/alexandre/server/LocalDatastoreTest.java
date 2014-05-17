package com.genelle.alexandre.server;

import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import com.google.appengine.api.datastore.Query;
//import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
//import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocalDatastoreTest {
/*
    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
*/
    // run this test twice to prove we're not leaking any state across tests
    private void doTest() {
        
        /*
        assertEquals(0, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
        ds.put(new Entity("yam"));
        ds.put(new Entity("yam"));
        assertEquals(2, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
        */
        monTest();
		

    }
    
	public static void main(String[] args) {
		
		LocalDatastoreTest datastoreTest = new LocalDatastoreTest();
		datastoreTest.monTest();

		
	}
	
	public void monTest() {
		//helper.setUp();
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		String value = "mon message à stocker";
		String id = "mId1";

		Entity message = new Entity("Message", id);
		message.setProperty("user","alex");
		message.setProperty("date", new Date());
		message.setProperty("value", value);
		Key keyResult = ds.put(message);
		
		System.out.println("key = "+keyResult);
		
		try {
			Entity res = ds.get(KeyFactory.createKey("Message", "mId1"));
			System.out.println(res.getProperty("value"));
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//helper.tearDown();
		
		
	}

    @Test
    public void testInsert1() {
        doTest();
    }

    @Test
    public void testInsert2() {
        doTest();
    }
}