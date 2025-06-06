package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.GrupoCompletoDto;
import edu.ProyectoFinal.Dto.GrupoEspecificadoDto;
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
 * Clase que muestra la pagina de grupos
 * 
 * @author jpribio - 07/05/25
 * @param sesionIniciada
 * @param redirectAttrs
 * @return
 */
@WebServlet("/PaginaGrupo")
public class GrupoControlador extends HttpServlet {
	private static final SesionLogger logger = new SesionLogger(GrupoControlador.class);

	GruposServicios servicioDeGrupos = new GruposServicios();

	/**
	 * MEtodo que muestra la pagina de grupos
	 * 
	 * @author jpribio - 07/05/25
	 * @param sesionIniciada
	 * @param redirectAttrs
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
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}
		logger.info("Cargando la vista de grupos");
		try {
			List<GrupoCompletoDto> listaGrupos = servicioDeGrupos.obtenerGrupos();
			request.setAttribute("listadoGruposTotales", listaGrupos);
			if (listaGrupos.isEmpty()) {
				request.setAttribute("mensajeGrupo", "No se encontraron grupos disponibles.");
			}
			request.getRequestDispatcher("/GrupoPagina.jsp").forward(request, response);
		} catch (Exception e) {
			logger.error("Error al cargar la página de grupos: " + e.getMessage());
			request.setAttribute("error", "Error al cargar los grupos.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}
}
