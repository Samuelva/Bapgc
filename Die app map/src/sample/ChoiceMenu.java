package sample;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * Created by Samuel on 24-1-2017.
 */
public class ChoiceMenu {
    protected ComboBox yearChoiceBox;
    protected ComboBox schoolYearChoiceBox;
    protected ComboBox blockChoiceBox;
    protected ComboBox courseChoiceBox;
    protected ComboBox typeChoiceBox;
    protected ComboBox attemptChoiceBox;
    public Button examLoadButton;
    protected ChoiceMenuDatabaseConnect choiceMenuDB;
    protected List<String> selection;

    protected VBox choiceMenuBox; // Box met het hele keuzemenu

    public ChoiceMenu() {
        choiceMenuDB = new ChoiceMenuDatabaseConnect();
        createButtons();
    }

    private void createButtons() {

        yearChoiceBox = new ComboBox();
        schoolYearChoiceBox = new ComboBox();
        blockChoiceBox = new ComboBox();
        courseChoiceBox = new ComboBox();
        typeChoiceBox = new ComboBox();
        attemptChoiceBox = new ComboBox();
        examLoadButton = new Button("Toets weergeven");

        examLoadButton.setMinWidth(150);
        examLoadButton.setMinHeight(30);
        examLoadButton.setDisable(true);

        setButtons(yearChoiceBox, "Jaar");
        setButtons(schoolYearChoiceBox, "Leerjaar");
        setButtons(blockChoiceBox, "Periode");
        setButtons(courseChoiceBox, "Module");
        setButtons(typeChoiceBox, "Toetsvorm");
        setButtons(attemptChoiceBox, "Gelegenheid");
    }

    private void setButtons(ComboBox choiceBox, String promptText) {
        choiceBox.setPromptText(promptText);
        choiceBox.setMinWidth(150);
        choiceBox.setMinHeight(30);
        choiceBox.setDisable(true);
        choiceBox.setOnMouseClicked(event -> {
            boxClickEvent(choiceBox);
        });
        choiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boxSelectedEventCheck(observable, choiceBox);
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

    private void boxSelectedEventCheck(ObservableValue observable, ComboBox comboBox) {
        if (observable.getValue() == null) {
        } else {
            boxSelectedEvent(observable, comboBox);
        }
    }

    private void boxSelectedEvent(ObservableValue observable, ComboBox comboBox) {
        switch (comboBox.getPromptText()) {
            case "Jaar":
                yearBoxEvent(observable);
                break;
            case "Leerjaar":
                schoolYearBoxEvent(observable);
                break;
            case "Periode":
                blockBoxEvent(observable);
                break;
            case "Module":
                courseBoxEvent(observable);
                break;
            case "Toetsvorm":
                typeBoxEvent(observable);
                break;
            case "Gelegenheid":
                attemptBoxEvent(observable);
                break;
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


    public void yearBoxEvent(ObservableValue observable) {
        choiceMenuDB.setYearSelection(observable.getValue().toString());

        schoolYearChoiceBox.getItems().clear();
        blockChoiceBox.getItems().clear();
        courseChoiceBox.getItems().clear();
        typeChoiceBox.getItems().clear();
        attemptChoiceBox.getItems().clear();

        disable(false, false, true, true, true, true, true);
    }

    public void schoolYearBoxEvent(ObservableValue observable) {
        choiceMenuDB.setSchoolYearSeleciton(observable.getValue().toString());

        blockChoiceBox.getItems().clear();
        courseChoiceBox.getItems().clear();
        typeChoiceBox.getItems().clear();
        attemptChoiceBox.getItems().clear();

        disable(false, false, false, true, true ,true, true);
    }

    public void blockBoxEvent(ObservableValue observable) {
        choiceMenuDB.setBlockSelection(observable.getValue().toString());

        courseChoiceBox.getItems().clear();
        typeChoiceBox.getItems().clear();
        attemptChoiceBox.getItems().clear();

        disable(false, false, false, false, true, true, true);
    }

    public void courseBoxEvent(ObservableValue observable) {
        choiceMenuDB.setCourseSelection(observable.getValue().toString());

        typeChoiceBox.getItems().clear();
        attemptChoiceBox.getItems().clear();

        disable(false, false, false, false, false, true, true);
    }

    public void typeBoxEvent(ObservableValue observable) {
        choiceMenuDB.setTypeSelection(observable.getValue().toString());

        attemptChoiceBox.getItems().clear();

        disable(false, false, false, false, false, false, true);
    }

    public void attemptBoxEvent(ObservableValue observable) {
        choiceMenuDB.setAttemptSelection(observable.getValue().toString());

        disable(false, false, false, false, false, false, false);
    }

    public VBox getChoiceMenuBox() {
        VBox fillBox = new VBox();
        VBox.setVgrow(fillBox, Priority.ALWAYS);

        yearChoiceBox.setDisable(false);

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
