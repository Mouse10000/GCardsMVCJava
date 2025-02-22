package org.example.services;

import org.example.dao.*;
import org.example.beans.*;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class MyAppService {

    private AuthorDAO authorDAO;

    public void setAuthorDAO(AuthorDAO authorDAO) { this.authorDAO = authorDAO; }

    public void addAuthor(Author Author) throws SQLException {
        authorDAO.addAuthor(Author);
    }
    public Author getAuthorById(int id) throws SQLException {
        return authorDAO.getAuthorById(id);
    }
    public void updateAuthor(Author Author) throws SQLException {
        authorDAO.updateAuthor(Author);
    }
    public void deleteAuthor(int id) throws SQLException {
        authorDAO.deleteAuthor(id);
    }
    public List<Author> getAllAuthors() throws SQLException {
        return authorDAO.getAllAuthors();
    }


}
