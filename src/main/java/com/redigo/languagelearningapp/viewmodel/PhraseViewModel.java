package com.redigo.languagelearningapp.viewmodel;

import com.redigo.languagelearningapp.model.Phrase;
import com.redigo.languagelearningapp.model.PhraseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PhraseViewModel {
    private final ObservableList<Phrase> phrases;
    private final PhraseManager phraseManager;

    public PhraseViewModel() {
        this.phraseManager = new PhraseManager();
        this.phrases = FXCollections.observableArrayList(phraseManager.getAllPhrases());
    }

    public ObservableList<Phrase> getPhrases() {
        return phrases;
    }

    public void addPhrase(Phrase phrase) {
        phraseManager.addPhrase(phrase);
        phrases.add(phrase); // Refresh list
    }

    public void deletePhrase(Phrase phrase) {
        phraseManager.deletePhrase(phrase.getId());
        phrases.remove(phrase); // Update observable list
    }
}