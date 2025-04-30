package edu.ProyectoFinal.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import edu.ProyectoFinal.Dto.GrupoCompletoDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.ClientBuilder;
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
	public ModelAndView obtenerLosGruposTops() {
		ModelAndView vista = new ModelAndView();
		String url = "http://localhost:8081/api/index/grupos";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<GruposListadoDto> listadoGruposCompletosTop = listadoGruposTop(respuestaApi);
				vista.addObject("listaGrupos", listadoGruposCompletosTop);

				if (listadoGruposCompletosTop.isEmpty()) {
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

	public ModelAndView recogidaDeGrupos(HttpSession sesionIniciada) {
		ModelAndView vista = new ModelAndView();
		String url = "http://localhost:8081/api/RecogerGruposTotales";

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
