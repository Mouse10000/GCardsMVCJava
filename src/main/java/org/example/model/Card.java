package org.example.model;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity(name = "Card")
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = true)
    private LocalDateTime dateOfAdd  = LocalDateTime.now();
    @Column(nullable = false)
    private String cardRank;

    @Column(nullable = false, name = "number")
    private int number;

    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private String image;

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
