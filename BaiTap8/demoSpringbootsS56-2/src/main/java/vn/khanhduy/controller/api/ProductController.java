package vn.khanhduy.controller.api;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.khanhduy.entities.CategoryEntity;
import vn.khanhduy.entities.ProductEntity;
import vn.khanhduy.model.Response;
import vn.khanhduy.repository.ProductRepository;
import vn.khanhduy.services.ICategoryService;
import vn.khanhduy.services.IProductService;
import vn.khanhduy.services.IStorageService;

@RestController
@RequestMapping(path = "/api/product")
public class ProductController {

	private final ProductRepository productRepository;
	@Autowired
	private IProductService productService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	IStorageService storageService;

	ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@GetMapping
	public ResponseEntity<?> getAllProduct() {
		return new ResponseEntity<Response>(new Response(true, "Thành công", productRepository.findAll()),
				HttpStatus.OK);
	}

	@GetMapping(path = "/getProduct")
	public ResponseEntity<?> getProduct(@Validated @RequestParam("id") Long id) {
		Optional<ProductEntity> product = productService.findById(id);

		if (product.isPresent()) {
			return new ResponseEntity<Response>(new Response(true, "Thành công", product.get()), HttpStatus.OK);
		} else {
			return new ResponseEntity<Response>(new Response(false, "Thất bại", null), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(path = "/addProduct")
	public ResponseEntity<?> addProduct(@RequestParam("productName") String productName,
			@RequestParam("categoryId") Long categoryId,
			@RequestParam(value = "createdDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdDate,
			@RequestParam("description") String description, @RequestParam("discount") Double discount,
			@RequestParam("quantity") Integer quantity, @RequestParam("status") Short status,
			@RequestParam("unitPrice") Double unitPrice, @RequestParam("images") MultipartFile images) {
		Optional<ProductEntity> optProduct = productService.findByName(productName);

		if (optProduct.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product đã tồn tại trong hệ thống");
		} else {
			ProductEntity product = new ProductEntity();

			// kiểm tra tồn tại file, lưu file
			if (!images.isEmpty()) {
				UUID uuid = UUID.randomUUID();
				String uuString = uuid.toString();

				// lưu file vào trường Images
				product.setImages(storageService.getStorageFilename(images, uuString));

				storageService.store(images, product.getImages());
			}
			if (createdDate == null) {
				createdDate = new Date();
			}
			product.setName(productName);
			product.setCreatedDate(createdDate);
			product.setDescription(description);
			product.setDiscount(discount);
			product.setQuantity(quantity);
			product.setStatus(status);
			product.setUnitPrice(unitPrice);

			// Lấy category từ DB
			Optional<CategoryEntity> categoryOpt = categoryService.findById(categoryId);
			if (categoryOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category không tồn tại");
			}
			product.setCategory(categoryOpt.get()); // set category

			productService.save(product);
			return new ResponseEntity<Response>(new Response(true, "Thêm thành công", product), HttpStatus.OK);
		}
	}

	@PutMapping(path = "/updateProduct")
	public ResponseEntity<?> updateProduct(@RequestParam("productId") Long productId,
			@RequestParam("productName") String productName, @RequestParam("categoryId") Long categoryId,
			@RequestParam("description") String description, @RequestParam("discount") Double discount,
			@RequestParam("quantity") Integer quantity, @RequestParam("status") Short status,
			@RequestParam("unitPrice") Double unitPrice, @RequestParam("images") MultipartFile images,
			@RequestParam(value = "oldImages", required = false) String oldImages) {
		Optional<ProductEntity> optProduct = productService.findById(productId);

		if (optProduct.isEmpty()) {
			return new ResponseEntity<Response>(new Response(false, "Không tìm thấy product", null),
					HttpStatus.BAD_REQUEST);
		} else if (optProduct.isPresent()) {
			// kiểm tra tồn tại file, lưu file
			if (!images.isEmpty()) {
				UUID uuid = UUID.randomUUID();
				String uuString = uuid.toString();
				// lưu file vào trường Images
				optProduct.get().setImages(storageService.getStorageFilename(images, uuString));
				storageService.store(images, optProduct.get().getImages());
			} else if (oldImages != null) {
				// giữ lại file cũ
				optProduct.get().setImages(oldImages);
			}

			optProduct.get().setName(productName);
			optProduct.get().setQuantity(quantity);
			optProduct.get().setUnitPrice(unitPrice);
			optProduct.get().setDescription(description);
			optProduct.get().setDiscount(discount);
			optProduct.get().setStatus(status);
			
			productService.save(optProduct.get());
			return new ResponseEntity<Response>(new Response(true, "Cập nhật thành công", optProduct.get()),
					HttpStatus.OK);
		}
		return null;
	}

	@DeleteMapping(path = "/deleteProduct")

	public ResponseEntity<?> deleteProduct(@Validated @RequestParam("productId") Long productId) {
		Optional<ProductEntity> optProduct = productService.findById(productId);
		if (optProduct.isEmpty()) {
			return new ResponseEntity<Response>(new Response(false, "Không tìm thấy Product", null),
					HttpStatus.BAD_REQUEST);
		} else if (optProduct.isPresent()) {
			productService.delete(optProduct.get());
			return new ResponseEntity<Response>(new Response(true, "Xóa thành công", optProduct.get()), HttpStatus.OK);
		}
		return null;
	}
}
