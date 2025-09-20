package vn.khanhduy.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.khanhduy.entities.ProductEntity;
import vn.khanhduy.repository.ProductRepository;
import vn.khanhduy.services.IProductService;

@Service
public class ProductServiceImpl implements IProductService{
	
	@Autowired
	ProductRepository productRepository;

	public <S extends ProductEntity> S save(S entity) {
		return productRepository.save(entity);
	}
	
	
}
