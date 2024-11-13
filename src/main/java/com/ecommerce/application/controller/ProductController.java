package com.ecommerce.application.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.application.entity.ImageModel;
import com.ecommerce.application.entity.Product;
import com.ecommerce.application.service.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	private ProductService prodService;
	
	@PreAuthorize("hasRole('admin')")
	@PostMapping(value = {"/product/add"}, consumes= {MediaType.MULTIPART_FORM_DATA_VALUE})
	public Product addNewProduct(@RequestPart("product") Product product,
			@RequestPart("imageFile") MultipartFile[] file) {
		try {
			Set<ImageModel> uploadedFile = uploadImage(file);
			product.setProductImages(uploadedFile);
			return prodService.addNewProduct(product);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	
	}
	
	public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
		Set<ImageModel> imageModel = new HashSet<>();
		for(MultipartFile file : multipartFiles) {
			ImageModel image = new ImageModel(file.getOriginalFilename(),file.getContentType()
					,file.getBytes());
			imageModel.add(image);
		}
		return imageModel;
	}
	
	@GetMapping("/product/get")
	public List<Product> getAllProduct() {
		return prodService.getAllProduct();
	}
	
	@DeleteMapping("/product/delete/{productId}")
	public String deleteProductDetails(@PathVariable("productId") Integer productId) {
		prodService.deleteProduct(productId);
		return "Deleted successfully";
	}
	
}
