package com.ues.Purchases.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PedidoProductoKey implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1101304394574547767L;

	/**
	 * 
	 */
	
	@Column(name = "pedido_id")
	Long pedidoId;
	
	@Column (name = "producto_id")
	Long productoId;

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
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
		result = prime * result + ((pedidoId == null) ? 0 : pedidoId.hashCode());
		result = prime * result + ((productoId == null) ? 0 : productoId.hashCode());
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
		PedidoProductoKey other = (PedidoProductoKey) obj;
		if (pedidoId == null) {
			if (other.pedidoId != null)
				return false;
		} else if (!pedidoId.equals(other.pedidoId))
			return false;
		if (productoId == null) {
			if (other.productoId != null)
				return false;
		} else if (!productoId.equals(other.productoId))
			return false;
		return true;
	}
	
	

}
