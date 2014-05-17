package com.genelle.alexandre.server;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class JavaEmail
{
    static Session mailSession;
    private static final int DAILY_MAX_MAILS = 100;
    private static final int GLOBAL_MAX_MAILS = 500;
    private static final int INIT_DAY = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    private static int currentDay;
    private static int compteur = 0;
    private static int compteurGlobal = 0;
    private static boolean stop = false;

    public static void main(String args[]) throws AddressException,    MessagingException, UnsupportedEncodingException
    {
    	System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
    	if(true) return;
    	
        JavaEmail javaEmail = new JavaEmail();
        javaEmail.setMailServerProperties();
        MimeMessage emailMessage = javaEmail.draftEmailMessage("test","test","test");
        javaEmail.sendEmail(emailMessage);
    }
    
    public static boolean sendAppEngineMail(String name, String email, String msg) throws UnsupportedEncodingException, MessagingException {
    	
    	currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    	System.out.println("currentDay : "+currentDay);
    	
    	if (currentDay != INIT_DAY) {
    		System.out.println("le jour a change car initDay = "+INIT_DAY);
    		compteur = 0;
    		stop = false;
    	}
    	
    	if (stop || msg.length() > 5000 || name.length() > 30 || email.length() > 60){
    		System.out.println("erreur");
    		return false;
    	}
    	
    	compteur++;
    	compteurGlobal++;
    	String emailBody = "";
    	
    	Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("alex@alexandregenelle.com", name));
		message.addRecipient(Message.RecipientType.TO,
	     new InternetAddress("alex@alexandregenelle.com", "Mr. User"));
		
		
    	if (compteur >= DAILY_MAX_MAILS || compteurGlobal >= GLOBAL_MAX_MAILS) {
    		emailBody = "La limite de message est atteinte";
    		message.setSubject("Limite atteinte");
    		stop = true;
    	}
    	else {
    		emailBody = "Name : "+name+"\n"+"mail : "+email+"\n"+"message : "+msg;
    		message.setSubject("mail from "+name);
    	}
		
		message.setContent(emailBody, "text/html");
		message.setText(emailBody);
		System.out.println("message sent !");
	    Transport.send(message);
	    return true;
    }

    public static void setMailServerProperties()
    {
    	
        Properties emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", "587");
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        mailSession = Session.getDefaultInstance(emailProperties, null);
        
    	
    }

    public static MimeMessage draftEmailMessage(String name, String email, String msg) throws AddressException, MessagingException
    {
        String[] toEmails = { "alexandre.genelle@gmail.com" };
        String emailSubject = "mail from "+name;
        String emailBody = "Name : "+name+"\n"+"mail : "+email+"message : "+msg;
        MimeMessage emailMessage = new MimeMessage(mailSession);
        /**
         * Set the mail recipients
         * */
        for (int i = 0; i < toEmails.length; i++)
        {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
        }
        emailMessage.setSubject(emailSubject);
        /**
         * If sending HTML mail
         * */
        emailMessage.setContent(emailBody, "text/html");
        /**
         * If sending only text mail
         * */
        emailMessage.setText(emailBody);// for a text email
        return emailMessage;
    }

    public static void sendEmail(MimeMessage msg) throws AddressException, MessagingException, UnsupportedEncodingException
    {
        /**
         * Sender's credentials
         * */
    	
        String fromUser = "alexandregenelle2@gmail.com";
        String fromUserEmailPassword = "4K7.cd.MD10";

        String emailHost = "smtp.gmail.com";
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromUser, fromUserEmailPassword);
		
        /**
         * Send the mail
         * */
    	
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
        
    	
        
        System.out.println("Email sent successfully.");
    }
}