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
public class CategoriaEmpleado {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_emp_seq")
	@SequenceGenerator(name = "categoria_emp_seq", sequenceName = "CategoriaEmp_seq", allocationSize = 1)
	@Column(name = "categoria_empleado_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long catEmpleadoId;
	
	@OneToMany(mappedBy = "categoriaEmpleado", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Empleado.class)
	private List<Empleado> empleados;
	
	@Column(name = "tipo",  nullable = false, length = 100)
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
	
	@Column(nullable = false, length = 1)
	private String estado;
	
	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Long getCatEmpleadoId() {
		return catEmpleadoId;
	}

	public void setCatEmpleadoId(Long catEmpleadoId) {
		this.catEmpleadoId = catEmpleadoId;
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
