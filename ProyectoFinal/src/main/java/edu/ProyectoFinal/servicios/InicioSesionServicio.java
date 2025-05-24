package edu.ProyectoFinal.servicios;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ProyectoFinal.Configuraciones.OAuthConfiguracion;
import edu.ProyectoFinal.Configuraciones.RutasGenericas;
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.Dto.UsuarioRegistroDto;
import edu.ProyectoFinal.Utilidades.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Clase donde se encuentra todos los metodos en relacion con la logica del
 * inicio de sesion y servicio
 * 
 * @author jpribio - 27/01/25
 */
public class InicioSesionServicio {

	GruposServicios servicioGrupos = new GruposServicios();

	ComentariosServicios servicioComentarios = new ComentariosServicios();

	Util utilidades = new Util();

	// representa un patrón de expresión regular para luego compararla
	private Pattern email1 = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.(com|net|es)$");

	/**
	 * Metodo privado que valida el email correctamente
	 *
	 * @author jpribio - 27/01/25
	 * @param correo electronico introducido por el usuario
	 * @return devuelve el email validado
	 */
	private boolean validarEmail(String email) {
		if (email == null) {
			return false;
		}
		return email1.matcher(email).matches();
	}

	/**
	 * Metodo que crea el nuevo usuario
	 * 
	 * @param usuarioNuevo DTO con datos de registro
	 * @param session      sesión HTTP donde se almacenará el perfil
	 * @return true si todo OK, false en caso contrario (y atributo "error" en
	 *         sesión o request)
	 */
	public boolean registrarUsuario(UsuarioRegistroDto usuarioNuevo, HttpSession session) {
		String apiRegistroUrl = RutasGenericas.rutaPrincipalApiString + "api/usuario/registro";
		if (!validarEmail(usuarioNuevo.getCorreoElectronicoUsu())) {
			session.setAttribute("error", "Correo electrónico inválido.");
			return false;
		}

		try (Client cliente = ClientBuilder.newClient()) {
			usuarioNuevo.setContraseniaUsu(utilidades.encriptarASHA256(usuarioNuevo.getContraseniaUsu()));
			String usuarioJson = new ObjectMapper().writeValueAsString(usuarioNuevo);

			Response resp = cliente.target(apiRegistroUrl).request(MediaType.APPLICATION_JSON)
					.post(Entity.json(usuarioJson));

			if (resp.getStatus() == Response.Status.OK.getStatusCode()) {
				UsuarioPerfilDto perfil = resp.readEntity(UsuarioPerfilDto.class);
				session.setAttribute("Usuario", perfil);
				session.setMaxInactiveInterval(60 * 60 * 24 * 7); // 7 días
				return true;
			} else {
				session.setAttribute("error", "Ha habido un error con la web, por favor vuelva en 5 minutos.");
			}

		} catch (Exception e) {
			session.setAttribute("error", "Ocurrió un problema inesperado. Inténtelo más tarde.");
		}
		return false;
	}

	/**
	 * Metodo que manda los dato necesarios hacia la api para buscar al usuario en
	 * la base de datos y tener la informacion de su perfil
	 * 
	 * @author jpribio - 28/01/25
	 * @param buscarUsuario
	 * @param sesionIniciada
	 * @return
	 * @throws NullPointerException
	 * @throws Exception
	 * @throws IllegalArgumentException
	 */
	public boolean autenticarUsuario(UsuarioRegistroDto usuario, HttpSession session, HttpServletRequest request) {
		String apiLoginUrl = RutasGenericas.rutaPrincipalApiString + "api/usuario/inicioSesion";
		if (!validarEmail(usuario.getCorreoElectronicoUsu())) {
			request.setAttribute("error", "Correo electrónico inválido.");
			return false;
		}
		try (Client cliente = ClientBuilder.newClient()) {
			usuario.setContraseniaUsu(utilidades.encriptarASHA256(usuario.getContraseniaUsu()));
			String usuarioJson = new ObjectMapper().writeValueAsString(usuario);

			Response resp = cliente.target(apiLoginUrl).request(MediaType.APPLICATION_JSON)
					.post(Entity.json(usuarioJson));

			if (resp.getStatus() != Response.Status.OK.getStatusCode()) {
				request.setAttribute("error", "Ha habido un error con la web, por favor intente más tarde.");
				return false;
			}

			UsuarioPerfilDto perfil = resp.readEntity(UsuarioPerfilDto.class);
			if (perfil == null || !perfil.getCorreoElectronicoUsu().equals(usuario.getCorreoElectronicoUsu())) {
				request.setAttribute("mensaje", "El usuario no ha sido encontrado.");
				return false;
			}

			session.setAttribute("Usuario", perfil);
			session.setMaxInactiveInterval(60 * 60 * 24 * 7);
			return true;

		} catch (Exception e) {
			request.setAttribute("error", "Ocurrió un problema inesperado. Inténtelo más tarde.");
			return false;
		}
	}

	/**
	 * Metodo que envia el correo electronico hacia la api
	 * 
	 * @author jpribio - 21/04/25
	 * @param correo
	 * @return
	 */
	public boolean enviarCorreoRecuperacion(String correo) {
		String url = RutasGenericas.rutaPrincipalApiString + "api/usuario/recuperarContrasena";

		try (Client cliente = ClientBuilder.newClient()) {
			Map<String, String> datos = new HashMap<>();
			datos.put("correo", correo);
			String json = new ObjectMapper().writeValueAsString(datos);

			Response respuesta = cliente.target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(json, MediaType.APPLICATION_JSON));

			if (respuesta.getStatus() == Response.Status.OK.getStatusCode()) {
				return true;
			} else {
				System.err.println("Error al recuperar contraseña: Código " + respuesta.getStatus());
			}
		} catch (Exception e) {
			System.err.println("Excepción al recuperar contraseña: " + e.getMessage());
		}

		return false;
	}

	/**
	 * Metodo que manda la nueva contraseña y el token a la api
	 * 
	 * @author jpribio - 21/04/25
	 * @param token
	 * @param nuevaContrasena
	 * @return
	 */
	public boolean realizarCambioContrasena(String token, String nuevaContrasena) {
		String url = RutasGenericas.rutaPrincipalApiString + "api/usuario/cambiarContrasena";

		try (Client cliente = ClientBuilder.newClient()) {
			Map<String, String> datos = new HashMap<>();
			datos.put("token", token);
			datos.put("nuevaContrasena", utilidades.encriptarASHA256(nuevaContrasena));

			String json = new ObjectMapper().writeValueAsString(datos);

			Response respuesta = cliente.target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(json, MediaType.APPLICATION_JSON));

			return respuesta.getStatus() == Response.Status.OK.getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * MEtodo para realizar un nuevo usuario y mandarlo a la api con google
	 * 
	 * @author jpribio - 26/04/25
	 * @param code
	 * @param sesionIniciada
	 * @return
	 */
	public boolean nuevoUsuarioGoogle(String code, HttpServletRequest request, HttpSession session) {
		String accessToken;
		try (Client cliente = ClientBuilder.newClient()) {
			accessToken = obtenerAccessToken(cliente, code);
		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener token de Google.");
			return false;
		}
		if (accessToken == null) {
			request.setAttribute("error", "Error al obtener token de Google.");
			return false;
		}
		JsonNode userInfo;
		try (Client cliente = ClientBuilder.newClient()) {
			userInfo = obtenerUserInfo(cliente, accessToken);
		} catch (Exception e) {
			request.setAttribute("error", "Error al obtener datos del usuario.");
			return false;
		}
		if (userInfo == null) {
			request.setAttribute("error", "Error al obtener datos del usuario.");
			return false;
		}
		UsuarioRegistroDto usuarioNuevo = crearUsuarioRegistroDto(userInfo);
		Client clienteApi = ClientBuilder.newClient();
		UsuarioPerfilDto perfil;
		try {
			Response respuestaApi = registrarUsuarioEnApi(clienteApi, usuarioNuevo);

			if (respuestaApi.getStatus() != Response.Status.OK.getStatusCode()) {
				respuestaApi.close();
				request.setAttribute("error", "Error al registrar el usuario en la plataforma.");
				return false;
			}
			perfil = respuestaApi.readEntity(UsuarioPerfilDto.class);
			respuestaApi.close();
		} catch (Exception e) {
			request.setAttribute("error", "Error al registrar el usuario en la plataforma.");
			return false;
		} finally {
			clienteApi.close();
		}
		session.setAttribute("Usuario", perfil);
		session.setMaxInactiveInterval(60 * 60 * 24 * 7);
		request.setAttribute("infoVerificacion", "¡¡Bienvenido usuario!!");

		return true;
	}

	/**
	 * Metodo para tener acceso al token de google
	 * 
	 * @author jpribio - 26/04/25
	 * @param cliente
	 * @param code
	 * @return
	 * @throws IOException
	 */
	private String obtenerAccessToken(Client cliente, String code) throws IOException {
		Form form = new Form().param("code", code).param("client_id", OAuthConfiguracion.CLIENT_ID)
				.param("client_secret", OAuthConfiguracion.CLIENT_SECRET)
				.param("redirect_uri", OAuthConfiguracion.REDIRECT_URI).param("grant_type", "authorization_code");

		Response response = cliente.target(OAuthConfiguracion.TOKEN_ENDPOINT).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		if (response.getStatus() != 200)
			return null;

		String tokenJson = response.readEntity(String.class);
		return new ObjectMapper().readTree(tokenJson).get("access_token").asText();
	}

	/**
	 * Metodo para tener la info del usuario
	 * 
	 * @author jpribio - 26/04/25
	 * @param cliente
	 * @param accessToken
	 * @return
	 * @throws IOException
	 */
	private JsonNode obtenerUserInfo(Client cliente, String accessToken) throws IOException {
		Response response = cliente.target(OAuthConfiguracion.USERINFO_ENDPOINT).request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken).get();

		if (response.getStatus() != 200)
			return null;

		return new ObjectMapper().readTree(response.readEntity(String.class));
	}

	/**
	 * Metodo para crear un nuevo dto para registra el usuario
	 * 
	 * @author jpribio - 26/04/25
	 * @param userInfo
	 * @return
	 */
	private UsuarioRegistroDto crearUsuarioRegistroDto(JsonNode userInfo) {
		UsuarioRegistroDto dto = new UsuarioRegistroDto();
		dto.setNombreCompletoUsu(userInfo.get("name").asText());
		dto.setAliasUsu(generarAliasAleatorio());
		dto.setCorreoElectronicoUsu(userInfo.get("email").asText());
		dto.setContraseniaUsu(utilidades.encriptarASHA256(userInfo.get("name").asText()));
		dto.setEsConGoogle(true);
		return dto;
	}

	/**
	 * Metodo para mandar el usuario hacia la api para ver si es un registro o un
	 * login
	 * 
	 * @author jpribio - 26/04/25
	 * @param cliente
	 * @param usuario
	 * @return
	 * @throws JsonProcessingException
	 */
	private Response registrarUsuarioEnApi(Client cliente, UsuarioRegistroDto usuario) throws JsonProcessingException {
		String urlApi = RutasGenericas.rutaPrincipalApiString + "api/usuario/googleLogin";
		String usuarioJson = new ObjectMapper().writeValueAsString(usuario);

		return cliente.target(urlApi).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));
	}

	/**
	 * Metodo para generar un alias aleatorio diferente a todos los anteriores de 9
	 * caracteres
	 * 
	 * @author jpribio - 26/04/25
	 * @return
	 */
	private String generarAliasAleatorio() {
		final String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		final int longitudAlias = 9;
		SecureRandom random = new SecureRandom();
		List<String> aliasExistentes = obtenerTodosLosAlias();

		if (aliasExistentes == null) {
			throw new RuntimeException("No se pudo obtener los alias desde la API");
		}

		String aliasGenerado;
		do {
			StringBuilder alias = new StringBuilder(longitudAlias);
			for (int i = 0; i < longitudAlias; i++) {
				int index = random.nextInt(caracteres.length());
				alias.append(caracteres.charAt(index));
			}
			aliasGenerado = alias.toString();
		} while (aliasExistentes.contains(aliasGenerado));

		return aliasGenerado;
	}

	/**
	 * Metodo para recoger todos los alias existentes en la base de datos
	 * 
	 * @author jpribio - 26/04/25
	 * @return
	 */
	private List<String> obtenerTodosLosAlias() {
		String API_URL_ALIAS = RutasGenericas.rutaPrincipalApiString + "api/usuario/alias";
		try (Client cliente = ClientBuilder.newClient()) {
			WebTarget target = cliente.target(API_URL_ALIAS);
			return target.request(MediaType.APPLICATION_JSON).get(new GenericType<List<String>>() {
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
