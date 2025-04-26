package edu.ProyectoFinal.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * Clase donde se encuentra los atributos de los usuarios para el perfil
 */
public class UsuarioPerfilDto {

	private Long idUsu;
	private String nombreCompletoUsu = "aaaaa";
	private String aliasUsu = "aaaaa";
	private String correoElectronicoUsu = "aaaaa";
	private int movilUsu = 0;
	private String fotoString = "";
	@JsonFormat(shape = Shape.STRING)
	private byte[] fotoUsu;
	private Boolean esPremium = false;
	private String rolUsu = "user";
	private Boolean esVerificadoEntidad = false;
	private String ContraseniaUsu = "aaaaa";
	// Getter y setter

	public Boolean getEsVerificadoEntidad() {
		return esVerificadoEntidad;
	}

	public String getContraseniaUsu() {
		return ContraseniaUsu;
	}

	public void setContraseniaUsu(String contraseniaUsu) {
		ContraseniaUsu = contraseniaUsu;
	}

	public String getFotoString() {
		return fotoString;
	}

	public void setFotoString(String fotoString) {
		this.fotoString = fotoString;
	}

	public Long getIdUsu() {
		return idUsu;
	}

	public void setIdUsu(Long idUsu) {
		this.idUsu = idUsu;
	}

	public void setEsVerificadoEntidad(Boolean esVerificadoEntidad) {
		this.esVerificadoEntidad = esVerificadoEntidad;
	}

	public String getRolUsu() {
		return rolUsu;
	}

	public void setRolUsu(String rolUsu) {
		this.rolUsu = rolUsu;
	}

	public String getNombreCompletoUsu() {
		return nombreCompletoUsu;
	}

	public void setNombreCompletoUsu(String nombreCompletoUsu) {
		this.nombreCompletoUsu = nombreCompletoUsu;
	}

	public String getAliasUsu() {
		return aliasUsu;
	}

	public void setAliasUsu(String aliasUsu) {
		this.aliasUsu = aliasUsu;
	}

	public String getCorreoElectronicoUsu() {
		return correoElectronicoUsu;
	}

	public void setCorreoElectronicoUsu(String correoElectronicoUsu) {
		this.correoElectronicoUsu = correoElectronicoUsu;
	}

	public int getMovilUsu() {
		return movilUsu;
	}

	public void setMovilUsu(int movilUsu) {
		this.movilUsu = movilUsu;
	}

	public byte[] getFotoUsu() {
		return fotoUsu;
	}

	public void setFotoUsu(byte[] fotoUsu) {
		this.fotoUsu = fotoUsu;
	}

	public Boolean getEsPremium() {
		return esPremium;
	}

	public void setEsPremium(Boolean esPremium) {
		this.esPremium = esPremium;
	}

	public UsuarioPerfilDto() {
		super();
	}
}
