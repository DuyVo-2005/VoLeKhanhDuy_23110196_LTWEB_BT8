package vn.khanhduy.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import vn.khanhduy.entities.CategoryEntity;

public interface ICategoryService {

	void deleteAll();

	void delete(CategoryEntity entity);

	void deleteById(Long id);

	long count();

	Optional<CategoryEntity> findById(Long id);

	List<CategoryEntity> findAllById(Iterable<Long> ids);

	Page<CategoryEntity> findAll(org.springframework.data.domain.Pageable pageable);

	List<CategoryEntity> findAll(Sort sort);

	<S extends CategoryEntity> Optional<S> findOne(Example<S> example);
	
	public Optional<CategoryEntity> findByName(String name);

	Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);

	List<CategoryEntity> findByNameContaining(String name);

	List<CategoryEntity> findAll();

	<S extends CategoryEntity> S save(S entity);
	
}
