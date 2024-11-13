package com.ecommerce.application.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.application.entity.Product;

@Repository
public interface ProductDao extends CrudRepository<Product,Integer>{

}
