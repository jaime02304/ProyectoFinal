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
import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.Dto.UsuarioRegistroDto;
import edu.ProyectoFinal.Utilidades.Util;
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
	 * @author jpribio - 27/01/25
	 * @param usuarioNuevo
	 * @return
	 * @throws NullPointerException
	 * @throws Exception
	 * @throws IllegalArgumentException
	 */
	public ModelAndView nuevoUsuario(UsuarioRegistroDto usuarioNuevo, HttpSession sesionIniciada) {
		ModelAndView vista = new ModelAndView("InicioSesion");

		if (!validarEmail(usuarioNuevo.getCorreoElectronicoUsu())) {
			vista.addObject("error", "Correo electrónico inválido.");
			return vista;
		}

		String url = "http://localhost:8081/api/usuario/registro";

		try (Client cliente = ClientBuilder.newClient()) {
			usuarioNuevo.setContraseniaUsu(utilidades.encriptarASHA256(usuarioNuevo.getContraseniaUsu()));
			String usuarioJson = new ObjectMapper().writeValueAsString(usuarioNuevo);

			Response respuestaApi = cliente.target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));

			if (respuestaApi.getStatus() == Response.Status.OK.getStatusCode()) {
				UsuarioPerfilDto usuarioPerfil = respuestaApi.readEntity(UsuarioPerfilDto.class);
				sesionIniciada.setAttribute("Usuario", usuarioPerfil);
				sesionIniciada.setMaxInactiveInterval(60 * 60 * 24 * 7);
				vista = servicioGrupos.obtenerLosGruposTops();
				vista.setViewName("LandinPage");
				vista.addObject("infoVerificacion",
						"Registro completado con éxito. Por favor, revisa tu correo electrónico para verificar tu cuenta.");
			} else {
				vista.setViewName("error");
				vista.addObject("error", "Ha habido un error con la web, por favor vuelva en 5 minutos.");
			}
		} catch (Exception e) {
			vista.setViewName("error");
			vista.addObject("error", "Ocurrió un problema inesperado. Inténtelo más tarde.");
		}

		return vista;
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
	public ModelAndView inicioSesion(UsuarioRegistroDto usuario, HttpSession sesion) {
		ModelAndView vista = new ModelAndView("InicioSesion");

		if (!validarEmail(usuario.getCorreoElectronicoUsu())) {
			vista.addObject("error", "Correo electrónico inválido.");
			return vista;
		}

		String url = "http://localhost:8081/api/usuario/inicioSesion";

		try (Client cliente = ClientBuilder.newClient()) {
			usuario.setContraseniaUsu(utilidades.encriptarASHA256(usuario.getContraseniaUsu()));
			String usuarioJson = new ObjectMapper().writeValueAsString(usuario);

			Response respuesta = cliente.target(url).request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));

			if (respuesta.getStatus() != Response.Status.OK.getStatusCode()) {
				vista.setViewName("error");
				vista.addObject("error", "Ha habido un error con la web, por favor intente más tarde.");
				return vista;
			}

			UsuarioPerfilDto usuarioPerfil = respuesta.readEntity(UsuarioPerfilDto.class);
			if (usuarioPerfil != null
					&& usuarioPerfil.getCorreoElectronicoUsu().equals(usuario.getCorreoElectronicoUsu())) {
				sesion.setAttribute("Usuario", usuarioPerfil);
				sesion.setMaxInactiveInterval(60 * 60 * 24 * 7);
				vista = servicioGrupos.obtenerLosGruposTops();
				vista.setViewName("LandinPage");
			} else {
				vista.setViewName("InicioSesion");
				vista.addObject("mensaje", "El usuario no ha sido encontrado.");
			}
		} catch (Exception e) {
			vista.setViewName("error");
			vista.addObject("error", "Ocurrió un problema inesperado. Inténtelo más tarde.");
		}

		return vista;
	}

	/**
	 * Metodo que envia el correo electronico hacia la api
	 * 
	 * @author jpribio - 21/04/25
	 * @param correo
	 * @return
	 */
	public boolean enviarCorreoRecuperacion(String correo) {
		String url = "http://localhost:8081/api/usuario/recuperarContrasena";

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
		String url = "http://localhost:8081/api/usuario/cambiarContrasena";

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
	public ModelAndView nuevoUsuarioGoogle(String code, HttpSession sesionIniciada) {
		ModelAndView vista = new ModelAndView("InicioSesion");

		try (Client cliente = ClientBuilder.newClient()) {
			// Obtener access token de Google
			String accessToken = obtenerAccessToken(cliente, code);
			if (accessToken == null) {
				return manejarError(vista, "Error al obtener token de Google.");
			}

			// Obtener información del usuario
			JsonNode userInfo = obtenerUserInfo(cliente, accessToken);
			if (userInfo == null) {
				return manejarError(vista, "Error al obtener datos del usuario.");
			}

			// Crear DTO de registro
			UsuarioRegistroDto usuarioNuevo = crearUsuarioRegistroDto(userInfo);

			// Registrar usuario en nuestra API
			Response respuestaApi = registrarUsuarioEnApi(cliente, usuarioNuevo);
			if (respuestaApi.getStatus() != Response.Status.OK.getStatusCode()) {
				return manejarError(vista, "Error al registrar el usuario en la plataforma.");
			}

			// Manejar respuesta exitosa
			manejarRegistroExitoso(respuestaApi, sesionIniciada, vista);

		} catch (Exception e) {
			return manejarError(vista, "Problema inesperado, inténtelo más tarde.");
		}

		return vista;
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
		String urlApi = "http://localhost:8081/api/usuario/googleLogin";
		String usuarioJson = new ObjectMapper().writeValueAsString(usuario);

		return cliente.target(urlApi).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(usuarioJson, MediaType.APPLICATION_JSON));
	}

	/**
	 * Metodo para manejar la informacion si viene de manera exitosa la informacion
	 * 
	 * @author jpribio - 26/04/25
	 * @param respuestaApi
	 * @param sesion
	 * @param vista
	 */
	private void manejarRegistroExitoso(Response respuestaApi, HttpSession sesion, ModelAndView vista) {
		sesion.setAttribute("Usuario", respuestaApi.readEntity(UsuarioPerfilDto.class));
		sesion.setMaxInactiveInterval(60 * 60 * 24 * 7);
		vista.setViewName("LandinPage");
		vista.addObject("infoVerificacion", "¡¡Bienvenido usuario!!");
	}

	/**
	 * Metodo para manejar la informacion si viene de erronea exitosa la informacion
	 * 
	 * @author jpribio - 26/04/25
	 * @param vista
	 * @param mensajeError
	 * @return
	 */
	private ModelAndView manejarError(ModelAndView vista, String mensajeError) {
		vista.setViewName("error");
		vista.addObject("error", mensajeError);
		return vista;
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
		String API_URL_ALIAS = "http://localhost:8081/api/usuario/alias";
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
