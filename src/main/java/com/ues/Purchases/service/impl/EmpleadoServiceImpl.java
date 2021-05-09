package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Empleado;
import com.ues.Purchases.repository.IApplicationUserRepository;
import com.ues.Purchases.repository.IEmpleadoRepository;
import com.ues.Purchases.service.IEmpleadoService;

@Service
public class EmpleadoServiceImpl implements IEmpleadoService{
	
	@Autowired
	private IEmpleadoRepository empleadoRepository; 
	@Autowired
	private IApplicationUserRepository userRepository; 
	
	public EmpleadoServiceImpl(IEmpleadoRepository empleadoRepository,IApplicationUserRepository userRepository) {
		super();
		this.empleadoRepository = empleadoRepository;
		this.userRepository = userRepository;
	}
	
	@Override
	public List<Empleado> findAll() throws Exception {
		return (List<Empleado>)empleadoRepository.findAll();
	}

	@Override
	public Empleado findById(Long id) throws Exception {
		return empleadoRepository.findById(id).orElse(null);
	}

	@Override
	public Empleado findByApplicationUser(Long applicationUserId) throws Exception {
		return empleadoRepository.findByUser(userRepository.findById(applicationUserId).get());

	}

}
