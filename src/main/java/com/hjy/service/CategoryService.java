package com.hjy.service;

import com.hjy.dao.CategoryDao;
import com.hjy.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {

    @Autowired
    CategoryDao categoryDao;

    /**
     * 查询所有的类型
     * @return
     */

    public List<Category> list(){

        List<Category> categories = categoryDao.findAll();

        return categories;
    }

    /**
     * 根据ID获取一个类型
     * @param id
     * @return
     */

    public Category get(String id){

        return categoryDao.findById(id).get();
    }

    /**
     * 根据类型名称获取一个类型
     * @param name
     * @return
     */

    public Category fingdByName(String name){

        return categoryDao.findByName(name);
    }
}
