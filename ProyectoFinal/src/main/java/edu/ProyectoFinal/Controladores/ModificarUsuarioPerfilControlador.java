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
 * Clase que modifica al usuario con los valores incorporados
 *
 * @author jpribio - 11/02/25
 */
@WebServlet("/ModificarUsuario")
@MultipartConfig
public class ModificarUsuarioPerfilControlador extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PerfilServicio servicioPerfil = new PerfilServicio();

	/**
	 * Metodo que modifica al usuario con los valores incorporados
	 *
	 * @author jpribio - 11/02/25
	 * @param usuarioAModificar
	 * @param sesionDelUsuario
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		UsuarioPerfilDto usuarioSesion = session != null ? (UsuarioPerfilDto) session.getAttribute("Usuario") : null;
		if (usuarioSesion == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		UsuarioPerfilDto usuarioDto = new UsuarioPerfilDto();
		usuarioDto.setAliasUsu(request.getParameter("aliasUsu"));
		usuarioDto.setNombreCompletoUsu(request.getParameter("nombreCompletoUsu"));
		usuarioDto.setMovilUsu(Integer.parseInt(request.getParameter("movilUsu")));
		usuarioDto.setFotoString(request.getParameter("fotoString"));
		UsuarioPerfilDto updated = servicioPerfil.modificarUsuario(usuarioDto, usuarioSesion);
		if (updated != null) {
			session.setAttribute("Usuario", updated);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("");
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("");
		}
	}
}
