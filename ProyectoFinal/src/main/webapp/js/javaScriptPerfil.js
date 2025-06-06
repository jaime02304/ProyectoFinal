//PARTE ALERTA
function alertaDelPremium() {
	let mensaje = "Sentimos las molestias pero esta función no está activada en esta versión de la web, más adelante estará.";
	var alerta = document.getElementById("alertaPersonalizada");
	document.getElementById("alertaMensaje").textContent = mensaje;
	alerta.style.display = "flex";
}

// Función para mostrar alertas personalizadas
function mostrarAlertaPersonalizada(mensaje) {
	var alerta = document.getElementById("alertaPersonalizada");
	document.getElementById("alertaMensaje").textContent = mensaje;
	alerta.style.display = "flex";
}

function cerrarAlertaPersonalizada() {
	document.getElementById("alertaPersonalizada").style.display = "none";
	location.reload();
}

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

//PARTE MODIFICACION USUARIO
// Función que maneja el envío del formulario de forma asíncrona
function enviarFormulario(event) {
	event.preventDefault(); // Evita el envío tradicional del formulario (recarga de página)

	// Obtención de los datos del formulario
	var aliasInput = document.getElementById("aliasInput");
	var nombreCompletoInput = document.getElementById("nombreCompletoInput");
	var movilInput = document.getElementById("movilInput");
	var fotoInput = document.getElementById("fotoInput");

	// Verificar que todos los elementos estén disponibles
	if (!aliasInput || !nombreCompletoInput || !movilInput || !fotoInput) {
		console.error("Uno o más elementos del formulario no se encontraron.");
		return;
	}

	// Recoger los valores actuales y originales del formulario
	var aliasOriginal = aliasInput.defaultValue;
	var nombreOriginal = nombreCompletoInput.defaultValue;
	var movilOriginal = movilInput.defaultValue;
	var aliasNuevo = aliasInput.value;
	var nombreNuevo = nombreCompletoInput.value;
	var movilNuevo = movilInput.value;
	var fotoNuevo = fotoInput.files.length > 0 ? fotoInput.files[0] : null;

	if (aliasNuevo !== aliasOriginal || nombreNuevo !== nombreOriginal || movilNuevo !== movilOriginal || fotoNuevo) {
		// Crear un objeto FormData para enviar los datos
		const formData = new FormData();
		formData.append("aliasUsu", aliasNuevo);
		formData.append("nombreCompletoUsu", nombreNuevo);
		formData.append("movilUsu", movilNuevo);

		if (fotoNuevo) {
			const reader = new FileReader();
			reader.onload = function(event) {
				let fotoBase64 = event.target.result;
				fotoBase64 = fotoBase64.split(',')[1];
				formData.append("fotoString", fotoBase64);
				enviarDatosAlServidor(formData);
			};
			reader.readAsDataURL(fotoNuevo);
		} else {
			const fotoExistente = document.getElementById("fotoActual").src.split(',')[1]; // Obtener la imagen existente
			if (fotoExistente) {
				formData.append("fotoString", fotoExistente); // Enviar la imagen existente
			}
			enviarDatosAlServidor(formData);
		}
	} else {
		closeFormularioModal();
	}
	closeFormularioModal();
}

function enviarDatosAlServidor(formData) {

	fetch("/ModificarUsuario", {
		method: "POST",
		body: formData
	})
		.then(response => {
			if (response.ok) {
				mostrarAlertaPersonalizada("Datos guardados correctamente.");
			} else {
				mostrarAlertaPersonalizada("Error al guardar los datos.");
			}
		})
		.catch(error => {
			console.error("Error en la solicitud:", error);
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}

// Función para abrir el formulario modal
function openFormularioModal() {
	document.getElementById("formularioModal").style.display = "flex";
}

// Función para cerrar el formulario modal
function closeFormularioModal() {
	document.getElementById("formularioModal").style.display = "none";
}

// Evento que escucha cuando el DOM está cargado
document.addEventListener("DOMContentLoaded", function() {
	var formulario = document.getElementById("formularioDatosModal");
	if (formulario) {
		formulario.addEventListener("submit", enviarFormulario); // Asociamos el evento submit al formulario
	} else {
		console.error("No se encontró el formulario.");
	}
});

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

//PARTE DE ELIMINAR
function openEliminacionModal(id, filtradorNombre, esUsuario) {
	// Cacheamos los elementos del DOM
	const modal = document.getElementById("formularioEliminacionModal");
	const idInput = document.getElementById("idElementoAEliminar");
	const nombreInput = document.getElementById("elementoAEliminar");
	const esUsuarioV = document.getElementById("esUsuarioElementoAEliminar");
	const modalTitulo = document.getElementById("modalTitulo");
	const confirmarEliminacion = document.getElementById("confirmarEliminacion");

	// Abrimos el modal y asignamos los valores correspondientes
	modal.style.display = "flex";
	idInput.value = id;
	nombreInput.value = filtradorNombre;
	esUsuarioV.value = esUsuario;
	modalTitulo.innerText = `Eliminar a: ${filtradorNombre}`;
	confirmarEliminacion.innerText = `	Confirma eliminacion escribiendo "Eliminar/${filtradorNombre}":`;

}

function closeEliminacionModal() {
	document.getElementById("formularioEliminacionModal").style.display = "none";
}

function enviarEliminacion(event) {
	event.preventDefault(); // Evita la recarga de la página

	// Obtenemos los valores de los inputs una sola vez
	const nombre = document.getElementById("elementoAEliminar").value;
	const id = document.getElementById("idElementoAEliminar").value;
	const esUsuario = document.getElementById("esUsuarioElementoAEliminar").value;
	const confirmacion = document.getElementById("confirmacionInput").value.trim();
	const confirmacionCorrecta = `Eliminar/${nombre}`;


	// Validamos que la confirmación sea exactamente "Eliminar/NOMBRE"
	if (confirmacion !== confirmacionCorrecta) {
		closeEliminacionModal();
		mostrarAlertaPersonalizada(`Debes escribir exactamente: "${confirmacionCorrecta}" para confirmar la eliminación.`);
		return;
	}

	// Creamos el FormData e incluimos únicamente los campos deseados
	const formData = new FormData();
	formData.append("elementoEliminar", nombre);
	formData.append("idElementoEliminar", id);
	formData.append("esUsuarioEliminar", esUsuario)

	// Realizamos la solicitud POST usando fetch con promesas
	fetch(`/EliminarElementosComoAdmin`, {
		method: "POST",
		body: formData
	})
		.then(function(response) {
			closeEliminacionModal();
			if (response.ok) {
				mostrarAlertaPersonalizada("El elemento ha sido eliminado correctamente. Dale a aceptar para que se vean los cambios.");
			} else {
				mostrarAlertaPersonalizada("Error al eliminar el elemento. Inténtelo nuevamente.");
			}
		})
		.catch(function(error) {
			console.error("Error en la solicitud:", error);
			closeEliminacionModal();
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}


/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

// Abrir modal de modificación de usuario
// Abrir modal y cargar los valores en los campos
function openModificacionModal(id, nombreCompleto, alias, correo, movil, esPremium, rol, esVerificadoEntidad, fotoString) {
	const modal = document.getElementById("formularioModificacionModal");
	modal.style.display = "flex";

	// Asignamos los valores correspondientes a cada campo del formulario
	document.getElementById("idUsu").value = id;
	document.getElementById("nombreCompletoUsu").value = nombreCompleto;
	document.getElementById("aliasUsu").value = alias;
	document.getElementById("correoElectronicoUsu").value = correo;
	document.getElementById("movilUsu").value = movil;
	document.getElementById("esPremium").checked = esPremium;
	document.getElementById("rolUsu").value = rol;
	document.getElementById("esVerificadoEntidad").checked = esVerificadoEntidad;

	// Mostrar imagen actual si existe
	const fotoElement = document.getElementById("fotoActualAdmin");
	if (fotoString) {
		fotoElement.src = `data:image/jpeg;base64,${fotoString}`;
		fotoElement.style.display = 'block';
	} else {
		fotoElement.style.display = 'none';
	}

	// Actualizamos el título del modal
	document.getElementById("modalTituloMod").innerText = `Modificar usuario: ${alias}`;
}

// Cerrar modal
function closeModificacionModal() {
	document.getElementById("formularioModificacionModal").style.display = "none";
}

// Enviar datos de modificación
function enviarModificacion(event) {
	event.preventDefault(); // Evita la recarga de la página

	// Obtenemos los valores de los inputs
	const id = document.getElementById("idUsu").value;
	const nombreCompleto = document.getElementById("nombreCompletoUsu").value;
	const alias = document.getElementById("aliasUsu").value;
	const correo = document.getElementById("correoElectronicoUsu").value;
	const movil = document.getElementById("movilUsu").value;
	const fotoFile = document.getElementById("fotoUsu").files[0]; // Archivo de foto (si se seleccionó)
	const esPremium = document.getElementById("esPremium").checked;
	const rol = document.getElementById("rolUsu").value;
	const esVerificadoEntidad = document.getElementById("esVerificadoEntidad").checked;

	// Creamos el FormData e incluimos los campos deseados
	const formData = new FormData();
	formData.append("idUsu", id);
	formData.append("nombreCompletoUsu", nombreCompleto);
	formData.append("aliasUsu", alias);
	formData.append("correoElectronicoUsu", correo);
	formData.append("movilUsu", movil);
	//formData.append("esPremium", esPremium);
	formData.append("rolUsu", rol);
	formData.append("esVerificadoEntidad", esVerificadoEntidad);
	// Si se seleccionó una nueva foto
	if (fotoFile) {
		const reader = new FileReader();
		reader.onload = function(event) {
			let fotoBase64 = event.target.result;
			fotoBase64 = fotoBase64.split(',')[1]; // Obtener solo la parte base64
			formData.append("fotoString", fotoBase64);
			enviarDatosAlServidorM(formData); // Enviar al servidor después de leer la imagen
		};
		reader.readAsDataURL(fotoFile); // Leer la foto como base64
	} else {
		// Si no se seleccionó una nueva foto, obtener la foto existente y enviarla
		const fotoExistente = document.getElementById("fotoActualAdmin").src.split(',')[1]; // Obtener la imagen existente
		if (fotoExistente) {
			formData.append("fotoString", fotoExistente); // Enviar la imagen existente
		}
		enviarDatosAlServidorM(formData); // Enviar al servidor sin nueva foto
	}


}

// Función para enviar los datos al servidor
function enviarDatosAlServidorM(formData) {
	// Realizamos la solicitud POST usando fetch
	fetch(`/ModificarUsuarioComoAdmin`, {
		method: "POST",
		body: formData
	})
		.then(function(response) {
			closeModificacionModal(); // Cerrar el modal
			if (response.ok) {
				mostrarAlertaPersonalizada("El usuario ha sido modificado correctamente.");
			} else {
				mostrarAlertaPersonalizada("Error al modificar el usuario. Inténtelo nuevamente.");
			}
		})
		.catch(function(error) {
			console.error("Error en la solicitud:", error);
			closeModificacionModal();
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}


/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
//Abrir modal para la modificacion de grupos
function openModificacionGrupoModal(id, nombreGrupo, categoria, subcategoria, descripcion) {
	// Cacheamos el modal del DOM y lo mostramos
	const modal = document.getElementById("formularioModificacionGrupoModal");
	modal.style.display = "flex";

	// Asignamos los valores a los inputs
	document.getElementById("idGrupo").value = id;
	document.getElementById("nombreGrupo").value = nombreGrupo;
	document.getElementById("categoriaGrupo").value = categoria;
	document.getElementById("descripcionGrupo").value = descripcion

	// Actualizar subcategorías según la categoría seleccionada
	actualizarSubcategoriasGM();
	document.getElementById("subCategoriaGrupo").value = subcategoria;

	// Actualizamos el título del modal (opcional)
	document.getElementById("modalTituloGrupo").innerText = `Modificar Grupo: ${nombreGrupo}`;
}

// Función para cerrar el modal
function closeModificacionGrupoModal() {
	document.getElementById("formularioModificacionGrupoModal").style.display = "none";
}

// Función para actualizar dinámicamente las subcategorías
function actualizarSubcategoriasGM() {
	const categoria = document.getElementById("categoriaGrupo").value;
	const subcategoriaSelect = document.getElementById("subCategoriaGrupo");

	// Limpiar opciones previas
	subcategoriaSelect.innerHTML = "";

	// Subcategorías según la categoría seleccionada
	const subcategoriasPorCategoria = {
		anime: [
			{ value: "isekai", text: "Isekai - Mundos paralelos o alternativos" },
			{ value: "shonen", text: "Shonen - Para público juvenil masculino" },
			{ value: "shojo", text: "Shojo - Para público juvenil femenino" }
		],
		videojuegos: [
			{ value: "shooters", text: "Shooters - Disparos en primera o tercera persona" },
			{ value: "aventuras", text: "Aventuras - Exploración y narrativas" },
			{ value: "deportes", text: "Deportes - Juegos deportivos" }
		], auxiliar: [
			{ value: 'auxiliar', text: 'Auxiliar - Grupos auxiliares' }
		]
	};

	// Agregar las opciones de subcategoría según la categoría seleccionada
	subcategoriasPorCategoria[categoria].forEach(sub => {
		const option = document.createElement("option");
		option.value = sub.value;
		option.textContent = sub.text;
		if (sub.value === document.getElementById("subCategoriaGrupo").value) {
			option.selected = true;
		}

		subcategoriaSelect.appendChild(option);
	});
}

// Enviar datos del formulario de modificación del grupo
function enviarModificacionGrupo(event) {
	event.preventDefault(); // Evita la recarga de la página

	// Obtener los valores de los inputs
	const idGrupo = document.getElementById("idGrupo").value;
	const nombreGrupo = document.getElementById("nombreGrupo").value;
	const categoriaGrupo = document.getElementById("categoriaGrupo").value;
	const subCategoriaGrupo = document.getElementById("subCategoriaGrupo").value;
	const descripcionGrupo = document.getElementById("descripcionGrupo").value;
	console.log(subCategoriaGrupo);

	// Crear objeto FormData para enviar los datos
	const formData = new FormData();
	formData.append("idGrupo", idGrupo);
	formData.append("nombreGrupo", nombreGrupo);
	formData.append("categoriaNombre", categoriaGrupo);
	formData.append("subCategoriaNombre", subCategoriaGrupo);
	formData.append("descripcionGrupo", descripcionGrupo)

	// Realizar la solicitud POST usando fetch
	fetch(`/ModificarGrupoComoAdmin`, {
		method: "POST",
		body: formData
	})
		.then(function(response) {
			closeModificacionGrupoModal();
			if (response.ok) {
				mostrarAlertaPersonalizada("El grupo ha sido modificado correctamente. Dale a aceptar para ver los cambios. ");
			} else {
				mostrarAlertaPersonalizada("Error al modificar el grupo. Es posible que ya haya un grupo existente con el mismo nombre. Inténtelo nuevamente.");
			}
		})
		.catch(function(error) {
			console.error("Error en la solicitud:", error);
			closeModificacionGrupoModal();
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
//abrir modal para la creacion de usuarios
function openCreacionUsuarioModal() {
	document.getElementById("formularioCreacionUsuarioModal").style.display = "flex";
}

function closeCreacionUsuarioModal() {
	document.getElementById("formularioCreacionUsuarioModal").style.display = "none";
}

document.getElementById("fotoNuevo").addEventListener("change", function(event) {
	const file = event.target.files[0];
	if (file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			const fotoPreview = document.getElementById("fotoPreview");
			fotoPreview.src = e.target.result;
			fotoPreview.style.display = "block"; // Muestra la imagen una vez cargada
		}
		reader.readAsDataURL(file);
	}
});

function enviarCreacionUsuario(event) {
	event.preventDefault(); // Evita recargar la página

	// Obtener los valores del formulario
	const nombreCompleto = document.getElementById("nombreCompletoNuevo").value;
	const alias = document.getElementById("aliasNuevo").value;
	const correo = document.getElementById("correoNuevo").value;
	const movil = document.getElementById("movilNuevo").value;
	const fotoFile = document.getElementById("fotoNuevo").files[0];
	const esPremium = document.getElementById("esPremiumNuevo").checked;
	const esVerificado = document.getElementById("esVerificadoNuevo").checked;
	const rol = document.getElementById("rolNuevo").value;

	// Crear FormData para enviar los datos
	const formData = new FormData();
	formData.append("nombreCompletoUsu", nombreCompleto);
	formData.append("aliasUsu", alias);
	formData.append("correoElectronicoUsu", correo);
	formData.append("movilUsu", movil);
	//formData.append("esPremium", esPremium);
	formData.append("esVerificadoEntidad", esVerificado);
	formData.append("rolUsu", rol);

	if (fotoFile) {
		const reader = new FileReader();
		reader.onload = function(event) {
			let fotoBase64 = event.target.result;
			fotoBase64 = fotoBase64.split(',')[1];
			formData.append("fotoString", fotoBase64);
			enviarDatosAlServidorC(formData);
		};
		reader.readAsDataURL(fotoFile);
	} else {
		enviarDatosAlServidorC(formData);
	}
}

function enviarDatosAlServidorC(formData) {
	// Realizamos la solicitud POST usando fetch
	fetch(`/CrearUsuarioComoAdmin`, {
		method: "POST",
		body: formData
	})
		.then(function(response) {
			closeCreacionUsuarioModal();
			if (response.ok) {
				mostrarAlertaPersonalizada("El usuario ha sido creado correctamente.");
			} else {
				mostrarAlertaPersonalizada("Error al crear el usuario. Es posible que el usuario ya tenga un alias existente. Inténtelo nuevamente.");
			}
		})
		.catch(function(error) {
			console.error("Error en la solicitud:", error);
			closeCreacionUsuarioModal();
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
//abrir modal para la creacion de grupos
// Función para abrir el modal de creación de grupo
function openCreacionGrupoModal() {
	const modal = document.getElementById("formularioCreacionGrupoModal");
	modal.style.display = "flex";

	// Establecer un valor por defecto en la categoría y actualizar subcategorías
	document.getElementById("categoriaGrupoNuevo").value = "anime";
	actualizarSubcategoriasCreacionG();
}

// Función para cerrar el modal de creación de grupo
function closeCreacionGrupoModal() {
	document.getElementById("formularioCreacionGrupoModal").style.display = "none";
}

// Función para actualizar dinámicamente las subcategorías en el modal de creación
function actualizarSubcategoriasCreacionG() {
	const categoria = document.getElementById("categoriaGrupoNuevo").value;
	const subCategoriaSelect = document.getElementById("subCategoriaGrupoNuevo");

	// Limpiar opciones previas
	subCategoriaSelect.innerHTML = "";

	// Definir las subcategorías disponibles para cada categoría
	const subcategoriasPorCategoria = {
		anime: [
			{ value: "isekai", text: "Isekai - Mundos paralelos o alternativos" },
			{ value: "shonen", text: "Shonen - Para público juvenil masculino" },
			{ value: "shojo", text: "Shojo - Para público juvenil femenino" }
		],
		videojuegos: [
			{ value: "shooters", text: "Shooters - Disparos en primera o tercera persona" },
			{ value: "aventuras", text: "Aventuras - Exploración y narrativas" },
			{ value: "deportes", text: "Deportes - Juegos deportivos" }
		], auxiliar: [
			{ value: 'auxiliar', text: 'Auxiliar - Grupos auxiliares' }
		]
	};

	// Agregar las opciones al select de subcategoría según la categoría seleccionada
	subcategoriasPorCategoria[categoria].forEach(sub => {
		const option = document.createElement("option");
		option.value = sub.value;
		option.textContent = sub.text;
		subCategoriaSelect.appendChild(option);
	});
}

// Función para enviar los datos del formulario de creación del grupo
function enviarCreacionGrupo(event) {
	event.preventDefault(); // Evitar la recarga de la página

	// Obtener los valores de los inputs
	const nombreGrupo = document.getElementById("nombreGrupoNuevo").value;
	const categoriaGrupo = document.getElementById("categoriaGrupoNuevo").value;
	const subCategoriaGrupo = document.getElementById("subCategoriaGrupoNuevo").value;
	const aliasCreador = document.getElementById("aliasCreadorNuevo").value;
	const descripcionGrupo = document.getElementById("descripcionGrupoNuevo").value;


	// Crear FormData para enviar los datos
	const formData = new FormData();
	formData.append("nombreGrupo", nombreGrupo);
	formData.append("categoriaNombre", categoriaGrupo);
	formData.append("subCategoriaNombre", subCategoriaGrupo);
	formData.append("aliasCreadorUString", aliasCreador);
	formData.append("descripcionGrupo", descripcionGrupo); // Añadir la descripción

	// Realizar la solicitud POST usando fetch
	fetch(`/CrearGrupoComoAdmin`, {
		method: "POST",
		body: formData
	})
		.then(function(response) {
			closeCreacionGrupoModal();
			if (response.ok) {
				mostrarAlertaPersonalizada("Grupo creado correctamente. Dale a aceptar para observar los cambios. ");

			} else {
				mostrarAlertaPersonalizada("El grupo ya existe. Intentelo Con otro nombre. ");

			}
		})
		.catch(function(error) {
			console.error("Error en la solicitud:", error);
			closeCreacionGrupoModal();
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

// Función para abrir el modal de creación de comentarios
function openCreacionComentarioModal(primerComentario) {
	const modal = document.getElementById("formularioCreacionComentarioModal");
	modal.style.display = "flex";
	document.getElementById("categoriaComentarioNuevo").value = "auxiliar"; // Establecer valor por defecto
	actualizarSubcategoriasCreacionComM();

	const contenidoComentario = document.getElementById("contenidoComentarioNuevo");
	const categoriaSelect = document.getElementById("categoriaComentarioNuevo");
	const subCategoriaSelect = document.getElementById("subCategoriaComentarioNuevo");

	if (primerComentario) {
		// Cargar automáticamente el comentario de bienvenida
		categoriaSelect.disabled = true;  // Deshabilitar categoría si es comentario de bienvenida
		subCategoriaSelect.disabled = true; // Deshabilitar subcategoría
	} else {
		// Habilitar el formulario de categorías y subcategorías si ya tiene un comentario
		categoriaSelect.disabled = false;
		subCategoriaSelect.disabled = false;
	}
}
// Función para cerrar el modal de creación de comentarios
function closeCreacionComentarioModal() {
	document.getElementById("formularioCreacionComentarioModal").style.display = "none";
}

// Función para actualizar dinámicamente las subcategorías
function actualizarSubcategoriasCreacionComM() {
	const categoria = document.getElementById("categoriaComentarioNuevo").value;
	const subCategoriaSelect = document.getElementById("subCategoriaComentarioNuevo");
	subCategoriaSelect.innerHTML = "";

	const subcategoriasPorCategoria = {
		anime: [
			{ value: "isekai", text: "Isekai - Mundos paralelos o alternativos" },
			{ value: "shonen", text: "Shonen - Para público juvenil masculino" },
			{ value: "shojo", text: "Shojo - Para público juvenil femenino" }
		],
		videojuegos: [
			{ value: "shooters", text: "Shooters - Disparos en primera o tercera persona" },
			{ value: "aventuras", text: "Aventuras - Exploración y narrativas" },
			{ value: "deportes", text: "Deportes - Juegos deportivos" }
		],
		auxiliar: [
			{ value: "auxiliar", text: "Auxiliar - Comentarios auxiliares" }
		]
	};

	subcategoriasPorCategoria[categoria].forEach(sub => {
		const option = document.createElement("option");
		option.value = sub.value;
		option.textContent = sub.text;
		subCategoriaSelect.appendChild(option);
	});
}

// Función para enviar el comentario
function enviarCreacionComentario(event) {
	event.preventDefault();
	const contenidoComentario = document.getElementById("contenidoComentarioNuevo").value;
	const categoriaComentario = document.getElementById("categoriaComentarioNuevo").value;
	const subCategoriaComentario = document.getElementById("subCategoriaComentarioNuevo").value;
	const idUsuario = document.getElementById("idUsuarioComentario").value;

	const formData = new FormData();
	formData.append("comentarioTexto", contenidoComentario);
	formData.append("categoriaTipo", categoriaComentario);
	formData.append("subCategoriaTipo", subCategoriaComentario);
	formData.append("idUsuario", idUsuario);

	fetch(`/CrearComentario`, {
		method: "POST",
		body: formData
	})
		.then(response => {
			closeCreacionComentarioModal();
			if (response.ok) {
				mostrarAlertaPersonalizada("El comentario ha sido creado correctamente. Dale a aceptar para verificar los cambios. ");
			} else {
				mostrarAlertaPersonalizada("Error al crear el comentario. Inténtelo nuevamente.");
			}
		})
		.catch(error => {
			console.error("Error en la solicitud:", error);
			closeCreacionComentarioModal();
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}


/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */


function descargarUsuariosPdf() {
	const formData = new FormData();
		formData.append("Hola", "Hola");
		fetch("/ExportPDFUsu", {
				method: "POST",
				body: formData
			})
		.then(response => {
			if (!response.ok) {
				throw new Error(`Error al generar el PDF (status ${response.status})`);
			}
			return response.blob();
		})
		.then(blob => {
			const downloadUrl = window.URL.createObjectURL(blob);
			const a = document.createElement('a');
			a.href = downloadUrl;
			a.download = 'usuarios.pdf';
			document.body.appendChild(a);
			a.click();
			a.remove();
			window.URL.revokeObjectURL(downloadUrl);
		})
		.catch(error => {
			console.error('Error al descargar el PDF:', error);
			alert('Se produjo un error al descargar el PDF.');
		});
}

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */


document.addEventListener('DOMContentLoaded', function() {
	const desktopInput = document.getElementById('filtroAdminGrupos');
	const desktopClear = document.getElementById('clearFiltroAdmin');
	const mobileInput = document.getElementById('filtroAdminGruposMobile');
	const mobileClear = document.getElementById('clearFiltroAdminMobile');

	const desktopRows = Array.from(document.querySelectorAll('.tablaGrupos tbody .trozoGrupo'));
	const mobileRows = Array.from(document.querySelectorAll('.tablaGruposMobile tbody .trozoGrupo'));

	let currentFilter = '';

	function applyFilter(txt, rows) {
		rows.forEach(r => {
			const nombre = r.querySelector('.NombreGrupo').textContent.trim().toLowerCase();
			r.style.display = nombre.startsWith(txt) ? '' : 'none';
		});
	}

	function updateClearButton(input, clearBtn) {
		clearBtn.style.display = input.value.trim() ? 'inline' : 'none';
	}

	// Cuando uno escribe en pantalla, sincronizamos movil, y viceversa
	desktopInput.addEventListener('input', function() {
		currentFilter = this.value.trim().toLowerCase();
		mobileInput.value = this.value;
		updateClearButton(desktopInput, desktopClear);
		updateClearButton(mobileInput, mobileClear);
		applyFilter(currentFilter, desktopRows);
	});
	mobileInput.addEventListener('input', function() {
		currentFilter = this.value.trim().toLowerCase();
		desktopInput.value = this.value;
		updateClearButton(desktopInput, desktopClear);
		updateClearButton(mobileInput, mobileClear);
		applyFilter(currentFilter, mobileRows);
	});

	// Cuando pulsas la X en pantalla o movil
	desktopClear.addEventListener('click', () => {
		currentFilter = '';
		desktopInput.value = mobileInput.value = '';
		updateClearButton(desktopInput, desktopClear);
		updateClearButton(mobileInput, mobileClear);
		desktopRows.forEach(r => r.style.display = '');
		mobileRows.forEach(r => r.style.display = '');
		desktopInput.focus();
	});
	mobileClear.addEventListener('click', () => {
		currentFilter = '';
		desktopInput.value = mobileInput.value = '';
		updateClearButton(desktopInput, desktopClear);
		updateClearButton(mobileInput, mobileClear);
		desktopRows.forEach(r => r.style.display = '');
		mobileRows.forEach(r => r.style.display = '');
		mobileInput.focus();
	});

	const mq = window.matchMedia('(max-width: 768px)');
	function onBreakpointChange(e) {
		if (e.matches) {
			// estamos en movil
			mobileInput.value = currentFilter;
			updateClearButton(mobileInput, mobileClear);
			applyFilter(currentFilter, mobileRows);
		} else {
			// estamos en pantalla
			desktopInput.value = currentFilter;
			updateClearButton(desktopInput, desktopClear);
			applyFilter(currentFilter, desktopRows);
		}
	}
	mq.addEventListener('change', onBreakpointChange);

	window.addEventListener('resize', () => onBreakpointChange(mq));

	onBreakpointChange(mq);
});
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
document.addEventListener('DOMContentLoaded', function() {
	const userDesktopField = document.getElementById('filtroUsuariosAdmin');
	const userDesktopClearBtn = document.getElementById('clearFiltroUsuariosAdmin');
	const userMobileField = document.getElementById('filtroUsuariosAdminMobile');
	const userMobileClearBtn = document.getElementById('clearFiltroUsuariosAdminMobile');

	const userDesktopItems = Array.from(document.querySelectorAll('.tablaUsuarios tbody .trozoGrupo'));
	const userMobileItems = Array.from(document.querySelectorAll('.tablaUsuariosMobile tbody .trozoGrupo'));

	let userFilterText = '';

	function filterUsers(txt, items) {
		items.forEach(item => {
			const alias = item.querySelector('.NombreGrupo').textContent.trim().toLowerCase();
			item.style.display = alias.startsWith(txt) ? '' : 'none';
		});
	}

	function refreshClearBtn(field, clearBtn) {
		clearBtn.style.display = field.value.trim() ? 'inline' : 'none';
	}

	// sincroniza valor y botón “X” al escribir en pantalla
	userDesktopField.addEventListener('input', function() {
		userFilterText = this.value.trim().toLowerCase();
		userMobileField.value = this.value;
		refreshClearBtn(userDesktopField, userDesktopClearBtn);
		refreshClearBtn(userMobileField, userMobileClearBtn);
		filterUsers(userFilterText, userDesktopItems);
	});

	// sincroniza valor y botón “X” al escribir en movil
	userMobileField.addEventListener('input', function() {
		userFilterText = this.value.trim().toLowerCase();
		userDesktopField.value = this.value;
		refreshClearBtn(userDesktopField, userDesktopClearBtn);
		refreshClearBtn(userMobileField, userMobileClearBtn);
		filterUsers(userFilterText, userMobileItems);
	});

	// limpiar ambos filtros al clicar la “X” en pantalla
	userDesktopClearBtn.addEventListener('click', () => {
		userFilterText = '';
		userDesktopField.value = userMobileField.value = '';
		refreshClearBtn(userDesktopField, userDesktopClearBtn);
		refreshClearBtn(userMobileField, userMobileClearBtn);
		userDesktopItems.forEach(i => i.style.display = '');
		userMobileItems.forEach(i => i.style.display = '');
		userDesktopField.focus();
	});

	// limpiar ambos filtros al clicar la “X” en movil
	userMobileClearBtn.addEventListener('click', () => {
		userFilterText = '';
		userDesktopField.value = userMobileField.value = '';
		refreshClearBtn(userDesktopField, userDesktopClearBtn);
		refreshClearBtn(userMobileField, userMobileClearBtn);
		userDesktopItems.forEach(i => i.style.display = '');
		userMobileItems.forEach(i => i.style.display = '');
		userMobileField.focus();
	});

	const userViewMq = window.matchMedia('(max-width: 768px)');
	function onUserViewChange(evt) {
		if (evt.matches) {
			// vista mobile
			userMobileField.value = userFilterText;
			refreshClearBtn(userMobileField, userMobileClearBtn);
			filterUsers(userFilterText, userMobileItems);
		} else {
			// vista desktop
			userDesktopField.value = userFilterText;
			refreshClearBtn(userDesktopField, userDesktopClearBtn);
			filterUsers(userFilterText, userDesktopItems);
		}
	}

	userViewMq.addEventListener('change', onUserViewChange);
	if (userViewMq.addListener) userViewMq.addListener(onUserViewChange);
	window.addEventListener('resize', () => onUserViewChange(userViewMq));
	onUserViewChange(userViewMq);
});
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

document.addEventListener('DOMContentLoaded', function() {
	const sAdminDesktopInput = document.getElementById('filtroUsuariosSAdmin');
	const sAdminDesktopClear = document.getElementById('clearFiltroUsuariosSAdmin');
	const sAdminMobileInput = document.getElementById('filtroUsuariosSAdminMobile');
	const sAdminMobileClear = document.getElementById('clearFiltroUsuariosSAdminMobile');

	const sAdminDesktopRows = Array.from(document.querySelectorAll('.tablaUsuarios tbody .trozoGrupo'));
	const sAdminMobileRows = Array.from(document.querySelectorAll('.tablaUsuarios tbody .trozoGrupo'));

	let currentFilterSAdmin = '';

	function applySAdminFilter(txt, rows) {
		rows.forEach(r => {
			const alias = r.querySelector('.NombreGrupo').textContent.trim().toLowerCase();
			r.style.display = alias.startsWith(txt) ? '' : 'none';
		});
	}

	function updateSAdminClearBtn(input, btn) {
		btn.style.display = input.value.trim() ? 'inline' : 'none';
	}

	sAdminDesktopInput.addEventListener('input', function() {
		currentFilterSAdmin = this.value.trim().toLowerCase();
		sAdminMobileInput.value = this.value;
		updateSAdminClearBtn(sAdminDesktopInput, sAdminDesktopClear);
		updateSAdminClearBtn(sAdminMobileInput, sAdminMobileClear);
		applySAdminFilter(currentFilterSAdmin, sAdminDesktopRows);
	});

	sAdminMobileInput.addEventListener('input', function() {
		currentFilterSAdmin = this.value.trim().toLowerCase();
		sAdminDesktopInput.value = this.value;
		updateSAdminClearBtn(sAdminDesktopInput, sAdminDesktopClear);
		updateSAdminClearBtn(sAdminMobileInput, sAdminMobileClear);
		applySAdminFilter(currentFilterSAdmin, sAdminMobileRows);
	});

	sAdminDesktopClear.addEventListener('click', () => {
		currentFilterSAdmin = '';
		sAdminDesktopInput.value = sAdminMobileInput.value = '';
		updateSAdminClearBtn(sAdminDesktopInput, sAdminDesktopClear);
		updateSAdminClearBtn(sAdminMobileInput, sAdminMobileClear);
		sAdminDesktopRows.forEach(r => r.style.display = '');
		sAdminMobileRows.forEach(r => r.style.display = '');
		sAdminDesktopInput.focus();
	});

	sAdminMobileClear.addEventListener('click', () => {
		currentFilterSAdmin = '';
		sAdminDesktopInput.value = sAdminMobileInput.value = '';
		updateSAdminClearBtn(sAdminDesktopInput, sAdminDesktopClear);
		updateSAdminClearBtn(sAdminMobileInput, sAdminMobileClear);
		sAdminDesktopRows.forEach(r => r.style.display = '');
		sAdminMobileRows.forEach(r => r.style.display = '');
		sAdminMobileInput.focus();
	});

	const sAdminMq = window.matchMedia('(max-width: 768px)');
	function onSAdminBreakpointChange(e) {
		if (e.matches) {
			sAdminMobileInput.value = currentFilterSAdmin;
			updateSAdminClearBtn(sAdminMobileInput, sAdminMobileClear);
			applySAdminFilter(currentFilterSAdmin, sAdminMobileRows);
		} else {
			sAdminDesktopInput.value = currentFilterSAdmin;
			updateSAdminClearBtn(sAdminDesktopInput, sAdminDesktopClear);
			applySAdminFilter(currentFilterSAdmin, sAdminDesktopRows);
		}
	}
	sAdminMq.addEventListener('change', onSAdminBreakpointChange);
	if (sAdminMq.addListener) sAdminMq.addListener(onSAdminBreakpointChange);
	window.addEventListener('resize', () => onSAdminBreakpointChange(sAdminMq));
	onSAdminBreakpointChange(sAdminMq);
});

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */