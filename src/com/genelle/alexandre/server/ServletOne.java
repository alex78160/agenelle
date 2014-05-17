package com.genelle.alexandre.server;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.genelle.alexandre.server.mail.MailService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ServletOne extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3692227770570704960L;
	private static final String CONTROL = "cd.DVD.bd10_14nval!_09071988_TwEEt";
	private static final Logger LOG = Logger.getLogger(MailService.class.getName());

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String control = req.getParameter("control");
		LOG.info("ServletOne - service - control = "+control);
		if (CONTROL.equals(control)) {
			LOG.info("ServletOne - service - ok");
			
			try {
				DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
				
				String value = "mon message à stocker";
				String id = "mId1";
				Entity message = new Entity("Message", id);
				message.setProperty("user","alex");
				message.setProperty("date", new Date());
				message.setProperty("value", value);
				Key keyResult = ds.put(message);
				LOG.info("key = "+keyResult);
				
				/*
				Entity res = ds.get(KeyFactory.createKey("Message", "mId1"));
				LOG.info(res.getProperty("value").toString());
				*/
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			LOG.warning("ServletOne - service - KO");
		}
	}

	
}	
	



