package com.redigo.languagelearningapp.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class AddPhraseDialogController {

    @FXML
    private TextField phraseField;

    @FXML
    private TextField translationField;

    @FXML
    private TextField categoryField;

    @FXML
    private Spinner<Integer> masterySpinner;

    @FXML private TextField structureField;

    @FXML
    public void initialize() {
        //initialize with a default value, but it will be overwritten by the setter
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        masterySpinner.setValueFactory(valueFactory);
    }

    public int getMasteryLevel() {
        return masterySpinner.getValue();
    }

    public void setInitialMasteryLevel(int level) {
        if (masterySpinner.getValueFactory() != null) {
            masterySpinner.getValueFactory().setValue(level);
        }
    }

    public void setInitialCategory(String category) {
        categoryField.setText(category);
    }

    public void setInitialStructure(String structure) {
        structureField.setText(structure);
    }

    public String getPhrase() {
        return phraseField.getText();
    }

    public String getTranslation() {
        return translationField.getText();
    }

    public String getCategory() {
        return categoryField.getText();
    }

    public String getStructure() {
        return structureField.getText();
    }

    // Method to set initial values for editing
    public void setInitialValues(String phrase, String translation, String category, int masteryLevel, String structure) {
        phraseField.setText(phrase);
        translationField.setText(translation);
        categoryField.setText(category);
        masterySpinner.getValueFactory().setValue(masteryLevel);
        structureField.setText(structure);
    }

}