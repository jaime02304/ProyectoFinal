package edu.ProyectoFinal.Dto;

/**
 * Clase donde se encuentra los atributos del registro del nuevo usuario
 */
public class UsuarioRegistroDto {

	private String nombreCompletoUsu = "aaaaa";
	private String aliasUsu = "aaaaa";
	private String correoElectronicoUsu = "aaaaa";
	private String contraseniaUsu = "aaaaa";
	private boolean esConGoogle = false;

	public boolean isEsConGoogle() {
		return esConGoogle;
	}

	public void setEsConGoogle(boolean esConGoogle) {
		this.esConGoogle = esConGoogle;
	}

	public String getContraseniaUsu() {
		return contraseniaUsu;
	}

	public void setContraseniaUsu(String contraseniaUsu) {
		this.contraseniaUsu = contraseniaUsu;
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

	public UsuarioRegistroDto() {
		super();
	}

	public UsuarioRegistroDto(String correoElectronicoUsu, String contraseniaUsu) {
		super();
		this.correoElectronicoUsu = correoElectronicoUsu;
		this.contraseniaUsu = contraseniaUsu;
	}

}
