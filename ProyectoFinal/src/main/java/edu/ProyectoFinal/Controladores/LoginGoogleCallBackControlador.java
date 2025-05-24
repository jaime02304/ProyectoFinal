package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.util.List;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.ComentariosIndexDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.servicios.ComentariosServicios;
import edu.ProyectoFinal.servicios.GruposServicios;
import edu.ProyectoFinal.servicios.InicioSesionServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase que llamna al servicio del nuevo usuario con el codigo y la sesion
 * 
 * @author jpribio - 26/04/25
 * @param code
 * @param session
 * @return
 */
@WebServlet("/LoginGoogle")
public class LoginGoogleCallBackControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(InicioSesionControlador.class);

	InicioSesionServicio servicioDeInicioDeSesion = new InicioSesionServicio();

	GruposServicios servicioGrupos = new GruposServicios();

	ComentariosServicios servicioComentarios = new ComentariosServicios();

	/**
	 * Clase que llamna al servicio del nuevo usuario con el codigo y la sesion
	 * 
	 * @author jpribio - 26/04/25
	 * @param code
	 * @param session
	 * @return
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String code = request.getParameter("code");
		if (code == null || code.isEmpty()) {
			request.setAttribute("error", "No se recibió el código de autorización de Google.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}

		HttpSession session = request.getSession();
		boolean exito = servicioDeInicioDeSesion.nuevoUsuarioGoogle(code, request, session);

		if (exito) {
			if (!cargarDatosLanding(request)) {
				forwardError(request, response, "Error cargando datos de la página principal.");
				return;
			}
			request.getRequestDispatcher("/LandinPage.jsp").forward(request, response);
		} else {
			forwardError(request, response, null);
		}
	}

	private boolean cargarDatosLanding(HttpServletRequest request) {
		try {
			List<GruposListadoDto> listaGrupos = servicioGrupos.obtenerLosGruposTops();
			List<ComentariosIndexDto> listaComentarios = servicioComentarios.recogidaDeComentariosIndex();
			request.setAttribute("listaGrupos", listaGrupos);
			request.setAttribute("listaComentariosIndex", listaComentarios);
			return true;
		} catch (Exception e) {
			logger.warn("Error al obtener datos para LandinPage: " + e.getMessage());
			return false;
		}
	}

	private void forwardError(HttpServletRequest request, HttpServletResponse response, String mensaje)
			throws ServletException, IOException {
		if (mensaje != null) {
			request.setAttribute("error", mensaje);
		}
		request.getRequestDispatcher("/error.jsp").forward(request, response);
	}
}
