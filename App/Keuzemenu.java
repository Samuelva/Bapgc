package sample;

import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by Samuel on 4-12-2016.
 * Deze klasse maakt het keuzemenu gedeelte aan en kan de inhoud van de keuzeknoppen
 * aanpassen.
 */
public class Keuzemenu {
    // Pane met de selectie menu's en de 2 selecteer en reset knoppen
    private BorderPane choiceMenu;
    // Box met de selectie menu's
    private VBox choiceMenuSelectionBox;
    // Box met de 2 selecteer en reset knoppen
    private HBox choiceMenuButtonBox;
    private ComboBox year;
    private ComboBox studyYear;
    private ComboBox period;
    private ComboBox module;
    private ComboBox type;
    private ComboBox chance;

    // Selecteer en resetknoppen
    Button allButton;
    Button resetButton;

    public Keuzemenu() {
        setComboBox();
        setChoiceMenuButtonBox();
    }

    // Maakt de selectie menu's aan met een aantal standaard waardes.
    private void setComboBox() {
        choiceMenuSelectionBox = new VBox();
        choiceMenuSelectionBox.setSpacing(20);
        choiceMenuSelectionBox.setMinWidth(150);

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

    // Maakt de keuzeknoppen aan met opgegeven weergeef waarde
    private ComboBox makeComboBox(String promptText) {
        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText(promptText);
        comboBox.setPrefWidth(150);
        comboBox.setPrefHeight(30);
        return comboBox;
    }

    // Maakt de selecteer en resetknoppen aan.
    private void setChoiceMenuButtonBox() {
        choiceMenuButtonBox = new HBox();
        choiceMenuButtonBox.setMinWidth(150);

        allButton = new Button("Alles");
        resetButton = new Button("Reset");
        allButton.setPrefWidth(75);
        allButton.setPrefHeight(30);
        resetButton.setPrefWidth(75);
        resetButton.setPrefHeight(30);
        choiceMenuButtonBox.getChildren().addAll(allButton, resetButton);
    }

    // Returned het keuzemenu voor de toetstab
    public BorderPane getTestMenu() {
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, period, module, type, chance);
        makeChoiceMenu();

        return choiceMenu;
    }

    // Returned het keuzemenu voor de moduletab
    public BorderPane getModuleMenu() {
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, period, choiceMenuButtonBox);
        makeChoiceMenu();

        return choiceMenu;
    }

    // Returned het keuzemenu voor de periodetab
    public BorderPane getPeriodMenu() {
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, choiceMenuButtonBox);
        makeChoiceMenu();

        return choiceMenu;
    }

    // Voegt de selectie menu's en selectie en reset knoppen samen en past wat styling toe
    private void makeChoiceMenu() {
        choiceMenu = new BorderPane();
        choiceMenu.setTop(choiceMenuSelectionBox);
        choiceMenu.setBottom(choiceMenuButtonBox);
        choiceMenu.setPadding(new Insets(5, 10, 5, 5));
        choiceMenu.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, null, null)));
    }

    // Zet de inhoud van het "Jaar" boxje. Input is een of meerdere strings.
    public void setYearContent(String... values) {
        for (String value : values) {
            year.getItems().add(value);
        }
    }

    // Zet de inhoud van het "Module" boxje. Input is een of meerdere strings.
    public void setModuleContent(String... values) {
        for (String value : values) {
            module.getItems().add(value);
        }
    }

    // Zet de inhoud van het "Gelegenheid" boxje. Input is een of meerdere strings.
    public void setChanceContent(String... values) {
        for (String value : values) {
            chance.getItems().add(value);
        }
    }

}
