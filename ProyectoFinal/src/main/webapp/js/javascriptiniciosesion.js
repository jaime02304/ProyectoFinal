const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => {
	container.classList.add("right-panel-active");
});

signInButton.addEventListener('click', () => {
	container.classList.remove("right-panel-active");
});

function flipForm() {
	const container = document.getElementById('formContainer');
	container.classList.toggle('flip');
}


document.getElementById("formRegistro2").addEventListener("submit", function(event) {
	var contrasenia = document.getElementById("contraseniaUsu2").value;
	var repetirContrasenia = document.getElementById("repetirContrasenia2").value;

	if (contrasenia !== repetirContrasenia) {
		// Si las contraseñas no coinciden, evitamos el envío del formulario
		event.preventDefault();
		mostrarAlertaPersonalizada("Las contraseñas no coinciden. Por favor, revisa e intenta nuevamente.");
	}
});

document.getElementById("formRegistro").addEventListener("submit", function(event) {
	var contrasenia = document.getElementById("contraseniaUsu").value;
	var repetirContrasenia = document.getElementById("repetirContrasenia").value;

	if (contrasenia !== repetirContrasenia) {
		// Si las contraseñas no coinciden, evitamos el envío del formulario
		event.preventDefault();
		mostrarAlertaPersonalizada("Las contraseñas no coinciden. Por favor, revisa e intenta nuevamente.");
	}
});


function mostrarAlertaPersonalizada(mensaje) {
	var alerta = document.getElementById("alertaPersonalizada");
	document.getElementById("alertaMensaje").textContent = mensaje;
	alerta.style.display = "flex";
}

function cerrarAlertaPersonalizada() {
	document.getElementById("alertaPersonalizada").style.display = "none";
}


function openRecuperacionModal() {
	const modal = document.getElementById("formularioRecuperacionModal");
	modal.style.display = "flex";
}

function closeRecuperacionModal() {
	document.getElementById("formularioRecuperacionModal").style.display = "none";
}

function enviarRecuperacion(event) {
	event.preventDefault();

	const correo = document.getElementById("emailRecuperacion").value.trim();

	if (!correo) {
		mostrarAlertaPersonalizada("Por favor, introduce un correo válido.");
		return;
	}

	const formData = new FormData();
	formData.append("correoElectronicoUsu", correo);

	const contextPath = window.location.pathname.split('/')[1];
	closeRecuperacionModal();
	mostrarAlertaPersonalizada("Espere un momento a que le llegue el correo para la recuperación de la contraseña.");
	fetch(`/${contextPath}/RecuperarContrasena`, {
		method: "POST",
		body: formData
	})
		.then(function(response) {
			closeRecuperacionModal();
			if (response.ok) {
				mostrarAlertaPersonalizada("Se ha enviado un correo para recuperar tu contraseña.");
			} else {
				mostrarAlertaPersonalizada("No se pudo procesar la solicitud. Inténtalo de nuevo.");
			}
		})
		.catch(function(error) {
			console.error("Error en la solicitud:", error);
			closeRecuperacionModal();
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}




function openCambioModal() {
	const modal = document.getElementById("formularioCambioModal");
	modal.style.display = "flex";
}

function closeCambioModal() {
	document.getElementById("formularioCambioModal").style.display = "none";
}

function enviarCambioContrasena(event) {
	event.preventDefault();

	const nuevaContra = document.getElementById("nuevaContrasena").value.trim();
	const confirmarContra = document.getElementById("confirmarContrasena").value.trim();
	const token = document.getElementById("tokenRecuperacion").value;

	if (!nuevaContra || !confirmarContra) {
		mostrarAlertaPersonalizada("Por favor, completa ambos campos.");
		return;
	}

	if (nuevaContra !== confirmarContra) {
		mostrarAlertaPersonalizada("Las contraseñas no coinciden.");
		return;
	}

	const formData = new FormData();
	formData.append("nuevaContrasena", nuevaContra);
	formData.append("token", token);

	const contextPath = window.location.pathname.split('/')[1];
	closeCambioModal();
	mostrarAlertaPersonalizada("Guardando nueva contraseña...");

	fetch(`/${contextPath}/CambiarContrasena`, {
		method: "POST",
		body: formData
	})
		.then(response => {
			if (response.ok) {
				mostrarAlertaPersonalizada("¡Contraseña cambiada exitosamente!");
			} else {
				mostrarAlertaPersonalizada("No se pudo cambiar la contraseña.");
			}
		})
		.catch(error => {
			console.error("Error:", error);
			mostrarAlertaPersonalizada("Ocurrió un error inesperado.");
		});
}




document.addEventListener("DOMContentLoaded", function() {
	const parametros = new URLSearchParams(window.location.search);
	const modal = parametros.get("modal");
	const token = parametros.get("token");

	if (modal === "recuperar" && token) {
		openCambioModal();
		document.getElementById("tokenRecuperacion").value = token;
		//Quita lo innecesario de la url para que no lo puedan ver
		const nuevaUrl = window.location.origin + window.location.pathname;
		window.history.replaceState({}, document.title, nuevaUrl);
	}
});

