package com.ues.Purchases.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Sucursal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sucursal_seq")
	@SequenceGenerator(name = "sucursal_seq", sequenceName = "Sucursal_seq", allocationSize = 1)
	@Column(name = "sucursal_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long sucursalId;

	@Column(name = "nombre", unique = true, nullable = false, length = 100)
	private String nombre;

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
	
	@ManyToOne
	@JoinColumn(name = "horario_id", nullable = false )
	private Horario horarioId;
	
	
	@ManyToOne
	@JoinColumn(name = "ubicacion_id", nullable = false )
	private Ubicacion ubicacionId;
		
	
	

	public Long getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Long sucursalId) {
		this.sucursalId = sucursalId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public Long getHorarioId() {
		return horarioId.horarioId;
	}

	

	public Long getUbicacionId() {
		return ubicacionId.UbicacionId;
	}

	
	


}
