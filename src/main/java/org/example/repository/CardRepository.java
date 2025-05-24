package org.example.repository;

import org.example.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {

    Optional<Card> findCardByNumber(int number);

    List<Card> findAllByName(String name);

    Page<Card> findAll(Pageable pageable); // Стандартный метод

    Card getCardByNumber(int number);

    Page<Card> findByNameContainingIgnoreCase(String name, Pageable pageable);
    @Query("SELECT c FROM Card c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(c.number AS string) LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Card> search(@Param("query") String query, Pageable pageable);
}
