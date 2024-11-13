package com.ecommerce.application.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.application.entity.Users;

@Repository
public interface UserDao extends CrudRepository<Users,String>{

}
