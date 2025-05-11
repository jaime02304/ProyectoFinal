package edu.ProyectoFinal.servicios;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import edu.ProyectoFinal.Dto.ComentariosDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Clase donde se encuentra los metodos de los comentarios
 * 
 * @author jpribio - 16/04/25
 */
public class ComentariosServicios {

	/**
	 * MEtodo para recoger los comentarios
	 * 
	 * @author jpribio - 16/04/25
	 * @param sesionInicaida
	 * @return
	 */
	public ModelAndView recogidaDeComentarios(HttpSession sesionInicaida) {
		ModelAndView vista = new ModelAndView();
		String url = "http://localhost:8081/api/RecogerComentarios";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<ComentariosDTO> listadoComentarios = listadoComentarios(respuestaApi);
				vista.addObject("listadoComentarios", listadoComentarios);

				if (listadoComentarios.isEmpty()) {
					vista.addObject("mensajeGrupo", "No se encontraron comentarios disponibles.");
				}
			} else {
				vista.addObject("error", "Error al obtener los grupos: " + respuestaApi.getStatusInfo().toString());
			}
		} catch (Exception e) {
			vista.addObject("error", "Error al conectar con la API: " + e.getMessage());
		}

		return vista;
	}

	/**
	 * MEtodo privado para converti lo que le viene en un listado del dto
	 * 
	 * @author jpribio - 16/04/25
	 * @param respuestaApi
	 * @return
	 */
	private List<ComentariosDTO> listadoComentarios(Response respuestaApi) {
		List<ComentariosDTO> listaGrupos = new ArrayList<>();

		try {
			String jsonString = respuestaApi.readEntity(String.class);
			JSONObject jsonResponse = new JSONObject(jsonString);
			JSONArray comentarioArray = jsonResponse.optJSONArray("listaCompletaComentarios");

			if (comentarioArray != null) {
				for (int i = 0; i < comentarioArray.length(); i++) {
					JSONObject jsonComentario = comentarioArray.getJSONObject(i);
					ComentariosDTO comentario = new ComentariosDTO();

					comentario.setAliasUsuarioComentario(jsonComentario.optString("aliasUsuarioComentario"));
					comentario.setCategoriaTipo(jsonComentario.optString("categoriaTipo"));
					comentario.setComentarioTexto(jsonComentario.optString("comentarioTexto"));
					comentario.setSubCategoriaTipo(jsonComentario.optString("subCategoriaTipo"));
					comentario.setIdUsuario(jsonComentario.optLong("idUsuario"));
					comentario.setGrupoComentario(jsonComentario.optString("grupoComentario"));
					listaGrupos.add(comentario);
				}
			} else {
				System.out.println("No se encontró el array 'comentarioArray' en la respuesta JSON.");
			}

		} catch (Exception e) {
			System.err.println("Error al parsear la respuesta JSON: " + e.getMessage());
		}

		return listaGrupos;
	}

}
