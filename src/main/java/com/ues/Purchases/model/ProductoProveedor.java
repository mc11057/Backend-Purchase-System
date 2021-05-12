package com.ues.Purchases.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "producto_proveedor")
public class ProductoProveedor {
	
	@EmbeddedId
	@JsonIgnore
	ProductoProveedorKey id;

	@ManyToOne
	@MapsId("proveerdorId")
	@JoinColumn(name = "proveedor_id")
	@JsonIgnore
	Proveedor proveedor;
	
	@ManyToOne
	@MapsId("productoId")
	@JoinColumn(name = "producto_id")
	Producto producto;

	@Column(name = "precio",  nullable = false)
	private double precio;

	public ProductoProveedorKey getId() {
		return id;
	}

	public void setId(ProductoProveedorKey id) {
		this.id = id;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}
	
	
	
}
