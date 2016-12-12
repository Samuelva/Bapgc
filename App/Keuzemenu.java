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
    private BorderPane choiceMenu;
    private VBox choiceMenuSelectionBox;
    private HBox choiceMenuButtonBox;

    private ComboBox year;
    private ComboBox studyYear;
    private ComboBox period;
    private ComboBox module;
    private ComboBox type;
    private ComboBox chance;

    private Button allButton;
    private Button resetButton;

    public Keuzemenu() {
        /**
         * Hoofd functies roept subfuncties aan om keuzemenu onderdelen te maken.
         */
        setComboBox();
        setChoiceMenuButtonBox();
    }

    private void setComboBox() {
        /**
         * Creërt het keuzemenu gedeelte met de comboBoxen.
         */
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

    private ComboBox makeComboBox(String promptText) {
        /**
         * Maakt een combobox en returned deze met de opgegeven weergeef waarde.
         */
        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText(promptText);
        comboBox.setPrefWidth(150);
        comboBox.setPrefHeight(30);
        return comboBox;
    }

    private void setChoiceMenuButtonBox() {
        /**
         * Creërt het keuzemenu gedeelte met de "Alles" en "Reset" knoppen.
         */
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

    public BorderPane getTestMenu() {
        /**
         * Returned het volledge keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de toetstab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, period, module, type, chance);
        makeChoiceMenu();

        return choiceMenu;
    }

    public BorderPane getModuleMenu() {
        /**
         * Returned het volledge keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de moduletab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, period, choiceMenuButtonBox);
        makeChoiceMenu();

        return choiceMenu;
    }

    public BorderPane getPeriodMenu() {
        /**
         * Returned het volledge keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de periodeTab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, choiceMenuButtonBox);
        makeChoiceMenu();

        return choiceMenu;
    }

    private void makeChoiceMenu() {
        /**
         * Stopt het comboBox gedeelte en knoppen gedeelte samen in één box
         */
        choiceMenu = new BorderPane();
        choiceMenu.setTop(choiceMenuSelectionBox);
        choiceMenu.setBottom(choiceMenuButtonBox);
        choiceMenu.setPadding(new Insets(5, 10, 5, 5));
        choiceMenu.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, null, null)));
    }

    public void setYearContent(String... values) {
        /**
         * Stel inhoud van de "Jaar" comboBox in.
         * Gebruik: Instantie.setYearContent("2014", "2015", "2016", ...);
         */
        for (String value : values) {
            year.getItems().add(value);
        }
    }

    public void setModuleContent(String... values) {
        /**
         * Stel inhoud van de "Module" comboBox in.
         * Gebruik: Instantie.setModuleContent("Bapgc", "Bacf", "Bpsda", ...);
         */
        for (String value : values) {
            module.getItems().add(value);
        }
    }

    public void setChanceContent(String... values) {
        /**
         * Stel inhoud van de "Gelegenheid" comboBox in.
         * Gebruik: Instantie.setChangeContent("1e kans", "2e kans", "3e kans", ...);
         */
        for (String value : values) {
            chance.getItems().add(value);
        }
    }

}
