package com.ecommerce.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.application.dao.ProductDao;
import com.ecommerce.application.entity.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductDao productdao;
	
	public Product addNewProduct(Product product) {
		return productdao.save(product);
	}
	
	public List<Product> getAllProduct() {
		return (List)productdao.findAll();
	}
	
	public void deleteProduct(Integer productId) {
		productdao.deleteById(productId);
	}
}
