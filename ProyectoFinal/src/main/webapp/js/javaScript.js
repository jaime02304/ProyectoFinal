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

// PARTE DEL CARRUSEL
const usuarios = [
	{
		nombre: "Juan",
		imagen: "imagenes/artbreeder-image-2024-12-14T13_53_57.670Z.jpeg",
		texto: "Hola, soy Juan. ¡Encantado de conocerte!",
		meGusta: 0,
		leGusta: false,
	},
	{
		nombre: "Maria",
		imagen: "imagenes/artbreeder-image-2024-12-14T13_53_57.670Z.jpeg",
		texto: "Soy María y me encanta la programación.",
		meGusta: 0,
		leGusta: false,
	},
	{
		nombre: "Carlos",
		imagen: "imagenes/artbreeder-image-2024-12-14T13_53_57.670Z.jpeg",
		texto:
			"Hola, soy Carlos. Disfruto aprender cosas nuevas jadvn j bnb dhuhfuhbvfvhf hsdavhhguoashvhasuodhvsah suhadouvhasovhasouhasudhv duhgasudhaouhaughqdhiasdhgisduhg",
		meGusta: 0,
		leGusta: false,
	},
];

// function obtenerUsuarios() {
//     return fetch('https://example.com/api/usuarios') // Cambiar URL
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('Error al obtener los usuarios');
//             }
//             return response.json();
//         });
// }
// let usuarios = [];

let indiceActual = 1;
let indiceActual2 = 1;
let indiceActual3 = 1;

// Función genérica para actualizar carruseles
templateActualizarCarrusel = (
	indice,
	claseTitulo,
	claseImagen,
	claseTexto,
	claseBoton,
	usuario
) => {
	document.querySelector(claseTitulo).textContent = usuario.nombre;
	document.querySelector(claseImagen).src = usuario.imagen;
	document.querySelector(claseTexto).textContent = usuario.texto;

	const botonCorazon = document.querySelector(claseBoton);
	botonCorazon.classList.toggle("clicado", usuario.leGusta);

	botonCorazon.replaceWith(botonCorazon.cloneNode(true));
	const nuevoBotonCorazon = document.querySelector(claseBoton);

	nuevoBotonCorazon.addEventListener("click", function() {
		usuario.leGusta = !usuario.leGusta;
		usuario.meGusta += usuario.leGusta ? 1 : -1;
		nuevoBotonCorazon.classList.toggle("clicado", usuario.leGusta);
	});
};

function actualizarCarrusel() {
	templateActualizarCarrusel(
		indiceActual,
		".titulo-usuario",
		".imagen-usuario",
		".texto-usuario",
		".corazonBoton",
		usuarios[indiceActual]
	);
}

function actualizarCarrusel2() {
	templateActualizarCarrusel(
		indiceActual2,
		".titulo-usuario2",
		".imagen-usuario2",
		".texto-usuario2",
		".corazonBoton2",
		usuarios[indiceActual2]
	);
}

function actualizarCarrusel3() {
	templateActualizarCarrusel(
		indiceActual3,
		".titulo-usuario3",
		".imagen-usuario3",
		".texto-usuario3",
		".corazonBoton3",
		usuarios[indiceActual3]
	);
}

function usuarioSiguiente() {
	indiceActual = (indiceActual + 1) % usuarios.length;
	actualizarCarrusel();
}

function usuarioAnterior() {
	indiceActual = (indiceActual - 1 + usuarios.length) % usuarios.length;
	actualizarCarrusel();
}

function usuarioSiguiente2() {
	indiceActual2 = (indiceActual2 + 1) % usuarios.length;
	actualizarCarrusel2();
}

function usuarioAnterior2() {
	indiceActual2 = (indiceActual2 - 1 + usuarios.length) % usuarios.length;
	actualizarCarrusel2();
}

function usuarioSiguiente3() {
	indiceActual3 = (indiceActual3 + 1) % usuarios.length;
	actualizarCarrusel3();
}

function usuarioAnterior3() {
	indiceActual3 = (indiceActual3 - 1 + usuarios.length) % usuarios.length;
	actualizarCarrusel3();
}

// Inicializar carruseles con datos del servidor
// obtenerUsuarios()
//   .then((data) => {
//     usuarios = data;
//     if (usuarios.length > 0) {
//       actualizarCarrusel();
//       actualizarCarrusel2();
//       actualizarCarrusel3();

//       // Configurar intervalos para avanzar automáticamente
//       let intervaloCarrusel = setInterval(usuarioSiguiente, 6000);
//       let intervaloCarrusel2 = setInterval(usuarioSiguiente2, 6000);
//       let intervaloCarrusel3 = setInterval(usuarioSiguiente3, 6000);

//       // Detener intervalos al hacer clic en los botones
//       document.querySelector(".botones").addEventListener("click", () => {
//         clearInterval(intervaloCarrusel);
//         clearInterval(intervaloCarrusel2);
//         clearInterval(intervaloCarrusel3);
//       });
//     } else {
//       console.error("No se encontraron usuarios en la respuesta.");
//     }
//   })
//   .catch((error) => {
//     console.error("Error al inicializar los carruseles:", error);
//   });
