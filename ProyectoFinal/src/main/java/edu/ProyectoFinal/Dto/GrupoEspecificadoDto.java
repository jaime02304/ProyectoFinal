package edu.ProyectoFinal.Dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Dto para los grupos especificados y ver las caracteristicas especificas
 * 
 * @author jpribio - 07/05/25
 */
public class GrupoEspecificadoDto {

	private Long grupoId;

	private Long usuarioId;

	private String nombreGrupo = "aaaaa";

	private String aliasCreadorGrupo = "aaaaa";

	private LocalDateTime fechaCreacion;

	private String descripcionGrupoString = "aaaaa";

	private String categoriaGrupo = "aaaaa";

	private String subcategoriaGrupo = "aaaaa";

	private Long numeroUsuarios = (long) 0;

	private List<UsuarioDeGruposDto> listadoDeUsuariosSuscritos;

	public List<UsuarioDeGruposDto> getListadoDeUsuariosSuscritos() {
		return listadoDeUsuariosSuscritos;
	}

	public void setListadoDeUsuariosSuscritos(List<UsuarioDeGruposDto> listadoDeUsuariosSuscritos) {
		this.listadoDeUsuariosSuscritos = listadoDeUsuariosSuscritos;
	}

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

	public String getAliasCreadorGrupo() {
		return aliasCreadorGrupo;
	}

	public void setAliasCreadorGrupo(String aliasCreadorGrupo) {
		this.aliasCreadorGrupo = aliasCreadorGrupo;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public String getFechaCreacionFormateada() {
		if (fechaCreacion == null)
			return "";
		return fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getDescripcionGrupoString() {
		return descripcionGrupoString;
	}

	public void setDescripcionGrupoString(String descripcionGrupoString) {
		this.descripcionGrupoString = descripcionGrupoString;
	}

	public String getCategoriaGrupo() {
		return categoriaGrupo;
	}

	public void setCategoriaGrupo(String categoriaGrupo) {
		this.categoriaGrupo = categoriaGrupo;
	}

	public String getSubcategoriaGrupo() {
		return subcategoriaGrupo;
	}

	public void setSubcategoriaGrupo(String subcategoriaGrupo) {
		this.subcategoriaGrupo = subcategoriaGrupo;
	}

	public Long getNumeroUsuarios() {
		return numeroUsuarios;
	}

	public void setNumeroUsuarios(Long numeroUsuarios) {
		this.numeroUsuarios = numeroUsuarios;
	}
}
