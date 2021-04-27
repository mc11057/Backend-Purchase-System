package com.ues.Purchases.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class VencimientoProducto {
	
	@OneToOne
	@JoinColumn(name = "producto_id", nullable = false )
	private Producto productoId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "venc_prod_seq")
	@SequenceGenerator(name = "venc_prod_seq", sequenceName = "VencProd_seq", allocationSize = 1)
	@Column(name = "vencProd_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long vencimientoProductoId;

	@Column(name = "fecha_vencimiento", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaVencimiento;

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
	public Long getVencimientoProductoId() {
		return vencimientoProductoId;
	}

	public void setVencimientoProductoId(Long vencimientoProductoId) {
		this.vencimientoProductoId = vencimientoProductoId;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Long getProductoId() {
		return productoId.productoId;
	}
	
	

}
