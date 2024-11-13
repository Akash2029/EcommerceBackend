package com.ecommerce.application.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.application.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role,String>{

}
