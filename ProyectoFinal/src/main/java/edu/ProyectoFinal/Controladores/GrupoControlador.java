package edu.ProyectoFinal.Controladores;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.GrupoEspecificadoDto;
import edu.ProyectoFinal.Dto.SuscripcionDto;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.servicios.GruposServicios;
import jakarta.servlet.http.HttpSession;

@Controller
public class GrupoControlador {
	private static final SesionLogger logger = new SesionLogger(GrupoControlador.class);

	GruposServicios servicioDeGrupos = new GruposServicios();

	/**
	 * MEtodo que muestra la pagina de grupos
	 * 
	 * @author jpribio - 07/05/25
	 * @param sesionIniciada
	 * @param redirectAttrs
	 * @return
	 */
	@GetMapping("/PaginaGrupo")
	public ModelAndView PaginaComentario(HttpSession sesionIniciada, RedirectAttributes redirectAttrs) {
		try {
			UsuarioPerfilDto usuario = (UsuarioPerfilDto) sesionIniciada.getAttribute("Usuario");
			if (sesionIniciada == null || usuario == null) {
				ModelAndView errorVista = new ModelAndView("error");
				errorVista.addObject("error",
						"No se ha detectado un usuario activo. Por favor, inicie sesión antes de continuar.");
				return errorVista;
			}
			if (usuario.getEsVerificadoEntidad() == false) {
				ModelAndView vista = new ModelAndView("redirect:/");
				redirectAttrs.addFlashAttribute("infoVerificacion",
						"No se ha detectado un usuario verificado. Por favor, debe de verificarse antes de continuar.");
				return vista;
			}
			logger.info("Cargando la vista de grupos");
			ModelAndView vista = new ModelAndView();
			vista = servicioDeGrupos.recogidaDeGrupos(sesionIniciada);
			vista.setViewName("GrupoPagina");
			return vista;
		} catch (Exception e) {
			logger.error("Error al cargar la página de comentarios\n" + e);
			ModelAndView vista = new ModelAndView("GrupoPagina");
			vista.addObject("error", "Error al cargar la comentarios.");
			vista.setViewName("error");
			return vista;
		}
	}

	/**
	 * Metodo que recoge toda la informacion del grupo especificado
	 * 
	 * @author jpribio - 07/05/25
	 * @param sesionIniciada
	 * @param redirectAttrs
	 * @param grupoEspecificado
	 * @return
	 */
	@GetMapping("/PaginaGrupoEspecificado")
	public ModelAndView paginaGrupoEspecificado(@RequestParam String nombreGrupo, HttpSession sesionIniciada,
			RedirectAttributes redirectAttrs) {
		GrupoEspecificadoDto grupoEspecificado = new GrupoEspecificadoDto();
		grupoEspecificado.setNombreGrupo(nombreGrupo);
		try {
			UsuarioPerfilDto usuario = (UsuarioPerfilDto) sesionIniciada.getAttribute("Usuario");
			if (sesionIniciada == null || usuario == null) {
				ModelAndView errorVista = new ModelAndView("error");
				errorVista.addObject("error",
						"No se ha detectado un usuario activo. Por favor, inicie sesión antes de continuar.");
				return errorVista;
			}
			if (usuario.getEsVerificadoEntidad() == false) {
				ModelAndView vista = new ModelAndView("redirect:/");
				redirectAttrs.addFlashAttribute("infoVerificacion",
						"No se ha detectado un usuario verificado. Por favor, debe de verificarse antes de continuar.");
				return vista;
			}
			logger.info("Cargando la vista de grupos especificado");
			ModelAndView vista = new ModelAndView();
			vista = servicioDeGrupos.verGrupoEspecificado(sesionIniciada, grupoEspecificado);
			return vista;
		} catch (Exception e) {
			logger.error("Error al cargar la página del grupo especificado\n" + e);
			ModelAndView vista = new ModelAndView("error");
			vista.addObject("error", "Error al cargar la comentarios.");
			return vista;
		}
	}

	/**
	 * Metodo para poder suscriburme a un grupo
	 * 
	 * @author jpribio - 07/05/25
	 * @param suscripcion
	 * @param sesionIniciada
	 * @return
	 */
	@PostMapping("/UnirseAlGrupo")
	public ResponseEntity<?> unirmeAlGrupoEspecificado(@ModelAttribute SuscripcionDto suscripcion,
			HttpSession sesionIniciada) {
		UsuarioPerfilDto usuario = (UsuarioPerfilDto) sesionIniciada.getAttribute("Usuario");
		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Usuario no autenticado.");
		}

		suscripcion.setAliasUsuario(usuario.getAliasUsu());

		try {
			logger.info("Enviando solicitud de suscripción al grupo.");
			return servicioDeGrupos.enviarSuscripcion(suscripcion);
		} catch (Exception e) {
			logger.error("Error al procesar la suscripción al grupo");
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.body("Error inesperado al procesar la suscripción.");
		}

	}

	@PostMapping("/EliminarDelGrupo")
	public ResponseEntity<?> eliminarDelGrupo(@ModelAttribute SuscripcionDto suscripcion, HttpSession sesionIniciada) {
		UsuarioPerfilDto usuario = (UsuarioPerfilDto) sesionIniciada.getAttribute("Usuario");
		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body("Usuario no autenticado.");
		}

		suscripcion.setAliasUsuario(usuario.getAliasUsu());

		try {
			logger.info("Enviando solicitud para eliminar suscripción del grupo.");
			return servicioDeGrupos.enviarEliminacionSuscripcion(suscripcion);
		} catch (Exception e) {
			logger.error("Error al procesar la eliminación de la suscripción");
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.body("Error inesperado al procesar la eliminación.");
		}
	}

}
