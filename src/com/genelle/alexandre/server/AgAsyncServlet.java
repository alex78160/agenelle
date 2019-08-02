package com.genelle.alexandre.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;





public class AgAsyncServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2643957818773697107L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		System.out.println("AgAsyncServlet - doGet");
		try {
			System.out.println("id = "+req.getParameter("id"));
			System.out.println("format = "+req.getParameter("format"));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		JSONObject jso = new JSONObject();
		
		try {
			resp.setContentType("application/json");
			jso.put("lib", "Salut !");
		}
		catch(Exception e) {
			e.printStackTrace();
			jso.put("lib", "erreur");
		}
		finally {
			resp.getWriter().print(jso.toString());
			resp.flushBuffer();
		}
		
		
	}

}
