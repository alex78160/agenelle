package com.genelle.alexandre.server.mail;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.genelle.alexandre.bean.MailBean;

public class BasicMailServiceImpl extends MailService {

	static {
		currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}
	
	private static final Logger LOG = Logger.getLogger(BasicMailServiceImpl.class.getName());
    private static final int DAILY_MAX_MAILS = 100;
    private static final int GLOBAL_MAX_MAILS = 500;
    private static final int MAX_CONTENT_SIZE = 5000;
    private static final int MAX_NAME_SIZE = 30;
    private static final int MAX_FROM_SIZE = 60;
    private static int currentDay;
    private static int compteur = 0;
    private static int compteurGlobal = 0;
	
	
	public BasicMailServiceImpl() {
		super();
		LOG.info("BasicMailServiceImpl - init");
		Properties props = new Properties();
		session = Session.getDefaultInstance(props, null);
		message = new MimeMessage(session);
	}
	


	@Override
	protected void buildMessage(MailBean mailBean) throws UnsupportedEncodingException, MessagingException {
		LOG.info("BasicMailServiceImpl - buildMessage");
		message = new MimeMessage(session);
		message.setFrom(new InternetAddress(GMAIL_SENDER, MimeUtility.encodeText(mailBean.getName(), "utf-8", "B")));
        for (String recipient : mailBean.getRecipients()) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        }
        message.setSubject(MimeUtility.encodeText(mailBean.getSubject(), "utf-8", "B"));
        StringBuilder body = new StringBuilder().append("Name : "+mailBean.getName()+"\n"+"mail : "+mailBean.getFrom()+"\n"+"message : "+mailBean.getContent().toString());
		message.setContent(body.toString(), "text/html");
	}



	@Override
	protected boolean checkConditions() {
		
    	int today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    	LOG.info("checkConditions - today : "+today);
    	LOG.info("checkConditions - currentDay : "+currentDay);
    	LOG.info("checkConditions - compteur : "+compteur);
    	LOG.info("checkConditions - compteurGlonal : "+compteurGlobal);
    	
    	if (today != currentDay) {
    		LOG.warning("le jour a changé");
    		currentDay = today;
    		compteur = 0;
    	}
    	
    	if (mailBean.getContent().toString().length() > MAX_CONTENT_SIZE) {
    		LOG.warning("checkConditions - erreur : msg length : " + mailBean.getContent().toString().length() + " > limit : " + MAX_CONTENT_SIZE);
    		return false;
    	}
    	
    	if (mailBean.getName().toString().length() > MAX_NAME_SIZE) {
    		LOG.warning("checkConditions - erreur : name length : " + mailBean.getName().toString().length() + " > limit : " + MAX_NAME_SIZE);
    		return false;
    	}
    	
    	if (mailBean.getFrom().toString().length() > MAX_FROM_SIZE) {
    		LOG.warning("checkConditions - erreur : from length : " + mailBean.getFrom().toString().length() + " > limit : " + MAX_FROM_SIZE);
    		return false;
    	}
    	
    	if (compteur >= DAILY_MAX_MAILS) {
    		LOG.warning("checkConditions - erreur : compteur du jour : " + compteur + " au dela du max : " + DAILY_MAX_MAILS);
    		return false;
    	}
    	
    	if (compteurGlobal >= GLOBAL_MAX_MAILS) {
    		LOG.warning("checkConditions - erreur : compteur global : " + compteurGlobal + " au dela du max : " + GLOBAL_MAX_MAILS);
    		return false;
    	}

    	//FIXME l'incrémentation du compteur n'a rien à faire ici !!!
    	compteur++;
    	compteurGlobal++;

		return true;
	}

}
