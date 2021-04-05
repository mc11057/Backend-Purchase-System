package com.ues.Purchases.model;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Pais {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pais_seq")
	@SequenceGenerator(name = "pais_seq", sequenceName = "Pais_seq",allocationSize = 1)
    @Column(name="id", unique=true, nullable=false, precision=15, scale=0)
	Long id;

	@Column(name = "nombre", unique = true, nullable = false, length = 100)
    private String nombre;

	@Column(name = "create_date",  nullable = false)
	@Temporal(TemporalType.DATE)
    private Date createDate;
	@PrePersist
	public void prePersist() {
		createDate = new Date();
	}


	public List<Estado> getEstados() {
		return estados;
	}


	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}


	@Column(name = "user_create",  nullable = false, length = 100)
    private String userCreate;

	@Column(name = "update_date",  nullable = true)
	@Temporal(TemporalType.DATE)
    private Date UpdateDate;

	@Column(name = "user_update",  nullable = true, length = 100)
    private String userUpdate;


	@Column(name="region_id", updatable=true, insertable=true)
	public Long regionId;
	
	@OneToMany(mappedBy = "paisId", cascade = { CascadeType.ALL },fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Estado> estados;
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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
		return UpdateDate;
	}


	public void setUpdateDate(Date updateDate) {
		UpdateDate = updateDate;
	}


	public String getUserUpdate() {
		return userUpdate;
	}


	public void setUserUpdate(String userUpdate) {
		this.userUpdate = userUpdate;
	}


	public Long getRegionId() {
		return regionId;
	}


	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
}
