package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import edu.ProyectoFinal.Configuraciones.OAuthConfiguracion;
import edu.ProyectoFinal.Configuraciones.SesionLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase que crea la url con el codigo y la manda al metodo de a continuacion
 * 
 * @author jpribio - 26/04/25
 * @return
 */
@WebServlet("/loginGoogle")
public class LoginGoogleControlador extends HttpServlet {
	private static final SesionLogger logger = new SesionLogger(LoginGoogleControlador.class);

	private static final long serialVersionUID = 1L;

	/**
	 * Metodo que crea la url con el codigo y la manda al metodo de a continuacion
	 * 
	 * @author jpribio - 26/04/25
	 * @return
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String url = "https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + OAuthConfiguracion.CLIENT_ID
					+ "&redirect_uri=" + OAuthConfiguracion.REDIRECT_URI + "&response_type=code"
					+ "&scope=openid%20email%20profile";

			response.sendRedirect(url);
		} catch (Exception e) {
			logger.warn("Error al obtener los datos del Login de google " + e.getMessage());
		}
	}

}
