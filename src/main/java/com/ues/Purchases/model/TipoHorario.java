package com.ues.Purchases.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="tipo_horario")
public class TipoHorario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_horario_seq")
	@SequenceGenerator(name = "tipo_horario_seq", sequenceName = "Tipo_horario_seq", allocationSize = 1)
	@Column(name = "tipo_horario_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long tipoHorarioId;

	@Column(name = "tipo", unique = true, nullable = false, length = 100)
	private String tipo;

	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@PrePersist
	public void prePersist() {
		createDate = new Date();
	}

	@Column(name = "user_create", nullable = false, length = 100)
	private String userCreate;

	@Column(name = "update_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@Column(name = "user_update", nullable = true, length = 100)
	private String userUpdate;
	
	@OneToMany(mappedBy = "tipoHorarioId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Horario.class)
	private List<Horario> horarios;

	

	public Long getTipoHorarioId() {
		return tipoHorarioId;
	}

	public void setTipoHorarioId(Long tipoHorarioId) {
		this.tipoHorarioId = tipoHorarioId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserCreate() {
		return userCreate;
	}

	public void setUserCreate(String userCreate) {
		this.userCreate = userCreate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUserUpdate() {
		return userUpdate;
	}

	public void setUserUpdate(String userUpdate) {
		this.userUpdate = userUpdate;
	}

	
	
	

}
