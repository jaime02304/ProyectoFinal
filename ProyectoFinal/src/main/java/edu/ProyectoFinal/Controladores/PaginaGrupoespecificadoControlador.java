package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.GrupoEspecificadoDto;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.servicios.GruposServicios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase que recoge toda la informacion del grupo especificado
 * 
 * @author jpribio - 07/05/25
 */
@WebServlet("/PaginaGrupoEspecificado")
public class PaginaGrupoespecificadoControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(PaginaGrupoespecificadoControlador.class);
	GruposServicios servicioDeGrupos = new GruposServicios();

	/**
	 * Metodo que recoge toda la informacion del grupo especificado
	 * 
	 * @author jpribio - 07/05/25
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nombreGrupo = request.getParameter("nombreGrupo");
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
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}

		logger.info("Cargando la vista de grupo especificado: " + nombreGrupo);
		try {
			GrupoEspecificadoDto dto = servicioDeGrupos.obtenerGrupoEspecifico(nombreGrupo);
			if (dto == null || dto.getNombreGrupo() == null) {
				request.setAttribute("error", "No se encontró el grupo especificado.");
				request.getRequestDispatcher("/error.jsp").forward(request, response);
				return;
			}
			request.setAttribute("grupoEspecificado", dto);
			request.getRequestDispatcher("/GrupoEspecifico.jsp").forward(request, response);

		} catch (Exception e) {
			logger.error("Error al cargar la página del grupo especificado: " + e.getMessage());
			request.setAttribute("error", "Error al conectar con la API: " + e.getMessage());
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}
}
