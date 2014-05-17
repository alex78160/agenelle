package com.genelle.alexandre.server.mail;

import com.genelle.alexandre.bean.MailBean;

public final class MailServiceFactory {
	
	private static MailService mailService;
	
	public static MailService getInstance(MailBean mailBean) {
		if (mailService == null) {
			mailService = new BasicMailServiceImpl(mailBean);
		}
		return mailService;
	}

}
