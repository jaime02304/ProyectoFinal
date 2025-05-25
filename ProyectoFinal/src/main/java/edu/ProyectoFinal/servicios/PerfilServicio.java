package edu.ProyectoFinal.servicios;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ProyectoFinal.Configuraciones.RutasGenericas;
import edu.ProyectoFinal.Dto.ComentariosPerfilDto;
import edu.ProyectoFinal.Dto.GruposDto;
import edu.ProyectoFinal.Dto.GruposListadoDto;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.Dto.eliminarElementoPerfilDto;
import edu.ProyectoFinal.Utilidades.Util;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Clase donde se encuentra los metodos que tienen relacion con la pagina de
 * perfil
 * 
 * @author jpribio - 03/02/25
 */
public class PerfilServicio {

	Util utilidades = new Util();

	/**
	 * Metodo privado que pasa de json a a comentario del perfil (DTO)
	 * 
	 * @author jpribio - 04/02/25
	 * @param jsonResponse
	 * @return
	 */
	private ComentariosPerfilDto obtenerComentario(JSONObject jsonResponse) {
		try {
			JSONObject jsonComentario = jsonResponse.optJSONObject("comentarios");

			if (jsonComentario == null) {
				return null; // Retorna un objeto vacío en lugar de null
			}

			ComentariosPerfilDto comentariosPerfilDto = new ComentariosPerfilDto();
			comentariosPerfilDto.setComentarioTexto(jsonComentario.optString("comentarioTexto"));
			comentariosPerfilDto.setCategoriaTipo(jsonComentario.optString("categoriaTipo"));
			comentariosPerfilDto.setSubCategoriaTipo(jsonComentario.optString("subCategoriaTipo"));
			return comentariosPerfilDto;
		} catch (JSONException e) {
			throw new RuntimeException("Error procesando la respuesta JSON", e);
		}
	}

	/**
	 * metodo que manda una peticion a la api para recoger los grupos creados por el
	 * usuario
	 * 
	 * @author jpribio - 04/02/25
	 * @param ususarioParaFiltrar
	 * @return vista
	 */
	private Map<String, Object> obtenerGruposDelUsuario(UsuarioPerfilDto usuarioParaFiltrar) {
		Map<String, Object> datos = new HashMap<>();
		String url = RutasGenericas.rutaPrincipalApiString + "api/perfil/grupos";

		try {
			String usuarioJson = new ObjectMapper().writeValueAsString(usuarioParaFiltrar);
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<GruposListadoDto> listado = listadoGrupos(respuestaApi);
				datos.put("listadoGruposUsuario", listado);

				if (listado.isEmpty()) {
					datos.put("mensajeGrupo", "No se encontraron grupos disponibles.");
				}
			} else {
				datos.put("error", "Error al obtener los grupos: " + respuestaApi.getStatusInfo().toString());
			}
		} catch (Exception e) {
			datos.put("error", "Error al conectar con la API: " + e.getMessage());
		}

		return datos;
	}

	/**
	 * Metodo que manda la peticion y coge todos los grupos para que lo pueda
	 * observar el administrador
	 * 
	 * @author jpribio - 06/02/25
	 * @return vista
	 */
	private Map<String, Object> obtenerGruposParaAdmin() {
		Map<String, Object> datos = new HashMap<>();
		String url = RutasGenericas.rutaPrincipalApiString + "api/grupos";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<GruposListadoDto> listado = listadoGrupos(respuestaApi);
				datos.put("listadoGruposAdmin", listado);

				if (listado.isEmpty()) {
					datos.put("mensajeGrupo", "No se encontraron grupos disponibles.");
				}
			} else {
				datos.put("error", "Error al obtener los grupos: " + respuestaApi.getStatusInfo().toString());
			}
		} catch (Exception e) {
			datos.put("error", "Error al conectar con la API: " + e.getMessage());
		}

		return datos;
	}

	/**
	 * Metodo que hace la peticion a la api y devuelve todos los usuarios con el rol
	 * de user
	 * 
	 * @author jpribio - 06/02/25
	 * @return
	 */
	private Map<String, Object> obtenerUsuariosRolUser() {
		Map<String, Object> datos = new HashMap<>();
		String url = RutasGenericas.rutaPrincipalApiString + "api/usuariosPerfil";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<UsuarioPerfilDto> listado = listadoUsuarios(respuestaApi);
				datos.put("listadoUsuariosAdmin", listado);

				if (listado.isEmpty()) {
					datos.put("mensajeGrupo", "No se encontraron usuarios disponibles.");
				}
			} else {
				datos.put("error", "Error al obtener los usuarios: " + respuestaApi.getStatusInfo().toString());
			}
		} catch (Exception e) {
			datos.put("error", "Error al conectar con la API: " + e.getMessage());
		}

		return datos;
	}

	/**
	 * Metodo que hace la peticion a la api y devuelve todos los usuarios
	 * 
	 * @author jpribio - 06/02/25
	 * @return
	 */
	private Map<String, Object> obtenerUsuariosParaSAdmin() {
		Map<String, Object> datos = new HashMap<>();
		String url = RutasGenericas.rutaPrincipalApiString + "api/usuarioSAdminPerfil";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<UsuarioPerfilDto> listado = listadoUsuarios(respuestaApi);
				datos.put("listadoUsuariosSAdmin", listado);

				if (listado.isEmpty()) {
					datos.put("mensajeGrupo", "No se encontraron usuarios disponibles.");
				}
			} else {
				datos.put("error", "Error al obtener los usuarios: " + respuestaApi.getStatusInfo().toString());
			}
		} catch (Exception e) {
			datos.put("error", "Error al conectar con la API: " + e.getMessage());
		}

		return datos;
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
	private List<GruposListadoDto> listadoGrupos(Response respuestaApi) {
		List<GruposListadoDto> listaGrupos = new ArrayList<>();

		try {
			String jsonString = respuestaApi.readEntity(String.class);
			JSONObject jsonResponse = new JSONObject(jsonString);

			JSONArray gruposArray = jsonResponse.optJSONArray("gruposPerfil");

			if (gruposArray != null) {
				for (int i = 0; i < gruposArray.length(); i++) {
					JSONObject jsonGrupo = gruposArray.getJSONObject(i);
					GruposListadoDto grupo = new GruposListadoDto();
					grupo.setIdGrupo(jsonGrupo.getLong("idGrupo"));
					grupo.setNombreGrupo(jsonGrupo.optString("nombreGrupo"));
					grupo.setCategoriaNombre(jsonGrupo.optString("categoriaNombre"));
					grupo.setSubCategoriaNombre(jsonGrupo.optString("subCategoriaNombre"));
					grupo.setDescripcionGrupo(jsonGrupo.optString("descripcionGrupo"));

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
	 * Metodo privado que coge del texto plano los usuarios y los pasa a
	 * usuarioPerfilDto
	 * 
	 * @author jpribio - 06/02/25
	 * @param respuestaApi
	 * @return
	 */
	private List<UsuarioPerfilDto> listadoUsuarios(Response respuestaApi) {
		List<UsuarioPerfilDto> listadoUsuario = new ArrayList<>();

		try {
			String jsonString = respuestaApi.readEntity(String.class);
			JSONObject jsonResponse = new JSONObject(jsonString);

			JSONArray gruposArray = jsonResponse.optJSONArray("usuarioPerfil");

			if (gruposArray != null) {
				for (int i = 0; i < gruposArray.length(); i++) {
					JSONObject jsonUsuario = gruposArray.getJSONObject(i);
					UsuarioPerfilDto usuario = new UsuarioPerfilDto();
					usuario.setIdUsu(jsonUsuario.getLong("idUsu"));
					usuario.setAliasUsu(jsonUsuario.getString("aliasUsu"));
					usuario.setCorreoElectronicoUsu(jsonUsuario.getString("correoElectronicoUsu"));
					usuario.setEsPremium(jsonUsuario.getBoolean("esPremium"));
					usuario.setEsVerificadoEntidad(jsonUsuario.getBoolean("esVerificadoEntidad"));
					usuario.setFotoString(jsonUsuario.optString("fotoString"));
					usuario.setMovilUsu(jsonUsuario.getInt("movilUsu"));
					usuario.setNombreCompletoUsu(jsonUsuario.getString("nombreCompletoUsu"));
					usuario.setRolUsu(jsonUsuario.getString("rolUsu"));
					listadoUsuario.add(usuario);
				}
			} else {
				System.out.println("No se encontró el array 'usuarioPerfil' en la respuesta JSON.");
			}

		} catch (Exception e) {
			System.err.println("Error al parsear la respuesta JSON: " + e.getMessage());
		}

		return listadoUsuario;
	}

	/**
	 * Metodo que llama a la api para modificar al usuario y recarga la pagina
	 * 
	 * @author jpribio - 11/02/25
	 * @param usuarioAModificar
	 * @param usuarioPaFiltrar
	 * @return
	 */
	public UsuarioPerfilDto modificarUsuario(UsuarioPerfilDto dtoModificado, UsuarioPerfilDto usuarioSesion) {
		String URL_API = RutasGenericas.rutaPrincipalApiString + "api/ModificarUsuario";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			UsuarioPerfilDto combinado = combinarUsuario(dtoModificado, usuarioSesion);
			String json = objectMapper.writeValueAsString(combinado);
			try (Client client = ClientBuilder.newClient()) {
				Response apiResponse = client.target(URL_API).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(json, MediaType.APPLICATION_JSON));
				if (apiResponse.getStatus() == Response.Status.OK.getStatusCode() && apiResponse.hasEntity()) {
					UsuarioPerfilDto updated = apiResponse.readEntity(UsuarioPerfilDto.class);
					return updated;
				}
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Metodo de eliminar un elemento (ya sea usuario o un grupo siendo
	 * administrador)
	 * 
	 * @author jpribio - 14/02/25
	 * @param eliminarElemento
	 * @param sesion
	 * @return
	 */
	public boolean eliminarElemento(eliminarElementoPerfilDto eliminarElemento) {
		String URL_ELIMINAR = RutasGenericas.rutaPrincipalApiString + "api/EliminarElemento";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String json = objectMapper.writeValueAsString(eliminarElemento);
			try (Client client = ClientBuilder.newClient()) {
				Response apiResponse = client.target(URL_ELIMINAR).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(json, MediaType.APPLICATION_JSON));
				return apiResponse.getStatus() == Response.Status.OK.getStatusCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * MEtodo que envia el formulario del usuario con las modificaciones pertinentes
	 * 
	 * @author jpribio - 15/02/25
	 * @param usuarioAModificar
	 * @param sesion
	 * @return
	 */
	public boolean enviarUsuarioAModificarComoAdmin(UsuarioPerfilDto usuarioAModificar, HttpSession sesion) {
		String URL_MODIFICAR_ADMIN = RutasGenericas.rutaPrincipalApiString + "api/ModificarUsuarioComoAdmin";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			byte[] fotoBytes = obtenerFotoUsuario(usuarioAModificar.getFotoString(), usuarioAModificar.getFotoUsu());
			usuarioAModificar.setFotoUsu(fotoBytes);
			String usuarioJson = objectMapper.writeValueAsString(usuarioAModificar);
			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(URL_MODIFICAR_ADMIN).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));
				return respuestaApi.getStatus() == Response.Status.OK.getStatusCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo que envia el grupo con las caracteristicas cambiadas y devuelve una
	 * vista
	 * 
	 * @author jpribio - 15/02/25
	 * @param usuarioAModificar
	 * @param sesion
	 * @return
	 */
	public boolean enviarGrupoAModificarComoAdmin(GruposListadoDto grupoAModificar) {
		String URL_MODIFICAR_GRUPO_ADMIN = RutasGenericas.rutaPrincipalApiString + "api/ModificarGrupoComoAdmin";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String grupoJson = objectMapper.writeValueAsString(grupoAModificar);
			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(URL_MODIFICAR_GRUPO_ADMIN).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(grupoJson, MediaType.APPLICATION_JSON));
				return respuestaApi.getStatus() == Response.Status.OK.getStatusCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo que envia un usuario nuevo a la api para que se cree
	 * 
	 * @author jptibio - 16/02/25
	 * @param usuarioCreado
	 * @param sesion
	 * @return
	 */
	public boolean crearUsuarioComoAdmin(UsuarioPerfilDto usuarioCreado) {
		String URL_CREAR_ADMIN = RutasGenericas.rutaPrincipalApiString + "api/CrearUsuarioComoAdmin";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			// Encryptar contraseña y convertir fotoString a bytes si aplica
			if (usuarioCreado.getContraseniaUsu() != null) {
				usuarioCreado.setContraseniaUsu(utilidades.encriptarASHA256(usuarioCreado.getContraseniaUsu()));
			}
			byte[] fotoBytes = obtenerFotoUsuario(usuarioCreado.getFotoString(), usuarioCreado.getFotoUsu());
			usuarioCreado.setFotoUsu(fotoBytes);

			String usuarioJson = objectMapper.writeValueAsString(usuarioCreado);
			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(URL_CREAR_ADMIN).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));
				return respuestaApi.getStatus() == Response.Status.OK.getStatusCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo que envia un grupo nuevo a la api para que se cree
	 * 
	 * @author jptibio - 16/02/25
	 * @param usuarioCreado
	 * @param sesion
	 * @return
	 */
	public boolean crearGrupoComoAdmin(GruposDto grupoCreado) {
		String URL_CREAR_GRUPO_ADMIN = RutasGenericas.rutaPrincipalApiString + "api/CrearGrupoComoAdmin";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String grupoJson = objectMapper.writeValueAsString(grupoCreado);

			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(URL_CREAR_GRUPO_ADMIN).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(grupoJson, MediaType.APPLICATION_JSON));

				if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
					return true;
				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo que coge el grupo de la web y lo manda hacia la api
	 * 
	 * @author jpribio - 20/02/25
	 * @param nuevoComentarios
	 * @param sesion
	 * @return
	 */
	public boolean crearComentarioComoAdmin(ComentariosPerfilDto nuevoComentario) {
		String URL_CREAR_COMENTARIO = RutasGenericas.rutaPrincipalApiString + "api/CrearComentarioPerfil";
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String comentarioJson = objectMapper.writeValueAsString(nuevoComentario);

			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(URL_CREAR_COMENTARIO).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(comentarioJson, MediaType.APPLICATION_JSON));

				return respuestaApi.getStatus() == Response.Status.OK.getStatusCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Metodo para observar la pantalla de inicio en el perfil definiendo los casos
	 * posibles
	 * 
	 * @author jpribio - 14/02/25
	 * @param usuarioABuscar
	 * @param vista
	 * @return
	 */
	public Map<String, Object> obtenerDatosPerfil(UsuarioPerfilDto usuario) {
		Map<String, Object> modelo = new HashMap<>();

		if (usuario != null) {
			switch (usuario.getRolUsu()) {
			case "user":
				modelo.putAll(obtenerGruposDelUsuario(usuario));
				modelo.putAll(busquedaDelComentarioDelUsuario(usuario));
				break;

			case "admin":
				modelo.putAll(obtenerGruposParaAdmin());
				modelo.putAll(obtenerUsuariosRolUser());
				break;

			default: // Super admin u otros
				modelo.putAll(obtenerGruposParaAdmin());
				modelo.putAll(obtenerUsuariosParaSAdmin());
				break;
			}
			modelo.put("vista", "perfilUsuario");
		} else {
			modelo.put("vista", "error");
			modelo.put("error", "Usuario no encontrado en la sesión.");
		}

		return modelo;
	}

	/**
	 * Metodo que coge el comentario por defecto del usuario
	 * 
	 * @author jpribio - 04/02/25
	 * @param usuarioParaBuscar
	 * @return
	 */
	private Map<String, Object> busquedaDelComentarioDelUsuario(UsuarioPerfilDto usuario) {
		Map<String, Object> datos = new HashMap<>();
		String url = RutasGenericas.rutaPrincipalApiString + "api/perfil/comentario";

		try (Client cliente = ClientBuilder.newClient()) {
			String usuarioJson = new ObjectMapper().writeValueAsString(usuario);
			Response respuestaApi = cliente.target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				String jsonString = respuestaApi.readEntity(String.class);
				JSONObject jsonResponse = new JSONObject(jsonString);
				ComentariosPerfilDto comentario = obtenerComentario(jsonResponse);

				if (comentario != null) {
					datos.put("comentario", comentario);
				} else {
					datos.put("mensajePerfil", "No se encontraron comentarios para el usuario.");
				}
			} else {
				datos.put("error", "Error al obtener comentario: " + respuestaApi.getStatusInfo().toString());
			}
		} catch (Exception e) {
			datos.put("error", "Error al conectar con la API: " + e.getMessage());
		}

		return datos;
	}

	/**
	 * Metodo que mete los valores de la sesion del usuario con el usuario que se
	 * modifica
	 * 
	 * @author jpribio - 27/02/25
	 * @param usuarioAModificar
	 * @param usuarioPaFiltrar
	 * @return
	 */
	private UsuarioPerfilDto combinarUsuario(UsuarioPerfilDto usuarioAModificar, UsuarioPerfilDto usuarioPaFiltrar) {
		usuarioAModificar.setIdUsu(usuarioPaFiltrar.getIdUsu());
		usuarioAModificar.setCorreoElectronicoUsu(usuarioPaFiltrar.getCorreoElectronicoUsu());
		usuarioAModificar.setEsPremium(usuarioPaFiltrar.getEsPremium());
		usuarioAModificar.setEsVerificadoEntidad(usuarioPaFiltrar.getEsVerificadoEntidad());
		usuarioAModificar.setRolUsu(usuarioPaFiltrar.getRolUsu());
		usuarioAModificar
				.setFotoUsu(obtenerFotoUsuario(usuarioAModificar.getFotoString(), usuarioPaFiltrar.getFotoUsu()));

		return usuarioAModificar;
	}

	/**
	 * MEtodo privado que recoge el string de la imagen y la transforma a un array
	 * de bytes
	 * 
	 * @author jpribio - 27/02/25
	 * @param fotoString
	 * @param fotoBytesExistente
	 * @return
	 */
	private byte[] obtenerFotoUsuario(String fotoString, byte[] fotoBytesExistente) {
		if (fotoString != null && !fotoString.isEmpty()) {
			return Base64.getDecoder().decode(fotoString);
		}
		return fotoBytesExistente;
	}

}
