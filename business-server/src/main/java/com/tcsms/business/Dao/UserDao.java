package com.tcsms.business.Dao;

import com.tcsms.business.Entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<User, Integer> {
    User findByUsername(String username);

    List<User> findAllByRole(String role);
}
