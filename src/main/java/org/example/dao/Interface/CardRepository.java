package org.example.dao.Interface;

import org.example.beans.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByRank(String rank); // Пример кастомного метода
}