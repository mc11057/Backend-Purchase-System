package com.ues.Purchases.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
public class Pedido {
	
	@OneToMany(mappedBy = "pedidoId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.IngresoOrdenCompra.class)
	private List<IngresoOrdenCompra> ordenesCompra;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq")
	@SequenceGenerator(name = "pedido_seq", sequenceName = "Pedido_seq", allocationSize = 1)
	@Column(name = "pedido_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long pedidoId;
	
	@Column(name = "create_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	

	@OneToMany(mappedBy = "pedido")
	public Set<PedidoProducto> productos;
	
	
	public Set<PedidoProducto> getProductos() {
		return productos;
	}

	public void setProductos(Set<PedidoProducto> productos) {
		this.productos = productos;
	}

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
	@JoinColumn(name = "proPedi_id", nullable = false )
	private ProgresoPedido progresoPedido;
	
	@ManyToOne
	@JoinColumn(name = "empleado_id", nullable = false )
	private Empleado empleado;

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
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



	public ProgresoPedido getProgresoPedido() {
		return progresoPedido;
	}

	public void setProgresoPedido(ProgresoPedido progresoPedido) {
		this.progresoPedido = progresoPedido;
	}

	public Empleado getEmpleado() {
		return empleado;
	}
	
	

}
