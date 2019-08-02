package com.genelle.alexandre.server.filters;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


/**
 * This class implements a filter to limit the number of calls to the servlets
 * that match the pattern defined in web.xml
 * 
 * @author alexandre.genelle
 * 
 */
public class LimitFilter implements Filter {

	private static final int DEFAULT_INTERVAL = 3000;
	private static final Logger LOG = Logger.getLogger(LimitFilter.class
			.getName());
	private Date last = null;
	private String intervalStr = null;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// Level l = LOG.getLevel();
		LOG.fine("LimitFilter.doFilter - start");
		//((HttpServletResponse)response).setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		httpServletResponse.setHeader("Cache-Control", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setHeader("Pragma", "No-cache");
		
		int interval = DEFAULT_INTERVAL;

		try {
			interval = Integer.valueOf(intervalStr);
		} catch (Exception e) {
			LOG.warning("LimitFilter.doFilter - interval param : "
					+ intervalStr
					+ " ne peut pas être converti en nombre, on prend donc une valeur par défaut : "
					+ DEFAULT_INTERVAL + "ms");
		}

		LOG.fine("LimitFilter.doFilter - intervalle entre 2 requêtes : "
				+ interval + "millisecondes");

		// on récupère la date courante
		Date now = new Date();

		// si il y a eu une requête avant
		if (last != null) {
			// si la requête est trop récente on ne la propage pas jusqu'à la servlet
			if (now.getTime() - last.getTime() < interval) {
				LOG.warning("LimitFilter.doFilter - la dernière requête a eu lieu il y moins de "
						+ interval + "ms : rejet !");
				// on répond un json d'erreur
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("status", "ko");
				jsonResponse.put("cause", "timeout");
				response.getWriter().write(jsonResponse.toString());
				return;
			} 
			// si l'écart de temps entre cette requête et la précédente est supérieur
			// à l'intervalle paramétré alors c'est bon, on propage la requête
			else {
				LOG.fine("LimitFilter.doFilter - délai OK");
				// on redéfinit la date de la dernière requête
				last = now;
				chain.doFilter(request, response);
			}
		} else {
			last = now;
			LOG.warning("LimitFilter.doFilter - premier appel");
			chain.doFilter(request, response);
		}

		LOG.fine("LimitFilter.doFilter - end");

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		intervalStr = filterConfig.getInitParameter("interval");
		LOG.fine("LimitFilter - init - interval = " + intervalStr);
	}
}