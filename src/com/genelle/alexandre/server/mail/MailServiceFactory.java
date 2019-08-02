package com.genelle.alexandre.server.mail;

public final class MailServiceFactory {
	
	private static MailService mailService;
	
	public static MailService getInstance() {
		if (mailService == null) {
			mailService = new BasicMailServiceImpl();
		}
		return mailService;
	}

}
