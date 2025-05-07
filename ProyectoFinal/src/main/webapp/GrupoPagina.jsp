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
	<c:set var="usuario" value="${sessionScope.Usuario}" />
	<main class="contenedorMainGrupo">

		<div class="contenedorPrincipalGrupo d-none d-lg-block">
			<div class="columnaPrincipalGrupo">
				<div class="contenedorFiltro">
					<div class="filtrado">
						<h3>Categorías</h3>
						<div class="categorias">
							<button data-target="anime" class="active">Anime</button>
							<button data-target="videojuegos">Videojuegos</button>
							<button data-target="auxiliar">Auxiliar</button>
						</div>
						<div id="anime" class="subcategorias active">
							<h4>Subcategorías</h4>
							<ul>
								<li><button data-sub="Shonen">Shonen</button></li>
								<li><button data-sub="Isekai">Isekai</button></li>
								<li><button data-sub="Shojo">Shojo</button></li>
							</ul>
						</div>
						<div id="videojuegos" class="subcategorias">
							<h4>Subcategorías</h4>
							<ul>
								<li><button data-sub="Shooters">Shooters</button></li>
								<li><button data-sub="Aventuras">Aventuras</button></li>
								<li><button data-sub="Deportes">Deportes</button></li>
							</ul>
						</div>
						<div id="auxiliar" class="subcategorias">
							<h4>Subcategorías</h4>
							<ul>
								<li><button data-sub="auxiliar">Auxiliar</button></li>
							</ul>
						</div>
					</div>
					<div class="nuevoGrupo">
						<button class="crear" id="crear"
							onclick="openCreacionGrupoModal(false)">Crear</button>
					</div>
				</div>
				<div class="contenedorGrupo">
					<div class="trozoPaginaGrupoInfo">
						<div class="nombrePaginaGrupoInfo">Nombre</div>
						<div class="categoriaPaginaGrupoInfo">Categoría</div>
						<div class="tematicaPaginaGrupoInfo">Subcategoría</div>
						<i class="fas fa-search abrir-search"></i> <input
							id="filtroGrupos" type="text" class="search-inputInfo"
							placeholder="Buscar..."> <i
							class="fas fa-times cerrar-search"></i>
					</div>
					<c:forEach var="grupo" items="${listadoGruposTotales}">
						<div class="trozoPaginaGrupo" style="--i: ${st.index}"
							data-categoria="${grupo.categoriaNombre}"
							data-subcategoria="${grupo.subCategoriaNombre}">
							<div class="nombrePaginaGrupo">
								<c:out value="${grupo.nombreGrupo}" />
							</div>
							<div class="categoriaPaginaGrupo">
								<c:out value="${grupo.categoriaNombre}" />
							</div>
							<div class="tematicaPaginaGrupo">
								<c:out value="${grupo.subCategoriaNombre}" />
							</div>
							<div class="descripcionPaginaGrupo">
								Descripción del grupo: <br>
								<c:out value="${grupo.descripcionGrupo}" />
							</div>
							<div>
								<c:url value="/PaginaGrupoEspecificado" var="grupoUrl">
									<c:param name="nombreGrupo" value="${grupo.nombreGrupo}" />
								</c:url>
								<a href="${grupoUrl}" class="verPaginaGrupo">Ver</a>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
		<!-- Versión móvil -->
		<div class="contenedorPrincipalGrupo d-lg-none d-block">
			<div class="columnaPrincipalGrupo2"
				style="display: flex; height: 100%; flex-direction: column; justify-content: center;">
				<div class="contenedorFiltro2">
					<div class="filtroHorizontal">
						<div class="grupoSelect">
							<div class="selectorGrupo">
								<label for="categoriaFiltro">Categoría:</label> <select
									id="categoriaFiltro">
									<option value="">-- Todas las categorías --</option>
									<option value="anime">Anime</option>
									<option value="videojuegos">Videojuegos</option>
									<option value="auxiliar">Auxiliar</option>
								</select>
							</div>
							<div class="selectorGrupo">
								<label for="subcategoriaFiltro">Subcategoría:</label> <select
									id="subcategoriaFiltro"></select>
							</div>
						</div>
						<button class="crear2 d-none d-sm-block"
							onclick="openCreacionGrupoModal(false)">Crear</button>
					</div>
				</div>
				<div class="contenedorGrupo2">
					<div class="trozoPaginaGrupoInfo">
						<div class="nombrePaginaGrupoInfo">Nombre</div>
						<div class="categoriaPaginaGrupoInfo">Categoría</div>
						<div class="tematicaPaginaGrupoInfo">Subcategoría</div>
						<i class="fas fa-search abrir-search"></i> <input
							id="filtroGrupos" type="text" class="search-inputInfo"
							placeholder="Buscar..."> <i
							class="fas fa-times cerrar-search"></i>
					</div>
					<c:forEach var="grupo" items="${listadoGruposTotales}">
						<div class="trozoPaginaGrupo" style="--i: ${st.index}"
							data-categoria="${grupo.categoriaNombre}"
							data-subcategoria="${grupo.subCategoriaNombre}">
							<div class="nombrePaginaGrupo">
								<c:out value="${grupo.nombreGrupo}" />
							</div>
							<div class="categoriaPaginaGrupo">
								<c:out value="${grupo.categoriaNombre}" />
							</div>
							<div class="tematicaPaginaGrupo">
								<c:out value="${grupo.subCategoriaNombre}" />
							</div>
							<div class="descripcionPaginaGrupo">
								Descripción del grupo: <br>
								<c:out value="${grupo.descripcionGrupo}" />
							</div>
							<div>
								<c:url value="/PaginaGrupoEspecificado" var="grupoUrl">
									<c:param name="nombreGrupo" value="${grupo.nombreGrupo}" />
								</c:url>
								<a href="${grupoUrl}" class="verPaginaGrupo">Ver</a>
							</div>
						</div>
					</c:forEach>
				</div>
				<button class="crear3 d-sm-none d-block"
					onclick="openCreacionGrupoModal(false)">Crear</button>
			</div>
		</div>


		<!-- Modal Creación de Grupo -->
		<div id="formularioCreacionGrupoModal" class="modal">
			<div class="contenidoModal2">
				<span class="close" onclick="closeCreacionGrupoModal()">&times;</span>
				<h2 id="modalTituloGrupoNuevo">Nuevo Grupo</h2>
				<form id="formularioCreacionGrupo"
					onsubmit="enviarCreacionGrupo(event)">
					<input type="hidden" id="AliasCreador" name="AliasCreador"
						value="${usuario.aliasUsu}" />
					<div>
						<label for="nombreGrupoNuevo">Nombre Grupo</label> <input
							id="nombreGrupoNuevo" name="nombreGrupoNuevo" required>
					</div>
					<div
						style="display: flex; flex-direction: column; justify-content: center; align-items: center;">
						<label for="descripcionGrupoNuevo">Descripción</label>
						<textarea id="descripcionGrupoNuevo" name="descripcionGrupoNuevo"
							placeholder="Escribe el nombre del grupo aquí..." required
							maxlength="255"
							style="padding: 10px; border: 3px solid black; font-size: 18px; text-align: center; color: black; max-width: 400px; height: 136px;"></textarea>
					</div>
					<div>
						<label for="categoriaGrupoNuevo">Categoría</label> <select
							id="categoriaGrupoNuevo" name="categoriaGrupoNuevo"
							onchange="actualizarSubcategoriasCreacionGru()" disabled>
							<option value="anime">Anime</option>
							<option value="videojuegos">Videojuegos</option>
							<option value="auxiliar">Auxiliar</option>
						</select>
					</div>
					<div>
						<label for="subCategoriaGrupoNuevo">Subcategoría</label> <select
							id="subCategoriaGrupoNuevo" name="subCategoriaGrupoNuevo"
							disabled></select>
					</div>
					<div>
						<button type="submit" class="manga-button">Crear</button>
					</div>
				</form>
			</div>
		</div>

		<!-- Alerta Personalizada -->
		<div id="alertaPersonalizada" class="alerta-personalizada">
			<div class="alerta-contenido">
				<p id="alertaMensaje"></p>
				<button onclick="cerrarAlerta()">Aceptar</button>
			</div>
		</div>
	</main>
	<script src="<%=request.getContextPath()%>/js/javaScript.js"></script>
	<script src="<%=request.getContextPath()%>/js/javaScriptGrupo.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>

</body>
</html>