package org.example.models;

public class CardForm {
    private Long id;
    private String name;
    private String description;
    private String rank;
    private int number;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
}