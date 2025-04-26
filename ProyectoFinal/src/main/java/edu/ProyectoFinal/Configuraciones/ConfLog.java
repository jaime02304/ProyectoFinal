package edu.ProyectoFinal.Configuraciones;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro personalizado que se ejecuta una vez por cada solicitud HTTP.
 * 
 * @author jpribio - 02/03/25
 * @Component Indica que esta clase es un componente de Spring y se gestionará
 *            como un bean.
 */
@Component
public class ConfLog extends OncePerRequestFilter {

	/**
	 * Método principal del filtro que se ejecuta en cada solicitud HTTP.
	 * 
	 * @author jpribio - 02/03/25
	 * @param request     La solicitud HTTP recibida.
	 * @param response    La respuesta HTTP que se enviará.
	 * @param filterChain La cadena de filtros a la que se delega la solicitud.
	 * @throws ServletException Si ocurre un error durante el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error de entrada/salida durante el
	 *                          procesamiento.
	 * 
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpSession sesion = request.getSession(true);
		String sesionId = sesion.getId();

		// Almacenar el sessionId en el MDC
		MDC.put("sessionId", sesionId);

		try {
			filterChain.doFilter(request, response);
		} finally {
			// Limpiar el MDC al finalizar la solicitud
			MDC.remove("sessionId");
			SesionManual.cerrarEscritor(sesionId);
		}
	}

}
