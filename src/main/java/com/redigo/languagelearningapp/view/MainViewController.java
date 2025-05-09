package com.redigo.languagelearningapp.view;

import com.redigo.languagelearningapp.model.Phrase;
import com.redigo.languagelearningapp.viewmodel.PhraseViewModel;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;


import java.io.IOException;

public class MainViewController {
    @FXML private TableView<Phrase> phraseTable;
    @FXML private TableColumn<Phrase, String> colPhrase;
    @FXML private TableColumn<Phrase, String> colTranslation;
    @FXML private TableColumn<Phrase, String> colCategory;
    @FXML private TableColumn<Phrase, String> colStructure;

    @FXML private Spinner<Integer> masterySpinner;

    @FXML private TextField searchField;
    @FXML private FontIcon searchIcon;

    private PauseTransition pause;

    private final PhraseViewModel viewModel = new PhraseViewModel();
    private int lastUsedMasteryLevel = 0;

    @FXML
    public void initialize() {

        colPhrase.setCellValueFactory(new PropertyValueFactory<>("phrase"));
        colTranslation.setCellValueFactory(new PropertyValueFactory<>("translation"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colStructure.setCellValueFactory(new PropertyValueFactory<>("structure"));

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0); // min=0, max=10, default=0
        masterySpinner.setValueFactory(valueFactory);

        phraseTable.setItems(viewModel.getPhrases());

        // Filter table when mastery level changes
        masterySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterPhrasesByMastery(newVal);
        });

        // Initial filter
        filterPhrasesByMastery(masterySpinner.getValue());

        searchField.setOnAction(event -> onSearch());
        searchField.setTooltip(new Tooltip("Press Enter to search"));

        pause = new PauseTransition(Duration.millis(300)); // delay of 300ms

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            pause.setOnFinished(event -> performSearch(newText.trim()));
            pause.playFromStart();
        });

        // Action to trigger search when user clicks the icon
        searchIcon.setOnMouseClicked(event -> {
            onSearch();
        });

        // Set focus to the TableView after initialization as a quick fix to keep displaying "Search phrases" field text after initialization.
        Platform.runLater(() -> phraseTable.requestFocus());
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
                    String structure = controller.getStructure();

                    if (!phrase.isEmpty()) {
                        Phrase newPhrase = new Phrase(phrase, translation, category, masteryLevel, structure);
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
    public void onEditPhrase() {
        // Get the selected phrase from the table
        Phrase selected = phraseTable.getSelectionModel().getSelectedItem();

        // If no phrase is selected, show a warning message
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No phrase selected to edit.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Show the dialog for editing the phrase
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddPhraseDialog.fxml"));
            DialogPane dialogPane = loader.load();
            AddPhraseDialogController controller = loader.getController();

            // Pre-fill fields with selected phrase's data
            controller.setInitialValues(selected.getPhrase(), selected.getTranslation(), selected.getCategory(), selected.getMasteryLevel(), selected.getStructure());

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Phrase");
            dialog.setDialogPane(dialogPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    // Get updated values from the dialog
                    selected.setPhrase(controller.getPhrase());
                    selected.setTranslation(controller.getTranslation());
                    selected.setCategory(controller.getCategory());
                    selected.setMasteryLevel(controller.getMasteryLevel());
                    selected.setStructure(controller.getStructure());

                    // Save updated values to the database
                    viewModel.updatePhrase(selected);

                    // Refresh the table to reflect the changes
                    phraseTable.refresh();
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

    @FXML
    public void onSearch() {
        performSearch(searchField.getText().trim());
    }

    private void performSearch(String keyword) {
        if (keyword.isEmpty()) {
            phraseTable.setItems(viewModel.getPhrases());
        } else {
            phraseTable.setItems(viewModel.searchPhrases(keyword));
        }
    }


}