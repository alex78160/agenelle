package com.genelle.alexandre.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.genelle.alexandre.bean.MailBean;
import com.genelle.alexandre.bean.ResponseBean;
import com.genelle.alexandre.server.commun.HeaderProcess;
import com.genelle.alexandre.server.mail.MailService;
import com.genelle.alexandre.server.mail.MailServiceFactory;


public class MsgServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 996713370404536320L;
	private static final Logger LOG = Logger.getLogger(MsgServlet.class.getName());
	
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Map<String, String> headers = HeaderProcess.extractHeaders(req);
		LOG.info("addr : "+req.getRemoteAddr());
		
		//TODO recuperer l'IP et limiter un message par minute par IP
		LOG.info("MsgServlet - doPost");
		JSONObject jso = new JSONObject();
		try {
			/*
	        JavaEmail.setMailServerProperties();
	        MimeMessage emailMessage = JavaEmail.draftEmailMessage(name,email,msg);
	        JavaEmail.sendEmail(emailMessage);
	        */
			
			MailBean mailBean = buildMailBean(req);
			MailService mailService = MailServiceFactory.getInstance(mailBean);
			ResponseBean responseBean = mailService.sendMail();
			
			LOG.info("status : "+responseBean.getStatus());
			LOG.info("lib : "+responseBean.getLib());
			
	        resp.setContentType("application/json");
	        
	        if (responseBean.getStatus()) {
	        	jso.put("lib", "message envoye");
	        	jso.put("status", "ok");
	        }
	        else {
	        	jso.put("lib", responseBean.getLib());
	        	jso.put("status", "ko");
	        }
		}
		catch(Exception e) {
			LOG.severe("MsgServlet - exception lors de l'envoi : "+e.getMessage());
			e.printStackTrace();
			jso.put("lib", e.getMessage());
			jso.put("status", "ko");
		}
		finally {
			resp.getWriter().print(jso.toString());
			resp.flushBuffer();
		}
		
		
	}

	private MailBean buildMailBean(HttpServletRequest req) {
		
		MailBean mailBean = new MailBean();
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String msg = req.getParameter("msg");
		LOG.info("name : "+name+" - email : "+email+" - msg : "+msg);
		mailBean.setName(name);
		mailBean.setFrom(email);
		mailBean.setSubject("message de "+name+" - "+email);
		List<String> recipients = new ArrayList<String>();
		recipients.add("alex@alexandregenelle.com");
		mailBean.setRecipients(recipients);
		mailBean.setContent(new StringBuilder(msg));
		return mailBean;
		
	}
	

}
