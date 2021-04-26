package com.ues.Purchases.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Nacionalidad {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nacionalidad_seq")
	@SequenceGenerator(name = "nacionalidad_seq", sequenceName = "Nacionalidad_seq", allocationSize = 1)
	@Column(name = "nacionalidad_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long nacionalidadId;
	
	@OneToMany(mappedBy = "nacionalidadId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Empleado.class)
	private List<Empleado> empleados;
	
	@Column(name = "nombre",  nullable = false, length = 100)
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

	public Long getNacionalidadId() {
		return nacionalidadId;
	}

	public void setNacionalidadId(Long nacionalidadId) {
		this.nacionalidadId = nacionalidadId;
	}

	public List<Empleado> getEmpleados() {
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
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
	
	
}
