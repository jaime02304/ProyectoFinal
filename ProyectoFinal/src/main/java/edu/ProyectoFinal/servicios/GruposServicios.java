package edu.ProyectoFinal.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ProyectoFinal.Configuraciones.RutasGenericas;
import edu.ProyectoFinal.Dto.GrupoCompletoDto;
import edu.ProyectoFinal.Dto.GrupoEspecificadoDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.Dto.SuscripcionDto;
import edu.ProyectoFinal.Dto.UsuarioDeGruposDto;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Servicio donde se encuentre todos los metodos de los grupos
 * 
 * @author jpribio - 23/01/25
 */
public class GruposServicios {

	/**
	 * Metodo que obtiene los 5 grupos mas top del total
	 * 
	 * @author jpribio - 23/01/25
	 * @return devuelve el modelAndView con el objeto
	 * @throws Exception
	 * @throws NullPointerException
	 */
	public List<GruposListadoDto> obtenerLosGruposTops() throws Exception {
		String url = RutasGenericas.rutaPrincipalApiString + "api/index/grupos";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				return listadoGruposTop(respuestaApi);
			} else {
				throw new RuntimeException("Error al obtener los grupos: " + respuestaApi.getStatusInfo());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error al conectar con la API: " + e.getMessage(), e);
		}
	}

	/**
	 * Metodo privado que coge del texto plano los grupos y los pasa a gruposDTo
	 * 
	 * @author jpribio - 23/01/25
	 * @param respuestaApi (la respuesta de la api con el texto plano)
	 * @return devuelve la lista de los grupos
	 * @throws Exception
	 * @throws NullPointerException
	 */
	private List<GruposListadoDto> listadoGruposTop(Response respuestaApi) {
		List<GruposListadoDto> listaGrupos = new ArrayList<>();

		try {
			// Parsear directamente el JSON recibido desde la respuesta
			String jsonString = respuestaApi.readEntity(String.class);
			JSONObject jsonResponse = new JSONObject(jsonString);

			// Extraer el JSONArray de "grupos", si existe
			JSONArray gruposArray = jsonResponse.optJSONArray("grupos");

			// Si el array no es nulo, procesamos los grupos
			if (gruposArray != null) {
				for (int i = 0; i < gruposArray.length(); i++) {
					JSONObject jsonGrupo = gruposArray.getJSONObject(i);
					GruposListadoDto grupo = new GruposListadoDto();

					// Asignación directa de los valores
					grupo.setNombreGrupo(jsonGrupo.optString("nombreGrupo"));
					grupo.setCategoriaNombre(jsonGrupo.optString("categoriaNombre"));
					grupo.setSubCategoriaNombre(jsonGrupo.optString("subCategoriaNombre"));

					// Añadir el grupo a la lista
					listaGrupos.add(grupo);
				}
			} else {
				System.out.println("No se encontró el array 'grupos' en la respuesta JSON.");
			}

		} catch (Exception e) {
			System.err.println("Error al parsear la respuesta JSON: " + e.getMessage());
		}

		return listaGrupos;
	}

	/**
	 * Metodo para la recogida de los grupos
	 * 
	 * @author jpribio - 08/05/25
	 * @param sesionIniciada
	 * @return
	 */
	public ModelAndView recogidaDeGrupos(HttpSession sesionIniciada) {
		ModelAndView vista = new ModelAndView();
		String url = RutasGenericas.rutaPrincipalApiString + "api/RecogerGruposTotales";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<GrupoCompletoDto> listadoGrupos = listadoGrupos(respuestaApi);
				vista.addObject("listadoGruposTotales", listadoGrupos);

				if (listadoGrupos.isEmpty()) {
					vista.addObject("mensajeGrupo", "No se encontraron grupos disponibles.");
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
	 * metodo para enviar la solicitud para suscribirse
	 * 
	 * @author jpribio - 08/05/25
	 * @param suscripcion
	 * @return
	 */
	public ResponseEntity<String> enviarSuscripcion(SuscripcionDto suscripcion) {
		String API_URL = RutasGenericas.rutaPrincipalApiString + "api/UnirmeAlGrupo";
		try (Client cliente = ClientBuilder.newClient()) {
			String suscripcionJson = new ObjectMapper().writeValueAsString(suscripcion);

			Response respuestaApi = cliente.target(API_URL).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(suscripcionJson, MediaType.APPLICATION_JSON));

			int status = respuestaApi.getStatus();
			String mensaje = respuestaApi.getHeaderString("mensaje");

			if (mensaje == null) {
				mensaje = "No se ha realizado la suscripcion correctamente";
			}

			return ResponseEntity.status(status).body(mensaje);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.body("Error al conectar con la API: " + e.getMessage());
		}
	}

	public ResponseEntity<String> enviarEliminacionSuscripcion(SuscripcionDto suscripcion) {
		String API_URL = RutasGenericas.rutaPrincipalApiString + "api/AbandonarGrupo";
		try (Client cliente = ClientBuilder.newClient()) {
			String suscripcionJson = new ObjectMapper().writeValueAsString(suscripcion);

			Response respuestaApi = cliente.target(API_URL).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(suscripcionJson, MediaType.APPLICATION_JSON));

			int status = respuestaApi.getStatus();
			String mensaje = respuestaApi.getHeaderString("mensaje");

			if (mensaje == null) {
				mensaje = "No se ha podido eliminar la suscripción correctamente.";
			}

			return ResponseEntity.status(status).body(mensaje);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.body("Error al conectar con la API para eliminar la suscripción: " + e.getMessage());
		}
	}

	/**
	 * Metodo para ver el grupo especificado
	 * 
	 * @author jpribio - 08/05/25
	 * @param sesionIniciada
	 * @param grupoEspecificado
	 * @return
	 */
	public ModelAndView verGrupoEspecificado(HttpSession sesionIniciada, GrupoEspecificadoDto grupoEspecificado) {
		ModelAndView vista = new ModelAndView();
		String url = RutasGenericas.rutaPrincipalApiString + "api/VerGrupoEspecificado";

		try (Client cliente = ClientBuilder.newClient()) {
			String grupoEspecificoJson = new ObjectMapper().writeValueAsString(grupoEspecificado);
			Response respuestaApi = cliente.target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(grupoEspecificoJson, MediaType.APPLICATION_JSON));

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				String jsonString = respuestaApi.readEntity(String.class);
				JSONObject respuestaJson = new JSONObject(jsonString);
				JSONObject grupoJson = respuestaJson.optJSONObject("grupoEspecifico");
				if (grupoJson != null) {
					GrupoEspecificadoDto dto = mapearGrupoEspecificado(grupoJson);
					if (dto.getNombreGrupo() != null) {
						vista.addObject("grupoEspecificado", dto);
						vista.setViewName("GrupoEspecifico");
					} else {
						vista.addObject("error", "No se encontró el grupo especificado.");
						vista.setViewName("error");
					}
				} else {
					vista.addObject("error", "Respuesta inesperada: no venía ‘grupoEspecifico’");
					vista.setViewName("error");
				}

			} else {
				vista.addObject("error",
						"Error en la API al buscar el grupo (código " + respuestaApi.getStatus() + ").");
				vista.setViewName("error");
			}
		} catch (Exception e) {
			vista.addObject("error", "Error al conectar con la API: " + e.getMessage());
			vista.setViewName("error");
		}

		return vista;
	}

	/**
	 * Metodo privado para mapear los elementos que vienen del grupo especificado
	 * 
	 * @param respuestaJson
	 * @return
	 */
	private GrupoEspecificadoDto mapearGrupoEspecificado(JSONObject respuestaJson) {
		GrupoEspecificadoDto grupoEspecificado = new GrupoEspecificadoDto();
		if (!respuestaJson.isNull("grupoId"))
			grupoEspecificado.setGrupoId(respuestaJson.optLong("grupoId"));
		if (!respuestaJson.isNull("usuarioId"))
			grupoEspecificado.setUsuarioId(respuestaJson.optLong("usuarioId"));
		grupoEspecificado.setNombreGrupo(respuestaJson.optString("nombreGrupo"));
		grupoEspecificado.setAliasCreadorGrupo(respuestaJson.optString("aliasCreadorGrupo"));
		grupoEspecificado.setDescripcionGrupoString(respuestaJson.optString("descripcionGrupoString"));
		grupoEspecificado.setCategoriaGrupo(respuestaJson.optString("categoriaGrupo"));
		grupoEspecificado.setSubcategoriaGrupo(respuestaJson.optString("subcategoriaGrupo"));
		grupoEspecificado.setNumeroUsuarios(respuestaJson.optLong("numeroUsuarios"));

		if (!respuestaJson.isNull("fechaCreacion")) {
			String fechaString = respuestaJson.optString("fechaCreacion");
			grupoEspecificado.setFechaCreacion(LocalDateTime.parse(fechaString));
		}

		List<UsuarioDeGruposDto> lista = new ArrayList<>();
		if (respuestaJson.has("listadoDeUsuariosSuscritos")) {
			JSONArray arr = respuestaJson.getJSONArray("listadoDeUsuariosSuscritos");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject u = arr.getJSONObject(i);
				UsuarioDeGruposDto ud = new UsuarioDeGruposDto();
				ud.setAliasUsuario(u.optString("aliasUsuario"));
				ud.setNombreUsuario(u.optString("nombreUsuario"));
				ud.setEsPremium(u.optBoolean("esPremium"));
				lista.add(ud);
			}
		}
		grupoEspecificado.setListadoDeUsuariosSuscritos(lista);

		return grupoEspecificado;
	}

	/**
	 * Metodo para mapear los elementos del listado del grupo
	 * 
	 * @param respuestaApi
	 * @return
	 */
	private List<GrupoCompletoDto> listadoGrupos(Response respuestaApi) {
		List<GrupoCompletoDto> listaGrupos = new ArrayList<>();

		try {
			String jsonString = respuestaApi.readEntity(String.class);
			JSONObject jsonResponse = new JSONObject(jsonString);
			JSONArray grupoArray = jsonResponse.optJSONArray("listaCompletaGrupos");

			if (grupoArray != null) {
				for (int i = 0; i < grupoArray.length(); i++) {
					JSONObject j = grupoArray.getJSONObject(i);
					GrupoCompletoDto g = new GrupoCompletoDto();
					g.setIdGrupo(j.optLong("idGrupo"));
					g.setNombreGrupo(j.optString("nombreGrupo"));
					g.setCreadorUsuId(j.optLong("creadorUsuId"));
					g.setAliasCreadorUString(j.optString("aliasCreadorUString"));
					g.setNumeroUsuarios(j.optLong("numeroUsuarios"));
					String fechaStr = j.optString("fechaGrupo");
					if (!fechaStr.isEmpty()) {
						g.setFechaGrupo(LocalDateTime.parse(fechaStr));
					}

					g.setCategoriaNombre(j.optString("categoriaNombre"));
					g.setSubCategoriaNombre(j.optString("subCategoriaNombre"));
					g.setDescripcionGrupo(j.optString("descripcionGrupo"));

					listaGrupos.add(g);
				}
			} else {
				System.out.println("No se encontró el array 'listaCompletaGrupos' en la respuesta JSON.");
			}

		} catch (Exception e) {
			System.err.println("Error al parsear la respuesta JSON de grupos: " + e.getMessage());
		}

		return listaGrupos;
	}

}
