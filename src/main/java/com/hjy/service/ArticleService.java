package com.hjy.service;

import com.hjy.dao.ArticleDao;
import com.hjy.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public Article getById(String id){
        Article article = articleDao.findById(id).get();

        return article;
    }

    /**
     *查询所有列表
     * @return
     */
    public List<Article> list(){
        List<Article> articles = articleDao.findAll();

        return articles;
    }

    /**
     * 根据种类名称查询文章
     * @param categoryName
     */
    public List<Article> getArticleByCategoryName(String categoryName){

        return articleDao.findAllByCategory_Name(categoryName);
    }

    /**
     * 根据用户名称查询文章
     * @param username
     */
    public List<Article> findByUser(String username){

        return  articleDao.findByUser(username);

    }

    /**
     * 删除文章
     * @param id
     */
    public void delete(String id){
        articleDao.deleteById(id);
    }

    /**
     * 保存博客
     * @param article
     */
    public void save(Article article){

        articleDao.save(article);
    }
    /**
     * 根据标题查找
     */
    public List<Article> search(String key){

        return articleDao.findByTitleLike(key);
    }
}
