package com.ues.Purchases.model;

import java.math.BigDecimal;
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
public class Puesto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puesto_seq")
	@SequenceGenerator(name = "puesto_seq", sequenceName = "Puesto_seq", allocationSize = 1)
	@Column(name = "puesto_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long puestoId;
	
	@OneToMany(mappedBy = "puestoId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Empleado.class)
	private List<Empleado> empleados;
	
	
	@Column(name = "nombre",  nullable = false, length = 100)
	private String nombre;
	
	@Column(name="salary",precision=8, scale=2)
	private BigDecimal salary;
	
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

	public Long getPuestoId() {
		return puestoId;
	}

	public void setPuestoId(Long puestoId) {
		this.puestoId = puestoId;
	}


	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
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