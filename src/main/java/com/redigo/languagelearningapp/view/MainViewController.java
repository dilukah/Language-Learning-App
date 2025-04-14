package com.redigo.languagelearningapp.view;

import com.redigo.languagelearningapp.model.Phrase;
import com.redigo.languagelearningapp.viewmodel.PhraseViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class MainViewController {
    @FXML private TableView<Phrase> phraseTable;
    @FXML private TableColumn<Phrase, String> colPhrase;
    @FXML private TableColumn<Phrase, String> colTranslation;
    @FXML private TableColumn<Phrase, String> colCategory;

    @FXML private Spinner<Integer> masterySpinner;

    private final PhraseViewModel viewModel = new PhraseViewModel();
    private int lastUsedMasteryLevel = 0;

    @FXML
    public void initialize() {
        colPhrase.setCellValueFactory(new PropertyValueFactory<>("phrase"));
        colTranslation.setCellValueFactory(new PropertyValueFactory<>("translation"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0); // min=0, max=10, default=0
        masterySpinner.setValueFactory(valueFactory);

        phraseTable.setItems(viewModel.getPhrases());

        // Filter table when mastery level changes
        masterySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterPhrasesByMastery(newVal);
        });

        // Initial filter
        filterPhrasesByMastery(masterySpinner.getValue());
    }

    private void filterPhrasesByMastery(int level) {
        phraseTable.setItems(viewModel.getPhrases().filtered(p -> p.getMasteryLevel() == level));
    }

    @FXML
    public void onAddPhrase() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddPhraseDialog.fxml"));
            DialogPane dialogPane = loader.load();
            dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            AddPhraseDialogController controller = loader.getController();

            controller.setInitialMasteryLevel(lastUsedMasteryLevel);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add New Phrase");
            dialog.setDialogPane(dialogPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    String phrase = controller.getPhrase();
                    String translation = controller.getTranslation();
                    String category = controller.getCategory();
                    int masteryLevel = controller.getMasteryLevel();

                    if (!phrase.isEmpty()) {
                        Phrase newPhrase = new Phrase(phrase, translation, category, masteryLevel);
                        // Save to DB
                        viewModel.addPhrase(newPhrase);
                        filterPhrasesByMastery(masterySpinner.getValue()); // Reapply filter
                        // Save mastery level for next use
                        lastUsedMasteryLevel = masteryLevel;
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onDeleteSelected() {
        Phrase selected = phraseTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            viewModel.deletePhrase(selected);
        }
    }


}