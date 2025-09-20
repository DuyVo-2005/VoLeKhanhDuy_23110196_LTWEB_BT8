package vn.khanhduy.services.impl;

import java.util.List;
import java.util.Optional;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.khanhduy.entities.ProductEntity;
import vn.khanhduy.repository.ProductRepository;
import vn.khanhduy.services.IProductService;

@Service
public class ProductServiceImpl implements IProductService{
	
	@Autowired
	ProductRepository productRepository;
	
	@Override
	public <S extends ProductEntity> S save(S entity) {
		if (entity.getProductId() == null) {
			return productRepository.save(entity);
		} else {
			Optional<ProductEntity> opt = findById(entity.getProductId());
			if (opt.isPresent()) {
				/*
				 * if (StringUtils.isEmpty(entity.getName())) {
				 * entity.setName(opt.get().getName()); } else { // lay lai name cu
				 * entity.setName(entity.getName()); }
				 */
				if (StringUtils.isEmpty(entity.getImages())) {
					entity.setImages(opt.get().getImages());
				} else {
					// lấy lại images cũ
					entity.setImages(entity.getImages());
				}
			}
			return productRepository.save(entity);
		}
		
		
	}

	@Override
	public Optional<ProductEntity> findByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<ProductEntity> findAll() {
		return productRepository.findAll();
	}
	
	

	@Override
	public List<ProductEntity> findByNameContaining(String name) {
		return productRepository.findByNameContaining(name);
	}

	@Override
	public Page<ProductEntity> findByNameContaining(String name, Pageable pageable) {
		return productRepository.findByNameContaining(name, pageable);
	}

	@Override
	public Optional<ProductEntity> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public <S extends ProductEntity> Optional<S> findOne(Example<S> example) {
		return productRepository.findOne(example);
	}

	@Override
	public List<ProductEntity> findAll(Sort sort) {
		return productRepository.findAll(sort);
	}

	@Override
	public Page<ProductEntity> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@Override
	public List<ProductEntity> findAllById(Iterable<Long> ids) {
		return productRepository.findAllById(ids);
	}

	@Override
	public long count() {
		return productRepository.count();
	}

	@Override
	public void deleteById(Long id) {
		productRepository.deleteById(id);
	}

	@Override
	public void delete(ProductEntity entity) {
		productRepository.delete(entity);
	}

	@Override
	public void deleteAll() {
		productRepository.deleteAll();
	}

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
}
