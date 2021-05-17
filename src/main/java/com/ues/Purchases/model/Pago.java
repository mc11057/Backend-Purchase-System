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
public class Pago {
	
	@ManyToOne
	@JoinColumn(name = "moneda_id", nullable = false )
	private Moneda monedaId;
	
	@ManyToOne
	@JoinColumn(name = "forma_pago_id", nullable = false )
	private FormaPago formaPagoId;
	
	
	@ManyToOne
	@JoinColumn(name = "factOrdenPago_id", nullable = false )
	private FacturaOrdenPago facturaOrdenPagoId;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pago_seq")
	@SequenceGenerator(name = "pago_seq", sequenceName = "Pago_seq", allocationSize = 1)
	@Column(name = "pago_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long pagoId;


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
	public Moneda getMonedaId() {
		return monedaId;
	}

	public void setMonedaId(Moneda monedaId) {
		this.monedaId = monedaId;
	}

	public FormaPago getFormaPagoId() {
		return formaPagoId;
	}

	public void setFormaPagoId(FormaPago formaPagoId) {
		this.formaPagoId = formaPagoId;
	}

	public FacturaOrdenPago getFacturaOrdenPagoId() {
		return facturaOrdenPagoId;
	}

	public void setFacturaOrdenPagoId(FacturaOrdenPago facturaOrdenPagoId) {
		this.facturaOrdenPagoId = facturaOrdenPagoId;
	}

	public Long getPagoId() {
		return pagoId;
	}

	public void setPagoId(Long pagoId) {
		this.pagoId = pagoId;
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
	
	
	
}
