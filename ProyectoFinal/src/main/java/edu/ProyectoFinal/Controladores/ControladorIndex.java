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

/**
 * Controlador de la vista principal
 * 
 * @author jpribio - 23/01/25
 */
@WebServlet(value = "/LandinPage/*")
public class ControladorIndex extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(ControladorIndex.class);

	GruposServicios serviciosGrupos = new GruposServicios();

	ComentariosServicios servicioComentario = new ComentariosServicios();

	/**
	 * Metodo que muestra la vista y objetos de la misma
	 * 
	 * @return devuelve la vista y los metodos equivalentes
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<GruposListadoDto> listaGrupos = serviciosGrupos.obtenerLosGruposTops();
			List<ComentariosIndexDto> listaComentarios = servicioComentario.recogidaDeComentariosIndex(); 
			request.setAttribute("listaGrupos", listaGrupos);

			if (listaGrupos.isEmpty()) {
				request.setAttribute("mensajeGrupo", "No se encontraron grupos disponibles.");
			}

			request.setAttribute("listaComentariosIndex", listaComentarios);

			request.getRequestDispatcher("/LandinPage.jsp").forward(request, response);

		} catch (Exception e) {
			logger.error("Error al cargar la página inicial.\n" + e);
			request.setAttribute("error", "Error al cargar la página inicial.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}

}
