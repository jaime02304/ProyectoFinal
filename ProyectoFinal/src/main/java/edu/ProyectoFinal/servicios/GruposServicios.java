package edu.ProyectoFinal.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ProyectoFinal.Configuraciones.RutasGenericas;
import edu.ProyectoFinal.Dto.GrupoCompletoDto;
import edu.ProyectoFinal.Dto.GrupoEspecificadoDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.Dto.SuscripcionDto;
import edu.ProyectoFinal.Dto.UsuarioDeGruposDto;
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
	public List<GrupoCompletoDto> obtenerGrupos() {
		String URL_API = RutasGenericas.rutaPrincipalApiString + "api/RecogerGruposTotales";
		List<GrupoCompletoDto> listaGrupos = new ArrayList<>();
		Client client = ClientBuilder.newClient();
		Response resp = null;

		try {
			resp = client.target(URL_API).request(MediaType.APPLICATION_JSON).get();

			if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
				System.err.println("Error al obtener los grupos: " + resp.getStatusInfo());
				return listaGrupos;
			}
			listaGrupos = parsearListadoGrupos(resp);
		} catch (Exception e) {
		} finally {
			if (resp != null)
				resp.close();
			client.close();
		}

		return listaGrupos;
	}

	/**
	 * metodo para enviar la solicitud para suscribirse
	 * 
	 * @author jpribio - 08/05/25
	 * @param suscripcion
	 * @return
	 */
	public boolean enviarSuscripcion(SuscripcionDto suscripcion) {
		ObjectMapper mapper = new ObjectMapper();
		String API_URL = RutasGenericas.rutaPrincipalApiString + "api/UnirmeAlGrupo";
		try (Client cliente = ClientBuilder.newClient()) {
			String json = mapper.writeValueAsString(suscripcion);
			Response resp = cliente.target(API_URL).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(json, MediaType.APPLICATION_JSON));
			boolean ok = resp.getStatus() == Response.Status.OK.getStatusCode();
			resp.close();
			return ok;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * metodo para enviar la solicitud para borrarse del grupo
	 * 
	 * @author jpribio - 08/05/25
	 * @param suscripcion
	 * @return
	 */
	public boolean enviarEliminacionSuscripcion(SuscripcionDto suscripcion) {
		String API_URL = RutasGenericas.rutaPrincipalApiString + "api/AbandonarGrupo";
		try (Client cliente = ClientBuilder.newClient()) {
			String suscripcionJson = new ObjectMapper().writeValueAsString(suscripcion);
			Response respuestaApi = cliente.target(API_URL).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(suscripcionJson, MediaType.APPLICATION_JSON));
			int status = respuestaApi.getStatus();
			return status >= 200 && status < 300;

		} catch (Exception e) {
			return false;
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
	public GrupoEspecificadoDto obtenerGrupoEspecifico(String nombreGrupo) {
		String URL_ESPECIFICO = RutasGenericas.rutaPrincipalApiString + "api/VerGrupoEspecificado";
		Client client = ClientBuilder.newClient();
		GrupoEspecificadoDto resultado = null;
		try {
			JSONObject j = new JSONObject();
			j.put("nombreGrupo", nombreGrupo);
			Response resp = client.target(URL_ESPECIFICO).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(j.toString(), MediaType.APPLICATION_JSON));

			if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
				resp.close();
				return null;
			}
			String jsonString = resp.readEntity(String.class);
			resp.close();
			JSONObject root = new JSONObject(jsonString);
			JSONObject grupoJson = root.optJSONObject("grupoEspecifico");
			if (grupoJson != null) {
				resultado = mapearGrupoEspecificado(grupoJson);
			}
		} catch (Exception e) {
			System.err.println("Error en obtenerGrupoEspecifico: " + e.getMessage());
		} finally {
			client.close();
		}
		return resultado;
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
	private List<GrupoCompletoDto> parsearListadoGrupos(Response respuestaApi) {
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
