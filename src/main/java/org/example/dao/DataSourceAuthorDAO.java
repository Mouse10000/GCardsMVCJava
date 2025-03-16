package org.example.dao;

import org.example.beans.Author;
import javax.sql.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataSourceAuthorDAO implements AuthorDAO {

    private DataSource dataSource;
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addAuthor(Author author) {
        String sql = "INSERT INTO authors (author_name, author_country) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getName());
            stmt.setString(2, author.getCountry());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    author.setId(generatedId);
                    System.out.println("Inserted Author ID: " + generatedId);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Author getAuthorById(Long author_id) {
        String sql = "SELECT * FROM authors WHERE author_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, author_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Author author = new Author();
                    author.setId(rs.getLong("author_id"));
                    author.setName(rs.getString("author_name"));
                    author.setCountry(rs.getString("author_country"));
                    return author;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateAuthor(Author author) {
        String sql = "UPDATE authors SET author_name = ?, author_country = ? WHERE author_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, author.getName());
            stmt.setString(2, author.getCountry());
            stmt.setLong(3, author.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuthor(int author_id) {
        String sql = "DELETE FROM authors WHERE author_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, author_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors";
        try (
              Connection connection = dataSource.getConnection();
              PreparedStatement stmt = connection.prepareStatement(sql);
              ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Author author = new Author();
                author.setId(rs.getInt("author_id"));
                author.setName(rs.getString("author_name"));
                author.setCountry(rs.getString("author_country"));
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(authors);
        return authors;
    }
}


