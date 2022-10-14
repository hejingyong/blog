package com.hjy.service;

import com.hjy.dao.UserDao;
import com.hjy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public boolean login(String username, String password){
        User user = userDao.findByUsernameAndPassword(username, DigestUtils.md5DigestAsHex(password.getBytes()));
        if(null == user){

            return false;
        }else {

            return true;
        }
    }

    public  User findByEmail(String email)
    {
            User user= userDao.findByEmail(email);
            return user;
    }

    public  User  findByUsername(String username)
    {

        User user= userDao.findByUsername(username);
        return user;
    }
    public void save(User user){

        userDao.save(user);
    }
}
