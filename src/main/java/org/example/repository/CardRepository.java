package org.example.repository;

import org.example.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findCardByNumber(int number);

    List<Card> findAllByName(String name);

    Page<Card> findAll(Pageable pageable); // Стандартный метод

    Card getCardByNumber(int number);
}
