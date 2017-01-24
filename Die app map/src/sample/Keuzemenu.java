package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

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
    protected KeuzemenuDBVerbind choiceMenuDB;

    protected VBox choiceMenuBox;

    public Keuzemenu() {
        choiceMenuDB = new KeuzemenuDBVerbind();
        createButtons();
    }

    private void createButtons() {
        yearChoiceBox = new ComboBox();
        yearChoiceBox.setPromptText("Jaar");
        yearChoiceBox.setPrefWidth(150);
        yearChoiceBox.setPrefHeight(30);
        yearChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(yearChoiceBox);
        });
        yearChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, yearChoiceBox);
        });

        schoolYearChoiceBox = new ComboBox();
        schoolYearChoiceBox.setPromptText("Leerjaar");
        schoolYearChoiceBox.setPrefWidth(150);
        schoolYearChoiceBox.setPrefHeight(30);
        schoolYearChoiceBox.setDisable(true);
        schoolYearChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(schoolYearChoiceBox);
        });
        schoolYearChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, schoolYearChoiceBox);
        });

        blockChoiceBox = new ComboBox();
        blockChoiceBox.setPromptText("Periode");
        blockChoiceBox.setPrefWidth(150);
        blockChoiceBox.setPrefHeight(30);
        blockChoiceBox.setDisable(true);
        blockChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(blockChoiceBox);
        });
        blockChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, blockChoiceBox);
        });

        courseChoiceBox = new ComboBox();
        courseChoiceBox.setPromptText("Module");
        courseChoiceBox.setPrefWidth(150);
        courseChoiceBox.setPrefHeight(30);
        courseChoiceBox.setDisable(true);
        courseChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(courseChoiceBox);
        });
        courseChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, courseChoiceBox);
        });

        typeChoiceBox = new ComboBox();
        typeChoiceBox.setPromptText("Toetsvorm");
        typeChoiceBox.setPrefWidth(150);
        typeChoiceBox.setPrefHeight(30);
        typeChoiceBox.setDisable(true);
        typeChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(typeChoiceBox);
        });
        typeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, typeChoiceBox);
        });

        attemptChoiceBox = new ComboBox();
        attemptChoiceBox.setPromptText("Gelegenheid");
        attemptChoiceBox.setPrefWidth(150);
        attemptChoiceBox.setPrefHeight(30);
        attemptChoiceBox.setDisable(true);
        attemptChoiceBox.setOnMouseClicked(event -> {
            boxClickEvent(attemptChoiceBox);
        });
        attemptChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEvent(observable, attemptChoiceBox);
        });
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
                    disable(false, false, true, true, true, true);
                    clearContent("Jaar");
                    break;
                case "Leerjaar":
                    choiceMenuDB.setSchoolYearSeleciton(observable.getValue().toString());
                    disable(false, false, false, true, true ,true);
                    clearContent("Leerjaar");
                    break;
                case "Periode":
                    choiceMenuDB.setBlockSelection(observable.getValue().toString());
                    disable(false, false, false, false, true, true);
                    clearContent("Periode");
                    break;
                case "Module":
                    choiceMenuDB.setCourseSelection(observable.getValue().toString());
                    disable(false, false, false, false, false, true);
                    clearContent("Module");
                    break;
                case "Toetsvorm":
                    choiceMenuDB.setTypeSelection(observable.getValue().toString());
                    disable(false, false, false, false, false, false);
                    clearContent("Toetsvorm");
                    break;
                case "Gelegenheid":
                    choiceMenuDB.setAttemptSelection(observable.getValue().toString());
                    break;
            }
        }
    }

    private void disable(boolean bool1, boolean bool2, boolean bool3, boolean bool4, boolean bool5, boolean bool6) {
        yearChoiceBox.setDisable(bool1);
        schoolYearChoiceBox.setDisable(bool2);
        blockChoiceBox.setDisable(bool3);
        courseChoiceBox.setDisable(bool4);
        typeChoiceBox.setDisable(bool5);
        attemptChoiceBox.setDisable(bool6);
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
        choiceMenuBox = new VBox();
        choiceMenuBox.getChildren().addAll(yearChoiceBox, schoolYearChoiceBox, blockChoiceBox, courseChoiceBox, typeChoiceBox, attemptChoiceBox);
        choiceMenuBox.setSpacing(20);
        return choiceMenuBox;
    }


}
