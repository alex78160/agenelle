package com.genelle.alexandre.server.mail;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import com.genelle.alexandre.bean.MailBean;
import com.genelle.alexandre.bean.ResponseBean;

public abstract class MailService {
	
	private static final Logger LOG = Logger.getLogger(MailService.class.getName());
	protected static final String GMAIL_SENDER = "alex@alexandregenelle.com";
	protected Session session;
	protected Message message;
	protected MailBean mailBean;
	
	protected MailBean getMailBean() {
		return mailBean;
	}

	protected void setMailBean(MailBean mailBean) {
		this.mailBean = mailBean;
	}

	public MailService() {
		
	}
	
	protected abstract boolean checkConditions();
	
	protected abstract void buildMessage(MailBean mailBean) throws UnsupportedEncodingException, MessagingException;
	
	
	public ResponseBean sendMail(MailBean mailBean) {
		
		LOG.info("MailService - sendMail");
		this.mailBean = mailBean;
		ResponseBean responseBean = new ResponseBean();
		
		try {
			buildMessage(mailBean);
			if (checkConditions()) {
				LOG.info("MailService - sendMail - conditions OK -> on envoie le mail");
				Transport.send(message);
				responseBean.setStatus(true);
			}
			else {
				responseBean.setStatus(false);
				responseBean.setLib("condition");
				LOG.warning("MailService - sendMail - conditions KO -> on n'envoie pas le mail");
			}
			LOG.info("MailService - sendMail - pas d'erreur");
		} catch (MessagingException | UnsupportedEncodingException e) {
			LOG.severe("MailService.sendMail : exception : "+e.getMessage());
			responseBean.setStatus(false);
			responseBean.setLib("erreur");
		}
		
		return responseBean;
		
	}
	
	

}
