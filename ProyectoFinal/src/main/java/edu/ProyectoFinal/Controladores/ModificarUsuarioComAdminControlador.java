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
 * Clase que manda el usuario seleccionado a modificar hacia la api y lo
 * modifica
 * 
 * @author jpribio - 15/02/25
 */
@WebServlet("/ModificarUsuarioComoAdmin")
@MultipartConfig
public class ModificarUsuarioComAdminControlador extends HttpServlet {
	long serialVersionUID = 1L;
	PerfilServicio servicioPerfil = new PerfilServicio();

	/**
	 * Metodo que manda el usuario seleccionado a modificar hacia la api y lo
	 * modifica
	 * 
	 * @author jpribio - 15/02/25
	 * @param usuarioAModificar
	 * @param sesion
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Validar sesión
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("Usuario") == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		UsuarioPerfilDto dto = new UsuarioPerfilDto();
		dto.setIdUsu(Long.parseLong(request.getParameter("idUsu")));
		dto.setNombreCompletoUsu(request.getParameter("nombreCompletoUsu"));
		dto.setAliasUsu(request.getParameter("aliasUsu"));
		dto.setCorreoElectronicoUsu(request.getParameter("correoElectronicoUsu"));
		dto.setMovilUsu(Integer.parseInt(request.getParameter("movilUsu")));
		dto.setEsPremium(Boolean.parseBoolean(request.getParameter("esPremium")));
		dto.setRolUsu(request.getParameter("rolUsu"));
		dto.setEsVerificadoEntidad(Boolean.parseBoolean(request.getParameter("esVerificadoEntidad")));
		String fotoString = request.getParameter("fotoString");
		if (fotoString != null)
			dto.setFotoString(fotoString);
		boolean success = servicioPerfil.enviarUsuarioAModificarComoAdmin(dto, session);
		response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
		response.getWriter().write("");
	}
}
