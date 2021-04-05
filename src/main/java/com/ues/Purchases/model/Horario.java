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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Horario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "horario_seq")
	@SequenceGenerator(name = "horario_seq", sequenceName = "Horario_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false, precision = 15, scale = 0)
	Long id;

	@Column(name = "hora_entrada", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date horaEntrada;
	
	@Column(name = "hora_salida", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date horaSalida;
		
	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date createDate;

	@PrePersist
	public void prePersist() {
		createDate = new Date();
	}

	@Column(name = "user_create", nullable = false, length = 100)
	private String userCreate;

	@Column(name = "update_date", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date updateDate;

	@Column(name = "user_update", nullable = true, length = 100)
	private String userUpdate;
	
	@Column(name="tipo_horario_id", updatable=true, insertable=true)
	public Long tipoHorarioId;

	
	@OneToMany(mappedBy = "horarioId", cascade = { CascadeType.ALL },fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Sucursal> sucursales;
	
	
	public List<Sucursal> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Date horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public Date getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(Date horaSalida) {
		this.horaSalida = horaSalida;
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

	public Long getTipoHorarioId() {
		return tipoHorarioId;
	}

	public void setTipoHorarioId(Long tipoHorarioId) {
		this.tipoHorarioId = tipoHorarioId;
	}
	
	

}
