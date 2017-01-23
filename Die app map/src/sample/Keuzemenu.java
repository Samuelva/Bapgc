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
public class Keuzemenu {
    private VBox choiceMenu;
    private VBox choiceMenuSelectionBox;
    private HBox choiceMenuButtonBox;

    private Label choiceMenuLbl;
    private ComboBox year;
    private ComboBox studyYear;
    private ComboBox period;
    private ComboBox module;
    private ComboBox type;
    private ComboBox chance;
    private ListView<String> selectionMenu;
    private ObservableList<String> selectionMenuItems;

    private Button allButton;
    private Button resetButton;


    private static DatabaseConn databaseConn = new DatabaseConn();

    public Keuzemenu() {
        /**
         * Hoofd functies roept subfuncties aan om keuzemenu onderdelen te maken.
         */
        test();
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

        period.setOnAction(event -> {
            module.getItems().clear();
            String[] modules = new String[' '];
            System.out.println(period.getValue());

            if (period.getValue() == "Periode 1") {
                modules = databaseConn.GetModulecodesPerPeriode('1');
            }
            else if (period.getValue() == "Periode 2") {
                modules = databaseConn.GetModulecodesPerPeriode('2');
            }
            else if (period.getValue() == "Periode 3") {
                modules = databaseConn.GetModulecodesPerPeriode('3');
            }
            else if (period.getValue() == "Periode 4") {
                modules = databaseConn.GetModulecodesPerPeriode('4');
            }
            else if (period.getValue() == "Periode 5") {
                modules = databaseConn.GetModulecodesPerPeriode('5');
            }
            for (int i = 0; i < modules.length; i++) {
                module.getItems().addAll(modules[i]);
            }

        });

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

    private void setChoiceMenuButtonBox() {
        /**
         * Creërt het keuzemenu gedeelte met de "Alles" en "Reset" knoppen.
         */
        choiceMenuButtonBox = new HBox();

        allButton = new Button("Alles");
        resetButton = new Button("Reset");
        allButton.setPrefWidth(75);
        allButton.setMinHeight(30);
        resetButton.setPrefWidth(75);
        resetButton.setMinHeight(30);
        allButton.setOnAction(event -> {
            System.out.println("Alles knop");
        });
        resetButton.setOnAction(event -> {

        });
        choiceMenuButtonBox.setSpacing(5);
        choiceMenuButtonBox.getChildren().addAll(allButton, resetButton);
    }

    public VBox getTestMenu() {
        /**
         * Returned het volledge keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de toetstab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, period, module, type, chance);
        makeChoiceMenu();

        return choiceMenu;
    }

    public VBox getModuleMenu() {
        /**
         * Returned het volledge keuzemenu met de opgegeven comboBoxen.
         * Wordt gebruikt voor de moduletab.
         */
        choiceMenuSelectionBox.getChildren().addAll(year, studyYear, period, choiceMenuButtonBox);
        makeChoiceMenu();

        return choiceMenu;
    }

    public VBox getPeriodMenu() {
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
        choiceMenuLbl = new Label("Keuzemenu");
        choiceMenuLbl.setFont(new Font("Arial", 18));

        selectionMenu = new ListView<String>();
        selectionMenu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectionMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Event handler voor het selecteren van items in het menu.
                ObservableList<String> selection = selectionMenu.getSelectionModel().getSelectedItems();

                for (String s : selection) {
                    System.out.println(s);
                }
                System.out.println("\n");
            }
        });
        VBox.setVgrow(selectionMenu, Priority.ALWAYS);

        choiceMenu = new VBox();
        choiceMenu.getChildren().add(choiceMenuLbl);
        choiceMenu.getChildren().add(choiceMenuSelectionBox);
        choiceMenu.getChildren().add(selectionMenu);
        choiceMenu.getChildren().add(choiceMenuButtonBox);
        choiceMenu.setMaxWidth(150);
        choiceMenu.setSpacing(20);
        choiceMenu.setAlignment(Pos.TOP_CENTER);

        choiceMenu.setPadding(new Insets(0, 20, 0, 0));
//        choiceMenu.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, null, null)));
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

    public void setSelectionMenuItems(String... selection) {
        /**
         * Stel inhoud van het paneel in het keuzemenu in.
         * Gebruik: Instantie.setSelectionMenuItems("Toets1", "Toets2", "Toets3", ...);
         */
        selectionMenuItems = FXCollections.observableArrayList(selection);
        selectionMenu.setItems(selectionMenuItems);
    }

    private void test() {
        databaseConn.InputModule("Bapgc", "jemama", 8);
        databaseConn.InputModule("Bacf", "jepapa", 8);
        databaseConn.InputToets("2016","3","2","Bapgc","Praktijk","2",70, 30);
        //new Reader(Paths.get("src/brela_1e_1617.csv").toString(), d.GetToetsID("Bapgc", "2016", "3", "2", "1", "Toets"));
        databaseConn.InputVraag("2a", 10, 1, true);
        databaseConn.InputVraag("2b", 10, 1, true);
        databaseConn.InputStudent(1088948,"Tim", "Bin3b");
        databaseConn.InputStudent(1000000,"noname", "Bin3b");
        databaseConn.InputScore(1088948, 1, 10);
        databaseConn.InputScore(1088948, 2, 5);
        databaseConn.InputScore(1000000, 1, 10);
        databaseConn.InputScore(1000000, 2, 8);
    }

}
