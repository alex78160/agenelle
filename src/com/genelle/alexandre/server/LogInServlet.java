package com.genelle.alexandre.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.genelle.alexandre.authenticator.Authenticator.Main;



/**
 * L'idée de cette classe est de fournir un certain niveau de sécurité lors d'authentifications. Il ne s'agit pas à proprement parler d'authentification sur
 * le site en question mais plutôt d'une servlet permettant d'efffecuer des contrôles génériques pour différents cas d'utilisation. Lorsque les cas se
 * présentent, l'application cliente effectuera une requête sur cette servlet et cette dernière renverra un statut ok ou ko en fonction du résultat des
 * contrôles. Exemple de cas d'utilisation : Je souhaite me connecter en ssh sur mon PC perso à partir de l'extérieur. Pour cela, j'ai ouvert un port et
 * paramétré le serveur ssh de la machine hôte. Par mesure de sécurité, je n'autorise pas de user avec privilèges à se connecter en SSH, jsute un user
 * avec droits restreints. Pour pouvoir effectuer des opérations sensibles il faut donc, dans l'ordre : - s'authentifier en SSH avec le user créé à cet
 * effet - faire un su sur le user disposant des privilèges et faisant partie des sudoers Pour la première étape, il suffit de connaître le password du user
 * (qui fait tout de même 50 caractères, chiffres, lettres minuscules et majuscules, caractères spéciaux...) Ensuite, la commande su a été aliasée de
 * façon à ce que l'utilisateur souhaitant faire un su se voie demander de saisir 2 choses : - un mot de passe supplémentaire - un token à 6 chiffres
 * généré par Google Authenticator pour cette application Ces 2 données sont postées sur cette servlet qui compare : - le premier mot de passe avec une
 * clé interne stockée en properties - le 2e token avec un token généré en temps réel Si ces contrôles sont OK, le su est autorisé. L'utilisateur doit
 * donc disposer du mot de passe du user avec privilèges. Ce qui fait une quadruple sécurité : 3 mots de passe et une 2-factor-authentication De plus, un
 * daemon tourne et surveille en permanence les tentatives infructueuses de connections SSH. A partir d'un certain seuil, le serveur est tout simplement
 * arrêté.
 * 
 * @author alexandre.genelle
 *
 */
public class LogInServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7115975835081658649L;
	private static final Logger LOG = Logger.getLogger(LogInServlet.class.getName());

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String key = "";
		String secret = "";
		JSONObject jsonResponse = new JSONObject();

		try {

			Properties props = new Properties();
			try (InputStream in = getServletContext().getResourceAsStream("/props-ignore.properties")) {
				props.load(in);
				in.close();
			}
			key = props.getProperty("alex78160");
			secret = props.getProperty("secret");
			/*
			 * on verrouille tous les objets pour verrouiller les objets on doit les retrouver donc il faut avoir les variables des autres operations
			 */
			String inToken = req.getParameter("token");
			String inputKey = req.getParameter("key");

			// TODO implémenSter un 2e contrôle avec google authenticator
			// 1. modifier la chaine secret et ajouter une entrée g.auth

			// si key ou secret ou un des params requis est null ou vide, erreur
			if (key == null || secret == null || inToken == null || inputKey == null || key.length() == 0 || secret.length() == 0 || inToken.length() == 0
					|| inputKey.length() == 0) {
				LOG.severe("LogInServlet - service - une des cles ou params requis est null ou vide - erreur");
				jsonResponse.put("status", "ko");
			}
			// sinon on poursuit
			else {
				// on logue juste la taille des clés, pas leur contenu par sécurité
				LOG.info("LogInServlet - service - key length : " + key.length());
				LOG.info("LogInServlet - service - secret length : " + secret.length());
				LOG.info("LogInServlet - service - token : " + inToken);
				LOG.info("LogInServlet - service - key : " + key);
				// 1er contrôle : param key <-> valeur d'une clé dans un fichier de props
				if (/*key.equals(inputKey)*/true) {
					LOG.info("LogInServlet - service - ok 1er contrôle");
					// 2e contrôle : token google authenticator <-> token de contrôle généré côté serveur
					String newout = Main.computePin(secret, null);
					if (inToken.equals(newout)) {
						/*
						 * on compare le token à 6 chiffres posté par l'utilidateur au token généré côté serveur à partir de la même clé secrète
						 */
						LOG.info("LogInServlet - service - ok 2e contrôle");
						jsonResponse.put("status", "ok");
					} else {
						LOG.warning("LogInServlet - service - KO 2e contrôle");
						jsonResponse.put("status", "ko");
					}
				} else {
					LOG.warning("LogInServlet - service - KO 1er contrôle");
					jsonResponse.put("status", "ko");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.put("status", "ko");
		} finally {
			resp.setContentType("application/json");
			resp.getWriter().print(jsonResponse.toString());
			resp.flushBuffer();
		}
	}

}
