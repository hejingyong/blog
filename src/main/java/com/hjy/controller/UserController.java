package com.hjy.controller;

import com.hjy.config.SecurityInterceptor;
import com.hjy.entity.Article;
import com.hjy.entity.Category;
import com.hjy.entity.User;
import com.hjy.service.ArticleService;
import com.hjy.service.CategoryService;
import com.hjy.service.SendEmailService;
import com.hjy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.util.FileCopyUtils;

@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    SendEmailService sendEmailService;
    @Autowired
    TemplateEngine templateEngine;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


    /**
     * 后台主页
     */
    @RequestMapping("")
    public String admin(HttpServletRequest request,Model model){
        Cookie[] cookie = request.getCookies();
        User u = userService.findByUsername(cookie[1].getValue());
        if (u.getId().equals("0"))
        {
            List<Article> articles = articleService.list();
            model.addAttribute("articles", articles);
        }
        else
        {

            List<Article> articles =articleService.findByUser(cookie[1].getValue());
            //articleService.list();
            model.addAttribute("articles", articles);
        }


        return "admin/index";
    }


    /**
     * 文章主页
     */
    @RequestMapping("article")
    public String article(){


        return "front/index";
    }
    /**
     * 忘记密码
     * @return
     */
    @RequestMapping("/register")
    public String register(HttpServletResponse response, User user, Model model){
        if (user.getUsername() != null || user.getPassword()!= null||user.getEmail()!= null) {

            ;
            if(userService.findByUsername(user.getUsername()) !=null|| userService.findByEmail(user.getEmail()) !=null)
            {

                model.addAttribute("error", "用户已存在!");
                System.out.println("register failture");
            }

            else
            {
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                userService.save(user);
                model.addAttribute("error", "注册成功！");
                System.out.println("register successful");
                return "admin/login";

            }
        }

        return "admin/register";

    }

    /**
     * 修改密码
     * @return
     */
    @RequestMapping("/changePwd")
    public String changePwd(HttpServletRequest request,HttpServletResponse response, Model model,User user) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {

            String username = cookies[1].getValue();
            User u = userService.findByUsername(username);
            String newPwd =user.getPassword();


            if ( newPwd == null ){

            } else {
                String pwd[] = newPwd.split(",",2);
                if (!pwd[0].equals(pwd[1]))
                {
                    model.addAttribute("error", "更改失败");
                    // System.out.println("failture");
                }
                else
                {
                 u.setPassword(DigestUtils.md5DigestAsHex(pwd[0].getBytes()));
                 userService.save(u);
                    model.addAttribute("error", "更改成功");
                    //  System.out.println("successful");


                    for (Cookie cookie : cookies)
                    {

                        cookie.setMaxAge(0);
                        response.addCookie(cookie);

                    }



                }
            }

            return "admin/changePwd";

        }
        else
        {
            return "admin/login";

        }



    }


    /**
     * 注销登录
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
        {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return "admin/login";
    }


    /**
     * 忘记密码
     * @return
     */
    @RequestMapping("/forgotPwd")
    public String forgotPwd(){

        return "admin/forgotPwd";
    }

    /**
     * 邮箱验证
     * @param user
     * @param model
     * @return
     */

    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public String sendMail(HttpServletResponse response, User user, Model model) throws  Exception{


        User u = userService.findByEmail(user.getEmail());
        if(u != null){
            String to = u.getEmail();
            String subject = "找回密码";
            String pwd = UUID.randomUUID().toString().substring(0,6);
            u.setPassword(DigestUtils.md5DigestAsHex(pwd.getBytes()));
            userService.save(u);

            Context context = new Context();
            context.setVariable("username",u.getUsername());
            context.setVariable("password",pwd);
            String emailContent = templateEngine.process("emailTemplate_forgotPwd",context);


            try
            {
                sendEmailService.sendSimpleEmail(to,subject,emailContent);
                model.addAttribute("error", "密码已重置!请检查邮箱!");
                System.out.println("successful");
            }
             catch (MessagingException e){

                model.addAttribute("error", "发送失败,请重试!");
                System.out.println("failture");
            }

        }else {
            model.addAttribute("error", "输入错误");
            System.out.println("failture");


        }
        return "admin/login";
    }

    /**

    /**
     * 登录模块
     * @return
     */
    @RequestMapping("/login")
    public String login(){

        return "admin/login";
    }

    /**
     * 登录验证
     * @param user
     * @param model
     * @return
     */

    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String doLogin(HttpServletResponse response, User user, Model model){

        if(userService.login(user.getUsername(), user.getPassword())){
            Cookie cookie = new Cookie(SecurityInterceptor.SESSION_KEY, DigestUtils.md5DigestAsHex(user.getUsername().getBytes()));
            Cookie cookie2 = new Cookie(SecurityInterceptor.USERNAME, user.getUsername());
            response.addCookie(cookie);
            response.addCookie(cookie2);
            model.addAttribute("user", user);
            System.out.println(cookie.getName());

            return "redirect:/admin";
        }else {
            model.addAttribute("error", "用户名或者密码错误");
            System.out.println("failture");

            return "admin/login";
        }
    }

    /**
     * 删除博客
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id){
        articleService.delete(id);

        return "redirect:/admin";
    }

    @RequestMapping("/write")
    public String write(Model model){
        List<Category> categories = categoryService.list();
        model.addAttribute("categories", categories);
        model.addAttribute("article", new Article());
        return "admin/write";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(HttpServletRequest request, Article article){
        //设置种类
        String name = article.getCategory().getName();
        Category category = categoryService.fingdByName(name);
        article.setCategory(category);

        Cookie[] cookies = request.getCookies();
        article.setUser(cookies[1].getValue());
        //设置摘要,取前40个字
        if(article.getContent().length() > 40){
            article.setSummary(article.getContent().substring(0, 40));
        }else {
            article.setSummary(article.getContent().substring(0, article.getContent().length()));
        }

        article.setDate(sdf.format(new Date()));
        articleService.save(article);

        return "redirect:/admin";
    }

    @RequestMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model){
        Article article = articleService.getById(id);
        model.addAttribute("target", article);
        List<Category> categories = categoryService.list();
        model.addAttribute("categories", categories);
        model.addAttribute("article", article);

        return "admin/update";
    }

    //上传图片并回显
    @ResponseBody
    @RequestMapping("/uploadImg")
    public Map<String,Object> uploadImg(HttpServletRequest request, @RequestParam(value = "editormd-image-file", required = false) MultipartFile file){
        Map<String,Object> map = new HashMap<>();
        if (file != null){
            //获取此项目的tomcat路径
            String webapp = request.getServletContext().getRealPath("/");
            try{
                //获取文件名
                String filename = file.getOriginalFilename();
                UUID uuid = UUID.randomUUID();
                String name = "";
                if (filename != null){
                    name = filename.substring(filename.lastIndexOf(".")); //获取文件后缀名
                }
                // 图片的路径+文件名称
                String fileName = "/upload/" + uuid + name;
                // 图片的在服务器上面的物理路径
                File destFile = new File(webapp, fileName);
                // 生成upload目录
                File parentFile = destFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();// 自动生成upload目录
                }
                // 把上传的临时图片，复制到当前项目的webapp路径
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(destFile));
                map.put("success",1); //设置回显的数据 0 表示上传失败，1 表示上传成功
                map.put("message","上传成功"); //提示的信息，上传成功或上传失败及错误信息等
                map.put("url","/blog/"+fileName); //图片回显的url 上传成功时才返回
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return map;
    }
}


