package com.ues.Purchases.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Ubicacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ubicacion_seq")
	@SequenceGenerator(name = "ubicacion_seq", sequenceName = "Ubicacion_seq", allocationSize = 1)
	@Column(name = "ubicacion_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long UbicacionId;

	@Column(name = "direccion", unique = true, nullable = false, length = 100)
	private String direccion;

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
	
	@Column(nullable = false, length = 1)
	private String estado;
	
	@ManyToOne
	@JoinColumn(name = "barrio_id", nullable = false )
	private Barrio barrioId;
	
	@OneToMany(mappedBy = "ubicacionId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Sucursal.class)
	private List<Sucursal> sucursales;
	
	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Long getUbicacionId() {
		return UbicacionId;
	}

	public void setUbicacionId(Long ubicacionId) {
		UbicacionId = ubicacionId;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
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

	public Long getBarrioId() {
		return barrioId.barrioId;
	}

	

	
}
