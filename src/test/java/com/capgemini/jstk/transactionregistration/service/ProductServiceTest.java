package com.capgemini.jstk.transactionregistration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.capgemini.jstk.transactionregistration.exceptions.NoSuchProductInDatabaseException;
import com.capgemini.jstk.transactionregistration.types.ProductTO;
import com.capgemini.jstk.transactionregistration.types.ProductTO.ProductTOBuilder;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.profiles.active=mysql-test")
// @SpringBootTest (properties = "spring.profiles.active=hsql-test")
public class ProductServiceTest {

	@Autowired
	ProductService productService;

	@Test
	public void shouldAddAndFindProductById(){
		//given
		ProductTO savedProduct = productService.saveProduct(getProductSmartTV());
		
		//when
		ProductTO foundProduct = productService.findProductById(savedProduct.getId());
		
		// then
		assertEquals(foundProduct.getId(), savedProduct.getId());
		assertEquals(foundProduct.getName(), "Smart TV");
		assertEquals(foundProduct.getUnitPrice(), 4000.0, 0.01);
		assertEquals(foundProduct.getMarginPercent(), 23);
		assertEquals(foundProduct.getWeight(), 5.0, 0.0001);
	}
	
	@Test (expected = NoSuchProductInDatabaseException.class)
	public void shouldThrowExceptionWhenFindingNonExistingProduct(){
		//given
		ProductTO savedProduct = productService.saveProduct(getProductSmartTV());
		
		//when then
		productService.findProductById(savedProduct.getId() + 123123L);
	}
	
	@Test
	public void shouldUpdateProduct(){
		//given
		ProductTO savedProduct = productService.saveProduct(getProductSmartTV());
		ProductTO updatedProduct = getProductSmartPhone();
		updatedProduct.setId(savedProduct.getId());
		
		//when
		ProductTO returnedUpdatedProduct = productService.updateProduct(updatedProduct);
		
		// then
		assertEquals(returnedUpdatedProduct.getId(), savedProduct.getId());
		assertEquals(returnedUpdatedProduct.getName(), "Smartphone");
		assertEquals(returnedUpdatedProduct.getUnitPrice(), 700.0, 0.01);
		assertEquals(returnedUpdatedProduct.getMarginPercent(), 23);
		assertEquals(returnedUpdatedProduct.getWeight(), 0.3, 0.0001);
	}
	
	@Test (expected = NoSuchProductInDatabaseException.class)
	public void shouldReturnErrorWhenUpdatingNonExistingProduct(){
		//given
		ProductTO savedProduct = productService.saveProduct(getProductSmartTV());
		ProductTO updatedProduct = getProductSmartPhone();
		updatedProduct.setId(savedProduct.getId() + 1);
		
		//when then
		productService.updateProduct(updatedProduct);
	}
	
	@Test
	public void shouldDeleteProduct(){
		//given
		ProductTO savedProcuct = productService.saveProduct(getProductSmartTV());
		
		//when
		productService.deleteProduct(savedProcuct);
		
		// then
		assertFalse(productService.contains(savedProcuct.getId()));
	}
	
	@Test (expected = NoSuchProductInDatabaseException.class)
	public void shouldReturnErrorWhenDeletingNonExistingProduct(){
		//given
		ProductTO savedProcuct = productService.saveProduct(getProductSmartTV());
		savedProcuct.setId(savedProcuct.getId() + 123123L);
		
		//when then
		productService.deleteProduct(savedProcuct);
	}
	
	@Test
	public void shouldReturnCorrectSize(){
		//given when
		int sizeBeforeAdding = productService.size();
		for(int i=0; i<10; i++){
			productService.saveProduct(getProductSmartPhone());
		}
		int sizeAfterAdding = productService.size();
		
		// then
		assertEquals(10, sizeAfterAdding - sizeBeforeAdding);
	}
	
	private ProductTO getProductSmartTV(){
		return new ProductTOBuilder()
				.withName("Smart TV")
				.withUnitPrice(4000)
				.withMarginPercent(23)
				.withWeight(5.0)
				.build();
	}
	
	private ProductTO getProductSmartPhone(){
		return new ProductTOBuilder()
				.withName("Smartphone")
				.withUnitPrice(700)
				.withMarginPercent(23)
				.withWeight(0.3)
				.build();
	}
	

}
