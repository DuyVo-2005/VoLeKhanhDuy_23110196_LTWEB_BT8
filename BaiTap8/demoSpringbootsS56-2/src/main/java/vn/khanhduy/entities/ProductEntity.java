package vn.khanhduy.entities;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long productId;
	
	@Column(name = "product_name", length = 200, columnDefinition = "nvarchar(200) not null")
	String name;
	@Column(nullable = false)
	int quantity;
	@Column(nullable = false)
	double unitPrice;
	@Column(length = 200)
	String images;
	@Column(columnDefinition = "nvarchar(500) not null")
	String description;
	@Column(nullable = false)
	double discount;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "YYYY-MM-DD hh:mi:ss")
	Date createdDate;
	@Column(nullable = false)
	short status;
	
	@ManyToOne
	@JoinColumn(name = "categoryId", nullable = false, referencedColumnName = "categoryId")
	@JsonBackReference
	CategoryEntity category;
}
