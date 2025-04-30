package edu.ProyectoFinal.Dto;

/**
 * Dto donde se encuentra as caracteristicas necesaria de los grupos en el index
 */
public class GruposListadoDto {

	private Long idGrupo;
	private String nombreGrupo = "aaaaa";
	private String categoriaNombre = "aaaaa";
	private String subCategoriaNombre = "aaaaa";
	private String descripcionGrupo = "";

	public String getDescripcionGrupo() {
		return descripcionGrupo;
	}

	public void setDescripcionGrupo(String descripcionGrupo) {
		this.descripcionGrupo = descripcionGrupo;
	}

	public Long getIdGrupo() {
		return idGrupo;
	}

	public void setIdGrupo(Long idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	public String getCategoriaNombre() {
		return categoriaNombre;
	}

	public void setCategoriaNombre(String categoriaNombre) {
		this.categoriaNombre = categoriaNombre;
	}

	public String getSubCategoriaNombre() {
		return subCategoriaNombre;
	}

	public void setSubCategoriaNombre(String subCategoriaNombre) {
		this.subCategoriaNombre = subCategoriaNombre;
	}

}
