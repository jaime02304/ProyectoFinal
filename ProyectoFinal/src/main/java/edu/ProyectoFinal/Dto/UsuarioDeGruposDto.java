package edu.ProyectoFinal.Dto;

/**
 * Dto para los usuarios que se encuentran en la listas de los fgrupos
 * especificados
 * 
 * @author jpribio - 07/05/25
 */
public class UsuarioDeGruposDto {

	private String nombreUsuario = "aaaaa";

	private String aliasUsuario = "aaaaa";

	private Boolean esPremium = false;

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getAliasUsuario() {
		return aliasUsuario;
	}

	public void setAliasUsuario(String aliasUsuario) {
		this.aliasUsuario = aliasUsuario;
	}

	public Boolean getEsPremium() {
		return esPremium;
	}

	public void setEsPremium(Boolean esPremium) {
		this.esPremium = esPremium;
	}

}
