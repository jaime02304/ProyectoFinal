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
 * Clase para poder suscriburme a un grupo
 * 
 * @author jpribio - 07/05/25
 */
@WebServlet("/UnirseAlGrupo")
public class SuscripcionGrupos extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(SuscripcionGrupos.class);
	GruposServicios servicioDeGrupos = new GruposServicios();

	/**
	 * Metodo para poder suscriburme a un grupo
	 * 
	 * @author jpribio - 07/05/25
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UsuarioPerfilDto usuario = session != null ? (UsuarioPerfilDto) session.getAttribute("Usuario") : null;

		if (usuario == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		SuscripcionDto suscripcion = new SuscripcionDto();
		suscripcion.setAliasUsuario(usuario.getAliasUsu());
		suscripcion.setNombreGrupo(request.getParameter("nombreGrupo"));
		logger.info("Enviando solicitud de suscripción al grupo: " + suscripcion.getNombreGrupo());
		boolean success = servicioDeGrupos.enviarSuscripcion(suscripcion);
		if (success) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("");
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("");
		}
	}
}
