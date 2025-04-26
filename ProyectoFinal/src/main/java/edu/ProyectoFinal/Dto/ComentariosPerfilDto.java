package edu.ProyectoFinal.Dto;

/**
 * Clase donde se encuentra los atributos necesarios para el comentario del
 * usuario en cuestion
 * 
 * @author jpribio - 03/02/25
 */
public class ComentariosPerfilDto {

	private String comentarioTexto = "aaaaa";
	private String categoriaTipo = "aaaaa";
	private String subCategoriaTipo = "aaaaa";
	private long idUsuario;
	
	

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
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

}
