package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.servicios.ComentariosServicios;
import edu.ProyectoFinal.servicios.GruposServicios;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador de la vista principal
 * 
 * @author jpribio - 23/01/25
 */
@Controller
public class ControladorIndex {

	private static final SesionLogger logger = new SesionLogger(ControladorIndex.class);

	GruposServicios serviciosGrupos = new GruposServicios();

	ComentariosServicios servicioComentario = new ComentariosServicios();

	/**
	 * Metodo que muestra la vista y objetos de la misma
	 * 
	 * @return devuelve la vista y los metodos equivalentes
	 * @throws IOException
	 */
	@GetMapping("/")
	protected ModelAndView index(HttpSession sesionIniciada) {
		ModelAndView vista = new ModelAndView();

		try {
			vista.setViewName("LandinPage");
			ModelAndView gruposVista = serviciosGrupos.obtenerLosGruposTops();
			vista.addAllObjects(gruposVista.getModel());
			ModelAndView comentariosVista = servicioComentario.recogidaDeComentariosIndex();
			vista.addAllObjects(comentariosVista.getModel());

			logger.info("Grupos y comentarios cargados correctamente.");
		} catch (Exception e) {
			logger.error("Error al cargar la página inicial.\n" + e);
			vista.setViewName("error");
			vista.addObject("error", "Error al cargar la página inicial.");
			logger.warn("Se produjo un error al cargar la página inicial.");
		}

		return vista;
	}

}
