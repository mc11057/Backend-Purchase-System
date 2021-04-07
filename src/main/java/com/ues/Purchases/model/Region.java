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
public class Region {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reguion_seq")
	@SequenceGenerator(name = "reguion_seq", sequenceName = "Reguion_seq", allocationSize = 1)
	@Column(name = "region_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long regionId;

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
	
	@OneToMany(mappedBy = "regionId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Pais.class)
	private List<Pais> paises;

		public Long getRegionId() {
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
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

	

	
	
	
	
	
}
