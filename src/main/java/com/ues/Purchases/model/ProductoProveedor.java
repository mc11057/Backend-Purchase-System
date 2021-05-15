package com.ues.Purchases.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "producto_proveedor")
public class ProductoProveedor {
	
	@EmbeddedId
	ProductoProveedorKey id;

	@ManyToOne
	@MapsId("proveerdorId")
	@JoinColumn(name = "proveedor_id")
	Proveedor proveedor;
	
	@ManyToOne
	@MapsId("productoId")
	@JoinColumn(name = "producto_id")
	Producto producto;

	@Column(name = "precio",  nullable = false)
	private BigDecimal precio;

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

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}


	
	
}
