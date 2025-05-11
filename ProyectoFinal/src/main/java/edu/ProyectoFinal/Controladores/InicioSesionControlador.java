package edu.ProyectoFinal.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.ProyectoFinal.Configuraciones.OAuthConfiguracion;
import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.UsuarioRegistroDto;
import edu.ProyectoFinal.servicios.InicioSesionServicio;
import jakarta.servlet.http.HttpSession;

@Controller
public class InicioSesionControlador {

	private static final SesionLogger logger = new SesionLogger(InicioSesionControlador.class);

	InicioSesionServicio servicioDeInicioDeSesion = new InicioSesionServicio();

	/**
	 * Metodo que devuelve la vista del index y da alta el nuevo usuario
	 * 
	 * @author jpribio - 27/01/25
	 * @return
	 */
	@GetMapping("/InicioSesion")
	public ModelAndView InicioSesionVista() {
		try {
			logger.info("Cargando la vista de inicio de sesión");
			ModelAndView vista = new ModelAndView("InicioSesion");
			return vista;
		} catch (Exception e) {
			logger.error("Error al cargar la página de inicio de sesión\n" + e);
			ModelAndView vista = new ModelAndView("InicioSesion");
			vista.addObject("error", "Error al cargar la pagina inicial.");
			vista.setViewName("error");
			return vista;
		}
	}

	/**
	 * Metodo que registra a un nuevo sesion
	 * 
	 * @author jpribio - 27/01/25
	 * @return
	 */
	@PostMapping("/Registro")
	public ModelAndView registroUsuario(@ModelAttribute UsuarioRegistroDto nuevoUsuario, HttpSession sesionIniciada) {
		try {
			logger.info("Intentando registrar el usuario: " + nuevoUsuario.getCorreoElectronicoUsu());
			return servicioDeInicioDeSesion.nuevoUsuario(nuevoUsuario, sesionIniciada);
		} catch (Exception e) {
			logger.error("Error en el registro de usuario: " + nuevoUsuario.getCorreoElectronicoUsu());
			ModelAndView vista = new ModelAndView("error");
			vista.addObject("error", "Error al registrar usuario. Inténtelo más tarde.");
			return vista;
		}
	}

	/**
	 * Metodo que inicia sesion
	 * 
	 * @author jpribio - 27/01/25
	 * @return
	 */
	@PostMapping("/IS")
	public ModelAndView inicioSesionUsuario(@ModelAttribute UsuarioRegistroDto buscarUsuario,
			HttpSession sesionIniciada) {
		try {
			logger.info("Intentando iniciar sesión para el usuario: " + buscarUsuario.getCorreoElectronicoUsu());
			ModelAndView vista = new ModelAndView();
			vista = servicioDeInicioDeSesion.inicioSesion(buscarUsuario, sesionIniciada);
			return vista;
		} catch (Exception e) {
			logger.error("Error al iniciar sesión para el usuario: " + buscarUsuario.getCorreoElectronicoUsu());
			ModelAndView vista = new ModelAndView("InicioSesion");
			vista.addObject("error", "Error al iniciar sesión. Inténtelo de nuevo.");
			return vista;
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
