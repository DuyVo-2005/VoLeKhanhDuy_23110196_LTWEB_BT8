package vn.khanhduy.entities;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long categoryId;
	 
	@Column(name = "category_name", length = 200, nullable = false, columnDefinition = "nvarchar(200) not null")
	String name;
	
	//
	String icon;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	@JsonIgnore//@JsonManagedReference
	Set<ProductEntity> products;
}
