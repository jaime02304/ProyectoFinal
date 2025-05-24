package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.io.PrintWriter;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.servicios.InicioSesionServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase para poder cambiar la contraseña pòr una nueva
 * 
 * @author jpribio - 21/04/25
 * @param nuevaContrasena
 * @param token
 * @return
 */
@WebServlet("/CambiarContrasena")
@MultipartConfig
public class CambiarContraseniaControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(CambiarContraseniaControlador.class);
	InicioSesionServicio servicioDeInicioDeSesion = new InicioSesionServicio();

	/**
	 * Metodo para poder cambiar la contraseña pòr una nueva
	 * 
	 * @author jpribio - 21/04/25
	 * @param nuevaContrasena
	 * @param token
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String token = request.getParameter("token");
		String nuevaContrasena = request.getParameter("nuevaContrasena");
		if (token == null || token.trim().isEmpty() || nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
			logger.warn("Parámetros inválidos en cambio de contraseña. Token o nueva contraseña vacíos.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			try (PrintWriter out = response.getWriter()) {
				out.write("Token o nueva contraseña inválidos.");
			}
			return;
		}
		boolean exitoso = servicioDeInicioDeSesion.realizarCambioContrasena(token, nuevaContrasena);
		if (exitoso) {
			response.setStatus(HttpServletResponse.SC_OK);
			try (PrintWriter out = response.getWriter()) {
				out.write("Contraseña cambiada correctamente.");
			}
		} else {
			logger.warn("Error al cambiar contraseña con token: " + token);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			try (PrintWriter out = response.getWriter()) {
				out.write("No se pudo cambiar la contraseña.");
			}
		}
	}
}
