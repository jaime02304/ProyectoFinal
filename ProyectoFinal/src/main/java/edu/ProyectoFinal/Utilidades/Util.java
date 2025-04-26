package edu.ProyectoFinal.Utilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase donde se encuentra los metodos que seran de utilidad
 * 
 * @author jpribio - 20/01/25
 */
public class Util {
	/**
	 * Metodo que convierte un string enuna encriptacion de tipo Sha-256
	 * 
	 * @author jpribio -20/01/25
	 * @param entrada (parametro para encriptar)
	 * @return devuelve el parametro de entrada encriptada
	 * @throws NoSuchAlgorithmException
	 */
	public String encriptarASHA256(String entrada) {
		try {

			MessageDigest objetoDigest = MessageDigest.getInstance("SHA-256");
			// Calcula el hash de la contraseña
			byte[] arrayDeBytes = objetoDigest.digest(entrada.getBytes());

			// Convierte el byte array a un String hexadecimal
			StringBuilder cadenaHexadecimal = new StringBuilder();
			for (byte b : arrayDeBytes) {
				String hexadecimal = Integer.toHexString(0xff & b);
				if (hexadecimal.length() == 1) {
					cadenaHexadecimal.append('0');
				}
				cadenaHexadecimal.append(hexadecimal);
			}
			return cadenaHexadecimal.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
