package org.example.model;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Модель карты в системе.
 * Представляет информацию о карте, включая её характеристики и изображение.
 */
@Entity(name = "Card")
@Table(name = "card")
public class Card {
    /**
     * Уникальный идентификатор карты
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название карты
     */
    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = true)
    private LocalDateTime dateOfAdd  = LocalDateTime.now();

    /**
     * Ранг карты (например, обычная, редкая, эпическая)
     */
    @Column(nullable = false)
    private String cardRank;

    /**
     * Уникальный номер карты
     */
    @Column(nullable = false, name = "number")
    private int number;

    /**
     * Описание карты
     */
    @Column(nullable = true)
    private String description;

    /**
     * Путь к изображению карты
     */
    @Column(nullable = true)
    private String image;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCard> userCards = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardSender> userCardsSender = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardRecipient> userCardsRecipient = new ArrayList<>();

    public Card() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Card(String name, String cardRank, int number, String description, String image) {
        this.name = name;
        dateOfAdd = LocalDateTime.now();
        this.cardRank = cardRank;
        this.number = number;
        this.description = description;
        this.image = image;
    }
    public Card(Card card) {
        this.name = card.getName();
    }

    public LocalDateTime getDateOfAdd() {
        return dateOfAdd;
    }

    public String getCardRank() {
        return cardRank;
    }

    public void setCardRank(String cardRank) {
        this.cardRank = cardRank;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}