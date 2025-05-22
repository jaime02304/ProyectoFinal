package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.util.List;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.ComentariosIndexDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.Dto.UsuarioRegistroDto;
import edu.ProyectoFinal.servicios.ComentariosServicios;
import edu.ProyectoFinal.servicios.GruposServicios;
import edu.ProyectoFinal.servicios.InicioSesionServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase que inicia sesion
 * 
 * @author jpribio - 27/01/25
 * @return
 */
@WebServlet("/IS")
public class InicioSesionAccionControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(InicioSesionControlador.class);

	private final InicioSesionServicio servicioInicioSesion = new InicioSesionServicio();
	private final GruposServicios servicioGrupos = new GruposServicios();
	private final ComentariosServicios servicioComentarios = new ComentariosServicios();

	/**
	 * Metodo que inicia sesion
	 * 
	 * @author jpribio - 27/01/25
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. Construir DTO con datos del formulario
		UsuarioRegistroDto usuario = new UsuarioRegistroDto();
		usuario.setCorreoElectronicoUsu(request.getParameter("correoElectronicoUsu"));
		usuario.setContraseniaUsu(request.getParameter("contraseniaUsu"));

		try {
			logger.info("Intentando iniciar sesión para el usuario: " + usuario.getCorreoElectronicoUsu());

			boolean exito = servicioInicioSesion.autenticarUsuario(usuario, request.getSession(), request);

			if (exito) {
				List<GruposListadoDto> listaGrupos = servicioGrupos.obtenerLosGruposTops();
				List<ComentariosIndexDto> listaComentarios = servicioComentarios.recogidaDeComentariosIndex();
				request.setAttribute("listaGrupos", listaGrupos);
				request.setAttribute("listaComentariosIndex", listaComentarios);
				request.getRequestDispatcher("/LandinPage.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("/InicioSesion.jsp").forward(request, response);
			}

		} catch (Exception e) {
			logger.error("Error al iniciar sesión para el usuario: " + usuario.getCorreoElectronicoUsu());
			e.printStackTrace();
			request.setAttribute("error", "Error al iniciar sesión. Inténtelo de nuevo.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}
}
