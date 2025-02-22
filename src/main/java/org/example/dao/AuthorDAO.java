package org.example.dao;

import org.example.beans.Author;

import java.util.List;

public interface AuthorDAO {
    void addAuthor(Author author);
    Author getAuthorById(int author_id);
    void updateAuthor(Author author);
    void deleteAuthor(int author_id);
    List<Author> getAllAuthors();

}
