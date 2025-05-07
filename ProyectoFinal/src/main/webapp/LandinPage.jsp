<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>AnimeXp</title>
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/imagenes/Gemini_Generated_Image_3majls3majls3maj.jpg"
	type="image/x-icon" />
<link rel="stylesheet" href="./css/estilo.css" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous" />
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
	rel="stylesheet" />
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
		no-repeat top left/cover;">
	<!-- Este header es para las pantallas-->
	<header class="header d-none d-md-block">
		<nav class="navegadorPrincipal">
			<div class="contenedorPrincipal">
				<div class="logo-contenedor">
					<a href="<%=request.getContextPath()%>/"><img
						class="imagenLogo"
						src="<%=request.getContextPath()%>/imagenes/Modified_Image_Pure_Black_Background.png"
						alt="Logo de la pagina web" width="200px" /> </a>
				</div>
				<!-- Columna de los botones (a la derecha) -->
				<div class="boton-contenedor">
					<a href="<%=request.getContextPath()%>/PaginaGrupo"><button
							class="botonNavegador botonGrupo">GRUPOS</button></a> <a
						href="<%=request.getContextPath()%>/ComentarioPagina"
						class="d-xl-block d-none"><button
							class="botonNavegador botonAnime">ANIMES</button></a> <a
						href="<%=request.getContextPath()%>/ComentarioPagina"
						class="d-xl-block d-none"><button
							class="botonNavegador botonVideojuegos">VIDEOJUEGOS</button></a> <a
						href="<%=request.getContextPath()%>/ComentarioPagina"
						class="d-xl-none d-md-block d-none"><button
							class="botonNavegador botonCategoria">COMENTARIOS</button></a>
					<!-- Aqui deberia de poner una condicion que si es admin coja una cosa y si no que haga la otra -->
					<c:if test="${Usuario == null}">
						<a href="<%=request.getContextPath()%>/InicioSesion"><button
								class="botonIconoIS">
								<i class="fas fa-user icono"></i>
							</button></a>
					</c:if>
					<c:if test="${Usuario != null}">
						<c:if test="${Usuario.rolUsu != 'user'}">
							<a href="<%=request.getContextPath()%>/PerfilUsuario"><button
									class=" botonNavegador ">ADMIN</button></a>
						</c:if>
						<c:if test="${Usuario.rolUsu == 'user'}">
							<a href="<%=request.getContextPath()%>/PerfilUsuario"><button
									class="botonNavegador ">PERFIL</button></a>
						</c:if>
					</c:if>
				</div>
			</div>
		</nav>
	</header>

	<!-- Este header es para la parte de moviles y tablest pequeñas-->
	<header class="header d-block d-md-none">
		<nav class="navegadorPrincipalS">
			<div class="contenedorPrincipal">
				<div class="logo-contenedor">
					<a href="<%=request.getContextPath()%>/"> <img
						class="imagenLogo2"
						src="<%=request.getContextPath()%>/imagenes/Modified_Image_Pure_Black_Background.png"
						alt="Logo de la pagina web" width="200px" />
					</a>
				</div>

				<!-- MENU-->
				<div class="boton-contenedor2">
					<button class="botonNavegador2 d-block d-md-none" id="menuToggle">
						<i class="fa-solid fa-bars"></i>
					</button>
					<div class="menu-desplegable" id="menuOpciones">
						<a href="<%=request.getContextPath()%>/PaginaGrupo">GRUPOS</a> <a
							href="<%=request.getContextPath()%>/ComentarioPagina">COMENTARIOS</a>
						<!--AQUI DEBERIA DE PONER LA CONDICION EN LA CUAL SI SE INICIA SESION PONDRA PERFIL USUARIO O PERFIL ADMINISTRADOR-->
						<c:if test="${Usuario == null}">
							<a href="<%=request.getContextPath()%>/InicioSesion">INICIAR
								SESION</a>
						</c:if>
						<c:if test="${Usuario != null}">
							<c:if test="${Usuario.rolUsu != 'user'}">
								<a href="<%=request.getContextPath()%>/PerfilUsuario">ADMIN</a>
							</c:if>
							<c:if test="${Usuario.rolUsu == 'user'}">
								<a href="<%=request.getContextPath()%>/PerfilUsuario">PERFIL</a>
							</c:if>
						</c:if>
					</div>
				</div>
			</div>
		</nav>
	</header>

	<!-- Aqui comienza la parte Main-->
	<main class="contenedorMain">
		<!-- Parte PC -->
		<div class="container d-none d-xl-block">
			<div class="row">
				<div class="col">
					<div class="contenedor-carrusel">
						<div class="botones">
							<button onclick="usuarioAnterior()" class="boton1">
								<i class="fas fa-arrow-left"></i>
							</button>
							<button onclick="usuarioSiguiente()" class="boton2">
								<i class="fas fa-arrow-right"></i>
							</button>
						</div>
						<div id="contenido-carrusel">
							<div class="titulo-usuario" id="titulo-usuario">Nombre del
								Usuario</div>
							<div class="contenedorTextoEImagen">
								<img alt="Imagen del Usuario" class="imagen-usuario"
									id="imagen-usuario" />
								<div class="texto-usuario" id="texto-usuario">Texto del
									usuario</div>
								<div class="contenedorBoton">
									<button class="corazonBoton" type="button">
										<i class="fas fa-heart"></i>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col parteDerechaMain">
					<div class="row">
						<div class="col-12">
							<div class="zonaBienvenida">
								<div class="tituloBienvenida">Bienvenido</div>
								<div class="contenedorTextoBienvenida">¡Bienvenid@ a
									AnimeXP! 🌟 El espacio donde el anime, los videojuegos y la
									pasión de los fans se encuentran. 🎮✨ Descubre contenidos
									épicos, conecta con una comunidad increíble y vive tu afición
									como nunca antes. 🧡 ¡Explora, juega y siéntete como en casa!
									🚀</div>
							</div>
						</div>
						<div class="col-12">
							<div class="zonaGruposMain">
								<div class="tituloBienvenida">GRUPOS TOPS</div>
								<div class="contenedorGrupos">

									<c:if test="${empty listaGrupos}">
										<div class="mensajeGrupo">
											<c:out value="${mensajeGrupo}"></c:out>
										</div>
									</c:if>
									<c:if test="${not empty listaGrupos }">
										<div class="trozoGrupo">
											<div class="NombreGrupo">Nombre</div>
											<div class="categoriaGrupo">Categoría</div>
											<div class="tematicaGrupo">SubCategoría</div>
											<div>Acción</div>
										</div>
										<c:forEach var="grupo" items="${listaGrupos}">
											<div class="trozoGrupo">
												<div class="NombreGrupo">
													<c:out value="${grupo.nombreGrupo}"></c:out>
												</div>
												<div class="categoriaGrupo">
													<c:out value="${grupo.categoriaNombre}"></c:out>
												</div>
												<div class="tematicaGrupo">
													<c:out value="${grupo.subCategoriaNombre}"></c:out>
												</div>
												<div>
													<c:url value="/PaginaGrupoEspecificado" var="grupoUrl">
														<c:param name="nombreGrupo" value="${grupo.nombreGrupo}" />
													</c:url>
													<a href="${grupoUrl}" class="verGrupo">Ver</a>
												</div>
											</div>
										</c:forEach>
									</c:if>

								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Parte tablet y portatiles -->
		<div class="container d-none d-md-block d-xl-none">
			<div class="row rowTablet">
				<div class="col-12">
					<div class="zonaBienvenida">
						<div class="tituloBienvenida">Bienvenido</div>
						<div class="contenedorTextoBienvenida">¡Bienvenid@ a
							AnimeXP! 🌟 El espacio donde el anime, los videojuegos y la
							pasión de los fans se encuentran. 🎮✨ Descubre contenidos épicos,
							conecta con una comunidad increíble y vive tu afición como nunca
							antes. 🧡 ¡Explora, juega y siéntete como en casa! 🚀</div>
					</div>
				</div>
				<div class="col-12">
					<div class="row">
						<div class="col">
							<div class="contenedor-carrusel2">
								<div class="botones">
									<button onclick="usuarioAnterior2()" class="boton1">
										<i class="fas fa-arrow-left"></i>
									</button>
									<button onclick="usuarioSiguiente2()" class="boton2">
										<i class="fas fa-arrow-right"></i>
									</button>
								</div>
								<div class="contenido-carrusel2">
									<div class="titulo-usuario2">Nombre del Usuario</div>
									<div class="contenedorTextoEImagen2">
										<img alt="Imagen del Usuario" class="imagen-usuario2" />
										<div class="texto-usuario2">Texto del usuario</div>
										<div class="contenedorBoton">
											<button class="corazonBoton2" type="button">
												<i class="fas fa-heart"></i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="col">
							<div class="zonaGruposMain2 d-md-none d-lg-block">
								<div class="tituloBienvenida">GRUPOS TOPS</div>
								<div class="contenedorGrupos">
									<c:if test="${empty listaGrupos}">
										<div class="mensajeGrupo">
											<c:out value="${mensajeGrupo}"></c:out>
										</div>
									</c:if>
									<c:if test="${not empty listaGrupos }">
										<div class="trozoGrupo">
											<div class="NombreGrupo">Nombre</div>
											<div class="categoriaGrupo">Categoría</div>
											<div class="tematicaGrupo">SubCategoría</div>
											<div>Acción</div>
										</div>
										<c:forEach var="grupo" items="${listaGrupos}">
											<div class="trozoGrupo">
												<div class="NombreGrupo">
													<c:out value="${grupo.nombreGrupo}"></c:out>
												</div>
												<div class="categoriaGrupo" style="margin: 5%;">
													<c:out value="${grupo.categoriaNombre}"></c:out>
												</div>
												<div class="tematicaGrupo" style="margin: 5%;">
													<c:out value="${grupo.subCategoriaNombre}"></c:out>
												</div>
												<div>
													<c:url value="/PaginaGrupoEspecificado" var="grupoUrl">
														<c:param name="nombreGrupo" value="${grupo.nombreGrupo}" />
													</c:url>
													<a href="${grupoUrl}" class="verGrupo">Ver</a>
												</div>
											</div>
										</c:forEach>
									</c:if>
								</div>
							</div>
							<div class="zonaGruposMain2 d-lg-none">
								<div class="tituloBienvenida">GRUPOS TOPS</div>
								<div class="contenedorGrupos">
									<c:if test="${empty listaGrupos}">
										<div class="mensajeGrupo">
											<c:out value="${mensajeGrupo}"></c:out>
										</div>
									</c:if>
									<c:if test="${not empty listaGrupos }">
										<div class="trozoGrupo">
											<div class="NombreGrupo">Nombre</div>
											<div class="categoriaGrupo">Categoría</div>
											<div>Acción</div>
										</div>
										<c:forEach var="grupo" items="${listaGrupos}">
											<div class="trozoGrupo">
												<div class="NombreGrupo">
													<c:out value="${grupo.nombreGrupo}"></c:out>
												</div>
												<div class="categoriaGrupo" style="margin: 5%;">
													<c:out value="${grupo.categoriaNombre}"></c:out>
												</div>
												<div>
													<c:url value="/PaginaGrupoEspecificado" var="grupoUrl">
														<c:param name="nombreGrupo" value="${grupo.nombreGrupo}" />
													</c:url>
													<a href="${grupoUrl}" class="verGrupo">Ver</a>
												</div>
											</div>
										</c:forEach>
									</c:if>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Parte Movil -->
		<div class="container d-block d-md-none">
			<div class="row rowMovil">
				<div class="col-12">
					<div class="zonaBienvenida">
						<div class="tituloBienvenida">Bienvenido</div>
						<div class="contenedorTextoBienvenida">¡Bienvenid@ a
							AnimeXP! 🌟 El espacio donde el anime, los videojuegos y la
							pasión de los fans se encuentran. 🎮✨ Descubre contenidos épicos,
							conecta con una comunidad increíble y vive tu afición como nunca
							antes. 🧡 ¡Explora, juega y siéntete como en casa! 🚀</div>
					</div>
				</div>
				<div class="col-12">
					<div class="contenedor-carrusel3">
						<div class="botones">
							<button onclick="usuarioAnterior3()" class="boton1">
								<i class="fas fa-arrow-left"></i>
							</button>
							<button onclick="usuarioSiguiente3()" class="boton2">
								<i class="fas fa-arrow-right"></i>
							</button>
						</div>
						<div class="contenido-carrusel3">
							<div class="titulo-usuario3">Nombre del Usuario</div>
							<div class="contenedorTextoEImagen3">
								<img alt="Imagen del Usuario" class="imagen-usuario3" />
								<div class="texto-usuario3">Texto del usuario</div>
								<div class="contenedorBoton">
									<button class="corazonBoton3" type="button">
										<i class="fas fa-heart"></i>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-12">
					<div class="zonaGruposMain2">
						<div class="tituloBienvenida">GRUPOS TOPS</div>
						<div class="contenedorGrupos">
							<c:if test="${empty listaGrupos}">
								<div class="mensajeGrupo">
									<c:out value="${mensajeGrupo}"></c:out>
								</div>
							</c:if>
							<c:if test="${not empty listaGrupos }">
								<div class="trozoGrupo">
									<div class="NombreGrupo">Nombre</div>
									<div class="categoriaGrupo">Categoría</div>
									<div>Acción</div>
								</div>
								<c:forEach var="grupo" items="${listaGrupos}">
									<div class="trozoGrupo">
										<div class="NombreGrupo">
											<c:out value="${grupo.nombreGrupo}"></c:out>
										</div>
										<div class="categoriaGrupo">
											<c:out value="${grupo.categoriaNombre}"></c:out>
										</div>
										<div>
											<c:url value="/PaginaGrupoEspecificado" var="grupoUrl">
												<c:param name="nombreGrupo" value="${grupo.nombreGrupo}" />
											</c:url>
											<a href="${grupoUrl}" class="verGrupo">Ver</a>
										</div>
									</div>
								</c:forEach>
							</c:if>
						</div>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${not empty infoVerificacion}">
			<div id="alertaPersonalizada" class="alerta-personalizada">
				<div class="alerta-contenido">
					<p id="alertaMensaje">${infoVerificacion}</p>
					<button onclick="cerrarAlertaPersonalizada()">Aceptar</button>
				</div>
			</div>
		</c:if>
	</main>

	<script src="<%=request.getContextPath()%>/js/javaScript.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>
