package com.ues.Purchases.model;

import java.util.Date;
import java.util.List;

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
public class Empleado {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_seq")
	@SequenceGenerator(name = "empleado_seq", sequenceName = "Empleado_seq", allocationSize = 1)
	@Column(name = "empleado_id", unique = true, nullable = false, precision = 15, scale = 0)
	Long empleadoId;
	
	
	@OneToMany(mappedBy = "empleadoId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Pedido.class)
	private List<Pedido> pedidos;
	
	@ManyToOne
	@JoinColumn(name = "sucursal_id", nullable = false )
	Sucursal sucursalId;
	
	@ManyToOne
	@JoinColumn(name = "horario_id", nullable = false )
	Horario horarioId;
	
	@ManyToOne
	@JoinColumn(name = "nacionalidad_id", nullable = false )
	Nacionalidad nacionalidadId;
	
	@OneToMany(mappedBy = "empleadoId", cascade = { CascadeType.ALL },targetEntity = com.ues.Purchases.model.Documento.class)
	private List<Documento> documentos;
	
	@ManyToOne
	@JoinColumn(name = "cat_empleado_id", nullable = false )
	CategoriaEmpleado categoriaEmpleadoId;
	
	@ManyToOne
	@JoinColumn(name = "puesto_id", nullable = false )
	Puesto puestoId;
	
	@Column(name = "primer_nombre",  nullable = false, length = 100)
	private String primerNombre;
	
	@Column(name = "segundo_nombre", length = 100)
	private String segundoNombre;
	
	@Column(name = "primer_apellido",  nullable = false, length = 100)
	private String primerApellido;
	
	@Column(name = "segundo_apellido",  length = 100)
	private String segundoApellido;
	
	@Column(name = "fecha_nacimiento", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;
	
	@Column(name = "fecha_contratacion", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fechaContratacion;
	
	@Column(nullable = false, length = 1)
	private String estado;

	
	@Column(name = "fecha_fin_contrato", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date fechaFinContrato;
	
	@Column(name="activo", length=1, columnDefinition="CHAR")
	private char activo;
	
	@Column(name = "numero_telefono",  length = 100)
	private String numeroTelefono;
	
	@Column(name = "email",  length = 100)
	private String email;
	
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
	
	
	
	public String getPrimerNombre() {
		return primerNombre;
	}
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	public String getSegundoNombre() {
		return segundoNombre;
	}
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public Date getFechaContratacion() {
		return fechaContratacion;
	}
	public void setFechaContratacion(Date fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}
	public Date getFechaFinContrato() {
		return fechaFinContrato;
	}
	public void setFechaFinContrato(Date fechaFinContrato) {
		this.fechaFinContrato = fechaFinContrato;
	}
	public String getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(String numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public Long getSucursalId() 
	{
		return sucursalId.sucursalId;
	}
	public Long getHorarioId() 
	{
		return horarioId.horarioId;
	}
	public Long getNacionalidadId() 
	{
		return nacionalidadId.nacionalidadId;
	}
	public Long getCategoriaEmpleadoId() 
	{
		return categoriaEmpleadoId.catEmpleadoId;
	}
	public Long getPuestoId() 
	{
		return puestoId.puestoId;
	}
	public Long getEmpleadoId() {
		return empleadoId;
	}
	public void setEmpleadoId(Long empleadoId) {
		this.empleadoId = empleadoId;
	}


	public char getActivo() {
		return activo;
	}
	public void setActivo(char activo) {
		this.activo = activo;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public void setSucursalId(Sucursal sucursalId) {
		this.sucursalId = sucursalId;
	}
	public void setHorarioId(Horario horarioId) {
		this.horarioId = horarioId;
	}
	public void setNacionalidadId(Nacionalidad nacionalidadId) {
		this.nacionalidadId = nacionalidadId;
	}
	public void setCategoriaEmpleadoId(CategoriaEmpleado categoriaEmpleadoId) {
		this.categoriaEmpleadoId = categoriaEmpleadoId;
	}
	public void setPuestoId(Puesto puestoId) {
		this.puestoId = puestoId;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	

}
