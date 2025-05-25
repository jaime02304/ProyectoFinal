package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.ComentariosPerfilDto;
import edu.ProyectoFinal.Dto.GruposDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.Dto.eliminarElementoPerfilDto;
import edu.ProyectoFinal.servicios.GruposServicios;
import edu.ProyectoFinal.servicios.PerfilServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador donde se encuentra todos los metodos de la pagina del perfil del
 * mismo usuario y de los administradores
 * 
 * @author jpribio - 30/01/25
 */
@WebServlet("/PerfilUsuario")
public class perfilUsuarioControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(perfilUsuarioControlador.class);

	GruposServicios servicioGrupos = new GruposServicios();

	PerfilServicio servicioPerfil = new PerfilServicio();

	/**
	 * Metodo que muestra la pagina del perfil tanto de los usuarios como la de los
	 * administradores
	 * 
	 * @author jpribio - 06/02/25
	 * @param sesionIniciada
	 * @return
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession sesion = request.getSession(false);
		UsuarioPerfilDto usuario = (UsuarioPerfilDto) (sesion != null ? sesion.getAttribute("Usuario") : null);
		if (sesion == null || usuario == null) {
			request.setAttribute("error",
					"No se ha detectado un usuario activo. Por favor, inicie sesión antes de continuar.");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}
		if (!usuario.getEsVerificadoEntidad()) {
			request.getSession().setAttribute("infoVerificacion",
					"No se ha detectado un usuario verificado. Debe verificarse antes de continuar.");
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}

		try {
			Map<String, Object> datos = servicioPerfil.obtenerDatosPerfil(usuario);
			for (Map.Entry<String, Object> entry : datos.entrySet()) {
				request.setAttribute(entry.getKey(), entry.getValue());
			}

			// Redirigir a la vista correcta
			String vista = (String) datos.getOrDefault("vista", "/perfilUsuario");
			request.getRequestDispatcher(vista + ".jsp").forward(request, response);

		} catch (Exception e) {
			request.setAttribute("error", "Se produjo un error al intentar cargar el perfil del usuario.");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

}