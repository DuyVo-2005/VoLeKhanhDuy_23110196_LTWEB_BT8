package vn.khanhduy.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("storage")
public class StorageProperties {
	private String location = "uploads"; // mặc định là "uploads"
}
