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
 * Clase que registra a un nuevo sesion
 * 
 * @author jpribio - 27/01/25
 * @return
 */
@WebServlet("/Registro")
public class ReginstroUsuarioControlador extends HttpServlet {

	private static final SesionLogger logger = new SesionLogger(ReginstroUsuarioControlador.class);
	private InicioSesionServicio servicioDeInicioDeSesion = new InicioSesionServicio();
	private final GruposServicios servicioGrupos = new GruposServicios();
	private final ComentariosServicios servicioComentarios = new ComentariosServicios();

	/**
	 * Metodo que registra a un nuevo sesion
	 * 
	 * @author jpribio - 27/01/25
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. Construcción del DTO a partir de los parámetros del formulario
		UsuarioRegistroDto nuevoUsuario = new UsuarioRegistroDto();
		nuevoUsuario.setNombreCompletoUsu(request.getParameter("nombreCompletoUsu"));
		nuevoUsuario.setAliasUsu(request.getParameter("aliasUsu"));
		nuevoUsuario.setCorreoElectronicoUsu(request.getParameter("correoElectronicoUsu"));
		nuevoUsuario.setContraseniaUsu(request.getParameter("contraseniaUsu"));

		try {
			logger.info("Intentando registrar el usuario: " + nuevoUsuario.getCorreoElectronicoUsu());

			boolean exito = servicioDeInicioDeSesion.registrarUsuario(nuevoUsuario, request.getSession());

			if (exito) {
				List<GruposListadoDto> listaGrupos = servicioGrupos.obtenerLosGruposTops();
				List<ComentariosIndexDto> listaComentarios = servicioComentarios.recogidaDeComentariosIndex();
				request.setAttribute("listaGrupos", listaGrupos);
				request.setAttribute("listaComentariosIndex", listaComentarios);
				request.setAttribute("infoVerificacion",
						"Registro completado con éxito. Por favor, revisa tu correo electrónico para verificar tu cuenta. Tras hacerlo cierre sesion en la página de perfil y vuelva a iniciar de nuevo para desbloquear toda la página web.");
				request.getRequestDispatcher("/LandinPage.jsp").forward(request, response);

			} else {
				request.setAttribute("nuevoUsuario", nuevoUsuario);
				request.setAttribute("error", request.getAttribute("error"));
				request.getRequestDispatcher("/registro.jsp").forward(request, response);
			}

		} catch (Exception e) {
			logger.error("Error en el registro de usuario: " + nuevoUsuario.getCorreoElectronicoUsu());
			e.printStackTrace();
			request.setAttribute("error", "Ocurrió un problema inesperado. Inténtelo más tarde.");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}

}
