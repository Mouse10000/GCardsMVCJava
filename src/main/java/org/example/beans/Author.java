package org.example.beans;

import org.jetbrains.annotations.NotNull;


public class Author extends BaseBean {
    @NotNull
    private String name = "";
    @NotNull
    private String country = "";

    public Author() {}
    public Author(@NotNull String name, @NotNull String country){
        this.name = name;
        this.country = country;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getCountry() {
        return country;
    }

    public void setCountry(@NotNull String country) {
        this.country = country;
    }
}

