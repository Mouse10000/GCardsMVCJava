package org.example;

import org.example.beans.Author;
import org.example.dao.AuthorDAO;
import org.example.dao.DataSourceAuthorDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("dao.xml");
        
        AuthorDAO authorDAO = ctx.getBean("authorDAO", DataSourceAuthorDAO.class);
        System.out.println(authorDAO);

        Author author = new Author("Пушкин", "Россия");
        authorDAO.addAuthor(author);
        var generatedAuthorId = author.getId();
        System.out.println(authorDAO.getAuthorById(generatedAuthorId).getId());
        System.out.println(author.getId());


    }
}