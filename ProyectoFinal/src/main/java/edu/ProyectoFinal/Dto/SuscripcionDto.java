package edu.ProyectoFinal.Dto;

/**
 * Dto para suscribirse el usuario a un grupo
 * 
 * @author jpribio - 07/05/25
 */
public class SuscripcionDto {

	private Long grupoId;

	private Long usuarioId;

	private String nombreGrupo = "aaaaa";

	private String aliasUsuario = "aaaaa";

	public Long getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Long grupoId) {
		this.grupoId = grupoId;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public String getAliasUsuario() {
		return aliasUsuario;
	}

	public void setAliasUsuario(String aliasUsuario) {
		this.aliasUsuario = aliasUsuario;
	}
}
