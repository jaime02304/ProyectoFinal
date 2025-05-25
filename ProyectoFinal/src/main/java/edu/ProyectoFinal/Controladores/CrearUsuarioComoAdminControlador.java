package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.servicios.PerfilServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase que coge el usuario de la web y lo manda hacia la api
 * 
 * @author jptibio - 16/02/25
 */
@WebServlet("/CrearUsuarioComoAdmin")
@MultipartConfig
public class CrearUsuarioComoAdminControlador extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PerfilServicio servicioPerfil = new PerfilServicio();

	/**
	 * metodo que coge el usuario de la web y lo manda hacia la api
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
		UsuarioPerfilDto dto = new UsuarioPerfilDto();
		dto.setNombreCompletoUsu(request.getParameter("nombreCompletoUsu"));
		dto.setAliasUsu(request.getParameter("aliasUsu"));
		dto.setCorreoElectronicoUsu(request.getParameter("correoElectronicoUsu"));
		dto.setMovilUsu(Integer.parseInt(request.getParameter("movilUsu")));
		dto.setEsPremium(Boolean.parseBoolean(request.getParameter("esPremium")));
		dto.setEsVerificadoEntidad(Boolean.parseBoolean(request.getParameter("esVerificadoEntidad")));
		dto.setRolUsu(request.getParameter("rolUsu"));
		String fotoString = request.getParameter("fotoString");
		if (fotoString != null)
			dto.setFotoString(fotoString);
		boolean success = servicioPerfil.crearUsuarioComoAdmin(dto);
		response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write("");
	}

}
