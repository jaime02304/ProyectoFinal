package edu.ProyectoFinal.servicios;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.ProyectoFinal.Configuraciones.RutasGenericas;
import edu.ProyectoFinal.Dto.ComentariosDTO;
import edu.ProyectoFinal.Dto.ComentariosIndexDto;
import jakarta.ws.rs.client.Client;
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
	 * Metodo para recoger los comentarios dde la pagina inicial de la aplicacion
	 * 
	 * @author jpribio - 15/05/25
	 * @return
	 */
	public List<ComentariosIndexDto> recogidaDeComentariosIndex() throws Exception {
		String url = RutasGenericas.rutaPrincipalApiString + "api/index/comentarios";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				return listadoComentariosIndex(respuestaApi);
			} else {
				throw new RuntimeException("Error al obtener los comentarios: " + respuestaApi.getStatusInfo());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error al conectar con la API: " + e.getMessage(), e);
		}
	}

	/**
	 * MEtodo privado para meterlo en el dto lo que le llegue del json
	 * 
	 * @author jpribio - 15/05/25
	 * @param respuestaApi
	 * @return
	 */
	private List<ComentariosIndexDto> listadoComentariosIndex(Response respuestaApi) {
		List<ComentariosIndexDto> listaComentarios = new ArrayList<>();

		try {
			String jsonString = respuestaApi.readEntity(String.class);
			JSONObject jsonResponse = new JSONObject(jsonString);
			JSONArray comentariosArray = jsonResponse.optJSONArray("comentarios");
			if (comentariosArray != null) {
				for (int i = 0; i < comentariosArray.length(); i++) {
					JSONObject jsonComentario = comentariosArray.getJSONObject(i);
					ComentariosIndexDto dto = new ComentariosIndexDto();

					dto.setAliasUsuarioComentario(jsonComentario.optString("aliasUsuarioComentario", ""));
					dto.setComentarioTexto(jsonComentario.optString("comentarioTexto", ""));
					dto.setImagenUsuario(jsonComentario.optString("imagenUsuario", null));

					listaComentarios.add(dto);
				}
			} else {
				System.out.println("No se encontró el array 'comentarios' en la respuesta JSON.");
			}
		} catch (Exception e) {
			System.err.println("Error al parsear la respuesta JSON: " + e.getMessage());
		}

		return listaComentarios;
	}

	/**
	 * MEtodo para recoger los comentarios
	 * 
	 * @author jpribio - 16/04/25
	 * @param sesionInicaida
	 * @return
	 */
	public List<ComentariosDTO> obtenerComentarios() throws Exception {
		String URL_API = RutasGenericas.rutaPrincipalApiString + "api/RecogerComentarios";
		Client client = ClientBuilder.newClient();
		Response resp = client.target(URL_API).request(MediaType.APPLICATION_JSON).get();
		try {
			if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
				throw new Exception("API devolvió status " + resp.getStatus());
			}
			return listadoComentarios(resp);
		} finally {
			resp.close();
			client.close();
		}
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
