package com.redigo.languagelearningapp.model;

public class Phrase {
    private int id;
    private String phrase;
    private String translation;
    private String category;
    private int masteryLevel;
    private String structure;

    public Phrase(String phrase, String translation, String category, int masteryLevel, String structure) {
        this.phrase = phrase;
        this.translation = translation;
        this.category = category;
        this.masteryLevel = masteryLevel;
        this.structure = structure;
    }

    public Phrase(int id, String phrase, String translation, String category, int masteryLevel, String structure) {
        this.id = id;
        this.phrase = phrase;
        this.translation = translation;
        this.category = category;
        this.masteryLevel = masteryLevel;
        this.structure = structure;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPhrase() { return phrase; }
    public void setPhrase(String phrase) { this.phrase = phrase; }

    public String getTranslation() { return translation; }
    public void setTranslation(String translation) { this.translation = translation; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getMasteryLevel() { return masteryLevel; }
    public void setMasteryLevel(int masteryLevel) { this.masteryLevel = masteryLevel; }

    public String getStructure() { return structure; }
    public void setStructure(String structure) { this.structure = structure; }


    @Override
    public String toString() {
        return phrase + " → " + translation;
    }
}