package edu.ProyectoFinal.Dto;

/**
 * Clase con los atributos de los comentarios
 * 
 * @author jpribio - 16/04/25
 */
public class ComentariosDTO {

	private String aliasUsuarioComentario = "aaaaa";
	private String comentarioTexto = "aaaaa";
	private String categoriaTipo = "aaaaa";
	private String subCategoriaTipo = "aaaaa";
	private long idUsuario;
	private String grupoComentario = "aaaaa";

	public String getAliasUsuarioComentario() {
		return aliasUsuarioComentario;
	}

	public void setAliasUsuarioComentario(String aliasUsuarioComentario) {
		this.aliasUsuarioComentario = aliasUsuarioComentario;
	}

	public String getComentarioTexto() {
		return comentarioTexto;
	}

	public void setComentarioTexto(String comentarioTexto) {
		this.comentarioTexto = comentarioTexto;
	}

	public String getCategoriaTipo() {
		return categoriaTipo;
	}

	public void setCategoriaTipo(String categoriaTipo) {
		this.categoriaTipo = categoriaTipo;
	}

	public String getSubCategoriaTipo() {
		return subCategoriaTipo;
	}

	public void setSubCategoriaTipo(String subCategoriaTipo) {
		this.subCategoriaTipo = subCategoriaTipo;
	}

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getGrupoComentario() {
		return grupoComentario;
	}

	public void setGrupoComentario(String grupoComentario) {
		this.grupoComentario = grupoComentario;
	}

}
