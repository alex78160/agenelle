package com.genelle.alexandre.server;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DlServlet extends HttpServlet {
	
	private static final long serialVersionUID = -9035524403343290796L;
	private String filePath;
	private String file;
	
	public void init() {
        // the file data.xls is under web application folder
        //filePath = getServletContext().getRealPath("") + File.separator + "data.txt";
    }
	private static final int BUFSIZE = 4096;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("DlServlet - doGet");
		String indexStr = req.getParameter("index");
		short index = 0;
		try {
			index = Short.parseShort(indexStr);
		}
		catch(Exception e) {
			e.printStackTrace();
			processError(resp);
		}
		
		System.out.println("index = "+index);
		
		switch(index) {
			case 1:
				file = "data.txt";
				break;
			case 3:
				file = "CV_Alexandre_Genelle.pdf";
				break;
			default:
				processError(resp);
				return;
					
		}
		
		filePath = getServletContext().getRealPath("") + File.separator + file;
		
		InputStream inputStream = getServletContext().getResourceAsStream("/WEB-INF/files/"+file);
		//inputStream.toString();
		
        int length   = 0;
        ServletOutputStream outStream = resp.getOutputStream();
        ServletContext context  = getServletConfig().getServletContext();
        String mimetype = context.getMimeType(filePath);
        System.out.println("mime = "+mimetype);
        
        // sets response content type
        if (mimetype == null) {
            mimetype = "application/octet-stream";
        }
        resp.setContentType(mimetype);
        //resp.setContentLength((int)file.length());
        String fileName = (new File(filePath)).getName();
        
        // sets HTTP header
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(inputStream);
        
        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1))
        {
            outStream.write(byteBuffer,0,length);
        }
        
        in.close();
        outStream.close();
	}
	private void processError(HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/html");
	    PrintWriter out = resp.getWriter();

	    out.println("<html>");
	    out.println("<head>");
	    out.println("<title>Erreur</title>");
	    out.println("</head>");
	    out.println("<body bgcolor=\"white\">");
	    out.println("<h1 style=\"color:red;\">Erreur</h1>");
	    out.println("</body>");
	    out.println("</html>");
	    
	    out.flush();out.close();
	    return;
	}

}
