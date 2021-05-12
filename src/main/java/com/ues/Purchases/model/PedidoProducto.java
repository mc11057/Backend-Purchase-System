package com.ues.Purchases.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pedido_producto")
public class PedidoProducto {

	@ManyToMany
	@JoinTable(
			  name = "proveedorPedidoProducto", 
			  joinColumns = {@JoinColumn(name = "pedido_id"),@JoinColumn(name = "producto_id")}, 
			  inverseJoinColumns = @JoinColumn(name = "pediProv_id"))
	Set<PedidoProveedor> pedidoproducto;

	@EmbeddedId
	@JsonIgnore
	PedidoProductoKey id;

	@ManyToOne
	@MapsId("pedidoId")
	@JoinColumn(name = "pedido_id")
	@JsonIgnore
	Pedido pedido;

	@ManyToOne
	@MapsId("productoId")
	@JoinColumn(name = "producto_id")
	Producto producto;

	/*
	 * @OneToMany(mappedBy = "pedidoProducto") public Set<PedidoProductoProveedor>
	 * pedidoProducto;
	 * 
	 * @OneToMany(mappedBy = "productoPedido") public Set<PedidoProductoProveedor>
	 * productoPedido;
	 */

	@Column(name = "cantidad", nullable = false)
	private int cantidad;

	public PedidoProductoKey getId() {
		return id;
	}

	public void setId(PedidoProductoKey id) {
		this.id = id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
