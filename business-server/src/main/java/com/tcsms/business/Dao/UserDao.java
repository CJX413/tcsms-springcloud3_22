package com.tcsms.business.Dao;

import com.tcsms.business.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDao extends CrudRepository<User, String> {
    User findByUsername(String username);

    List<User> findAllByRole(String role);

    @Query(value = "select * from user where user.role='ADMIN' LIMIT 1", nativeQuery = true)
    User getAdmin();
}
