package sample;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.*;


/**
 * Created by Samuel on 4-12-2016.
 */
public class Keuzemenu {
    private BorderPane choiceMenu;
    private VBox choiceMenuBox;
    private HBox choiceMenuButtonBox;
    private ComboBox year;
    private ComboBox studyYear;
    private ComboBox period;
    private ComboBox module;
    private ComboBox type;
    private ComboBox chance;

    Button allButton;
    Button resetButton;

    public Keuzemenu() {
        setComboBox();
        setChoiceMenuButtonBox();
    }

    private void setComboBox() {
        choiceMenuBox = new VBox();
        choiceMenuBox.setSpacing(20);
//        choiceMenuBox.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, null, null)));

        year = makeComboBox("Jaar");
        studyYear = makeComboBox("Studiejaar");
        period = makeComboBox("Periode");
        module = makeComboBox("Modules");
        type = makeComboBox("Toetsvorm");
        chance = makeComboBox("Gelegenheid");

        studyYear.getItems().addAll("Leerjaar 1", "Leerjaar 2", "Leerjaar 3", "Leerjaar 4");
        period.getItems().addAll("Periode 1", "Periode 2", "Periode 3", "Periode 4", "Periode 5");
        type.getItems().addAll("Theorietoets", "Praktijktoets", "Opdracht", "Aanwezigheid",
                "Logboek", "Project");
    }

    private void setChoiceMenuButtonBox() {
        choiceMenuButtonBox = new HBox();

        allButton = new Button("Alles");
        resetButton = new Button("Reset");
        allButton.setPrefWidth(75);
        resetButton.setPrefWidth(75);
        choiceMenuButtonBox.getChildren().addAll(allButton, resetButton);
    }

    private ComboBox makeComboBox(String promptText) {
        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText(promptText);
        comboBox.setPrefWidth(150);
        comboBox.setPrefHeight(30);
        return comboBox;
    }

    public BorderPane getTestMenu() {
        choiceMenu = new BorderPane();
        choiceMenuBox.getChildren().addAll(year, studyYear, period, module, type, chance);
        choiceMenu.setTop(choiceMenuBox);
        choiceMenu.setBottom(choiceMenuButtonBox);
        return choiceMenu;
    }

    public BorderPane getModuleMenu() {
        choiceMenu = new BorderPane();
        choiceMenuBox.getChildren().addAll(year, studyYear, period, choiceMenuButtonBox);
        choiceMenu.setTop(choiceMenuBox);
        choiceMenu.setBottom(choiceMenuButtonBox);
        return choiceMenu;
    }

    public BorderPane getPeriodMenu() {
        choiceMenu = new BorderPane();
        choiceMenuBox.getChildren().addAll(year, studyYear, choiceMenuButtonBox);
        choiceMenu.setTop(choiceMenuBox);
        choiceMenu.setBottom(choiceMenuButtonBox);
        return choiceMenu;
    }

    public void setYearContent(String... values) {
        for (String value : values) {
            year.getItems().add(value);
        }
    }

    public void setModuleContent(String... values) {
        for (String value : values) {
            module.getItems().add(value);
        }
    }

    public void setChanceContent(String... values) {
        for (String value : values) {
            chance.getItems().add(value);
        }
    }

}
