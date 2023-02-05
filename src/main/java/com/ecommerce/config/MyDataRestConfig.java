package com.ecommerce.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.ProductCategory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

	@Autowired
	EntityManager entityManager;

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

		HttpMethod[] theUnsupportedAction = { HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PUT };

		// disable HttpMethods for Product: PUT, POST and DELETE
		config.getExposureConfiguration().forDomainType(Product.class)
				.withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedAction))
				.withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedAction));

		// disable HttpMethods for ProductCategory: PUT, POST and DELETE
		config.getExposureConfiguration().forDomainType(ProductCategory.class)
				.withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedAction))
				.withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedAction));

		// call internal helper method to expose the ids

		exposeIds(config);

	}

	private void exposeIds(RepositoryRestConfiguration config) {

		// get list of all entities classes using from entity manager.

		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

		// create an array list of entity types
		List<Class> entityClasses = new ArrayList<>();

		// get the entity types for the entities
		for (EntityType tempEntityType : entities) {
			entityClasses.add(tempEntityType.getJavaType());

		}

		// expose the entity ids for the array of domain type
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);

	}

}