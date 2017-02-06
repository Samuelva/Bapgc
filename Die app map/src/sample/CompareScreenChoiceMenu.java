package sample;

import com.sun.org.apache.bcel.internal.generic.Select;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import database.DatabaseConn;

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
    public Button reloadButton;
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
         * Creërt het keuzemenu gedeelte met de comboBoxen.
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
        yearSelection = "%";
        schoolYearSelection = "%";
        blockSelection = "%";
        courseSelection = "%";
        typeSelection = "%";
        attemptSelection = "%";
    }

    private void setButtons(ComboBox choiceBox, String promptText) {
        /**
         * Maakt een combobox en returned deze met de opgegeven weergeef waarde.
         */
        choiceBox.setPromptText(promptText);
        choiceBox.setPrefWidth(150);
        choiceBox.setMinHeight(30);
        choiceBox.setOnMouseClicked(event -> {
            boxClickEvent(choiceBox);
        });
        choiceBox.valueProperty().addListener((observable, oldValue,
                                               newValue) -> {
            boxSelectedEvent(observable, choiceBox);
        });
    }

    private void boxClickEvent(ComboBox choiceBox) {
        choiceBox.getItems().clear();
        switch (choiceBox.getPromptText()) {
            case "Jaar":
                choiceBox.getItems().addAll(d.getItems("Jaar"));
                break;
            case "Leerjaar":
                choiceBox.getItems().addAll(d.getItems("Schooljaar"));
                break;
            case "Periode":
                choiceBox.getItems().addAll(d.getItems("Periode"));
                break;
            case "Module":
                choiceBox.getItems().addAll(d.getItems("ModuleCode"));
                break;
            case "Toetsvorm":
                choiceBox.getItems().addAll(d.getItems("Toetsvorm"));
                break;
            case "Gelegenheid":
                choiceBox.getItems().addAll(d.getItems("Gelegenheid"));
                break;
        }
    }

    private void boxSelectedEvent(ObservableValue observable, ComboBox
            choiceBox) {
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

    private void updateSelectionMenu() {
        System.out.println(yearSelection + " " + schoolYearSelection + " " +
                " " + blockSelection + " " + courseSelection);
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
         * Creërt het keuzemenu gedeelte met de "Alles" en "Reset" knoppen.
         */
        allButton = new Button("Alles");
        Image buttonImage = new Image(getClass().getResourceAsStream
                ("Reload.png"));
        reloadButton = new Button("", new ImageView(buttonImage));
        resetButton = new Button("Reset");

        allButton.setPrefWidth(75);
        allButton.setMinHeight(30);
        reloadButton.setMinWidth(30);
        reloadButton.setMinHeight(30);
        reloadButton.setOnAction(event -> {
            updateSelectionMenu();
            System.out.println(yearSelection + " " + schoolYearSelection + " " +
                    " " + blockSelection + " " + courseSelection);
        });
        resetButton.setPrefWidth(75);
        resetButton.setMinHeight(30);
        resetButton.setOnAction(event -> {
            clearSelections();
            setSelections();
            updateSelectionMenu();
        });
    }

    private void clearSelections() {
        year.getSelectionModel().clearSelection();
        studyYear.getSelectionModel().clearSelection();
        block.getSelectionModel().clearSelection();
        course.getSelectionModel().clearSelection();
        type.getSelectionModel().clearSelection();
        attempt.getSelectionModel().clearSelection();
    }

    private void createSelectionMenu() {
        selectionMenu = new ListView<>();
        selectionMenu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        buttonBox = new HBox();
        buttonBox.getChildren().addAll(allButton, reloadButton, resetButton);
        buttonBox.setSpacing(5);
        return buttonBox;
    }

    public VBox getTestChoiceMenu() {
        /**
         * Returned het volledge keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de toetstab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, block,
                course, type, attempt);
        createChoiceMenu();

        return choiceMenu;
    }

    public VBox getCourseChoiceMenu() {
        /**
         * Returned het volledge keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de moduletab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, block);
        createChoiceMenu();

        return choiceMenu;
    }

    public VBox getBlockChoiceMenu() {
        /**
         * Returned het volledge keuzemenu met de opgegeven comboBoxen.
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
