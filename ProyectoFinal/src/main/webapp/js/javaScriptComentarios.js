document.addEventListener('DOMContentLoaded', () => {
	// Quitar estado activo por defecto
	buttons.forEach(btn => btn.classList.remove('active'));
	subcategorias.forEach(sub => sub.classList.remove('active'));
});

const buttons = document.querySelectorAll(".categorias button");
const subcategorias = document.querySelectorAll(".subcategorias");

buttons.forEach((button) => {
	button.addEventListener("click", () => {
		// Quitar clase activa de todos los botones y secciones
		buttons.forEach((btn) => btn.classList.remove("active"));
		subcategorias.forEach((sub) => sub.classList.remove("active"));

		// Activar el botón y la sección correspondiente
		button.classList.add("active");
		const target = button.getAttribute("data-target");
		document.getElementById(target).classList.add("active");
	});
});

const categoriaFiltro = document.getElementById("categoriaFiltro");
const subcategoriaFiltro = document.getElementById("subcategoriaFiltro");

const subcategoriasPorCategoria = {
	anime: ["Shonen", "Isekai", "Shojo"],
	videojuegos: ["Shooters", "Aventuras", "Deportes"],
	auxiliar: ["auxiliar"],
};

function actualizarSubcategoriasFiltro() {
	const categoriaSeleccionada = categoriaFiltro.value;
	const subcategorias = subcategoriasPorCategoria[categoriaSeleccionada];

	// Limpiar subcategorías anteriores
	subcategoriaFiltro.innerHTML = "";

	// Añadir nuevas opciones
	subcategorias.forEach((sub) => {
		const opcion = document.createElement("option");
		opcion.value = sub.toLowerCase();
		opcion.textContent = sub;
		subcategoriaFiltro.appendChild(opcion);
	});
}

// Inicializar y actualizar cuando se cambie la categoría
categoriaFiltro.addEventListener("change", actualizarSubcategoriasFiltro);
actualizarSubcategoriasFiltro();



/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

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

	const contextPath = window.location.pathname.split('/')[1];

	fetch(`/${contextPath}/CrearComentario`, {
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

const subcatsAll = document.querySelectorAll(".subcategorias");
const comentarios = document.querySelectorAll(".comentario");

let currentCat = null;
let currentSub = null;

function filterComments() {
	const todos = document.querySelectorAll(".comentario, .miContenido, .contenidoOtros");
	todos.forEach(c => {
		if (!currentCat) {
			c.style.display = "";
		} else {
			const cat = c.dataset.categoria.toLowerCase();
			const sub = c.dataset.subcategoria.toLowerCase();
			const okCat = cat === currentCat;
			const okSub = !currentSub || sub === currentSub;
			c.style.display = (okCat && okSub) ? "" : "none";
		}
	});
}

// Handler de click en categorías (desktop)
buttons.forEach(btn => {
	btn.addEventListener("click", () => {
		const target = btn.dataset.target;
		// Si clicas la misma, desactivas todo
		if (currentCat === target) {
			currentCat = null;
			currentSub = null;
			buttons.forEach(b => b.classList.remove("active"));
			subcatsAll.forEach(s => s.classList.remove("active"));
		} else {
			// Si es nueva categoría, activas solo esa
			currentCat = target;
			currentSub = null;
			buttons.forEach(b => b.classList.toggle("active", b === btn));
			subcatsAll.forEach(s => s.classList.toggle("active", s.id === target));
			// además, desactiva cualquier sub-boton activo
			document.querySelectorAll(`[data-sub].active`).forEach(x => x.classList.remove("active"));
		}
		filterComments();
	});
});

subcatsAll.forEach(section => {
	section.addEventListener("click", e => {
		if (!e.target.matches("button[data-sub]")) return;
		const sub = e.target.dataset.sub.toLowerCase();
		if (currentSub === sub) {
			currentSub = null;
			e.target.classList.remove("active");
		} else {
			currentSub = sub;
			section.querySelectorAll("button[data-sub]").forEach(b => b.classList.remove("active"));
			e.target.classList.add("active");
		}
		filterComments();
	});
});


// Cuando cambias categoría en móvil, si vuelves a seleccionar la misma, la desactivas
categoriaFiltro.addEventListener("change", () => {
	if (currentCat === categoriaFiltro.value) {
		categoriaFiltro.value = "";   // forzamos “no selección”
		currentCat = null;
	} else {
		currentCat = categoriaFiltro.value;
	}
	// reset sub siempre que cambie categoría
	currentSub = null;
	actualizarSubcategoriasFiltro();
	filtrarMobile();
});

// Subcategoría móvil
subcategoriaFiltro.addEventListener("change", () => {
	if (currentSub === subcategoriaFiltro.value) {
		subcategoriaFiltro.value = "";
		currentSub = null;
	} else {
		currentSub = subcategoriaFiltro.value;
	}
	filtrarMobile();
});

function filtrarMobile() {
	const comentarios = document.querySelectorAll(
		".contenedorComentario2 .miContenido, " +
		".contenedorComentario2 .contenidoOtros"
	);
	comentarios.forEach(c => {
		if (!currentCat) {
			c.style.display = "";
		} else {
			const cat = c.dataset.categoria;
			const sub = c.dataset.subcategoria;
			const okCat = cat === currentCat;
			const okSub = !currentSub || sub === currentSub;
			c.style.display = (okCat && okSub) ? "" : "none";
		}
	});
}

// Al cargar la página
filterComments();
filtrarMobile();