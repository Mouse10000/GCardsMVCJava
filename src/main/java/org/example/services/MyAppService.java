package org.example.services;

import org.example.dao.AuthorDAO;
import org.example.beans.*;

import java.sql.SQLException;
import java.util.List;

import org.example.dao.Interface.*;

public class MyAppService {

    private AuthorDAO authorDAO;

    // Setter methods for DAOs
    public void setAuthorDAO(AuthorDAO authorDAO) { this.authorDAO = authorDAO; }

    // Author Service Methods
    public void addAuthor(Author author) throws SQLException {
        authorDAO.addAuthor(author);
    }
    public Author getAuthorById(int id) throws SQLException {
        return authorDAO.getAuthorById(id);
    }
    public void updateAuthor(Author author) throws SQLException {
        authorDAO.updateAuthor(author);
    }
    public void deleteAuthor(int id) throws SQLException {
        authorDAO.deleteAuthor(id);
    }
    public List<Author> getAllAuthors() throws SQLException {
        return authorDAO.getAllAuthors();
    }

}