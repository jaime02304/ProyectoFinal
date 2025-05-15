function cerrarAlertaPersonalizada() {
	document.getElementById("alertaPersonalizada").style.display = "none";
}



// LA PARTE DEL MENU DESPLEGABLE EN MOVIL
document.addEventListener("DOMContentLoaded", () => {
	const menuToggle = document.getElementById("menuToggle");
	const menuOpciones = document.getElementById("menuOpciones");

	menuToggle.addEventListener("click", () => {
		const isVisible = menuOpciones.style.display === "block";
		menuOpciones.style.display = isVisible ? "none" : "block";
	});

	//Cerrar el menú al hacer clic fuera de él
	document.addEventListener("click", (event) => {
		if (
			!menuOpciones.contains(event.target) &&
			!menuToggle.contains(event.target)
		) {
			menuOpciones.style.display = "none";
		}
	});
});


// Índices independientes para cada carrusel
var idx1 = 0, idx2 = 0, idx3 = 0;
// 'comentarios' vendrá del JSP; si no existe, se inicializa a array vacío
var comentariosIndex = window.comentariosIndex || [];

/**
 * Rellena el carrusel con prefijo '' | '2' | '3' en la posición idx
 */
function renderCarrusel(prefijo, idx) {
	if (!comentariosIndex.length) return;
	var c = comentariosIndex[idx];
	document.getElementById('titulo-usuario' + prefijo).textContent = c.alias;
	document.getElementById('imagen-usuario' + prefijo).src = c.imagenUrl;
	document.getElementById('texto-usuario' + prefijo).textContent = c.texto;
}

/** Actualiza el índice “anterior” y refresca */
function anterior(prefijo) {
	switch (prefijo) {
		case '': idx1 = (idx1 - 1 + comentariosIndex.length) % comentariosIndex.length; break;
		case '2': idx2 = (idx2 - 1 + comentariosIndex.length) % comentariosIndex.length; break;
		case '3': idx3 = (idx3 - 1 + comentariosIndex.length) % comentariosIndex.length; break;
	}
	renderCarrusel(prefijo, { '': idx1, '2': idx2, '3': idx3 }[prefijo]);
}

/** Actualiza el índice “siguiente” y refresca */
function siguiente(prefijo) {
	switch (prefijo) {
		case '': idx1 = (idx1 + 1) % comentariosIndex.length; break;
		case '2': idx2 = (idx2 + 1) % comentariosIndex.length; break;
		case '3': idx3 = (idx3 + 1) % comentariosIndex.length; break;
	}
	renderCarrusel(prefijo, { '': idx1, '2': idx2, '3': idx3 }[prefijo]);
}

// Wrappers que usan tus nombres de función en los botones
function usuarioAnterior() { anterior(''); }
function usuarioSiguiente() { siguiente(''); }
function usuarioAnterior2() { anterior('2'); }
function usuarioSiguiente2() { siguiente('2'); }
function usuarioAnterior3() { anterior('3'); }
function usuarioSiguiente3() { siguiente('3'); }

// Al cargar el DOM, inicializamos cada carrusel en la posición 0
document.addEventListener('DOMContentLoaded', function() {
	renderCarrusel('', 0);
	renderCarrusel('2', 0);
	renderCarrusel('3', 0);
});
