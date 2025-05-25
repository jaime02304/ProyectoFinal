package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.servicios.PerfilServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase que modifica un grupo segun los nuevos elementos dados en la vista
 * 
 * @author jpribio - 15/02/25
 */
@WebServlet("/ModificarGrupoComoAdmin")
@MultipartConfig
public class GrupoAModifiarComoAdminControlador extends HttpServlet {
	long serialVersionUID = 1L;
	PerfilServicio servicioPerfil = new PerfilServicio();
	private static final SesionLogger logger = new SesionLogger(GrupoAModifiarComoAdminControlador.class);

	/**
	 * Metodo que modifica un grupo segun los nuevos elementos dados en la vista
	 * 
	 * @author jpribio - 15/02/25
	 * @param usuarioAModificar
	 * @param sesion
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("Inicializacion de la modificacion de un grupo");
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("Usuario") == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			GruposListadoDto dto = new GruposListadoDto();
			dto.setIdGrupo(Long.parseLong(request.getParameter("idGrupo")));
			dto.setNombreGrupo(request.getParameter("nombreGrupo"));
			dto.setCategoriaNombre(request.getParameter("categoriaNombre"));
			dto.setSubCategoriaNombre(request.getParameter("subCategoriaNombre"));
			dto.setDescripcionGrupo(request.getParameter("descripcionGrupo"));
			boolean success = servicioPerfil.enviarGrupoAModificarComoAdmin(dto);
			response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("");
		} catch (Exception e) {
			logger.error("Error de la modificacion de un grupo" + e);
			request.setAttribute("error", "Error al  modificar un grupo");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}

}
