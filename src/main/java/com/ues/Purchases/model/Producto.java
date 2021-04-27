package com.ues.Purchases.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Producto {
	
	@OneToOne
	@JoinColumn(name = "existencia_id", nullable = false )
	private Existencia existenciaId;
	
	@OneToOne
	@JoinColumn(name = "vencProd_id", nullable = false )
	private VencimientoProducto vencimientoProductoId;
	
	@ManyToOne
	@JoinColumn(name = "catProd_id", nullable = false )
	private CategoriaProducto categoriaProductoId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_seq")
	@SequenceGenerator(name = "producto_seq", sequenceName = "Producto_seq", allocationSize = 1)
	@Column(name = "producto_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long productoId;
	
	@OneToMany(mappedBy = "producto")
	@JsonIgnore
	Set<PedidoProducto> producto;

	@Column(name = "nombre", unique = true, nullable = false, length = 100)
	private String nombre;

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
	
	@ManyToOne
	@JoinColumn(name = "tipoProd_id", nullable = false )
	private TipoProducto tipoProductoId;
	
	
	
	public Long getCategoriaProductoId() {
		return categoriaProductoId.categoriaProductoId;
	}

	public Set<PedidoProducto> getProducto() {
		return producto;
	}

	public void setProducto(Set<PedidoProducto> producto) {
		this.producto = producto;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public Long getTiporoductoId() {
		return tipoProductoId.tipoProductoId;
	}

	public Long getExistenciaId() {
		return existenciaId.existenciaId;
	}

	public Long getVencimientoProductoId() {
		return vencimientoProductoId.vencimientoProductoId;
	}

	
	
}
