<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>AnimeXp</title>
<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/imagenes/Gemini_Generated_Image_3majls3majls3maj.jpg"
	type="image/x-icon" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estilo.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estiloiniciosesion.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estiloPerfil.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/comentario.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/grupo.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/grupoEspecificado.css" />
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
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body
	style="	background:
		url('<%=request.getContextPath()%>/imagenes/artbreeder-image-2024-12-14T13_41_59.055Z.jpeg')
		no-repeat top left/cover;">
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


	<c:set var="grupo" value="${grupoEspecificado}" />
	<main class="contenedorMainGrupoEspecifico">
		<div class="contenedorPrincipalGrupoEspecifico d-none d-lg-block">
			<div class="contenedroInformacionGrupo">
				<div class="columnaIzquierda">
					<div class="titular">
						<h3 class="nombreGrupo">${grupo.nombreGrupo}</h3>
						<div class="categoriaGrupo">${grupo.categoriaGrupo}</div>
						<div class="subcategoriaGrupo">${grupo.subcategoriaGrupo}</div>
					</div>
					<div>
						<h5>Creador: ${grupo.aliasCreadorGrupo}</h5>
					</div>
					<div class="fechaCre">Fecha de creacion:
						${grupoEspecificado.fechaCreacionFormateada}</div>
				</div>
				<div class="columnaDerecha">
					<h4 class="tituloDescripcion">Descripcíon:</h4>
					<div class="contenedorDescripcion">${grupo.descripcionGrupoString}</div>
				</div>
			</div>
			<div class="contenedorListadoUsuarioGrupos">
				<div class="listadoUsuariosEspacio">
					<div class="listadoUsuarios">
						<h3 style="margin-top: 10px;">USUARIOS</h3>
						<div class="elemento">
							<div>Nombre</div>
							<div>Alias</div>
							<div>Tipo usuario</div>
						</div>
						<c:forEach var="usuario"
							items="${grupo.listadoDeUsuariosSuscritos}">
							<div class="elemento">
								<div>${usuario.nombreUsuario}</div>
								<div>${usuario.aliasUsuario}</div>
								<div>${usuario.esPremium ? 'Premium' : 'Normal'}</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<div class="acciones">
					<div class="bloque">
						<div class="numeroUsuarios">Nº de usuarios:
							${grupo.numeroUsuarios}</div>
						<div class="parteBuscador">
							<p class="BuscarUsuarioTexto">Buscar usuario</p>
							<input type="text">
							<Button class="buscar">Buscar</Button>
						</div>
						<div
							style="width: 100%; display: flex; align-items: center; justify-content: center;">
							<Button class="unirse">Unirse</Button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Parte Movil -->
		<div
			class="contenedorPrincipalGrupoEspecifico-movil d-block d-lg-none">
			<div class="contenedroInformacionGrupo-movil">
				<div class="columnaIzquierda-movil">
					<div class="titular-movil">
						<h3 class="nombreGrupo-movil">${grupo.nombreGrupo}</h3>
						<div class="categoriaGrupo-movil">${grupo.categoriaGrupo}</div>
						<div class="subcategoriaGrupo-movil">${grupo.subcategoriaGrupo}</div>
					</div>
					<div>
						<h5 class="creador-movil">Creador: ${grupo.aliasCreadorGrupo}</h5>
					</div>
					<div class="fechaCre-movil">Fecha de creación:
						${grupoEspecificado.fechaCreacionFormateada}</div>
				</div>
				<div class="columnaDerecha-movil">
					<h4 class="tituloDescripcion-movil">Descripción:</h4>
					<div class="contenedorDescripcion-movil">${grupo.descripcionGrupoString}</div>
				</div>
			</div>

			<div class="contenedorListadoUsuarioGrupos-movil">
				<div class="listadoUsuariosEspacio-movil">
					<div class="listadoUsuarios-movil">
						<h3 style="margin-top: 10px;">USUARIOS</h3>
						<div class="elemento-movil">
							<div>Nombre</div>
							<div>Alias</div>
							<div>Tipo</div>
						</div>
						<c:forEach var="usuario"
							items="${grupo.listadoDeUsuariosSuscritos}">
							<div class="elemento-movil">
								<div>${usuario.nombreUsuario}</div>
								<div>${usuario.aliasUsuario}</div>
								<div>${usuario.esPremium ? 'Premium' : 'Normal'}</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<div class="acciones-movil">
					<div class="bloque-movil">
						<div class="numeroUsuarios-movil">Nº de usuarios:
							${grupo.numeroUsuarios}</div>
						<div class="parteBuscador-movil">
							<p class="BuscarUsuarioTexto-movil">Buscar usuario</p>
							<input type="text" class="inputBuscar-movil">
							<button class="buscar-movil">Buscar</button>
						</div>
						<button class="unirse-movil">Unirse</button>
					</div>
				</div>
			</div>
		</div>

	</main>


	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>