package com.capgemini.jstk.transactionregistration.mappers;

import java.util.Collection;
import java.util.stream.Collectors;

import com.capgemini.jstk.transactionregistration.domain.ProductEntity;
import com.capgemini.jstk.transactionregistration.types.ProductTO;
import com.capgemini.jstk.transactionregistration.types.ProductTO.ProductTOBuilder;

public class ProductMapper {
	
	public static ProductTO toProductTO(ProductEntity productEntity) {
		
		if (productEntity == null) {
			return null;
		}
		
		return new ProductTOBuilder()
				.withId(productEntity.getId())
				.withName(productEntity.getName())
				.withUnitPrice(productEntity.getUnitPrice())
				.withMarginPercent(productEntity.getMarginPercent())
				.withWeight(productEntity.getWeight())
				.withTransactionIds(productEntity.getTransactions()
						.stream()
						.map(s -> s.getId())
						.collect(Collectors.toSet()))
				.build();
	}
	
	public static ProductEntity toProductEntity(ProductTO productTO) {
		
		if (productTO == null) {
			return null;
		}
		ProductEntity productEntity = new ProductEntity();
		productEntity.setName(productTO.getName());
		productEntity.setUnitPrice(productTO.getUnitPrice());
		productEntity.setMarginPercent(productTO.getMarginPercent());
		productEntity.setWeight(productTO.getWeight());
		return productEntity;
	}
	
	public static Collection<ProductTO> map2TOs(Collection<ProductEntity> productEntities) {
		return productEntities.stream().map(ProductMapper::toProductTO).collect(Collectors.toSet());
	}
	
	public static Collection<ProductEntity> map2Entities(Collection<ProductTO> productTOs) {
		return productTOs.stream().map(ProductMapper::toProductEntity).collect(Collectors.toSet());
	}
}
