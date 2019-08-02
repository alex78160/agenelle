package com.genelle.alexandre.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.genelle.alexandre.server.mail.MailService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class ServletOne extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3692227770570704960L;
	
	private static final Logger LOG = Logger.getLogger(MailService.class.getName());
	
	private static int MAX = 10;
	private static int COUNT = 0;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if (COUNT >= MAX) {
			LOG.warning("COUNT="+COUNT+" - max atteint -> pas de traitement");
			return;
		}
		else {
			COUNT++;
		}
		
		String secretToken = "";
		try {
			Properties props = new Properties();
			try (InputStream in = getServletContext().getResourceAsStream("/props-ignore.properties")) {
				props.load(in);
				in.close();
			}
			secretToken = props.getProperty("secretToken");
			System.out.println("token : "+secretToken);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		String control = req.getParameter("control");
		LOG.info("ServletOne - service - control = "+control);
		if (secretToken.equals(control)) {
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
	



