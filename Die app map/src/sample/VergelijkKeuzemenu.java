package sample;

import com.sun.org.apache.bcel.internal.generic.Select;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import database.DatabaseConn;


/**
 * Created by Samuel on 4-12-2016.
 * Deze klasse maakt het keuzemenu gedeelte aan en kan de inhoud van de keuzeknoppen
 * aanpassen.
 */
public class VergelijkKeuzemenu {
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
    private ListView<String> selectionMenu;
    private ObservableList<String> selectionMenuItems;

    private Button allButton;
    private Button resetButton;


    public VergelijkKeuzemenu() {
        /**
         * Hoofd functies roept subfuncties aan om keuzemenu onderdelen te maken.
         */
        //test();
        createComboBoxes();
        createSelectionMenu();
        createButtons();
    }

    private void createComboBoxes() {
        /**
         * Creërt het keuzemenu gedeelte met de comboBoxen.
         */
        choiceMenuSelectionBox = new VBox();
        choiceMenuSelectionBox.setSpacing(20);
        choiceMenuSelectionBox.setMinWidth(150);

        year = makeComboBox("Jaar");
        studyYear = makeComboBox("Studiejaar");
        block = makeComboBox("Periode");
        course = makeComboBox("Modules");
        type = makeComboBox("Toetsvorm");
        attempt = makeComboBox("Gelegenheid");

    }

    private ComboBox makeComboBox(String promptText) {
        /**
         * Maakt een combobox en returned deze met de opgegeven weergeef waarde.
         */
        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText(promptText);
        comboBox.setPrefWidth(150);
        comboBox.setMinHeight(30);
        return comboBox;
    }

    private void createButtons() {
        /**
         * Creërt het keuzemenu gedeelte met de "Alles" en "Reset" knoppen.
         */
        allButton = new Button("Alles");
        resetButton = new Button("Reset");

        allButton.setPrefWidth(75);
        allButton.setMinHeight(30);
        resetButton.setPrefWidth(75);
        resetButton.setMinHeight(30);
        allButton.setOnAction(event -> {
            selectionMenu.getSelectionModel().selectAll();
        });
        resetButton.setOnAction(event -> {
            selectionMenu.getSelectionModel().clearSelection();
        });
    }

    private void createSelectionMenu() {
        selectionMenu = new ListView<>();
        selectionMenu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectionMenu.setOnMouseClicked(event -> {
            // Event handler voor het selecteren van items in het menu.
            ObservableList<String> selection = selectionMenu.getSelectionModel().getSelectedItems();

            for (String s : selection) {
                System.out.println(s);
            }
            System.out.println("\n");
        });
        VBox.setVgrow(selectionMenu, Priority.ALWAYS);
    }

    private HBox buttonBox() {
        buttonBox = new HBox();
        buttonBox.getChildren().addAll(allButton, resetButton);
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
            course.getItems().add(value);
        }
    }

    public void setChanceContent(String... values) {
        /**
         * Stel inhoud van de "Gelegenheid" comboBox in.
         * Gebruik: Instantie.setChangeContent("1e kans", "2e kans", "3e kans", ...);
         */
        for (String value : values) {
            attempt.getItems().add(value);
        }
    }

    public void setSelectionMenuItems(String... selection) {
        /**
         * Stel inhoud van het paneel in het keuzemenu in.
         * Gebruik: Instantie.setSelectionMenuItems("Toets1", "Toets2", "Toets3", ...);
         */
        selectionMenuItems = FXCollections.observableArrayList(selection);
        selectionMenu.setItems(selectionMenuItems);
    }
}
