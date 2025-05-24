document.addEventListener('DOMContentLoaded', () => {
	// Quitar estado activo por defecto en categorías y subcategorías
	categoryButtons.forEach(btn => btn.classList.remove('active'));
	subcategorySections.forEach(sec => sec.classList.remove('active'));
});

// Elementos de filtrado de pantalla
const categoryButtons = document.querySelectorAll('.categorias button');
const subcategorySections = document.querySelectorAll('.subcategorias');

categoryButtons.forEach(button => {
	button.addEventListener('click', () => {
		// Resetear estado activo
		categoryButtons.forEach(b => b.classList.remove('active'));
		subcategorySections.forEach(s => s.classList.remove('active'));

		// Activar botón y sección correspondiente
		button.classList.add('active');
		const target = button.getAttribute('data-target');
		document.getElementById(target).classList.add('active');
	});
});

// Elementos de filtrado de mobile
const categoriaFiltro = document.getElementById('categoriaFiltro');
const subcategoriaFiltro = document.getElementById('subcategoriaFiltro');

// Definición de subcategorías por categoría
const subcatsByCategory = {
	anime: ['Shonen', 'Isekai', 'Shojo'],
	videojuegos: ['Shooters', 'Aventuras', 'Deportes'],
	auxiliar: ['auxiliar']
};

function updateMobilSubcategorias() {
	const selected = categoriaFiltro.value;
	const options = subcatsByCategory[selected] || [];
	subcategoriaFiltro.innerHTML = '';
	options.forEach(sub => {
		const opt = document.createElement('option');
		opt.value = sub.toLowerCase();
		opt.textContent = sub;
		subcategoriaFiltro.appendChild(opt);
	});
}

categoriaFiltro.addEventListener('change', updateMobilSubcategorias);
updateMobilSubcategorias();

// Funciones de alerta
function mostrarAlerta(msg) {
	const alerta = document.getElementById('alertaPersonalizada');
	document.getElementById('alertaMensaje').textContent = msg;
	alerta.style.display = 'flex';
}
function cerrarAlerta() {
	document.getElementById('alertaPersonalizada').style.display = 'none';
	location.reload();
}
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

// Modal creación de grupo
function openCreacionGrupoModal(yaExistenGrupo) {
	const modal = document.getElementById('formularioCreacionGrupoModal');
	modal.style.display = 'flex';
	const catSelect = document.getElementById('categoriaGrupoNuevo');
	const subSelect = document.getElementById('subCategoriaGrupoNuevo');
	catSelect.value = 'auxiliar';
	updateCreacionSubcategorias();
	if (yaExistenGrupo) {
		catSelect.disabled = true;
		subSelect.disabled = true;
	} else {
		catSelect.disabled = false;
		subSelect.disabled = false;
	}
}
function closeCreacionGrupoModal() {
	document.getElementById('formularioCreacionGrupoModal').style.display = 'none';
}

// Subcategorías dinámicas en creación
function updateCreacionSubcategorias() {
	const cat = document.getElementById('categoriaGrupoNuevo').value;
	const subSelect = document.getElementById('subCategoriaGrupoNuevo');
	subSelect.innerHTML = '';
	const mapping = {
		anime: [
			{ value: 'isekai', text: 'Isekai - Mundos paralelos o alternativos' },
			{ value: 'shonen', text: 'Shonen - Para público juvenil masculino' },
			{ value: 'shojo', text: 'Shojo - Para público juvenil femenino' }
		],
		videojuegos: [
			{ value: 'shooters', text: 'Shooters - Disparos en primera o tercera persona' },
			{ value: 'aventuras', text: 'Aventuras - Exploración y narrativas' },
			{ value: 'deportes', text: 'Deportes - Juegos deportivos' }
		],
		auxiliar: [
			{ value: 'auxiliar', text: 'Auxiliar - Grupos auxiliares' }
		]
	};
	(mapping[cat] || []).forEach(opt => {
		const o = document.createElement('option');
		o.value = opt.value;
		o.textContent = opt.text;
		subSelect.appendChild(o);
	});
}
document.getElementById('categoriaGrupoNuevo').addEventListener('change', updateCreacionSubcategorias);

// Envío de creación de grupo
function enviarCreacionGrupo(event) {
	event.preventDefault();
	const nombreGrupo = document.getElementById('nombreGrupoNuevo').value;
	const descripcion = document.getElementById('descripcionGrupoNuevo').value;
	const categoria = document.getElementById('categoriaGrupoNuevo').value;
	const subcategoria = document.getElementById('subCategoriaGrupoNuevo').value;
	const AliasCreador = document.getElementById('AliasCreador').value;

	const formData = new FormData();
	formData.append('nombreGrupo', nombreGrupo);
	formData.append('descripcionGrupo', descripcion);
	formData.append('categoriaNombre', categoria);
	formData.append('subCategoriaNombre', subcategoria);
	formData.append('aliasCreadorUString', AliasCreador);

	const contextPath = window.location.pathname.split('/')[1];
	fetch(`/${contextPath}/CrearGrupoComoAdmin`, {
		method: 'POST',
		body: formData
	})
		.then(resp => {
			closeCreacionGrupoModal();
			if (resp.ok) mostrarAlerta('El grupo ha sido creado correctamente.');
			else mostrarAlerta('Error al crear el grupo.');
		})
		.catch(() => {
			closeCreacionGrupoModal();
			mostrarAlerta('Ocurrió un error inesperado.');
		});
}


/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

const subcatsAll = document.querySelectorAll('.subcategorias');
let currentCat = null;
let currentSub = null;

// ----- FILTRO PARA PANTALLA GRANDE -----
function filtroGrupo() {
	const all = document.querySelectorAll('.contenedorGrupo .trozoPaginaGrupo');
	all.forEach(el => {
		if (!currentCat) {
			el.style.display = '';
		} else {
			const cat = el.dataset.categoria.toLowerCase();
			const sub = el.dataset.subcategoria.toLowerCase();
			const okCat = cat === currentCat;
			const okSub = !currentSub || sub === currentSub;
			el.style.display = (okCat && okSub) ? '' : 'none';
		}
	});
}

// ----- FILTRO PARA MÓVIL -----
function filtroMobilGrupo() {
	const mobiles = document.querySelectorAll('.contenedorGrupo2 .trozoPaginaGrupo');
	mobiles.forEach(el => {
		if (!currentCat) {
			el.style.display = '';
		} else {
			const cat = el.dataset.categoria.toLowerCase();
			const sub = el.dataset.subcategoria.toLowerCase();
			const okCat = cat === currentCat;
			const okSub = !currentSub || sub === currentSub;
			el.style.display = (okCat && okSub) ? '' : 'none';
		}
	});
}

// ----- BOTONES DE CATEGORÍA PARA PANTALLA -----
categoryButtons.forEach(btn => {
	btn.addEventListener('click', () => {
		const tgt = btn.dataset.target.toLowerCase();
		if (currentCat === tgt) {
			currentCat = null;
			currentSub = null;
			categoryButtons.forEach(b => b.classList.remove('active'));
			subcatsAll.forEach(s => s.classList.remove('active'));
		} else {
			currentCat = tgt;
			currentSub = null;
			categoryButtons.forEach(b => b.classList.toggle('active', b === btn));
			subcatsAll.forEach(s => s.classList.toggle('active', s.id.toLowerCase() === tgt));
		}
		filtroGrupo();
		filtroMobilGrupo(); // ← Actualiza también móvil
	});
});

// ----- BOTONES DE SUBCATEGORÍA PARA PANTALLA -----
subcatsAll.forEach(section => {
	section.addEventListener('click', e => {
		if (!e.target.matches('button[data-sub]')) return;
		const sub = e.target.dataset.sub.toLowerCase();
		if (currentSub === sub) {
			currentSub = null;
			e.target.classList.remove('active');
		} else {
			currentSub = sub;
			section.querySelectorAll('button[data-sub]').forEach(b => b.classList.remove('active'));
			e.target.classList.add('active');
		}
		filtroGrupo();
		filtroMobilGrupo(); // ← También actualiza móvil
	});
});

// ----- SELECTS PARA FILTRO MÓVIL (asegúrate de tener los selects con IDs correctos) -----
categoriaFiltro.addEventListener('change', () => {
	currentCat = categoriaFiltro.value.toLowerCase() || null;
	currentSub = null;
	filtroGrupo();       // opcional: también puede afectar desktop
	filtroMobilGrupo();
});

subcategoriaFiltro.addEventListener('change', () => {
	currentSub = subcategoriaFiltro.value.toLowerCase() || null;
	filtroGrupo();       // opcional: también puede afectar desktop
	filtroMobilGrupo();
});

// Inicializa al cargar
filtroGrupo();
filtroMobilGrupo();

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

document.addEventListener('DOMContentLoaded', function() {
	let lastOpenedDesc = null;

	document.querySelectorAll('.nombrePaginaGrupo').forEach(function(nombreDiv) {
		nombreDiv.style.cursor = 'pointer';
		nombreDiv.addEventListener('click', function(e) {
			e.stopPropagation();
			const cont = this.closest('.trozoPaginaGrupo');
			const desc = cont.querySelector('.descripcionPaginaGrupo');

			if (!desc) return;

			// Si ya hay uno abierto y no es el mismo, lo cerramos
			if (lastOpenedDesc && lastOpenedDesc !== desc) {
				lastOpenedDesc.classList.remove('show');
			}

			// Toggle el actual
			desc.classList.toggle('show');

			// Actualizamos la referencia
			lastOpenedDesc = desc.classList.contains('show') ? desc : null;
		});
	});

	// Clic fuera de cualquier bloque cierra el actual
	document.addEventListener('click', function(e) {
		if (!e.target.closest('.trozoPaginaGrupo') && lastOpenedDesc) {
			lastOpenedDesc.classList.remove('show');
			lastOpenedDesc = null;
		}
	});
});


/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

document.addEventListener('DOMContentLoaded', function() {
	document.querySelectorAll('.trozoPaginaGrupoInfo').forEach(container => {
		const abrir = container.querySelector('.abrir-search');
		const cerrar = container.querySelector('.cerrar-search');
		const input = container.querySelector('.search-inputInfo');

		// abrir al hacer click en la lupa
		abrir.addEventListener('click', () => {
			container.classList.add('active');
			input.focus();
		});

		// cerrar al hacer click en la X
		cerrar.addEventListener('click', () => {
			container.classList.remove('active');
			input.value = '';
		});

		// opcional: cerrar si se hace clic fuera
		document.addEventListener('click', e => {
			if (!container.contains(e.target)) {
				container.classList.remove('active');
			}
		});
	});
});

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */


document.addEventListener('DOMContentLoaded', function() {
	// apuntamos al contenedor “desktop”
	const container = document.querySelector('.contenedorGrupo .trozoPaginaGrupoInfo');
	const abrir = container.querySelector('.abrir-search');
	const cerrar = container.querySelector('.cerrar-search');
	const input = container.querySelector('.search-inputInfo');

	// todos los ítems a filtrar
	const grupos = Array.from(document.querySelectorAll('.contenedorGrupo .trozoPaginaGrupo'));

	// abrir input
	abrir.addEventListener('click', () => {
		container.classList.add('active');
		input.focus();
	});

	// cerrar input y resetear filtro
	cerrar.addEventListener('click', () => {
		container.classList.remove('active');
		input.value = '';
		grupos.forEach(g => g.style.display = '');
	});

	// filtro en tiempo real
	input.addEventListener('input', () => {
		const txt = input.value.trim().toLowerCase();
		grupos.forEach(div => {
			const nombre = div.querySelector('.nombrePaginaGrupo')
				.textContent.trim().toLowerCase();
			div.style.display = nombre.startsWith(txt) ? '' : 'none';
		});
	});

	// cerrar si clicas fuera
	document.addEventListener('click', e => {
		if (!container.contains(e.target)) {
			container.classList.remove('active');
		}
	});
});

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

document.addEventListener('DOMContentLoaded', function() {
	const container = document.querySelector('.contenedorGrupo2 .trozoPaginaGrupoInfo');
	const abrir = container.querySelector('.abrir-search');
	const cerrar = container.querySelector('.cerrar-search');
	const input = container.querySelector('#filtroGrupos');

	// todos los grupos bajo este contenedor
	const grupos = Array.from(document.querySelectorAll('.contenedorGrupo2 .trozoPaginaGrupo'));

	// abrir búsqueda
	abrir.addEventListener('click', () => {
		container.classList.add('active');
		input.focus();
	});

	// cerrar búsqueda
	cerrar.addEventListener('click', () => {
		container.classList.remove('active');
		input.value = '';
		// restaurar todos
		grupos.forEach(g => g.style.display = '');
	});

	// filtrar al teclear
	input.addEventListener('input', () => {
		const txt = input.value.trim().toLowerCase();
		grupos.forEach(div => {
			const nombre = div.querySelector('.nombrePaginaGrupo').textContent.trim().toLowerCase();
			div.style.display = nombre.startsWith(txt) ? '' : 'none';
		});
	});

	// cerrar si tocan fuera del área de búsqueda
	document.addEventListener('click', e => {
		if (!container.contains(e.target)) {
			container.classList.remove('active');
		}
	});
});

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */

function enviarSolicitudUnirse() {
	var nombreGrupoElement = document.querySelector(".nombreGrupo");
	var nombreGrupo = nombreGrupoElement ? nombreGrupoElement.textContent : null;

	if (!nombreGrupo) {
		mostrarAlertaPersonalizada("No se pudo obtener el nombre del grupo.");
		return;
	}

	const params = new URLSearchParams();
	params.append("nombreGrupo", nombreGrupo);

	const contextPath = window.location.pathname.split('/')[1];
	const url = `/${contextPath}/UnirseAlGrupo`;

	fetch(url, {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded"
		},
		body: params.toString()
	})
		.then(response => {
			if (response.ok) {
				mostrarAlertaPersonalizada("Su suscripción ha sido válida");
			} else {
				mostrarAlertaPersonalizada("Su suscripción ha sido denegada");
			}
		})
		.catch(error => {
			console.error("Error en la solicitud:", error);
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}


/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */


function enviarSolicitudEliminar() {
	var nombreGrupoElement = document.querySelector(".nombreGrupo");
	var nombreGrupo = nombreGrupoElement ? nombreGrupoElement.textContent : null;

	if (!nombreGrupo) {
		mostrarAlertaPersonalizada("No se pudo obtener el nombre del grupo.");
		return;
	}

	const params = new URLSearchParams();
	params.append("nombreGrupo", nombreGrupo);

	var contextPath = window.location.pathname.split('/')[1];

	fetch("/" + contextPath + "/EliminarDelGrupo", {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded"
		},
		body: params.toString()
	})
		.then(response => {
			if (response.ok) {
				mostrarAlertaPersonalizada("Te has eliminado del grupo correctamente.");
			} else {
				mostrarAlertaPersonalizada("No se pudo eliminar tu suscripción al grupo.");
			}
		})
		.catch(error => {
			console.error("Error en la solicitud:", error);
			mostrarAlertaPersonalizada("Ocurrió un error inesperado al intentar eliminar la suscripción.");
		});
}

/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */
/*---------------------------------------------------------------------------------------------------------------------------------------------------------------- */


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


