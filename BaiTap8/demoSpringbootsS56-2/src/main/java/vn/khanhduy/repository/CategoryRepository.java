package vn.khanhduy.repository;
import vn.khanhduy.entities.CategoryEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
	
	//Tim kiem theo ten
	List<CategoryEntity> findByNameContaining(String name);
	
	//Tim kiem va phan trang
	Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);
	Optional<CategoryEntity> findByName(String name);
}
