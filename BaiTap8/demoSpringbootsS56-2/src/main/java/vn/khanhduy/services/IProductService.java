package vn.khanhduy.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.khanhduy.entities.ProductEntity;
import vn.khanhduy.repository.ProductRepository;

public interface IProductService {

	void deleteAll();

	void delete(ProductEntity entity);

	void deleteById(Long id);

	long count();

	List<ProductEntity> findAllById(Iterable<Long> ids);

	Page<ProductEntity> findAll(Pageable pageable);

	List<ProductEntity> findAll(Sort sort);

	<S extends ProductEntity> Optional<S> findOne(Example<S> example);

	Optional<ProductEntity> findById(Long id);

	Page<ProductEntity> findByNameContaining(String name, Pageable pageable);

	List<ProductEntity> findByNameContaining(String name);

	List<ProductEntity> findAll();

	Optional<ProductEntity> findByName(String name);

	<S extends ProductEntity> S save(S entity);

}
