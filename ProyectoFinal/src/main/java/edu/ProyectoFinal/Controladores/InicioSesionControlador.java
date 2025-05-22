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

	/**
	 * Metodo para poder poner una nueva contraseña para el usuario del correo
	 * electronico
	 * 
	 * @author jpribio - 21/04/25
	 * @param correo
	 * @return
	 */
	@PostMapping("/RecuperarContrasena")
	public ResponseEntity<String> recuperarContrasena(@RequestParam("correoElectronicoUsu") String correo) {
		if (correo == null || correo.trim().isEmpty()) {
			logger.warn("Error en recuperación de contraseña: correo inválido o vacío.");
			return ResponseEntity.badRequest().body("Correo inválido.");
		}
		boolean enviado = servicioDeInicioDeSesion.enviarCorreoRecuperacion(correo);
		if (enviado) {
			return ResponseEntity.ok("Correo enviado con éxito.");
		} else {
			logger.warn("Fallo al enviar correo de recuperación para: {}" + correo);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo.");
		}
	}

	/**
	 * Metodo para poder cambiar la contraseña pòr una nueva
	 * 
	 * @author jpribio - 21/04/25
	 * @param nuevaContrasena
	 * @param token
	 * @return
	 */
	@PostMapping("/CambiarContrasena")
	public ResponseEntity<String> cambiarContrasenaWeb(@RequestParam("nuevaContrasena") String nuevaContrasena,
			@RequestParam("token") String token) {

		boolean exitoso = servicioDeInicioDeSesion.realizarCambioContrasena(token, nuevaContrasena);

		if (exitoso) {
			return ResponseEntity.ok("Contraseña cambiada correctamente.");
		} else {
			logger.warn("Error al cambiar contraseña con token: {}" + token);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo cambiar la contraseña.");
		}
	}

	/**
	 * MEtodo que crea la url con el codigo y la manda al metodo de a continuacion
	 * 
	 * @author jpribio - 26/04/25
	 * @return
	 */
	@GetMapping("/loginGoogle")
	public ModelAndView loginGoogle() {
		String url = "https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + OAuthConfiguracion.CLIENT_ID
				+ "&redirect_uri=" + OAuthConfiguracion.REDIRECT_URI + "&response_type=code"
				+ "&scope=openid%20email%20profile";
		return new ModelAndView("redirect:" + url);
	}

	/**
	 * Metodo que llamna al servicio del nuevo usuario con el codigo y la sesion
	 * 
	 * @author jpribio - 26/04/25
	 * @param code
	 * @param session
	 * @return
	 */
	@GetMapping("/LoginGoogle")
	public ModelAndView loginGoogleCallback(@RequestParam("code") String code, HttpSession session) {
		return servicioDeInicioDeSesion.nuevoUsuarioGoogle(code, session);
	}

}
