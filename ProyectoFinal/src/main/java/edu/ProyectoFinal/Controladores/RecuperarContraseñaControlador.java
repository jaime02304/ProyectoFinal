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
 * Clase para poder poner una nueva contraseña para el usuario del correo
 * electronico
 * 
 * @author jpribio - 21/04/25
 * @param correo
 * @return
 */
@WebServlet("/RecuperarContrasena")
@MultipartConfig
public class RecuperarContraseñaControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(RecuperarContraseñaControlador.class);
	private final InicioSesionServicio servicioInicioSesion = new InicioSesionServicio();

	/**
	 * Metodo para poder poner una nueva contraseña para el usuario del correo
	 * electronico
	 * 
	 * @author jpribio - 21/04/25
	 * @param correo
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String correo = request.getParameter("correoElectronicoUsu");
		if (correo == null || correo.trim().isEmpty()) {
			logger.warn("Error en recuperación de contraseña: correo inválido o vacío.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			try (PrintWriter out = response.getWriter()) {
				out.write("Correo inválido.");
			}
			return;
		}

		boolean enviado = servicioInicioSesion.enviarCorreoRecuperacion(correo);
		if (enviado) {
			response.setStatus(HttpServletResponse.SC_OK);
			try (PrintWriter out = response.getWriter()) {
				out.write("Correo enviado con éxito.");
			}
		} else {
			logger.warn("Fallo al enviar correo de recuperación para: " + correo);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try (PrintWriter out = response.getWriter()) {
				out.write("Error al enviar el correo.");
			}
		}
	}
}
