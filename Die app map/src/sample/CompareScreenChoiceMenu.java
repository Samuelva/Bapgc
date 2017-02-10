package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import database.DatabaseConn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Samuel on 4-12-2016.
 * Deze klasse maakt het keuzemenu gedeelte aan en kan de inhoud van de keuzeknoppen
 * aanpassen.
 */
public class CompareScreenChoiceMenu {
    private VBox choiceMenu;
    private VBox choiceMenuSelectionBox;
    private HBox buttonBox;

    private Label choiceMenuLbl;
    private ComboBox year;
    private ComboBox studyYear;
    private ComboBox block;
    private ComboBox course;
    private ComboBox type;
    private ComboBox attempt;
    public ListView<String> selectionMenu;

    public Button allButton;
    public Button resetButton;

    private String yearSelection;
    private String schoolYearSelection;
    private String blockSelection;
    private String courseSelection;
    private String typeSelection;
    private String attemptSelection;

    private int instance;
    private DatabaseConn d;

    public CompareScreenChoiceMenu(int instanceI) {
        /**
         * Hoofd functies roept subfuncties aan om keuzemenu onderdelen te maken.
         */
        d = new DatabaseConn();
        instance = instanceI;
        createComboBoxes();
        createSelectionMenu();
        createButtons();
    }

    private void createComboBoxes() {
        /**
         * Creëert het keuzemenu gedeelte met de comboBoxen.
         */
        setSelections();
        choiceMenuSelectionBox = new VBox();
        choiceMenuSelectionBox.setSpacing(20);
        choiceMenuSelectionBox.setMinWidth(150);

        year = new ComboBox();
        studyYear = new ComboBox();
        block = new ComboBox();
        course = new ComboBox();
        type = new ComboBox();
        attempt = new ComboBox();

        setButtons(year, "Jaar");
        setButtons(studyYear, "Leerjaar");
        setButtons(block, "Periode");
        setButtons(course, "Module");
        setButtons(type, "Toetsvorm");
        setButtons(attempt, "Gelegenheid");
    }

    private void setSelections() {
        /**
         * Reset de keuzemenu selectie waarden naar %, wat 'alles' voor een
         * query betekent.
         */
        yearSelection = new String("%");
        schoolYearSelection = new String("%");
        blockSelection = new String("%");
        courseSelection = new String("%");
        typeSelection = new String("%");
        attemptSelection = new String("%");
    }

    private void setButtons(ComboBox choiceBox, String promptText) {
        /**
         * Maakt een combobox en returned deze met de opgegeven weergeef
         * waarde. De event voor op het klikken van de combobox en het
         * selecteren van een combobox worden hier ook geregeld.
         */
        choiceBox.setPromptText(promptText);
        choiceBox.setPrefWidth(150);
        choiceBox.setMinHeight(30);
        choiceBox.setOnMousePressed(event -> {
            boxClickEvent(choiceBox);
        });
        choiceBox.valueProperty().addListener((observable, oldValue,
                                               newValue) -> {
            if (observable.getValue() != null) {
                boxSelectedEvent(observable, choiceBox);
            }
        });
    }

    private void boxClickEvent(ComboBox choiceBox) {
        /**
         * Regelt de event voor als er op een combobox wordt geklikt. De
         * selectie waarden van de andere comboboxen (indien geselecteerd)
         * worden in een lijst gestopt en meegegeven aan de functie welke
         * comboboxen vult en leegmaakt afhankelijk van de geselecteerde
         * combobox.
         */
        choiceBox.getItems().clear();
        clearSelectionOnPress(choiceBox);
        List<String> selection = new ArrayList<>();
        selection.addAll(Arrays.asList(yearSelection, schoolYearSelection,
                blockSelection, courseSelection, typeSelection,
                attemptSelection));

        boxClickEventCase(choiceBox, selection);
    }

    private void boxClickEventCase(ComboBox choiceBox, List<String> selection) {
        /**
         * Event handler voor als er op een combobox wordt gedrukt.
         * Afhankelijk van op welke combobox er gedrukt wordt, worden alle
         * boxjes boven deze combobox gereset. De combobox waar zelf op
         * gedrukt wordt, wordt gelijk gevuld met data waaruit gekozen kan
         * worden. De comboboxen werken zo dat de inhoud die door hun getoond
         * wordt, ook gefilterd wordt afhankelijk van de selectie in andere
         * comboboxen. Dus als er in de periode combobox, periode 1 wordt
         * geselecteerd, en dan op de module combobox wordt gedrukt, toont
         * deze alleen de modules uit periode 1.
         */
        if (choiceBox.getPromptText() == "Jaar") {
            choiceBox.getItems().addAll(d.getItems("Jaar", selection));
            clearComboBoxes(studyYear, block, course, type, attempt);
        } else if (choiceBox.getPromptText() == "Leerjaar") {
            choiceBox.getItems().addAll(d.getItems("Schooljaar", selection));
            clearComboBoxes(block, course, type, attempt);
        } else if (choiceBox.getPromptText() == "Periode") {
            choiceBox.getItems().addAll(d.getItems("Periode", selection));
            clearComboBoxes(course, type, attempt);
        } else if (choiceBox.getPromptText() == "Module") {
            choiceBox.getItems().addAll(d.getItems("ModuleCode", selection));
            clearComboBoxes(type, attempt);
        } else if (choiceBox.getPromptText() == "Toetsvorm") {
            choiceBox.getItems().addAll(d.getItems("Toetsvorm", selection));
            clearComboBoxes(attempt);
        } else if (choiceBox.getPromptText() == "Gelegenheid") {
            choiceBox.getItems().addAll(d.getItems("Gelegenheid", selection));
        }
    }

    private void boxSelectedEvent(ObservableValue observable, ComboBox
            choiceBox) {
        /**
         * Event handles voor als er op een waarde in de combobox is gedrukt.
         * Bij selectie, wordt de waarde opgeslagen in de juiste variabele.
         * Het selectiemenu wordt geupdate afhankelijk van de geselecteerde
         * waarden.
         */
        clearSelectionOnClick(choiceBox);
        if (choiceBox.getPromptText() == "Jaar") {
            yearSelection = (String) observable.getValue();
        } else if (choiceBox.getPromptText() == "Leerjaar") {
            schoolYearSelection = (String) observable.getValue();
        } else if (choiceBox.getPromptText() == "Periode") {
            blockSelection = (String) observable.getValue();
        } else if (choiceBox.getPromptText() == "Module") {
            courseSelection = (String) observable.getValue();
        } else if (choiceBox.getPromptText() == "Toetsvorm") {
            typeSelection = (String) observable.getValue();
        } else if (choiceBox.getPromptText() == "Gelegenheid") {
            attemptSelection = (String) observable.getValue();
        }
        updateSelectionMenu();
    }

    private void clearComboBoxes(ComboBox... comboBoxes) {
        /**
         * Maakt de inhoud van de opgegeven comboboxen leeg.
         */
        for (ComboBox i: comboBoxes) {
            i.getItems().clear();
        }
    }

    private void clearSelectionOnPress(ComboBox choiceBox) {
        /**
         * Reset de selectie variabelen afhankelijk van op welke combobox er
         * gedrukt wordt. De waarden voor de comboboxen boven de ingedrukte
         * combobox worden en van de ingedrukte combobox zelf worden gereset.
         */
        if (choiceBox.getPromptText() == "Jaar") {
            yearSelection = "%";
            schoolYearSelection = "%";
            blockSelection = "%";
            courseSelection = "%";
            typeSelection = "%";
            attemptSelection = "%";
        } else if (choiceBox.getPromptText() == "Leerjaar") {
            schoolYearSelection = "%";
            blockSelection = "%";
            courseSelection = "%";
            typeSelection = "%";
            attemptSelection = "%";
        } else if (choiceBox.getPromptText() == "Periode") {
            blockSelection = "%";
            courseSelection = "%";
            typeSelection = "%";
            attemptSelection = "%";
        }
        clearSelectionOnPress2(choiceBox);
    }

    private void clearSelectionOnPress2(ComboBox choiceBox) {
        /**
         * Verlenging van de clearSelectionOnPress() functie.
         */
        if (choiceBox.getPromptText() == "Module") {
            courseSelection = "%";
            typeSelection = "%";
            attemptSelection = "%";
        } else if (choiceBox.getPromptText() == "Toetsvorm") {
            typeSelection = "%";
            attemptSelection = "%";
        } else if (choiceBox.getPromptText() == "Gelegenheid") {
            attemptSelection = "%";
        }
    }

    private void clearSelectionOnClick(ComboBox choiceBox) {
        /**
         * Reset de selectie variabelen afhankelijk van op welke combobox er
         * gedrukt wordt. De waarden voor de comboboxen boven de ingedrukte
         * combobox worden gereset. Verschilt van de vorige functie doordat
         * hier de selectie waarden van de geselecteerde combobox niet
         * gereset wordt.
         */
        if (choiceBox.getPromptText() == "Jaar") {
            schoolYearSelection = "%";
            blockSelection = "%";
            courseSelection = "%";
            typeSelection = "%";
            attemptSelection = "%";
        } else if (choiceBox.getPromptText() == "Leerjaar") {
            blockSelection = "%";
            courseSelection = "%";
            typeSelection = "%";
            attemptSelection = "%";
        } else if (choiceBox.getPromptText() == "Periode") {
            courseSelection = "%";
            typeSelection = "%";
            attemptSelection = "%";
        } else if (choiceBox.getPromptText() == "Module") {
            typeSelection = "%";
            attemptSelection = "%";
        } else if (choiceBox.getPromptText() == "Toetsvorm") {
            attemptSelection = "%";
        }
    }

    public void updateSelectionMenu() {
        /**
         * Afhankelijk voor welke tab het keuzemenu gemakt is (toets, module
         * of periode) wordt het selectiemenu geupdate met de juiste
         * geselecteerde combobox waarden als filter. Elke tab gebruikt
         * andere waarden en halen data op hun eigen manier op.
         */
        selectionMenu.getItems().clear();
        if (instance == 1) {
            selectionMenu.getItems().addAll(d.filterTest(yearSelection,
                    schoolYearSelection, blockSelection, courseSelection,
                    typeSelection, attemptSelection));
        } else if (instance == 2) {
            selectionMenu.getItems().addAll(d.filterCourse(yearSelection,
                    schoolYearSelection, blockSelection));
        } else if (instance == 3) {
            selectionMenu.getItems().addAll(d.filterBlock(yearSelection,
                    schoolYearSelection));
        }
    }

    private void createButtons() {
        /**
         * Creëert het keuzemenu gedeelte met de "Alles" en "Reset" knoppen.
         */
        allButton = new Button("Alles");
        resetButton = new Button("Reset");

        allButton.setPrefWidth(75);
        allButton.setMinHeight(30);

        resetButton.setPrefWidth(75);
        resetButton.setMinHeight(30);
        resetButton.setOnAction(event -> {
            clearSelections();
            setSelections();
            updateSelectionMenu();
        });
    }

    private void clearSelections() {
        /**
         * Reset de geselecteerde waarden van de comboboxen, wordt
         * aangeroepen als er op de resetknop gedrukt wordt.
         */
        year.getSelectionModel().clearSelection();
        studyYear.getSelectionModel().clearSelection();
        block.getSelectionModel().clearSelection();
        course.getSelectionModel().clearSelection();
        type.getSelectionModel().clearSelection();
        attempt.getSelectionModel().clearSelection();
    }

    private void createSelectionMenu() {
        /**
         * Maakt het selectiemenu en vult deze gelijk met data, indien
         * beschikbaar.
         */
        selectionMenu = new ListView<>();
        selectionMenu.getSelectionModel().setSelectionMode(SelectionMode
                .MULTIPLE);
        if (instance == 1) {
            selectionMenu.setItems(FXCollections.observableArrayList(d
                    .filterTest("%", "%", "%", "%", "%", "%")));
        } else if (instance == 2) {
            selectionMenu.setItems(FXCollections.observableArrayList(d
                    .filterCourse("%", "%", "%")));
        } else if (instance == 3) {
            selectionMenu.setItems(FXCollections.observableArrayList(d
                    .filterBlock("%", "%")));
        }
        VBox.setVgrow(selectionMenu, Priority.ALWAYS);
    }

    private HBox buttonBox() {
        /**
         * Returned een box met de alles en reset knop.
         */
        buttonBox = new HBox();
        buttonBox.getChildren().addAll(allButton, resetButton);
        buttonBox.setSpacing(5);
        return buttonBox;
    }

    public VBox getTestChoiceMenu() {
        /**
         * Returned het volledige keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de toetstab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, block,
                course, type, attempt);
        createChoiceMenu();

        return choiceMenu;
    }

    public VBox getCourseChoiceMenu() {
        /**
         * Returned het volledige keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de moduletab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, block);
        createChoiceMenu();

        return choiceMenu;
    }

    public VBox getBlockChoiceMenu() {
        /**
         * Returned het volledige keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de periodeTab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear);
        createChoiceMenu();

        return choiceMenu;
    }

    private void createChoiceMenu() {
        /**
         * Stopt het comboBox gedeelte en knoppen gedeelte samen in één box
         */
        choiceMenuLbl = new Label("Keuzemenu");
        choiceMenuLbl.setFont(new Font("Arial", 18));

        choiceMenu = new VBox();
        choiceMenu.getChildren().add(choiceMenuLbl);
        choiceMenu.getChildren().add(choiceMenuSelectionBox);
        choiceMenu.getChildren().add(selectionMenu);
        choiceMenu.getChildren().add(buttonBox());
        choiceMenu.setMaxWidth(150);
        choiceMenu.setSpacing(20);
        choiceMenu.setAlignment(Pos.TOP_CENTER);
        choiceMenu.setPadding(new Insets(0, 20, 0, 0));
    }
}
