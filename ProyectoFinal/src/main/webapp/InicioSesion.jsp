<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>AnimeXp</title>
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/imagenes/Gemini_Generated_Image_3majls3majls3maj.jpg"
	type="image/x-icon" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estilo.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estiloiniciosesion.css" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous" />
<link
	href="https://fonts.googleapis.com/css2?family=Bangers&display=swap"
	rel="stylesheet" />
<link
	href="https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap"
	rel="stylesheet" />
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
	rel="stylesheet" />
</head>
<body
	style="	background:
		url('<%=request.getContextPath()%>/imagenes/artbreeder-image-2024-12-14T13_41_59.055Z.jpeg')
		no-repeat top left/cover">
	<!-- Header completo -->
	<header>
		<div class="d-none d-md-block">
			<a href="<%=request.getContextPath()%>/"><button
					class="botonNavegador botonVolvermd">
					<i class="fas fa-arrow-left"></i> Volver
				</button></a>
		</div>
		<div class="d-block d-md-none">
			<a href="<%=request.getContextPath()%>/"><button
					class="botonNavegador botonVolversm">
					<i class="fas fa-arrow-left"></i> Volver
				</button></a>
		</div>
	</header>
	<!-- Main completo -->
	<main class="contenedorMain">
		<div class="d-none d-md-block zonaCompleta">
			<div class="container" id="container">
				<div class="formularioContenedor sign-up-container">
					<form action="${pageContext.request.contextPath}/Registro"
						method="post" id="formRegistro">
						<h2 class="registroTitulo">Registrarse</h2>
						<input type="text" placeholder="Nombre Completo"
							name="nombreCompletoUsu"
							value="${nuevoUsuario.nombreCompletoUsu}" required /> <input
							type="text" placeholder="Alias" name="aliasUsu"
							value="${nuevoUsuario.aliasUsu}" required /> <input type="email"
							placeholder="Correo electrónico" name="correoElectronicoUsu"
							value="${nuevoUsuario.correoElectronicoUsu}" required /> <input
							type="password" placeholder="Contraseña" name="contraseniaUsu"
							id="contraseniaUsu" value="${nuevoUsuario.contraseniaUsu}"
							required /> <input type="password"
							placeholder="Repetir contraseña" id="repetirContrasenia" />
						<div class="debajoDeLosInputs">
							<div class="social-contenedor2">
								<a href="<%=request.getContextPath()%>/loginGoogle"
									class="social"><i class="fab fa-google-plus-g"></i></a>
							</div>
							<button type="submit" class="botonNavegador botonRegistro">
								Registrarse</button>
						</div>
					</form>
				</div>

				<div class="formularioContenedor sign-in-container">
					<form action="${pageContext.request.contextPath}/IS" method="post">
						<h2 class="inicioSesionTitulo">Inicio sesion</h2>
						<c:if test="${not empty mensaje}">
							<div class="error-message" style="color: red;">${mensaje}</div>
						</c:if>
						<input type="email" placeholder="Correo electrónico"
							name="correoElectronicoUsu"
							value="${nuevoUsuario.correoElectronicoUsu}" required /> <input
							type="password" placeholder="Contraseña" name="contraseniaUsu"
							value="${nuevoUsuario.contraseniaUsu}" required />
						<div class="debajoDeLosInputs">
							<div class="social-contenedor">
								<a href="<%=request.getContextPath()%>/loginGoogle"
									class="social"><i class="fab fa-google"></i></a>
							</div>
							<button type="button" class="botonOlvidado"
								onclick="openRecuperacionModal()">¿Has olvidado tu
								contraseña?</button>
						</div>
						<button type="submit" class="botonNavegador botonIS1">Iniciar
							Sesion</button>
					</form>
				</div>

				<div class="overlay-container">
					<div class="overlay">
						<div class="overlay-panel overlay-left">
							<h2 class="titulo">Bienvenido</h2>
							<p>Inicia sesión con tu cuenta</p>
							<button class="botonNavegador botonIS1" id="signIn">Iniciar
								sesion</button>
						</div>
						<div class="overlay-panel overlay-right">
							<h2 class="titulo">¿Eres nuevo?</h2>
							<p>Unete a esta gran familia</p>
							<button class="botonNavegador" id="signUp">Registrarse</button>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="d-block d-md-none contenedorIsMV">
			<div class="form-container capacidadMinima" id="formContainer">
				<div class="form-inner">
					<!-- Formulario Frontal -->
					<form action="${pageContext.request.contextPath}/IS" method="post"
						class="form formsm">
						<h2 class="inicioSesionTitulo1">Inicio sesion</h2>
						<c:if test="${not empty mensaje}">
							<div class="error-message" style="color: #00fff5;">${mensaje}</div>
						</c:if>
						<input type="email"
							placeholder="C o r r e o  e l e c t r ó n i c o"
							name="correoElectronicoUsu"
							value="${nuevoUsuario.correoElectronicoUsu}" required /> <input
							type="password" placeholder="C o n t r a s e ñ a"
							name="contraseniaUsu" value="${nuevoUsuario.contraseniaUsu}"
							required />
						<div class="debajoDeLosInputs1">
							<div class="social-contenedor">
								<a href="<%=request.getContextPath()%>/loginGoogle"
									class="social1"><i class="fab fa-google"></i></a>
							</div>
							<button type="button" class="olvidoContra"
								onclick="openRecuperacionModal()">¿Has olvidado tu
								contraseña?</button>
							>
						</div>
						<div
							style="display: flex; width: 100%; flex-direction: column; height: 30%; justify-content: space-evenly; align-items: center;">
							<button class="botonNavegador botonIS1">Iniciar Sesion</button>
							<button type="button" onclick="flipForm()"
								class="botonFlip botonNavegador"
								style="width: auto; height: auto;">Registrase</button>
						</div>
					</form>
					<!-- Formulario Trasero -->
					<form action="${pageContext.request.contextPath}/Registro"
						method="post" class="form1 form-back formsm" id="formRegistro2">
						<h2 class="registroTitulo1">Registrarse</h2>
						<input type="text" placeholder="N o m b r e  c o m p l e t o"
							name="nombreCompletoUsu"
							value="${nuevoUsuario.nombreCompletoUsu}" required /> <input
							type="text" placeholder="A l i a s" name="aliasUsu"
							value="${nuevoUsuario.aliasUsu}" required /> <input type="email"
							placeholder="C o r r e o  e l e c t r ó n i c o"
							name="correoElectronicoUsu"
							value="${nuevoUsuario.correoElectronicoUsu}" required /> <input
							type="password" placeholder="C o n t r a s e ñ a"
							name="contraseniaUsu" value="${nuevoUsuario.contraseniaUsu}"
							required id="contraseniaUsu2" /> <input type="password"
							placeholder="R e p e t i r  c o n t r a s e ñ a"
							id="repetirContrasenia2" />
						<div class="social-contenedor3">
							<a href="<%=request.getContextPath()%>/loginGoogle"
								class="social"><i class="fab fa-google-plus-g"></i></a>
						</div>
						<div class="debajoDeLosInputs2">
							<button type="submit" class="botonNavegador botonRegistro">
								Registrarse</button>
							<button type="button" onclick="flipForm()"
								class="botonFlip botonNavegador botonIS1">inicio Sesion</button>
						</div>
					</form>
				</div>
			</div>
		</div>

		<div id="alertaPersonalizada" class="alerta-personalizada">
			<div class="alerta-contenido">
				<p id="alertaMensaje">Mensaje de alerta</p>
				<button onclick="cerrarAlertaPersonalizada()">Aceptar</button>
			</div>
		</div>

		<div id="formularioRecuperacionModal" class="modal-recuperacion">
			<div class="contenidoModalRecuperacion">
				<span class="close" onclick="closeRecuperacionModal()">&times;</span>
				<h2 id="tituloRecuperacion">Recuperar contraseña</h2>
				<form id="formularioRecuperacionCorreo"
					onsubmit="enviarRecuperacion(event)">
					<div>
						<label for="emailRecuperacion">Introduce tu correo
							electrónico:</label> <input type="email" id="emailRecuperacion"
							placeholder="ejemplo@correo.com" required>
					</div>
					<div>
						<button type="submit" class="recuperacion-button">Enviar</button>
					</div>
				</form>
			</div>
		</div>

		<div id="formularioCambioModal" class="modal-recuperacion">
			<div class="contenidoModalRecuperacion">
				<span class="close" onclick="closeCambioModal()">&times;</span>
				<h2 id="tituloCambio">Cambiar contraseña</h2>
				<form id="formularioCambioContrasena"
					style="background-color: white;"
					onsubmit="enviarCambioContrasena(event)">
					<div>
						<label for="nuevaContrasena">Nueva contraseña:</label> <input
							type="password" id="nuevaContrasena" required
							style="border: 2px solid black; border-radius: 10px; color: black; text-align: center;">
					</div>
					<div>
						<label for="confirmarContrasena">Confirmar contraseña:</label> <input
							type="password" id="confirmarContrasena" required
							style="border: 2px solid black; border-radius: 10px; color: black; text-align: center;">
					</div>
					<input type="hidden" id="tokenRecuperacion" name="token">
					<div>
						<button type="submit" class="recuperacion-button">Guardar</button>
					</div>
				</form>
			</div>
		</div>

	</main>
	<c:set var="mensaje" value="${mensaje}" />
	<script
		src="<%=request.getContextPath()%>/js/javascriptiniciosesion.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>
