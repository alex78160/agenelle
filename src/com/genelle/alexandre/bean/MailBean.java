package com.genelle.alexandre.bean;

import java.util.List;

public class MailBean {
	
	private static final String HTML_CONTENT_TYPE = "text/html";
	private String name;
	private String from;
	private List<String> recipients;
	private String subject;
	private StringBuilder content;
	private String contentType;
	
	public MailBean() {
		this.contentType = HTML_CONTENT_TYPE;
	}
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public List<String> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public StringBuilder getContent() {
		return content;
	}
	public void setContent(StringBuilder content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
