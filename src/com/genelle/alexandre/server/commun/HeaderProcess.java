package com.genelle.alexandre.server.commun;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HeaderProcess {
	
	public static Map<String, String> extractHeaders(HttpServletRequest req) {
		
		Enumeration<String> headerNames = req.getHeaderNames();
		Map<String, String> map = new HashMap<String, String>();
		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			String headerValue = req.getHeader(headerName);
			map.put(headerName, headerValue);
			String message = new StringBuilder().append("headerName : ").append(
					headerName).append(" - value : ").append(headerValue)
					.toString();
			System.out.println("message : "+message);
		}
		//System.out.println("addr : "+req.getRemoteAddr());
		//System.out.println("host : "+req.getRemoteHost());
		return map;
	}

}
