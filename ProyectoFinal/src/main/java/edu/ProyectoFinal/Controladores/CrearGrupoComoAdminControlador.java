package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import edu.ProyectoFinal.Dto.GruposDto;
import edu.ProyectoFinal.servicios.PerfilServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase que coge el grupo de la web y lo manda hacia la api
 * 
 * @author jptibio - 16/02/25
 */
@WebServlet("/CrearGrupoComoAdmin")
@MultipartConfig
public class CrearGrupoComoAdminControlador extends HttpServlet {

	private static final long serialVersionUID = 1L;
	PerfilServicio servicioPerfil = new PerfilServicio();

	/**
	 * Metodo que coge el grupo de la web y lo manda hacia la api
	 * 
	 * @author jptibio - 16/02/25
	 * @param usuarioCreado
	 * @param sesion
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("Usuario") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		GruposDto dto = new GruposDto();
		dto.setNombreGrupo(request.getParameter("nombreGrupo"));
		dto.setCategoriaNombre(request.getParameter("categoriaNombre"));
		dto.setSubCategoriaNombre(request.getParameter("subCategoriaNombre"));
		dto.setAliasCreadorUString(request.getParameter("aliasCreadorUString"));
		dto.setDescripcionGrupo(request.getParameter("descripcionGrupo"));
		boolean success = servicioPerfil.crearGrupoComoAdmin(dto);

		response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write("");
	}

}
