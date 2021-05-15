package com.ues.Purchases.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductoProveedorKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1101304394574547767L;

	/**
	 * 
	 */
	
	
	@Column(name = "proveedor_id")
	Long proveedorId;
	
	@Column (name = "producto_id")
	Long productoId;

	public Long getProveedorId() {
		return proveedorId;
	}

	public void setProveedorId(Long proveedorId) {
		this.proveedorId = proveedorId;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productoId == null) ? 0 : productoId.hashCode());
		result = prime * result + ((proveedorId == null) ? 0 : proveedorId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductoProveedorKey other = (ProductoProveedorKey) obj;
		if (productoId == null) {
			if (other.productoId != null)
				return false;
		} else if (!productoId.equals(other.productoId))
			return false;
		if (proveedorId == null) {
			if (other.proveedorId != null)
				return false;
		} else if (!proveedorId.equals(other.proveedorId))
			return false;
		return true;
	}
	
	

}
