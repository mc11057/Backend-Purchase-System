package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ues.Purchases.model.Documento;
import com.ues.Purchases.repository.IDocumentoRepository;
import com.ues.Purchases.service.IDocumentoService;

@Service
public class DocumentoServiceImpl implements IDocumentoService{
	
	@Autowired
	private IDocumentoRepository documentoRepository;
	
	

	public DocumentoServiceImpl(IDocumentoRepository documentoRepository) {
		super();
		this.documentoRepository = documentoRepository;
	}

	@Override
	public List<Documento> findAll() throws Exception {
		// TODO Auto-generated method stub
		return documentoRepository.findAll();
	}

	@Override
	public Documento findById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return documentoRepository.findById(id).orElse(null);
	}

	@Override
	public List<Documento> obtenerActivos() throws Exception {
		// TODO Auto-generated method stub
		return documentoRepository.obtenerActivos();
	}
	
	

}
