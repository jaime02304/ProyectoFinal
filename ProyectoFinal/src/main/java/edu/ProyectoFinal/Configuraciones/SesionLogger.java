package edu.ProyectoFinal.Configuraciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Clase donde se encuentra los elementos para la estructura de los logs
 * 
 * @author jpribio - 02/03/25
 */
public class SesionLogger {
	private final Logger logger;

	/**
	 * Constructor que inicializa un logger para una clase que se le pasa por
	 * parametro
	 * 
	 * @author jpribio - 02/03/25
	 * @param clazz
	 */
	public SesionLogger(Class<?> clazz) {
		this.logger = LoggerFactory.getLogger(clazz);
	}

	/**
	 * Metodo que coge el string y lo pone en formato de log info (segun la sesion
	 * del usuario)
	 * 
	 * @author jpribio - 02/03/25
	 * @param mensaje
	 */
	public void info(String mensaje) {
		String sessionId = MDC.get("sessionId");
		if (sessionId != null) {
			SesionManual.log(sessionId, "[INFO] " + mensaje);
		} else {
			logger.info(mensaje); // Si no hay sesión, escribe en el log global
		}
	}

	/**
	 * Metodo que coge el string y lo pone en formato de log error (segun la sesion
	 * del usuario)
	 * 
	 * @author jpribio - 02/03/25
	 * @param mensaje
	 */
	public void error(String mensaje) {
		String sessionId = MDC.get("sessionId");
		if (sessionId != null) {
			SesionManual.log(sessionId, "[ERROR] " + mensaje);
		} else {
			logger.error(mensaje); // Si no hay sesión, escribe en el log global
		}
	}

	/**
	 * Metodo que coge el string y lo pone en formato de log warn (segun la sesion
	 * del usuario)
	 * 
	 * @author jpribio - 02/03/25
	 * @param mensaje
	 */
	public void warn(String mensaje) {
		String sessionId = MDC.get("sessionId");
		if (sessionId != null) {
			SesionManual.log(sessionId, "[WARN] " + mensaje);
		} else {
			logger.warn(mensaje); // Si no hay sesión, escribe en el log global
		}
	}
}
