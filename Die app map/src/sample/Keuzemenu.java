package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 24-1-2017.
 */
public class Keuzemenu {
    protected ComboBox yearChoiceBox;
    protected ComboBox schoolYearChoiceBox;
    protected ComboBox blockChoiceBox;
    protected ComboBox courseChoiceBox;
    protected ComboBox typeChoiceBox;
    protected ComboBox attemptChoiceBox;
    public Button examLoadButton;
    protected KeuzemenuDBVerbind choiceMenuDB;
    protected List<String> selection;

    protected VBox choiceMenuBox;

    public Keuzemenu() {
        choiceMenuDB = new KeuzemenuDBVerbind();
        createButtons();
    }

    private void createButtons() {
        yearChoiceBox = new ComboBox();
        yearChoiceBox.setPromptText("Jaar");
        yearChoiceBox.setMinWidth(150);
        yearChoiceBox.setMinHeight(30);
        yearChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(yearChoiceBox);
        });
        yearChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, yearChoiceBox);
        });

        schoolYearChoiceBox = new ComboBox();
        schoolYearChoiceBox.setPromptText("Leerjaar");
        schoolYearChoiceBox.setMinWidth(150);
        schoolYearChoiceBox.setMinHeight(30);
        schoolYearChoiceBox.setDisable(true);
        schoolYearChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(schoolYearChoiceBox);
        });
        schoolYearChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, schoolYearChoiceBox);
        });

        blockChoiceBox = new ComboBox();
        blockChoiceBox.setPromptText("Periode");
        blockChoiceBox.setMinWidth(150);
        blockChoiceBox.setMinHeight(30);
        blockChoiceBox.setDisable(true);
        blockChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(blockChoiceBox);
        });
        blockChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, blockChoiceBox);
        });

        courseChoiceBox = new ComboBox();
        courseChoiceBox.setPromptText("Module");
        courseChoiceBox.setMinWidth(150);
        courseChoiceBox.setMinHeight(30);
        courseChoiceBox.setDisable(true);
        courseChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(courseChoiceBox);
        });
        courseChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, courseChoiceBox);
        });

        typeChoiceBox = new ComboBox();
        typeChoiceBox.setPromptText("Toetsvorm");
        typeChoiceBox.setMinWidth(150);
        typeChoiceBox.setMinHeight(30);
        typeChoiceBox.setDisable(true);
        typeChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(typeChoiceBox);
        });
        typeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, typeChoiceBox);
        });

        attemptChoiceBox = new ComboBox();
        attemptChoiceBox.setPromptText("Gelegenheid");
        attemptChoiceBox.setMinWidth(150);
        attemptChoiceBox.setMinHeight(30);
        attemptChoiceBox.setDisable(true);
        attemptChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(attemptChoiceBox);
        });
        attemptChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, attemptChoiceBox);
        });

        examLoadButton = new Button("Toets weergeven");
        examLoadButton.setMinWidth(150);
        examLoadButton.setMinHeight(30);
        examLoadButton.setDisable(true);
    }

    private void boxClickEvent(ComboBox comboBox) {
        comboBox.getItems().clear();
        switch (comboBox.getPromptText()) {
            case "Jaar":
                comboBox.getItems().addAll(choiceMenuDB.getYear());
                break;
            case "Leerjaar":
                comboBox.getItems().addAll(choiceMenuDB.getSchoolYear());
                break;
            case "Periode":
                comboBox.getItems().addAll(choiceMenuDB.getBlock());
                break;
            case "Module":
                comboBox.getItems().addAll(choiceMenuDB.getCourse());
                break;
            case "Toetsvorm":
                comboBox.getItems().addAll(choiceMenuDB.getType());
                break;
            case "Gelegenheid":
                comboBox.getItems().addAll(choiceMenuDB.getAttempt());
        }
    }

    private void boxSelectedEvent(ObservableValue observable, ComboBox comboBox) {
        if (observable.getValue() == null) {
        }
        else {
            switch (comboBox.getPromptText()) {
                case "Jaar":
                    choiceMenuDB.setYearSelection(observable.getValue().toString());
                    disable(false, false, true, true, true, true, true);
                    clearContent("Jaar");
                    break;
                case "Leerjaar":
                    choiceMenuDB.setSchoolYearSeleciton(observable.getValue().toString());
                    disable(false, false, false, true, true ,true, true);
                    clearContent("Leerjaar");
                    break;
                case "Periode":
                    choiceMenuDB.setBlockSelection(observable.getValue().toString());
                    disable(false, false, false, false, true, true, true);
                    clearContent("Periode");
                    break;
                case "Module":
                    choiceMenuDB.setCourseSelection(observable.getValue().toString());
                    disable(false, false, false, false, false, true, true);
                    clearContent("Module");
                    break;
                case "Toetsvorm":
                    choiceMenuDB.setTypeSelection(observable.getValue().toString());
                    disable(false, false, false, false, false, false, true);
                    clearContent("Toetsvorm");
                    break;
                case "Gelegenheid":
                    choiceMenuDB.setAttemptSelection(observable.getValue().toString());
                    disable(false, false, false, false, false, false, false);
                    break;
            }
        }
    }

    private void disable(boolean bool1, boolean bool2, boolean bool3, boolean bool4, boolean bool5, boolean bool6, boolean bool7) {
        yearChoiceBox.setDisable(bool1);
        schoolYearChoiceBox.setDisable(bool2);
        blockChoiceBox.setDisable(bool3);
        courseChoiceBox.setDisable(bool4);
        typeChoiceBox.setDisable(bool5);
        attemptChoiceBox.setDisable(bool6);
        examLoadButton.setDisable(bool7);
    }

    private void clearContent(String selectedBox) {
        switch (selectedBox) {
            case "Jaar":
                schoolYearChoiceBox.getItems().clear();
                blockChoiceBox.getItems().clear();
                courseChoiceBox.getItems().clear();
                typeChoiceBox.getItems().clear();
                attemptChoiceBox.getItems().clear();
                break;
            case "Leerjaar":
                blockChoiceBox.getItems().clear();
                courseChoiceBox.getItems().clear();
                typeChoiceBox.getItems().clear();
                attemptChoiceBox.getItems().clear();
                break;
            case "Periode":
                courseChoiceBox.getItems().clear();
                typeChoiceBox.getItems().clear();
                attemptChoiceBox.getItems().clear();
                break;
            case "Module":
                typeChoiceBox.getItems().clear();
                attemptChoiceBox.getItems().clear();
                break;
            case "Toetsvorm":
                attemptChoiceBox.getItems().clear();
                break;
        }
    }

    public VBox getChoiceMenuBox() {
        VBox fillBox = new VBox();
        VBox.setVgrow(fillBox, Priority.ALWAYS);

        choiceMenuBox = new VBox();
        choiceMenuBox.getChildren().addAll(yearChoiceBox, schoolYearChoiceBox, blockChoiceBox, courseChoiceBox, typeChoiceBox, attemptChoiceBox, fillBox, examLoadButton);
        choiceMenuBox.setSpacing(20);
        VBox.setVgrow(choiceMenuBox, Priority.ALWAYS);
        return choiceMenuBox;
    }

    public List<String> getSelection() {
        selection = choiceMenuDB.getSelection();
        return selection;
    }
}
