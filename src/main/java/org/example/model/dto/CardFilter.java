package org.example.model.dto;

public class CardFilter {
    private String name;
    private Integer minNumber;
    private Integer maxNumber;
    // другие поля по необходимости


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return (name == null || name.isEmpty())
                && minNumber == null
                && maxNumber == null;
    }
}
