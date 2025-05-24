package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.util.List;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.ComentariosDTO;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.servicios.ComentariosServicios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Pagina controladora de la parte de los comentarios
 * 
 * @author jpribio - 30/04/25
 */
@WebServlet("/ComentarioPagina")
public class ComentariosControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(ComentariosControlador.class);

	ComentariosServicios servicioComentarios = new ComentariosServicios();

	/**
	 * Metodo para mostrar la pagina de comentarios al completo
	 * 
	 * @author jpribio - 30/04/25
	 * @param sesionIniciada
	 * @return
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		UsuarioPerfilDto usuario = session != null ? (UsuarioPerfilDto) session.getAttribute("Usuario") : null;

		if (session == null || usuario == null) {
			request.setAttribute("error",
					"No se ha detectado un usuario activo. Por favor, inicie sesión antes de continuar.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}
		if (!usuario.getEsVerificadoEntidad()) {
			session.setAttribute("infoVerificacion",
					"No se ha detectado un usuario verificado. Por favor, debe verificarse antes de continuar.");
			response.sendRedirect(request.getContextPath() + "/LandinPage");
			return;
		}

		logger.info("Cargando la vista de comentarios");
		try {
			List<ComentariosDTO> listaComentarios = servicioComentarios.obtenerComentarios();

			request.setAttribute("listadoComentarios", listaComentarios);
			if (listaComentarios.isEmpty()) {
				request.setAttribute("mensajeGrupo", "No se encontraron comentarios disponibles.");
			}

			request.getRequestDispatcher("/ComentarioPagina.jsp").forward(request, response);

		} catch (Exception e) {
			logger.error("Error al cargar la página de comentarios: " + e.getMessage());
			request.setAttribute("error", "Error al cargar los comentarios.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}

}
