package org.example.model.dto;

public class CardFilter {
    private String rank;
    private Integer minNumber;
    private Integer maxNumber;
    // другие поля по необходимости


    public CardFilter(String rank, Integer minNumber, Integer maxNumber) {
        this.rank = rank;
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Integer getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(Integer minNumber) {
        this.minNumber = minNumber;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

    public boolean isEmpty() {
        return (rank == null || rank.isEmpty())
                && minNumber == null
                && maxNumber == null;
    }
}
