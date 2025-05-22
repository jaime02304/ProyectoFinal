package edu.ProyectoFinal.Controladores;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.ComentariosPerfilDto;
import edu.ProyectoFinal.Dto.GruposDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.Dto.eliminarElementoPerfilDto;
import edu.ProyectoFinal.servicios.GruposServicios;
import edu.ProyectoFinal.servicios.PerfilServicio;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador donde se encuentra todos los metodos de la pagina del perfil del
 * mismo usuario y de los administradores
 * 
 * @author jpribio - 30/01/25
 */
@Controller
public class perfilUsuarioControlador {

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
	@GetMapping("/PerfilUsuario")
	public ModelAndView vistaPerfilYAdministradores(HttpSession sesionIniciada, RedirectAttributes redirectAttrs) {
		ModelAndView vista = new ModelAndView();
		UsuarioPerfilDto usuarioABuscar = (UsuarioPerfilDto) sesionIniciada.getAttribute("Usuario");

		try {
			UsuarioPerfilDto usuario = (UsuarioPerfilDto) sesionIniciada.getAttribute("Usuario");
			if (sesionIniciada == null || usuario == null) {
				ModelAndView errorVista = new ModelAndView("error");
				errorVista.addObject("error",
						"No se ha detectado un usuario activo. Por favor, inicie sesión antes de continuar.");
				return errorVista;
			}
			if (usuario.getEsVerificadoEntidad() == false) {
				vista.setViewName("redirect:/");
				redirectAttrs.addFlashAttribute("infoVerificacion",
						"No se ha detectado un usuario verificado. Por favor, debe de verificarse antes de continuar.");
				return vista;
			}
			logger.info(
					"Cargando perfil para el usuario: " + usuarioABuscar != null ? usuarioABuscar.getNombreCompletoUsu()
							: "Desconocido");
			vista = servicioPerfil.condicionYCasosPerfil(usuarioABuscar, vista);
			logger.info("Perfil cargado correctamente para el usuario: " + usuarioABuscar != null
					? usuarioABuscar.getNombreCompletoUsu()
					: "Desconocido");
		} catch (Exception e) {
			logger.error("Error al cargar el perfil del usuario.\n" + e);
			vista.setViewName("error");
			vista.addObject("error", "No se ha cargado la página del perfil personal");
			logger.warn("Se produjo un error al intentar cargar el perfil del usuario.");
		}

		return vista;
	}
	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Metodo que modifica al usuario con los valores incorporados
	 *
	 * @author jpribio - 11/02/25
	 * @param usuarioAModificar
	 * @param sesionDelUsuario
	 * @return
	 */
	@PostMapping("/ModificarUsuario")
	public ResponseEntity<?> modificarUsuario(@ModelAttribute UsuarioPerfilDto usuarioAModificar,
			HttpSession sesionDelUsuario) {
		try {
			logger.info("Intentando modificar los datos del usuario: " + usuarioAModificar.getNombreCompletoUsu());
			return servicioPerfil.modificarUsuario(usuarioAModificar, sesionDelUsuario);
		} catch (Exception ex) {
			logger.error("Error al modificar el usuario: " + usuarioAModificar.getNombreCompletoUsu() + "\n" + ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Metodo que recoge al usuario de la vista y lo manda a la api para que sea
	 * borrado
	 * 
	 * @author jpribio - 15/02/25
	 * @param elemento
	 * @param sesion
	 * @return
	 */
	@PostMapping("/EliminarElementosComoAdmin")
	public ResponseEntity<?> eliminarUsuario(@ModelAttribute eliminarElementoPerfilDto elemento, HttpSession sesion) {
		try {
			logger.info("Iniciando eliminación del elemento: " + elemento.getIdElementoEliminar());
			return servicioPerfil.enviarElementoParaBorrar(elemento, sesion);
		} catch (Exception ex) {
			logger.error("Error al eliminar el elemento: " + elemento.getIdElementoEliminar() + "\n" + ex);
			String errorMsg = "Ha ocurrido un error al eliminar el elemento (usuario o grupo). Por favor, inténtalo de nuevo.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMsg));
		}
	}

	/**
	 * MEtodo que manda el usuario seleccionado a modificar hacia la api y lo
	 * modifica
	 * 
	 * @author jpribio - 15/02/25
	 * @param usuarioAModificar
	 * @param sesion
	 * @return
	 */
	@PostMapping("/ModificarUsuarioComoAdmin")
	public ResponseEntity<?> modificarUSuarioComoAdmin(@ModelAttribute UsuarioPerfilDto usuarioAModificar,
			HttpSession sesion) {
		try {
			logger.info("Iniciando modificación del usuario como admin: " + usuarioAModificar.getNombreCompletoUsu());
			return servicioPerfil.enviarUsuarioAModificarComoAdmin(usuarioAModificar, sesion);
		} catch (Exception ex) {
			logger.error("Error al modificar el usuario como admin: " + usuarioAModificar.getNombreCompletoUsu() + "\n"
					+ ex);
			String errorMEnsajeString = "Ha ocurrido un error al modificar el usuario. Por favor, inténtalo de nuevo.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMEnsajeString));
		}
	}

	/**
	 * Metodo que modifica un grupo segun los nuevos elementos dados en la vista
	 * 
	 * @author jpribio - 15/02/25
	 * @param usuarioAModificar
	 * @param sesion
	 * @return
	 */
	@PostMapping("/ModificarGrupoComoAdmin")
	public ResponseEntity<?> modificarGrupoComoAdmin(@ModelAttribute GruposListadoDto grupoAModificar,
			HttpSession sesion) {
		try {
			logger.info("Iniciando la modificación del grupo con ID: " + grupoAModificar.getIdGrupo());
			return servicioPerfil.enviarGrupoAModificarComoAdmin(grupoAModificar, sesion);
		} catch (Exception ex) {
			logger.error("Error al modificar el grupo con ID: " + grupoAModificar.getIdGrupo() + "\n" + ex);
			Map<String, Object> respuestaError = new HashMap<>();
			String errorMEnsajeString = "Ha ocurrido un error al modificar el grupo. Por favor, inténtalo de nuevo.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMEnsajeString));
		}
	}

	/**
	 * metodo que coge el usuario de la web y lo manda hacia la api
	 * 
	 * @author jptibio - 16/02/25
	 * @param usuarioCreado
	 * @param sesion
	 * @return
	 */
	@PostMapping("/CrearUsuarioComoAdmin")
	public ResponseEntity<?> crearUnNuevoUsuarioComoAdmin(@ModelAttribute UsuarioPerfilDto usuarioCreado,
			HttpSession sesion) {
		try {
			logger.info(
					"Iniciando la creación de un nuevo usuario con nombre: " + usuarioCreado.getNombreCompletoUsu());
			return servicioPerfil.crearUsuarioComoAdmin(usuarioCreado, sesion);
		} catch (Exception ex) {
			logger.error("Error al crear el usuario con nombre: " + usuarioCreado.getNombreCompletoUsu() + "\n" + ex);
			String errorMensaje = "Ha ocurrido un error al crear el usuario. Por favor, inténtalo de nuevo.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMensaje));
		}
	}

	/**
	 * Metodo que coge el grupo de la web y lo manda hacia la api
	 * 
	 * @author jptibio - 16/02/25
	 * @param usuarioCreado
	 * @param sesion
	 * @return
	 */
	@PostMapping("/CrearGrupoComoAdmin")
	public ResponseEntity<?> crearUnNuevoGrupoComoAdmin(@ModelAttribute GruposDto grupoCreado, HttpSession sesion) {
		try {
			logger.info("Iniciando la creación de un nuevo grupo con nombre: " + grupoCreado.getNombreGrupo());
			return servicioPerfil.crearGrupoComoAdmin(grupoCreado, sesion);
		} catch (Exception ex) {
			logger.error("Error al crear el grupo con nombre: " + grupoCreado.getNombreGrupo() + "\n" + ex);
			String errorMsg = "Ha ocurrido un error al crear el grupo. Por favor, inténtalo de nuevo.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
		}
	}

	/**
	 * MEtodo de crear un nuevo comentario ya sea en la pagina de comentarios o en
	 * en la parte de perefil
	 * 
	 * @author jpribio - 20/02/25
	 * @param nuevoComentario
	 * @param sesion
	 * @return
	 */
	@PostMapping("/CrearComentario")
	public ResponseEntity<?> crearComentario(@ModelAttribute ComentariosPerfilDto nuevoComentario, HttpSession sesion) {
		try {
			logger.info("Iniciando la creación de un nuevo comentario: " + nuevoComentario.getComentarioTexto());
			return servicioPerfil.crearComentarioPerfil(nuevoComentario, sesion);
		} catch (Exception ex) {
			logger.error("Error al crear el comentario: " + nuevoComentario.getComentarioTexto() + "\n" + ex);
			String errorMensaje = "Ha ocurrido un error al crear el comentario. Por favor, inténtalo de nuevo.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMensaje));
		}
	}

	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/
	/**
	 * Metodo de cerrar la sesion del usuario
	 * 
	 * @author jpribio - 06/02/25
	 * @param cerrarSesion
	 * @return
	 */
	@GetMapping("/CerrarSesion")
	public ModelAndView cerrarSesionUsuario(HttpSession cerrarSesion) {
		ModelAndView vista = new ModelAndView();
		try {
			logger.info("Cerrando sesión del usuario.");
			cerrarSesion.invalidate();
			//vista = servicioGrupos.obtenerLosGruposTops();
			vista.setViewName("LandinPage");
		} catch (Exception e) {
			logger.error("Error al cerrar la sesión del usuario\n" + e);
			vista.setViewName("error");
			vista.addObject("error", "No se ha cargado la página principal.");
		}

		return vista;
	}

}