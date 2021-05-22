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
public class FacturaOrdenPago {
	

	@OneToMany(mappedBy = "facturaOrdenPagoId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Pago.class)
	private List<Pago> pagos;
	
	
	@ManyToOne
	@JoinColumn(name = "pediProv_id", nullable = false )
	private PedidoProveedor pedidoProveedorId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fact_orden_pago_seq")
	@SequenceGenerator(name = "fact_orden_pago_seq", sequenceName = "FactOrdenPago_seq", allocationSize = 1)
	@Column(name = "factOrdenPago_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long facturaOrdenPagoId;

	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "num_factura_seq")
	@SequenceGenerator(name = "num_factura_seq", sequenceName = "NunFactura_seq", allocationSize = 1)
	@Column(unique = true, nullable = false, precision = 15, scale = 0)
	Long numeroFactura;
	
	@Column(name = "fecha_emision", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaEmision;
	
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
	
	@Column(name = "estado_factura" ,nullable = false, length = 100)
	private String estadoFactura;
	
	@Column(nullable = false)
	private Double monto;

	public PedidoProveedor getPedidoProveedorId() {
		return pedidoProveedorId;
	}

	public void setPedidoProveedorId(PedidoProveedor pedidoProveedorId) {
		this.pedidoProveedorId = pedidoProveedorId;
	}

	public Long getFacturaOrdenPagoId() {
		return facturaOrdenPagoId;
	}

	public void setFacturaOrdenPagoId(Long facturaOrdenPagoId) {
		this.facturaOrdenPagoId = facturaOrdenPagoId;
	}

	public Long getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(Long numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
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

	public String getEstadoFactura() {
		return estadoFactura;
	}

	public void setEstadoFactura(String estadoFactura) {
		this.estadoFactura = estadoFactura;
	}

	public Double getMonto() {
		return monto;
	}

	public void setMonto(Double monto) {
		this.monto = monto;
	}
	
	
	
	
	

}
