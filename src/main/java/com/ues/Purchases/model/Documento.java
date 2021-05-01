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
public class Documento {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documento_seq")
	@SequenceGenerator(name = "documento_seq", sequenceName = "Documento_seq", allocationSize = 1)
	@Column(name = "documento_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long documentoId;
	
	
	
	@ManyToOne
	@JoinColumn(name = "empleado_id", nullable = false )
	Empleado empleadoId;
	
	@ManyToOne
	@JoinColumn(name = "tipo_identificacion_id", nullable = false )
	TipoIdentificacion tipoIdentificacionId;
	
	
	@Column(name = "numero",  nullable = false, length = 25)
	private String numero;
	
	@Column(name = "fecha_expedicion", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fechaExpedicion;
	
	@Column(name = "fecha_vencimiento", nullable = false)
	@Temporal(TemporalType.DATE)
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
	
	public Long getEmpleadoId() 
	{
		return empleadoId.empleadoId;
	}
	public Long getTipoIdentificacionId() 
	{
		return tipoIdentificacionId.tipoIdentificacionId;
	}
	public Long getDocumentoId() {
		return documentoId;
	}
	public void setDocumentoId(Long documentoId) {
		this.documentoId = documentoId;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Date getFechaExpedicion() {
		return fechaExpedicion;
	}
	public void setFechaExpedicion(Date fechaExpedicion) {
		this.fechaExpedicion = fechaExpedicion;
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
	public void setEmpleadoId(Empleado empleadoId) {
		this.empleadoId = empleadoId;
	}
	public void setTipoIdentificacionId(TipoIdentificacion tipoIdentificacionId) {
		this.tipoIdentificacionId = tipoIdentificacionId;
	}
	
	
}
