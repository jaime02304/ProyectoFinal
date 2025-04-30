package edu.ProyectoFinal.Controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.servicios.GruposServicios;
import jakarta.servlet.http.HttpSession;

@Controller
public class GrupoControlador {
	private static final SesionLogger logger = new SesionLogger(GrupoControlador.class);

	GruposServicios servicioDeGrupos = new GruposServicios();

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
			logger.info("Cargando la vista de comenatarios");
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
}
