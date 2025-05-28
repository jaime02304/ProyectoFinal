<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Animexp</title>

<link rel="shortcut icon"
	href="<%=request.getContextPath()%>/imagenes/Gemini_Generated_Image_3majls3majls3maj.jpg"
	type="image/x-icon" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estilo.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estiloiniciosesion.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/estiloPerfil.css" />
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
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>

<body
	style="	background:
		url('<%=request.getContextPath()%>/imagenes/artbreeder-image-2024-12-14T13_41_59.055Z.jpeg')
		no-repeat top left/cover;">
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

	<main class="contenedorMainPerfil">
		<c:set var="usuario" value="${sessionScope.Usuario}" />
		<!--PArte de PC-->
		<div class="container contenedorPrincipalPerfil d-none d-lg-block">
			<div class="row columnaPrincipal">
				<div class="col contenedorInfoPerfil">
					<div class="encabezado">
						<!--Si esta sin imagen un imput para insertar una imagen-->
						<div class="divImagenPerfil">
							<c:choose>
								<c:when test="${not empty usuario.fotoString}">
									<img src="data:image/jpeg;base64,${usuario.fotoString}"
										alt="Foto Perfil del usuario" class="imagenUsuPerfil">
								</c:when>
								<c:otherwise>
									<div class="imagenUsuPerfil">No Avatar</div>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="divAliasPerfil">
							<h1 class="aliasPerfil">
								<c:out value="${usuario.aliasUsu}" default="ALIAS" />
							</h1>
						</div>
						<button type="submit" class="botonModificador"
							onclick="openFormularioModal()">
							<i class="fa-solid fa-pen"></i>
						</button>
					</div>
					<div class="ContenedorCompleto">
						<div class="ContenedorDatosPerfil">
							<h3 class="DatosPerfil">Nombre Completo:</h3>
							<h3 class="DatosPerfilInfo">
								<c:out value="${usuario.nombreCompletoUsu}" default="" />

							</h3>
						</div>
						<div class="ContenedorDatosPerfil">
							<h3 class="DatosPerfil">Teléfono Móvil:</h3>
							<h3 class="DatosPerfilInfo">
								<c:out value="${usuario.movilUsu}" default="" />
							</h3>
						</div>
						<div class="ContenedorDatosPerfil">
							<h3 class="DatosPerfil">Correo:</h3>
							<h3 class="DatosPerfilInfo">
								<c:out value="${usuario.correoElectronicoUsu}" default="" />
							</h3>
						</div>
						<div class="ContenedorDatosPerfil">
							<h3 class="DatosPerfil">Tipo de Usuario:</h3>
							<h3 class="DatosPerfilInfo">
								<c:choose>
									<c:when test="${usuario.esPremium}">
                Usuario Premium
            </c:when>
									<c:otherwise>
                Usuario Básico
            </c:otherwise>
								</c:choose>
							</h3>
						</div>
						<div class="ContenedorDatosPerfil">
							<a href="<%=request.getContextPath()%>/CerrarSesion"><button
									class="hacersePremiumBoton botonNavegador">Cerrar</button></a>
							<!--  <button class="hacersePremiumBoton botonNavegador"
								onclick="alertaDelPremium()">Premium</button>-->
						</div>
					</div>

				</div>
				<div class="col" style="height: 100%;">
					<div class="row contenedorDosPerfil">
						<div class="col contenedorInfoPerfilDerecho contenedorGruposB">
							<h3>Grupos</h3>
							<!--Aqui en la parte admin se debera poner los usuario y grupos que se deban de poder modificar o eliminar-->
							<c:if test="${usuario.rolUsu == 'user'}">
								<c:choose>
									<c:when test="${empty listadoGruposUsuario}">
										<div class="mensajeGrupo">
											<c:out value="No se encontraron grupos disponibles." />
										</div>
										<div class="tiposMensaje">
											<button onclick="openCreacionGrupoModal()">Crear
												Grupo</button>
										</div>
									</c:when>
									<c:otherwise>
										<table class="tablaGrupos" style="width: 100%;">
											<thead>
												<tr class="trozoGrupo encabezado">
													<th class="NombreGrupo" style="font-weight: lighter;">Nombre</th>
													<th class="categoriaGrupo" style="font-weight: lighter;">Categoría</th>
													<th class="tematicaGrupo" style="font-weight: lighter;">Temática</th>
													<th style="font-weight: lighter;">Acciones</th>
												</tr>
											</thead>
											<tbody
												style="display: flex; justify-content: center; align-items: center; flex-direction: column;">
												<c:forEach var="grupo" items="${listadoGruposUsuario}">
													<tr class="trozoGrupo">
														<td class="NombreGrupo"><c:out
																value="${grupo.nombreGrupo}" /></td>
														<td class="categoriaGrupo"><c:out
																value="${grupo.categoriaNombre}" /></td>
														<td class="tematicaGrupo"><c:out
																value="${grupo.subCategoriaNombre}" /></td>
														<td><c:url value="/PaginaGrupoEspecificado"
																var="grupoUrl">
																<c:param name="nombreGrupo" value="${grupo.nombreGrupo}" />
															</c:url><a href="${grupoUrl}" class="verGrupo">Ver</a></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<c:set var="grupoCount"
											value="${fn:length(listadoGruposUsuario)}" />
										<c:set var="maxGrupos" value="${usuario.esPremium ? 6 : 3}" />
										<div class="tiposMensaje">
											<button
												onclick="${grupoCount < maxGrupos 
								                ? 'openCreacionGrupoModal()' 
								                        : 'mostrarAlertaPersonalizada(\'Ya ha realizado el límite de grupos posibles\')'}">Crear
												Grupo</button>
										</div>
									</c:otherwise>
								</c:choose>
							</c:if>
							<c:if
								test="${usuario.rolUsu == 'admin' or usuario.rolUsu == 'sadmin'}">
								<c:choose>
									<c:when test="${empty listadoGruposAdmin}">
										<div class="mensajeGrupo">
											<c:out value="No se encontraron grupos disponibles." />
										</div>
										<div class="tiposMensaje">
											<button onclick="openCreacionGrupoModal()">Crear
												Grupo</button>
										</div>
									</c:when>
									<c:otherwise>
										<table class="tablaGrupos" style="width: 100%;">
											<thead>
												<tr class="trozoGrupo encabezado" style="height: auto;">
													<th class="NombreGrupo" style="font-weight: lighter;">Nombre
														<div>
															<input id="filtroAdminGrupos" type="text"
																placeholder="Filtrar…" style="height: 30%;" /> <i
																class="fas fa-times" id="clearFiltroAdmin"
																style="cursor: pointer; color: red; display: none;"></i>
														</div>
													</th>
													<th style="font-weight: lighter;">Acciones</th>

												</tr>
											</thead>
											<tbody
												style="display: flex; justify-content: center; align-items: center; flex-direction: column;">
												<c:forEach var="grupo" items="${listadoGruposAdmin}">
													<tr class="trozoGrupo" style="height: auto;">
														<td class="NombreGrupo"><c:out
																value="${grupo.nombreGrupo}" /></td>
														<td><a class="verGrupo2"
															onclick="openModificacionGrupoModal('${grupo.idGrupo}', '${grupo.nombreGrupo}', '${grupo.categoriaNombre}', '${grupo.subCategoriaNombre}', '${grupo.descripcionGrupo}')">
																Modificar </a> <a class="verGrupo2"
															onclick="openEliminacionModal('${grupo.idGrupo}', '${grupo.nombreGrupo}', ${false})">
																Borrar </a></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<div class="tiposMensaje">
											<button onclick="openCreacionGrupoModal()">Crear
												Grupo</button>
										</div>
									</c:otherwise>
								</c:choose>
							</c:if>
						</div>


						<c:if test="${usuario.rolUsu == 'user'}">
							<div class="col contenedorInfoPerfilDerechoB">
								<div class="contenidoMensaje">
									<div class="contenidoMensaje2">
										<c:choose>
											<c:when
												test="${not empty comentario and not empty comentario.comentarioTexto}">
                    ${comentario.comentarioTexto}
                </c:when>
											<c:otherwise>
                    No se encontraron comentarios para este usuario.
                </c:otherwise>
										</c:choose>
									</div>
								</div>
								<c:choose>
									<c:when
										test="${not empty comentario and not empty comentario.comentarioTexto}">
										<div class="tiposMensaje">
											<button>${comentario.categoriaTipo}</button>
											<button>${comentario.subCategoriaTipo}</button>
										</div>
									</c:when>
									<c:otherwise>
										<div class="tiposMensaje">
											<button onclick="openCreacionComentarioModal(true)">Crear</button>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</c:if>
						<c:if test="${usuario.rolUsu == 'admin'}">
							<div class="col contenedorInfoPerfilDerecho contenedorGruposB">
								<h3>Usuarios</h3>
								<c:choose>
									<c:when test="${empty listadoUsuariosAdmin}">
										<div class="mensajeGrupo">
											<c:out value="No se encontraron usuarios disponibles." />
										</div>
										<div class="tiposMensaje">
											<button onclick="openCreacionUsuarioModal()">Crear
												Usuario</button>
										</div>
									</c:when>
									<c:otherwise>
										<table class="tablaUsuarios" style="width: 100%;">
											<thead>
												<tr class="trozoGrupo encabezado" style="height: auto;">
													<th class="NombreGrupo"
														style="font-weight: lighter; padding-right: 15px; width: 40%; position: relative;">Alias
														<div class="input-contenedor">
															<input id="filtroUsuariosAdmin" type="text"
																placeholder="Filtrar alias…" /> <i class="fas fa-times"
																id="clearFiltroUsuariosAdmin" title="Limpiar"></i>
														</div>
													</th>
													<th class="NombreGrupo"
														style="font-weight: lighter; padding-right: 15px; width: 40%; text-align: left;">Rol</th>
													<th
														style="font-weight: lighter; width: 40%; text-align: left;">Acciones</th>
												</tr>
											</thead>
											<tbody
												style="display: flex; justify-content: center; align-items: center; flex-direction: column;">
												<c:forEach var="usuario" items="${listadoUsuariosAdmin}">
													<tr class="trozoGrupo" style="height: auto;">
														<td class="NombreGrupo"><c:out
																value="${usuario.aliasUsu}" /></td>
														<td class="NombreGrupo"><c:out
																value="${usuario.rolUsu}" /></td>
														<td><a class="verGrupo2"
															onclick="openModificacionModal('${usuario.idUsu}', '${usuario.nombreCompletoUsu}', '${usuario.aliasUsu}', '${usuario.correoElectronicoUsu}', '${usuario.movilUsu}', ${usuario.esPremium}, '${usuario.rolUsu}', ${usuario.esVerificadoEntidad}, '${usuario.fotoString}')">
																Modificar </a> <a class="verGrupo2"
															onclick="openEliminacionModal('${usuario.idUsu}', '${usuario.aliasUsu}', ${true})">
																Borrar </a></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<div class="tiposMensaje">
											<button onclick="openCreacionUsuarioModal()">Crear
												Usuario</button>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</c:if>

						<c:if test="${usuario.rolUsu == 'sadmin'}">
							<div class="col contenedorInfoPerfilDerecho contenedorGruposB">
								<h3>Usuarios</h3>
								<c:choose>
									<c:when test="${empty listadoUsuariosSAdmin}">
										<div class="mensajeGrupo">
											<c:out value="No se encontraron grupos disponibles." />
										</div>
										<div class="tiposMensaje">
											<button onclick="openCreacionUsuarioModal()">Crear
												Usuario</button>
										</div>
									</c:when>
									<c:otherwise>
										<table class="tablaUsuarios tablaUsuariosSAdmin"
											style="width: 100%;">
											<thead>
												<tr class="trozoGrupo encabezado" style="height: auto;">
													<th class="NombreGrupo"
														style="font-weight: lighter; padding-right: 15px; width: 40%; position: relative;">Alias
														<div class="input-contenedor-sadmin">
															<input id="filtroUsuariosSAdmin" type="text"
																placeholder="Filtrar alias…" /> <i class="fas fa-times"
																id="clearFiltroUsuariosSAdmin" title="Limpiar"></i>
														</div>
													</th>
													<th class="NombreGrupo"
														style="font-weight: lighter; padding-right: 15px; width: 40%; text-align: left;">Rol</th>
													<th
														style="font-weight: lighter; width: 40%; text-align: left;">Acciones</th>
												</tr>
											</thead>
											<tbody
												style="display: flex; justify-content: center; align-items: center; flex-direction: column;">
												<c:forEach var="usuario" items="${listadoUsuariosSAdmin}">
													<tr class="trozoGrupo" style="height: auto;">
														<td class="NombreGrupo"><c:out
																value="${usuario.aliasUsu}" /></td>
														<td class="NombreGrupo"><c:out
																value="${usuario.rolUsu}" /></td>
														<td><a class="verGrupo2"
															onclick="openModificacionModal('${usuario.idUsu}', '${usuario.nombreCompletoUsu}', '${usuario.aliasUsu}', '${usuario.correoElectronicoUsu}', '${usuario.movilUsu}', ${usuario.esPremium}, '${usuario.rolUsu}', ${usuario.esVerificadoEntidad}, '${usuario.fotoString}')">
																Modificar </a> <a class="verGrupo2"
															onclick="openEliminacionModal('${usuario.idUsu}', '${usuario.aliasUsu}', ${true})">
																Borrar </a></td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<div class="tiposMensaje">
											<button onclick="openCreacionUsuarioModal()">Crear
												Usuario</button>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>

		<!--PArte de movil mini tablet-->
		<c:set var="usuario" value="${sessionScope.Usuario}" />
		<div class="container contenedorPrincipalPerfil d-block d-lg-none">
			<div class="row columnaPrincipal">
				<div class="col contenedorInfoPerfil">
					<div class="encabezado">
						<!--Si esta sin imagen un imput para insertar una imagen-->
						<div class="divImagenPerfil">
							<c:choose>
								<c:when test="${not empty usuario.fotoString}">
									<img src="data:image/jpeg;base64,${usuario.fotoString}"
										alt="Foto Perfil del usuario" class="imagenUsuPerfil">
								</c:when>
								<c:otherwise>
									<div class="imagenUsuPerfil">No Avatar</div>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="divAliasPerfil">
							<h1 class="aliasPerfil">
								<c:out value="${usuario.aliasUsu}" default="ALIAS" />
							</h1>
						</div>
						<button type="submit" class="botonModificador"
							onclick="openFormularioModal()">
							<i class="fa-solid fa-pen"></i>
						</button>
					</div>
					<div class="ContenedorCompleto">
						<div class="ContenedorDatosPerfilB">
							<h3 class="DatosPerfil2">Nombre Completo:</h3>
							<h3 class="DatosPerfilInfo2">
								<c:out value="${usuario.nombreCompletoUsu}" default="" />
							</h3>
						</div>
						<div class="ContenedorDatosPerfilB">
							<h3 class="DatosPerfil2">Teléfono Móvil:</h3>
							<h3 class="DatosPerfilInfo2">
								<c:out value="${usuario.movilUsu}" default="" />
							</h3>
						</div>
						<div class="ContenedorDatosPerfilB">
							<h3 class="DatosPerfil2">Correo:</h3>
							<h3 class="DatosPerfilInfo2">
								<c:out value="${usuario.correoElectronicoUsu}" default="" />
							</h3>
						</div>
						<div class="ContenedorDatosPerfilB">
							<h3 class="DatosPerfil2">Tipo de Usuario:</h3>
							<h3 class="DatosPerfilInfo2">
								<c:choose>
									<c:when test="${usuario.esPremium}">
                Usuario Premium
            </c:when>
									<c:otherwise>
                Usuario Básico
            </c:otherwise>
								</c:choose>
							</h3>
						</div>
						<div class="ContenedorDatosPerfil2">
							<div class="divBoton">
								<a href="<%=request.getContextPath()%>/CerrarSesion"><button
										class="hacersePremiumBoton2 botonNavegador">Cerrar</button></a>
							</div>
							<!--<div class="divBoton">
								  <button class="hacersePremiumBoton2 botonNavegador"
									onclick="alertaDelPremium()">Premium</button>
							</div>-->
						</div>
					</div>
				</div>
				<div class="row columnaPrincipalB">
					<div class="col contenedorInfoPerfilDerecho contenedorGruposB">
						<h3>Grupos</h3>
						<!--Aqui en la parte admin se debera poner los usuario y grupos que se deban de poder modificar o eliminar-->
						<c:if test="${usuario.rolUsu == 'user'}">
							<c:choose>
								<c:when test="${empty listadoGruposUsuario}">
									<div class="mensajeGrupo">
										<c:out value="No se encontraron grupos disponibles." />
									</div>
									<div class="tiposMensaje">
										<button onclick="openCreacionGrupoModal()">Crear
											Grupo</button>
									</div>
								</c:when>
								<c:otherwise>
									<table class="tablaGrupos" style="width: 100%;">
										<thead>
											<tr class="trozoGrupo encabezado">
												<th class="NombreGrupo" style="font-weight: lighter;">Nombre</th>
												<th class="categoriaGrupo" style="font-weight: lighter;">Categoría</th>
												<th class="tematicaGrupo" style="font-weight: lighter;">Temática</th>
												<th style="font-weight: lighter;">Acciones</th>
											</tr>
										</thead>
										<tbody
											style="display: flex; justify-content: center; align-items: center; flex-direction: column;">
											<c:forEach var="grupo" items="${listadoGruposUsuario}">
												<tr class="trozoGrupo">
													<td class="NombreGrupo"><c:out
															value="${grupo.nombreGrupo}" /></td>
													<td class="categoriaGrupo"><c:out
															value="${grupo.categoriaNombre}" /></td>
													<td class="tematicaGrupo"><c:out
															value="${grupo.subCategoriaNombre}" /></td>
													<td><c:url value="/PaginaGrupoEspecificado"
															var="grupoUrl">
															<c:param name="nombreGrupo" value="${grupo.nombreGrupo}" />
														</c:url><a href="${grupoUrl}" class="verGrupo">Ver</a></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<div class="tiposMensaje">
										<c:set var="grupoCount"
											value="${fn:length(listadoGruposUsuario)}" />
										<c:set var="maxGrupos" value="${usuario.esPremium ? 6 : 3}" />
										<button
											onclick="${grupoCount < maxGrupos 
								                ? 'openCreacionGrupoModal()' 
								                        : 'mostrarAlertaPersonalizada(\'Ya ha realizado el límite de grupos posibles\')'}">Crear
											Grupo</button>
									</div>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if
							test="${usuario.rolUsu == 'admin' or usuario.rolUsu == 'sadmin'}">
							<c:choose>
								<c:when test="${empty listadoGruposAdmin}">
									<div class="mensajeGrupo">
										<c:out value="No se encontraron grupos disponibles." />
									</div>
									<div class="tiposMensaje">
										<button onclick="openCreacionGrupoModal()">Crear
											Grupo</button>
									</div>
								</c:when>
								<c:otherwise>
									<table class="tablaGrupos tablaGruposMobile"
										style="width: 100%;">
										<thead>
											<tr class="trozoGrupo encabezado" style="height: auto;">
												<th class="NombreGrupo"
													style="font-weight: lighter; position: relative;">Nombre
													<div class="input-contenedor">
														<input id="filtroAdminGruposMobile" type="text"
															placeholder="Filtrar…" /> <i class="fas fa-times"
															id="clearFiltroAdminMobile" title="Limpiar"></i>
													</div>
												</th>
												<th style="font-weight: lighter;">Acciones</th>
											</tr>
										</thead>
										<tbody
											style="display: flex; justify-content: center; align-items: center; flex-direction: column;">
											<c:forEach var="grupo" items="${listadoGruposAdmin}">
												<tr class="trozoGrupo" style="height: auto;">
													<td class="NombreGrupo"><c:out
															value="${grupo.nombreGrupo}" /></td>
													<td><a class="verGrupo2"
														onclick="openModificacionGrupoModal('${grupo.idGrupo}', '${grupo.nombreGrupo}', '${grupo.categoriaNombre}', '${grupo.subCategoriaNombre}', '${grupo.descripcionGrupo}')">
															Modificar </a> <a class="verGrupo2"
														onclick="openEliminacionModal('${grupo.idGrupo}', '${grupo.nombreGrupo}', ${false})">
															Borrar </a></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<div class="tiposMensaje">
										<button onclick="openCreacionGrupoModal()">Crear
											Grupo</button>
									</div>
								</c:otherwise>
							</c:choose>
						</c:if>
					</div>
					<c:if test="${ usuario.rolUsu == 'user'}">
						<div class="col contenedorInfoPerfilDerechoB">
							<div class="contenidoMensaje">
								<div class="contenidoMensaje2">
									<c:choose>
										<c:when
											test="${not empty comentario and not empty comentario.comentarioTexto}">
                    ${comentario.comentarioTexto}
                </c:when>
										<c:otherwise>
                    No se encontraron comentarios para este usuario.
                </c:otherwise>
									</c:choose>
								</div>
							</div>
							<c:choose>
								<c:when
									test="${not empty comentario and not empty comentario.comentarioTexto}">
									<div class="tiposMensaje">
										<button>${comentario.categoriaTipo}</button>
										<button>${comentario.subCategoriaTipo}</button>
									</div>
								</c:when>
								<c:otherwise>
									<div class="tiposMensaje">
										<button onclick="openCreacionComentarioModal(true)">Crear</button>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</c:if>
					<c:if test="${usuario.rolUsu == 'admin'}">
						<div class="col contenedorInfoPerfilDerecho contenedorGruposB">
							<h3>Usuarios</h3>
							<c:choose>
								<c:when test="${empty listadoUsuariosAdmin}">
									<div class="mensajeGrupo">
										<c:out value="No se encontraron grupos disponibles." />
									</div>
									<div class="tiposMensaje">
										<button onclick="openCreacionUsuarioModal()">Crear
											Usuario</button>
									</div>
								</c:when>
								<c:otherwise>
									<table class="tablaUsuarios tablaUsuariosMobile"
										style="width: 100%;">
										<thead>
											<tr class="trozoGrupo encabezado" style="height: auto;">
												<th class="NombreGrupo"
													style="font-weight: lighter; padding-right: 15px; width: 40%;">Alias
													<div class="input-contenedor-mobile">
														<input id="filtroUsuariosAdminMobile" type="text"
															placeholder="Filtrar alias…" /> <i class="fas fa-times"
															id="clearFiltroUsuariosAdminMobile" title="Limpiar"></i>
													</div>
												</th>
												<th class="NombreGrupo"
													style="font-weight: lighter; padding-right: 15px; width: 40%; text-align: left;">Rol</th>
												<th
													style="font-weight: lighter; width: 40%; text-align: left;">Acciones</th>
											</tr>
										</thead>
										<tbody
											style="display: flex; justify-content: center; align-items: center; flex-direction: column;">
											<c:forEach var="usuario" items="${listadoUsuariosAdmin}">
												<tr class="trozoGrupo" style="height: auto;">
													<td class="NombreGrupo"><c:out
															value="${usuario.aliasUsu}" /></td>
													<td class="NombreGrupo"><c:out
															value="${usuario.rolUsu}" /></td>
													<td><a class="verGrupo2"
														onclick="openModificacionModal('${usuario.idUsu}', '${usuario.nombreCompletoUsu}', '${usuario.aliasUsu}', '${usuario.correoElectronicoUsu}', '${usuario.movilUsu}', ${usuario.esPremium}, '${usuario.rolUsu}', ${usuario.esVerificadoEntidad}, '${usuario.fotoString}')">
															Modificar </a> <a class="verGrupo2"
														onclick="openEliminacionModal('${usuario.idUsu}', '${usuario.aliasUsu}', ${true})">
															Borrar </a></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<div class="tiposMensaje">
										<button onclick="openCreacionUsuarioModal()">Crear
											Usuario</button>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</c:if>

					<c:if test="${usuario.rolUsu == 'sadmin'}">
						<div class="col contenedorInfoPerfilDerecho contenedorGruposB">
							<h3>USuarios</h3>
							<c:choose>
								<c:when test="${empty listadoUsuariosSAdmin}">
									<div class="mensajeGrupo">
										<c:out value="No se encontraron grupos disponibles." />
									</div>
									<div class="tiposMensaje">
										<button onclick="openCreacionUsuarioModal()">Crear
											Usuario</button>
									</div>
								</c:when>
								<c:otherwise>
									<table class="tablaUsuarios tablaUsuariosSAdminMobile"
										style="width: 100%;">
										<thead>
											<tr class="trozoGrupo encabezado" style="height: auto;">
												<th class="NombreGrupo"
													style="font-weight: lighter; padding-right: 15px; width: 40%; position: relative;">Alias
													<div class="input-contenedor-sadmin-mobile">
														<input id="filtroUsuariosSAdminMobile" type="text"
															placeholder="Filtrar alias…" /> <i class="fas fa-times"
															id="clearFiltroUsuariosSAdminMobile" title="Limpiar"></i>
													</div>
												</th>
												<th class="NombreGrupo"
													style="font-weight: lighter; padding-right: 15px; width: 40%; text-align: left;">Rol</th>
												<th
													style="font-weight: lighter; width: 40%; text-align: left;">Acciones</th>
											</tr>
										</thead>
										<tbody
											style="display: flex; justify-content: center; align-items: center; flex-direction: column;">
											<c:forEach var="usuario" items="${listadoUsuariosSAdmin}">
												<tr class="trozoGrupo" style="height: auto;">
													<td class="NombreGrupo"><c:out
															value="${usuario.aliasUsu}" /></td>
													<td class="NombreGrupo"><c:out
															value="${usuario.rolUsu}" /></td>
													<td><a class="verGrupo2"
														onclick="openModificacionModal('${usuario.idUsu}', '${usuario.nombreCompletoUsu}', '${usuario.aliasUsu}', '${usuario.correoElectronicoUsu}', '${usuario.movilUsu}', ${usuario.esPremium}, '${usuario.rolUsu}', ${usuario.esVerificadoEntidad}, '${usuario.fotoString}')">
															Modificar </a> <a class="verGrupo2"
														onclick="openEliminacionModal('${usuario.idUsu}', '${usuario.aliasUsu}', ${true})">
															Borrar </a></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<div class="tiposMensaje">
										<button onclick="openCreacionUsuarioModal()">Crear
											Usuario</button>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
					</c:if>
				</div>
			</div>
		</div>

		<!-- MODAL PARA LA CREACION DE USUARIOS -->
		<div id="formularioCreacionUsuarioModal" class="modal">
			<div class="contenidoModal2">
				<span class="close" onclick="closeCreacionUsuarioModal()">&times;</span>
				<h2 id="modalTituloCreacion"
					style="word-wrap: break-word; width: 90%;">Crear Nuevo Usuario</h2>
				<form id="formularioCreacionUsuario" enctype="multipart/form-data"
					onsubmit="enviarCreacionUsuario(event)">

					<div>
						<label for="nombreCompletoNuevo">Nombre Completo</label> <input
							type="text" id="nombreCompletoNuevo" name="nombreCompletoNuevo"
							placeholder="Nombre completo" required />
					</div>

					<div>
						<label for="aliasNuevo">Alias</label> <input type="text"
							id="aliasNuevo" name="aliasNuevo" placeholder="Alias" required />
					</div>

					<div>
						<label for="correoNuevo">Correo Electrónico</label> <input
							type="email" id="correoNuevo" name="correoNuevo"
							placeholder="correo@ejemplo.com" required />
					</div>

					<div>
						<label for="movilNuevo">Móvil</label> <input type="number"
							id="movilNuevo" name="movilNuevo" placeholder="Número de móvil"
							required />
					</div>

					<div class="foto-input-container"
						style="display: flex; flex-direction: column; align-items: center;">
						<div
							style="display: flex; justify-content: center; align-items: center;">
							<label for="fotoNuevo">Fotografía</label>
							<!-- Imagen preview, inicialmente oculta -->
							<img id="fotoPreview" src="" alt="Vista previa"
								style="width: 70px; height: auto; display: none; margin: 5px; border-radius: 10px;" />
						</div>
						<input type="file" id="fotoNuevo" name="fotoNuevo"
							accept="image/jpeg, image/png, image/svg+xml, image/gif, image/webp, image/bmp, image/tiff, image/x-icon" />
					</div>
					<div
						style="display: flex; justify-content: center; align-items: center;">
						<div
							style="display: none; justify-content: center; align-items: center; margin: 10px; flex-direction: column;">
							<label for="esPremiumNuevo">Cuenta Premium</label> <input
								type="checkbox" id="esPremiumNuevo" name="esPremiumNuevo" />
						</div>
						<div
							style="display: flex; justify-content: center; align-items: center; margin: 10px; flex-direction: column;">
							<label for="esVerificadoNuevo">Verificada</label> <input
								type="checkbox" id="esVerificadoNuevo" name="esVerificadoNuevo" />
						</div>
					</div>

					<div
						style="display: flex; justify-content: center; align-items: center;">
						<label for="rolNuevo">Rol de Usuario</label> <select id="rolNuevo"
							name="rolNuevo">
							<option value="user">User</option>
							<option value="admin">Admin</option>
						</select>
					</div>

					<div>
						<button type="submit" class="manga-button">Crear</button>
					</div>
				</form>
			</div>
		</div>

		<!-- MODAL PARA LA CREACION DE GRUPOS -->
		<c:set var="usuario" value="${sessionScope.Usuario}" />
		<div id="formularioCreacionGrupoModal" class="modal">
			<div class="contenidoModal2">
				<span class="close" onclick="closeCreacionGrupoModal()">&times;</span>
				<h2 id="modalTituloGrupoNuevo"
					style="word-wrap: break-word; width: 90%;">Crear Grupo</h2>
				<form id="formularioCreacionGrupo"
					onsubmit="enviarCreacionGrupo(event)">
					<!-- Campo oculto para el creador, tomado del sessionScope -->
					<input type="hidden" id="aliasCreadorNuevo" name="aliasUsu"
						value="${usuario.aliasUsu}" />

					<div>
						<label for="nombreGrupoNuevo">Nombre del Grupo</label> <input
							type="text" id="nombreGrupoNuevo" name="nombreGrupoNuevo"
							placeholder="Nombre del grupo" required />
					</div>

					<div>
						<label for="categoriaGrupoNuevo">Categoría</label> <select
							id="categoriaGrupoNuevo" name="categoriaGrupoNuevo"
							onchange="actualizarSubcategoriasCreacionG()">
							<option value="anime">Anime</option>
							<option value="videojuegos">Videojuegos</option>
							<option value="auxiliar">Auxiliar</option>
						</select>
					</div>

					<div>
						<label for="subCategoriaGrupoNuevo">Subcategoría</label> <select
							id="subCategoriaGrupoNuevo" name="subCategoriaGrupoNuevo">
							<!-- Las opciones se llenarán dinámicamente con JavaScript -->
						</select>
					</div>

					<div>
						<label for="descripcionGrupoNuevo">Descripción</label>
						<textarea id="descripcionGrupoNuevo" name="descripcionGrupoNuevo"
							class="descripcionGrupoNuevo"
							placeholder="Escribe una descripción breve para tu grupo"
							rows="4" cols="50"></textarea>
					</div>

					<div>
						<button type="submit" class="manga-button">Crear</button>
					</div>
				</form>
			</div>
		</div>

		<!-- MODAL PARA LA CREACION DE COMENTARIOS -->
		<c:set var="usuario" value="${sessionScope.Usuario}" />
		<div id="formularioCreacionComentarioModal" class="modal">
			<div class="contenidoModal2">
				<span class="close" onclick="closeCreacionComentarioModal()">&times;</span>
				<h2 id="modalTituloComentarioNuevo"
					style="word-wrap: break-word; width: 90%;">Nuevo Comentario</h2>
				<form id="formularioCreacionComentario"
					onsubmit="enviarCreacionComentario(event)">
					<!-- Campo oculto para el ID del usuario, tomado del sessionScope -->
					<input type="hidden" id="idUsuarioComentario" name="idUsuario"
						value="${usuario.idUsu}" />

					<div>
						<label for="contenidoComentarioNuevo">Comentario</label>
						<textarea id="contenidoComentarioNuevo"
							name="contenidoComentarioNuevo"
							placeholder="Escribe tu comentario de bienvenida aquí..."
							required maxlength="255"></textarea>
					</div>

					<div>
						<label for="categoriaComentarioNuevo">Categoría</label> <select
							id="categoriaComentarioNuevo" name="categoriaComentarioNuevo"
							onchange="actualizarSubcategoriasCreacionComM()" disabled>
							<option value="anime">Anime</option>
							<option value="videojuegos">Videojuegos</option>
							<option value="auxiliar">Auxiliar</option>
						</select>
					</div>

					<div>
						<label for="subCategoriaComentarioNuevo">Subcategoría</label> <select
							id="subCategoriaComentarioNuevo"
							name="subCategoriaComentarioNuevo" disabled>
							<!-- Las opciones se llenarán dinámicamente con JavaScript -->
						</select>
					</div>

					<div>
						<button type="submit" class="manga-button">Publicar</button>
					</div>
				</form>
			</div>
		</div>


		<!-- MODAL PARA LA ELIMINACION DE ELEMENTOS -->
		<div id="formularioEliminacionModal" class="modal">
			<div class="contenidoModal2">
				<span class="close" onclick="closeEliminacionModal()">&times;</span>
				<h2 id="modalTitulo" style="word-wrap: break-word; width: 90%;">Eliminar
				</h2>
				<form id="formularioDatosModal" enctype="multipart/form-data"
					onsubmit="enviarEliminacion(event)">
					<input type="hidden" id="elementoAEliminar" name="modal" /> <input
						type="hidden" id="idElementoAEliminar" name="modal" /> <input
						type="hidden" id="esUsuarioElementoAEliminar" name="modal" />

					<!-- Mostramos el nombre a eliminar de forma destacada -->
					<div>
						<label for="confirmacionInput" id="confirmarEliminacion">Confirma
							eliminacion escribiendo "Eliminar/":</label> <input type="text"
							id="confirmacionInput" placeholder="Escribe el alias aquí"
							required>
					</div>

					<div>
						<button type="submit" class="manga-button">Enviar</button>
					</div>
				</form>
			</div>
		</div>

		<!-- MODAL PARA LA MODIFICACIÓN DE USUARIO -->
		<div id="formularioModificacionModal" class="modal">
			<div class="contenidoModal2">
				<span class="close" onclick="closeModificacionModal()">&times;</span>
				<h2 id="modalTituloMod" style="word-wrap: break-word; width: 90%;">Modificar
					Usuario</h2>
				<form id="formularioModificacionDatosModal"
					enctype="multipart/form-data" onsubmit="enviarModificacion(event)">
					<!-- Campo oculto para el id del usuario -->
					<input type="hidden" id="idUsu" name="idUsu" />

					<div>
						<label for="nombreCompletoUsu">Nombre Completo</label> <input
							type="text" id="nombreCompletoUsu" name="nombreCompletoUsu"
							placeholder="Nombre completo" required />
					</div>
					<div>
						<label for="aliasUsu">Alias</label> <input type="text"
							id="aliasUsu" name="aliasUsu" placeholder="Alias" required />
					</div>
					<div>
						<input type="hidden" id="correoElectronicoUsu"
							name="correoElectronicoUsu" placeholder="correo@ejemplo.com"
							required />
					</div>
					<div>
						<label for="movilUsu">Móvil</label> <input type="number"
							id="movilUsu" name="movilUsu" placeholder="Número de móvil"
							required />
					</div>
					<div class="foto-input-container"
						style="display: flex; flex-direction: column; align-items: center;">
						<div
							style="display: flex; justify-content: center; align-items: center;">
							<label for="fotoUsu">Fotografía</label>
							<!-- Mostrar imagen actual -->
							<img id="fotoActualAdmin" src="" alt="Foto de usuario"
								style="width: 90px; height: auto; display: block; margin: 5px; border-radius: 10px;" />
						</div>
						<!-- Input tipo file -->
						<input type="file" id="fotoUsu" name="fotoUsu"
							accept="image/jpeg, image/png, image/svg+xml, image/gif, image/webp, image/bmp, image/tiff, image/x-icon" />
					</div>
					<div
						style="display: flex; justify-content: center; align-items: center;">
						<div
							style="display: none; justify-content: center; align-items: center; margin: 10px; flex-direction: column;">
							<label for="esPremium">Cuenta Premium</label> <input
								type="checkbox" id="esPremium" name="esPremium" />
						</div>
						<div
							style="display: flex; justify-content: center; align-items: center; margin: 10px; flex-direction: column;">
							<label for="esVerificadoEntidad">Verificada</label> <input
								type="checkbox" id="esVerificadoEntidad"
								name="esVerificadoEntidad" />
						</div>
					</div>
					<div
						style="display: flex; justify-content: center; align-items: center;">
						<label for="rolUsu">Rol de Usuario</label> <select id="rolUsu"
							name="rolUsu">
							<option value="user">User</option>
							<option value="admin">Admin</option>
							<!-- Puedes agregar más opciones según tus necesidades -->
						</select>
					</div>

					<div>
						<button type="submit" class="manga-button">Enviar</button>
					</div>
				</form>
			</div>
		</div>

		<!-- MODAL PARA LA MODIFICACIÓN DE GRUPOS -->
		<div id="formularioModificacionGrupoModal" class="modal">
			<div class="contenidoModal2">
				<span class="close" onclick="closeModificacionGrupoModal()">&times;</span>
				<h2 id="modalTituloGrupo" style="word-wrap: break-word; width: 90%;">Modificar
					Grupo</h2>
				<form id="formularioModificacionGrupo"
					onsubmit="enviarModificacionGrupo(event)">
					<input type="hidden" id="idGrupo" name="idGrupo" />

					<div>
						<label for="nombreGrupo">Nombre del Grupo</label> <input
							type="text" id="nombreGrupo" name="nombreGrupo"
							placeholder="Nombre del grupo" required />
					</div>

					<div>
						<label for="categoriaGrupo">Categoría</label> <select
							id="categoriaGrupo" name="categoriaGrupo"
							onchange="actualizarSubcategoriasGM()">
							<option value="anime">Anime</option>
							<option value="videojuegos">Videojuegos</option>
							<option value="auxiliar">Auxiliar</option>
						</select>
					</div>

					<div>
						<label for="subCategoriaGrupo">Subcategoría</label> <select
							id="subCategoriaGrupo" name="subCategoriaGrupo">
							<!-- Las opciones se llenarán dinámicamente con JavaScript -->
						</select>
					</div>

					<div>
						<label for="descripcionGrupo">Descripción</label>
						<textarea id="descripcionGrupo" name="descripcionGrupo"
							class="descripcionGrupoNuevo"
							placeholder="Escribe una descripción breve para tu grupo"
							rows="4" cols="50"></textarea>
					</div>

					<div>
						<button type="submit" class="manga-button">Guardar</button>
					</div>
				</form>
			</div>
		</div>

		<!-- MODAL PARA EDITAR EL PERFIL PERSONAL -->
		<c:set var="usuarioAModificar" value="${sessionScope.Usuario}" />
		<div id="formularioModal" class="modal">
			<div class="contenidoModal">
				<span class="close" onclick="closeFormularioModal()">&times;</span>
				<h2>Modificar Usuario</h2>
				<form id="formularioDatosModal" enctype="multipart/form-data"
					onsubmit="enviarFormulario(event)">
					<!-- Campos visibles -->
					<div>
						<label for="aliasInput">Alias:</label> <input type="text"
							id="aliasInput" name="aliasUsu"
							value="${usuarioAModificar.aliasUsu}" />
					</div>
					<div>
						<label for="nombreCompletoInput">Nombre Completo:</label> <input
							type="text" id="nombreCompletoInput" name="nombreCompletoUsu"
							value="${usuarioAModificar.nombreCompletoUsu}" />
					</div>
					<div>
						<label for="movilInput">Teléfono Móvil:</label> <input type="tel"
							id="movilInput" name="movilUsu"
							value="${usuarioAModificar.movilUsu}" />
					</div>
					<div>
						<label for="fotoInput">Fotografía:</label>
						<div class="foto-input-container"
							style="display: flex; flex-direction: column; align-items: center;">
							<!-- Mostrar imagen actual -->
							<img id="fotoActual"
								src="data:image/jpeg;base64,${usuarioAModificar.fotoString}"
								alt="Foto de usuario"
								style="width: 90px;  height: auto; display: block; margin: 5px; border-radius: 10px; display: ${usuarioAModificar.fotoString != '' ? 'block' : 'none'};" />
							<!-- Input tipo file -->
							<input type="file" id="fotoInput" name="fotoUsu"
								accept="image/jpeg, image/png, image/svg+xml, image/gif, image/webp, image/bmp, image/tiff, image/x-icon" />
						</div>
					</div>
					<button type="submit" class="manga-button">Guardar</button>
				</form>
			</div>
		</div>

		<!-- ALERTA PERSONALIZADA -->
		<div id="alertaPersonalizada" class="alerta-personalizada">
			<div class="alerta-contenido">
				<p id="alertaMensaje"></p>
				<button onclick="cerrarAlertaPersonalizada()">Aceptar</button>
			</div>
		</div>
	</main>
	<script src="<%=request.getContextPath()%>/js/javaScriptPerfil.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>