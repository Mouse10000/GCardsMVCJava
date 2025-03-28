package org.example.models;

public class StudentModel {
    private Integer id;
    private String firstName;
    private String sureName;
    private Boolean isGraduated;
    private Integer mark;

    public StudentModel() {}

    public StudentModel(Integer id, String firstName, String sureName, Boolean isGraduated, Integer mark) {
        this.id = id;
        this.firstName = firstName;
        this.sureName = sureName;
        this.isGraduated = isGraduated;
        this.mark = mark;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getSureName() { return sureName; }
    public void setSureName(String sureName) { this.sureName = sureName; }
    public Boolean getIsGraduated() { return isGraduated; }
    public void setIsGraduated(Boolean isGraduated) { this.isGraduated = isGraduated; }
    public Integer getMark() { return mark; }
    public void setMark(Integer mark) { this.mark = mark; }
}