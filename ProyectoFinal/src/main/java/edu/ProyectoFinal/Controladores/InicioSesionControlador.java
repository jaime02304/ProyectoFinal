package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.ProyectoFinal.Configuraciones.OAuthConfiguracion;
import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.ComentariosIndexDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.Dto.UsuarioRegistroDto;
import edu.ProyectoFinal.servicios.ComentariosServicios;
import edu.ProyectoFinal.servicios.GruposServicios;
import edu.ProyectoFinal.servicios.InicioSesionServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/InicioSesion")
public class InicioSesionControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(InicioSesionControlador.class);

	InicioSesionServicio servicioDeInicioDeSesion = new InicioSesionServicio();

	GruposServicios servicioGrupos = new GruposServicios();

	ComentariosServicios servicioComentarios = new ComentariosServicios();

	/**
	 * Metodo que devuelve la vista del index y da alta el nuevo usuario
	 * 
	 * @author jpribio - 27/01/25
	 * @return
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			HttpSession sesion = request.getSession();
			UsuarioPerfilDto usuario = (UsuarioPerfilDto) sesion.getAttribute("Usuario");

			if (usuario != null) {
				List<GruposListadoDto> listaGrupos = servicioGrupos.obtenerLosGruposTops();
				List<ComentariosIndexDto> listaComentarios = servicioComentarios.recogidaDeComentariosIndex();

				request.setAttribute("listaGrupos", listaGrupos);
				request.setAttribute("listaComentariosIndex", listaComentarios);

				if (listaGrupos.isEmpty()) {
					request.setAttribute("mensajeGrupo", "No se encontraron grupos disponibles.");
				}

				if (listaComentarios.isEmpty()) {
					request.setAttribute("mensajeComentario", "No se encontraron comentarios de bienvenida.");
				}

				logger.info("Usuario ya logueado. Cargando LandinPage con datos.");
				request.getRequestDispatcher("/LandinPage.jsp").forward(request, response);
				return;
			}

			logger.info("Cargando la vista de inicio de sesión.");
			request.getRequestDispatcher("/InicioSesion.jsp").forward(request, response);

		} catch (Exception e) {
			logger.error("Error al cargar la página de inicio de sesión.\n" + e);
			request.setAttribute("error", "Error al cargar la página de inicio.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}
}
