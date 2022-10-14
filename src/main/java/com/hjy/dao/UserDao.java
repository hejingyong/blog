package com.hjy.dao;

import com.hjy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<User, String>{

    @Query("from User u where u.username = :username and password = :password")
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Query("from User u where u.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("from User u where u.username = :username")
    User findByUsername(@Param("username") String username);



}
