package edu.ProyectoFinal.Controladores;

import java.io.IOException;

import edu.ProyectoFinal.Configuraciones.SesionLogger;
import edu.ProyectoFinal.Dto.ComentariosPerfilDto;
import edu.ProyectoFinal.servicios.PerfilServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clase de crear un nuevo comentario ya sea en la pagina de comentarios o en en
 * la parte de perefil
 * 
 * @author jpribio - 20/02/25
 */
@WebServlet("/CrearComentario")
@MultipartConfig
public class CrearComentarioControlador extends HttpServlet {
	private static final SesionLogger logger = new SesionLogger(CrearComentarioControlador.class);
	private static final long serialVersionUID = 1L;
	PerfilServicio servicioPerfil = new PerfilServicio();

	/**
	 * MEtodo de crear un nuevo comentario ya sea en la pagina de comentarios o en
	 * en la parte de perefil
	 * 
	 * @author jpribio - 20/02/25
	 * @param nuevoComentario
	 * @param sesion
	 * @return
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			logger.info("Inicializacion de la creacion de comentario");
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("Usuario") == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			ComentariosPerfilDto dto = new ComentariosPerfilDto();
			dto.setComentarioTexto(request.getParameter("comentarioTexto"));
			dto.setCategoriaTipo(request.getParameter("categoriaTipo"));
			dto.setSubCategoriaTipo(request.getParameter("subCategoriaTipo"));
			dto.setIdUsuario(Long.parseLong(request.getParameter("idUsuario")));
			boolean success = servicioPerfil.crearComentarioComoAdmin(dto);
			response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("");
		} catch (Exception e) {
			logger.error("Error al crear un comentario" + e);
			request.setAttribute("error", "Error al crear un comentario");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}
	}
}
