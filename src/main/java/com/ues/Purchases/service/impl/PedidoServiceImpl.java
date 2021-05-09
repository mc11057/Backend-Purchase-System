package com.ues.Purchases.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ues.Purchases.model.Pedido;
import com.ues.Purchases.model.PedidoProducto;
import com.ues.Purchases.model.PedidoProductoKey;
import com.ues.Purchases.model.ProgresoPedido;
import com.ues.Purchases.repository.IPedidoProductoRepository;
import com.ues.Purchases.repository.IPedidoRepository;
import com.ues.Purchases.repository.IProgresoPedidoRepository;
import com.ues.Purchases.service.IPedidoService;

@Service
public class PedidoServiceImpl  implements IPedidoService {
	
	@Autowired
	private IPedidoRepository pedidoRepository;
	@Autowired
	private IProgresoPedidoRepository progresoPedidoRepository;
	@Autowired
	private IPedidoProductoRepository pedidoProductoRepository;

	public PedidoServiceImpl(IPedidoRepository pedidoRepository,
			IProgresoPedidoRepository progresoPedidoRepository,
			IPedidoProductoRepository pedidoProductoRepository) {
		super();
		this.pedidoRepository = pedidoRepository;
		this.progresoPedidoRepository = progresoPedidoRepository;
		this.pedidoProductoRepository = pedidoProductoRepository;
		
	}

	
	@Override
	public List<Pedido> findAll() throws Exception {
		return (List<Pedido>)pedidoRepository.findAll();
	}

	@Override
	public Pedido findById(Long id) throws Exception {
		return pedidoRepository.findById(id).orElse(null);
	}

@Transactional
	@Override
	public Pedido create(Pedido pedido) throws Exception {
		fillPedido(pedido);
		Pedido pedidoSaved = pedidoRepository.save(pedido);		
		createLineItems(pedidoSaved,pedido);
		return pedidoSaved;
	}


private void fillPedido(Pedido pedido) {
	ProgresoPedido progreso = progresoPedidoRepository.findByEstadoPedido("Activo");
	pedido.setUserCreate(pedido.getEmpleado().getPrimerNombre()+pedido.getEmpleado().getPrimerApellido());
	pedido.setEstado("A");
	pedido.setProgresoPedido(progreso);	
	
}


private void createLineItems(Pedido pedidoSaved,Pedido pedido) {
	for(PedidoProducto pedProd: pedido.productos)
	{
		PedidoProductoKey pedProdKey = new PedidoProductoKey();
		pedProdKey.setPedidoId(pedidoSaved.getPedidoId());
		pedProdKey.setProductoId(pedProd.getProducto().getProductoId());
		pedProd.setId(pedProdKey);
		pedProd.setPedido(pedidoSaved);
		pedidoProductoRepository.save(pedProd);
	}
	
}


}
