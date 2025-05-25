package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.SuscripcionDto;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.servicios.GruposServicios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase donde el usuario realiza la baja de un grupo
 * 
 * @author jpribio - 24/05/25
 */
@WebServlet("/EliminarDelGrupo")
public class EliminarSuscripcionGrupoControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(EliminarSuscripcionGrupoControlador.class);

	GruposServicios servicioDeGrupos = new GruposServicios();

	/**
	 * Metodo donde el usuario realiza la baja de un grupo
	 * 
	 * @author jpribio - 24/05/25
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			logger.info("Inicializacion de eliminar la suscripcion del grupo");
			HttpSession session = request.getSession(false);
			UsuarioPerfilDto usuario = session != null ? (UsuarioPerfilDto) session.getAttribute("Usuario") : null;

			if (usuario == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			SuscripcionDto suscripcion = new SuscripcionDto();
			suscripcion.setAliasUsuario(usuario.getAliasUsu());
			suscripcion.setNombreGrupo(request.getParameter("nombreGrupo"));

			logger.info("Enviando solicitud para eliminar suscripción del grupo: " + suscripcion.getNombreGrupo());

			boolean success = servicioDeGrupos.enviarEliminacionSuscripcion(suscripcion);
			if (success) {
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("");
			} else {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("");
			}
		} catch (Exception e) {
			logger.error("Error al eliminar una suscripcion de un grupo" + e);
			request.setAttribute("error", "Error al eliminar una suscripcion de un grupo");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}
}
