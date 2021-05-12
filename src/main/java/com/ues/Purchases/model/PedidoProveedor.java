package com.ues.Purchases.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class PedidoProveedor {
	
	
	/*
	 * @OneToMany(mappedBy = "pedidoProveedor") public Set<PedidoProductoProveedor>
	 * proveedor;
	 */
	
	@ManyToMany(mappedBy = "pedidoproducto")
    Set<PedidoProducto> proveedor;
	
	@ManyToOne
	@JoinColumn(name = "proveedor_id", nullable = false )
	private Proveedor proveedorId;
	
	@ManyToOne
	@JoinColumn(name = "orden_compra_id", nullable = false )
	private IngresoOrdenCompra ordenCompraId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_provee_seq")
	@SequenceGenerator(name = "pedido_provee_seq", sequenceName = "PediProve_seq", allocationSize = 1)
	@Column(name = "pediProv_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long pedidoProveedorId;
	
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

	public Proveedor getProveedorId() {
		return proveedorId;
	}

	public void setProveedorId(Proveedor proveedorId) {
		this.proveedorId = proveedorId;
	}

	public IngresoOrdenCompra getOrdenCompraId() {
		return ordenCompraId;
	}

	public void setOrdenCompraId(IngresoOrdenCompra ordenCompraId) {
		this.ordenCompraId = ordenCompraId;
	}

	public Long getPedidoProveedorId() {
		return pedidoProveedorId;
	}

	public void setPedidoProveedorId(Long pedidoProveedorId) {
		this.pedidoProveedorId = pedidoProveedorId;
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
