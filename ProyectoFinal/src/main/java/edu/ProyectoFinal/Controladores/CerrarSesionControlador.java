package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.util.List;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.ComentariosIndexDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.servicios.ComentariosServicios;
import edu.ProyectoFinal.servicios.GruposServicios;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase de cerrar la sesion del usuario
 * 
 * @author jpribio - 06/02/25
 * @param cerrarSesion
 * @return
 */
@WebServlet("/CerrarSesion")
public class CerrarSesionControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(CerrarSesionControlador.class);

	private static final long serialVersionUID = 1L;

	GruposServicios serviciosGrupos = new GruposServicios();

	ComentariosServicios servicioComentario = new ComentariosServicios();

	/**
	 * Metodo de cerrar la sesion del usuario
	 * 
	 * @author jpribio - 06/02/25
	 * @param cerrarSesion
	 * @return
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("Se va a lanzar la ejecucion de cerrar sesion");
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}

			List<GruposListadoDto> listaGrupos = serviciosGrupos.obtenerLosGruposTops();
			List<ComentariosIndexDto> listaComentarios = servicioComentario.recogidaDeComentariosIndex();

			request.setAttribute("listaGrupos", listaGrupos);
			if (listaGrupos.isEmpty()) {
				request.setAttribute("mensajeGrupo", "No se encontraron grupos disponibles.");
			}
			request.setAttribute("listaComentariosIndex", listaComentarios);

			request.getRequestDispatcher("/LandinPage.jsp").forward(request, response);
		} catch (Exception e) {
			logger.error("Error al cerrar sesion" + e);
			request.setAttribute("error", "Error al cargar la página inicial.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}

}
