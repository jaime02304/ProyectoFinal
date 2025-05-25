package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.Dto.eliminarElementoPerfilDto;
import edu.ProyectoFinal.servicios.PerfilServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase que recoge al usuario o al grupo de la vista y lo manda a la api para
 * que sea borrado
 * 
 * @author jpribio - 15/02/25
 */
@WebServlet("/EliminarElementosComoAdmin")
@MultipartConfig
public class EliminarElemenetoPerfilControlador extends HttpServlet {
	long serialVersionUID = 1L;
	PerfilServicio servicioPerfil = new PerfilServicio();

	/**
	 * Metodo que recoge al usuario o grupo de la vista y lo manda a la api para que
	 * sea borrado
	 * 
	 * @author jpribio - 15/02/25
	 * @param elemento
	 * @param sesion
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
		eliminarElementoPerfilDto dto = new eliminarElementoPerfilDto();
		dto.setElementoEliminar(request.getParameter("elementoEliminar"));
		dto.setIdElementoEliminar(Long.parseLong(request.getParameter("idElementoEliminar")));
		dto.setEsUsuarioEliminar(Boolean.parseBoolean(request.getParameter("esUsuarioEliminar")));
		boolean success = servicioPerfil.eliminarElemento(dto);
		if (success) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("");
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("");
		}
	}

}
