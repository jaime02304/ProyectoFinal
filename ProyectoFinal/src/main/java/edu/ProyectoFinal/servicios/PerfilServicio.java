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
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
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
	 * Metodo que coge el comentario por defecto del usuario
	 * 
	 * @author jpribio - 04/02/25
	 * @param usuarioParaBuscar
	 * @return
	 */
	public ModelAndView busquedaDelComentarioDelUsuario(UsuarioPerfilDto usuarioParaBuscar) {
		ModelAndView vista = new ModelAndView();
		String url = RutasGenericas.rutaPrincipalApiString + "api/perfil/comentario";

		try (Client cliente = ClientBuilder.newClient()) {
			String usuarioJson = new ObjectMapper().writeValueAsString(usuarioParaBuscar);
			Response respuestaApi = cliente.target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				String jsonString = respuestaApi.readEntity(String.class);
				JSONObject jsonResponse = new JSONObject(jsonString);
				ComentariosPerfilDto comentario = obtenerComentario(jsonResponse);

				if (comentario != null) {
					vista.addObject("comentario", comentario);
				} else {
					vista.addObject("mensajePerfil", "No se encontraron comentarios para el usuario.");
				}
			} else {
				vista.addObject("error", "No se ha encontrado ningún comentario debido a un error en la API.");
				vista.setViewName("error");
			}
		} catch (Exception e) {
			vista.addObject("error", "Error al conectar con la API: " + e.getMessage());
			vista.setViewName("error");
		}

		return vista;
	}

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
	public ModelAndView obtenerGruposDelUsuario(UsuarioPerfilDto ususarioParaFiltrar) {
		ModelAndView vista = new ModelAndView();
		String url = RutasGenericas.rutaPrincipalApiString + "api/perfil/grupos";

		try {
			String usuarioJson = new ObjectMapper().writeValueAsString(ususarioParaFiltrar);
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<GruposListadoDto> listadoGruposUsuario = listadoGrupos(respuestaApi);
				vista.addObject("listadoGruposUsuario", listadoGruposUsuario);

				if (listadoGruposUsuario.isEmpty()) {
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
	 * Metodo que manda la peticion y coge todos los grupos para que lo pueda
	 * observar el administrador
	 * 
	 * @author jpribio - 06/02/25
	 * @return vista
	 */
	public ModelAndView obtenerGruposParaAdmin() {
		ModelAndView vista = new ModelAndView();
		String url = RutasGenericas.rutaPrincipalApiString + "api/grupos";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<GruposListadoDto> listadoGruposAdmin = listadoGrupos(respuestaApi);
				vista.addObject("listadoGruposAdmin", listadoGruposAdmin);

				if (listadoGruposAdmin.isEmpty()) {
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
	 * Metodo que hace la peticion a la api y devuelve todos los usuarios con el rol
	 * de user
	 * 
	 * @author jpribio - 06/02/25
	 * @return
	 */
	public ModelAndView obtenerUsuariosRolUser() {
		ModelAndView vista = new ModelAndView();
		String url = RutasGenericas.rutaPrincipalApiString + "api/usuariosPerfil";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<UsuarioPerfilDto> listadoUsuario = listadoUsuarios(respuestaApi);
				vista.addObject("listadoUsuariosAdmin", listadoUsuario);

				if (listadoUsuario.isEmpty()) {
					vista.addObject("mensajeGrupo", "No se encontraron usuarios disponibles.");
				}
			} else {
				vista.addObject("error", "Error al obtener los usuarios: " + respuestaApi.getStatusInfo().toString());
			}
		} catch (Exception e) {
			vista.addObject("error", "Error al conectar con la API: " + e.getMessage());
		}

		return vista;
	}

	/**
	 * Metodo que hace la peticion a la api y devuelve todos los usuarios
	 * 
	 * @author jpribio - 06/02/25
	 * @return
	 */
	public ModelAndView obtenerUsuariosParaSAdmin() {
		ModelAndView vista = new ModelAndView();
		String url = RutasGenericas.rutaPrincipalApiString + "api/usuarioSAdminPerfil";

		try {
			Response respuestaApi = ClientBuilder.newClient().target(url).request(MediaType.APPLICATION_JSON).get();

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				List<UsuarioPerfilDto> listadoUsuario = listadoUsuarios(respuestaApi);
				vista.addObject("listadoUsuariosSAdmin", listadoUsuario);

				if (listadoUsuario.isEmpty()) {
					vista.addObject("mensajeGrupo", "No se encontraron usuarios disponibles.");
				}
			} else {
				vista.addObject("error", "Error al obtener los usuarios: " + respuestaApi.getStatusInfo().toString());
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
	public ResponseEntity<?> modificarUsuario(UsuarioPerfilDto usuarioAModificar, HttpSession sesion) {
		String url = RutasGenericas.rutaPrincipalApiString + "api/ModificarUsuario";

		try {
			// Obtener el usuario actual de la sesión y combinarlo con los datos a modificar
			UsuarioPerfilDto usuarioPaFiltrar = (UsuarioPerfilDto) sesion.getAttribute("Usuario");
			usuarioAModificar = combinarUsuario(usuarioAModificar, usuarioPaFiltrar);

			// Convertir el objeto a JSON para enviarlo a la API
			String usuarioJson = new ObjectMapper().writeValueAsString(usuarioAModificar);

			// Usamos try-with-resources para asegurar el cierre del cliente
			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(url).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));

				// Leer la respuesta como un objeto UsuarioPerfilDto
				UsuarioPerfilDto usuarioPerfil = respuestaApi.readEntity(UsuarioPerfilDto.class);

				if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode() && usuarioPerfil != null) {
					// Actualizar la sesión con el usuario modificado
					sesion.setAttribute("Usuario", usuarioPerfil);
					return ResponseEntity.ok("Se ha modificado el usuario exitosamente");
				} else {
					String errorMsg = "Error al modificar el usuario: " + respuestaApi.getStatusInfo();
					return ResponseEntity.status(respuestaApi.getStatus()).body(errorMsg);
				}
			}
		} catch (Exception e) {
			String errorMsg = "Error al conectar con la API: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
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
	public ResponseEntity<?> enviarElementoParaBorrar(eliminarElementoPerfilDto eliminarElemento, HttpSession sesion) {
		String url = RutasGenericas.rutaPrincipalApiString + "api/EliminarElemento";

		try {
			// Convertir el objeto a JSON para enviarlo a la API
			String elementoJson = new ObjectMapper().writeValueAsString(eliminarElemento);

			// Usar try-with-resources para gestionar el cierre del cliente automáticamente
			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(url).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(elementoJson, MediaType.APPLICATION_JSON));

				// Convertir la respuesta en un Map
				Map<String, String> respuestaMap = respuestaApi.readEntity(new GenericType<Map<String, String>>() {
				});

				if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()
						&& respuestaMap.containsKey("message")) {
					// Actualizar la sesión si es necesario
					UsuarioPerfilDto usuarioPerfil = (UsuarioPerfilDto) sesion.getAttribute("Usuario");
					sesion.setAttribute("Usuario", usuarioPerfil);
					return ResponseEntity.ok(respuestaMap); // Devuelve el mismo Map con el mensaje de éxito
				} else {
					String errorMsg = respuestaMap.getOrDefault("error", "Error desconocido al eliminar el elemento.");
					return ResponseEntity.status(respuestaApi.getStatus()).body(Map.of("error", errorMsg));
				}
			}
		} catch (Exception e) {
			String errorMsg = "Error al conectar con la API: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", errorMsg));
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
	public ResponseEntity<Map<String, Object>> enviarUsuarioAModificarComoAdmin(UsuarioPerfilDto usuarioAModificar,
			HttpSession sesion) {
		String url = RutasGenericas.rutaPrincipalApiString + "api/ModificarUsuarioComoAdmin";
		Map<String, Object> responseMap = new HashMap<>();

		try {
			usuarioAModificar
					.setFotoUsu(obtenerFotoUsuario(usuarioAModificar.getFotoString(), usuarioAModificar.getFotoUsu()));
			String usuarioJson = new ObjectMapper().writeValueAsString(usuarioAModificar);
			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(url).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));
				// Convertir la respuesta de la API (en JSON) a un Map
				String jsonResponse = respuestaApi.readEntity(String.class);
				responseMap = new ObjectMapper().readValue(jsonResponse, new TypeReference<Map<String, Object>>() {
				});
			}
			if (responseMap.containsKey("message")
					&& "Usuario modificado correctamente.".equals(responseMap.get("message"))) {
				// Actualizar datos del usuario en la sesión si la modificación fue exitosa
				UsuarioPerfilDto usuarioPerfil = (UsuarioPerfilDto) sesion.getAttribute("Usuario");
				sesion.setAttribute("Usuario", usuarioPerfil);
				return ResponseEntity.ok(responseMap);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
			}
		} catch (Exception e) {
			responseMap.put("error", "Error al conectar con la API: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
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
	public ResponseEntity<Map<String, Object>> enviarGrupoAModificarComoAdmin(GruposListadoDto grupoAModificar,
			HttpSession sesion) {
		String url = RutasGenericas.rutaPrincipalApiString + "api/ModificarGrupoComoAdmin";
		Map<String, Object> responseMap = new HashMap<>();

		try {
			String grupoJson = new ObjectMapper().writeValueAsString(grupoAModificar);

			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(url).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(grupoJson, MediaType.APPLICATION_JSON));

				// Convertir la respuesta de la API (en JSON) a un Map
				String jsonResponse = respuestaApi.readEntity(String.class);
				responseMap = new ObjectMapper().readValue(jsonResponse, new TypeReference<Map<String, Object>>() {
				});
			}
			if (responseMap.containsKey("message")
					&& "Grupo modificado correctamente.".equals(responseMap.get("message"))) {
				return ResponseEntity.ok(responseMap);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
			}
		} catch (Exception e) {
			responseMap.put("error", "Error al conectar con la API: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
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
	public ResponseEntity<Map<String, Object>> crearUsuarioComoAdmin(UsuarioPerfilDto usuarioCreado,
			HttpSession sesion) {
		Map<String, Object> responseMap = new HashMap<>();
		String url = RutasGenericas.rutaPrincipalApiString + "api/CrearUsuarioComoAdmin";

		try {
			// Convertir el objeto a JSON para enviarlo a la API
			usuarioCreado.setContraseniaUsu(utilidades.encriptarASHA256(usuarioCreado.getContraseniaUsu()));
			usuarioCreado.setFotoUsu(obtenerFotoUsuario(usuarioCreado.getFotoString(), usuarioCreado.getFotoUsu()));
			String usuarioJson = new ObjectMapper().writeValueAsString(usuarioCreado);

			// Usar try-with-resources para gestionar el cierre del cliente automáticamente
			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(url).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));

				// Leer la respuesta de la API como un Map
				String jsonResponse = respuestaApi.readEntity(String.class);
				responseMap = new ObjectMapper().readValue(jsonResponse, new TypeReference<Map<String, Object>>() {
				});

				// Validar la respuesta y actuar en consecuencia
				if (responseMap.containsKey("message")
						&& "Usuario creado correctamente.".equals(responseMap.get("message"))) {
					UsuarioPerfilDto usuarioPerfil = (UsuarioPerfilDto) sesion.getAttribute("Usuario");
					usuarioPerfil.setFotoUsu(Base64.getDecoder().decode(usuarioPerfil.getFotoString()));
					sesion.setAttribute("Usuario", usuarioPerfil);
					return ResponseEntity.ok(responseMap);
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
				}
			}
		} catch (Exception e) {
			String errorMsg = "Error al conectar con la API: " + e.getMessage();
			responseMap.put("message", errorMsg); // Mensaje de error
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
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
	public ResponseEntity<?> crearGrupoComoAdmin(GruposDto grupoCreado, HttpSession sesion) {
		String url = RutasGenericas.rutaPrincipalApiString + "api/CrearGrupoComoAdmin";

		try {
			// Convertir el objeto a JSON para enviarlo a la API
			String grupoJson = new ObjectMapper().writeValueAsString(grupoCreado);

			// Usamos try-with-resources para asegurar el cierre del cliente
			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(url).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(grupoJson, MediaType.APPLICATION_JSON));

				// Convertir la respuesta a un Map para analizar los datos recibidos
				Map<String, Object> respuestaMap = respuestaApi.readEntity(new GenericType<Map<String, Object>>() {
				});

				if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()
						&& respuestaMap.containsKey("grupo")) {

					Object grupo = respuestaMap.get("grupo");

					// Validar si el grupo está vacío o es un string vacío
					if (grupo == null || grupo.toString().trim().isEmpty()) {
						String errorMsg = "El grupo ya existe.";
						return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMsg);
					} else {
						return ResponseEntity.ok("Se ha creado el grupo exitosamente");
					}
				} else {
					String errorMsg = "Error al crear el grupo: " + respuestaApi.getStatusInfo();
					return ResponseEntity.status(respuestaApi.getStatus()).body(errorMsg);
				}
			}
		} catch (Exception e) {
			String errorMsg = "Error al conectar con la API: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
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
	public ResponseEntity<?> crearComentarioPerfil(ComentariosPerfilDto nuevoComentarios, HttpSession sesion) {
		String url = RutasGenericas.rutaPrincipalApiString + "api/CrearComentarioPerfil";
		try {
			String comentarioJson = new ObjectMapper().writeValueAsString(nuevoComentarios);

			try (Client client = ClientBuilder.newClient()) {
				Response respuestaApi = client.target(url).request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(comentarioJson, MediaType.APPLICATION_JSON));
				Map<String, Object> respuestaMap = respuestaApi.readEntity(new GenericType<Map<String, Object>>() {
				});
				if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
					if (respuestaMap.containsKey("comentario")) {
						Object comentario = respuestaMap.get("comentario");
						if (comentario == null || comentario.toString().trim().isEmpty()) {
							String errorMsg = "No se pudo crear el comentario.";
							return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMsg);
						} else {
							return ResponseEntity.ok("Se ha creado el comentario exitosamente.");
						}
					} else {
						String errorMsg = "Error al crear el comentario: No se recibió información válida.";
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
					}
				} else {
					String errorMsg = "Error al crear el comentario: " + respuestaApi.getStatusInfo();
					return ResponseEntity.status(respuestaApi.getStatus()).body(errorMsg);
				}
			}
		} catch (Exception e) {
			String errorMsg = "Error al conectar con la API: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
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
	public ModelAndView condicionYCasosPerfil(UsuarioPerfilDto usuarioABuscar, ModelAndView vista) {
		if (usuarioABuscar != null) {
			switch (usuarioABuscar.getRolUsu()) {
			case "user":
				vista = obtenerGruposDelUsuario(usuarioABuscar);
				busquedaDelComentarioDelUsuario(usuarioABuscar).getModel().forEach(vista::addObject);
				break;

			case "admin":
				vista = obtenerGruposParaAdmin();
				obtenerUsuariosRolUser().getModel().forEach(vista::addObject);
				break;

			default: // Super Admin u otros roles
				vista = obtenerGruposParaAdmin();
				obtenerUsuariosParaSAdmin().getModel().forEach(vista::addObject);
				break;
			}
			vista.setViewName("perfilUsuario");
		} else {
			vista.setViewName("error");
			vista.addObject("error", "Usuario no encontrado en la sesión.");
		}
		return vista;
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
