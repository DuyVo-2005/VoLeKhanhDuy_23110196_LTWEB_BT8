package vn.khanhduy.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.khanhduy.entities.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>  {
	//Tìm Kiếm theo nội dung tên
	List<ProductEntity> findByNameContaining(String name);
	//Tìm kiếm và Phân trang
	Page<ProductEntity> findByNameContaining(String name, Pageable pageable);
	Optional<ProductEntity> findByName(String name);
	Optional<ProductEntity> findByCreatedDate(Date createdAt);
}
