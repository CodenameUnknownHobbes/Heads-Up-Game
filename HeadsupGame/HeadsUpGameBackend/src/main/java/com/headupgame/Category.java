package com.headupgame;

import java.util.List;

public class Category {
    private String name;
    private List<String> words;

    public Category(String name, List<String> words) {
        this.name = name;
        this.words = words;
    }

    public String getName() {
        return name;
    }

    public List<String> getWords() {
        return words;
    }
}
