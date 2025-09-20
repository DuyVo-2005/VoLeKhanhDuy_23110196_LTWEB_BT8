package vn.khanhduy.configs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

@PersistenceContext
public class JPAConfigs {
	public static EntityManager getEntityManager() {
		EntityManagerFactory enmafact = Persistence.createEntityManagerFactory("dataSource");
		return enmafact.createEntityManager();
	}
}
