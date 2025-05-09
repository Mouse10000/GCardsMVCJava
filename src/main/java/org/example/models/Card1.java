package org.example.models;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity(name = "Card1")
@Table(name = "card1")
public class Card1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDateTime DateOfAdd;
    @Column(nullable = false)
    private String CardRank;

    @Column(nullable = false)
    private int Number;

    @Column(nullable = true)
    private String description;
    @Column(nullable = true)
    private String Image;

    public Card1() {}

    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Card1(Long id, String name, LocalDateTime dateOfAdd, String cardRank, int number, String description, String image) {
        this.id = id;
        this.name = name;
        DateOfAdd = LocalDateTime.now();
        CardRank = cardRank;
        Number = number;
        this.description = description;
        Image = image;
    }

    public LocalDateTime getDateOfAdd() {
        return DateOfAdd;
    }

    public String getCardRank() {
        return CardRank;
    }

    public void setCardRank(String cardRank) {
        CardRank = cardRank;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
