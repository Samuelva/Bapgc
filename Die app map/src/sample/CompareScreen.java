package sample;

import database.DatabaseConn;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Random;

/**
 * Klasse dat het vergelijkscherm aanmaakt.
 */
public class CompareScreen extends StackPane {

    private TabPane tabPane; // paneel met de tabs
    private Tab testTab; // Toets tab
    private Tab courseTab; // Module tab
    private Tab blockTab; // PeriodeTab

    private HBox testTabBox; // Box met inhoud voor de toetstab
    private HBox courseTabBox; // Box met inhoud voor de moduletab
    private HBox blockTabBox; // Box met inhoud voor periodetab

    // Instanties voor de keuzemenu's voor elke tab
    public CompareScreenChoiceMenu testChoiceMenu;
    public CompareScreenChoiceMenu courseChoiceMenu;
    public CompareScreenChoiceMenu blockChoiceMenu;

    // Instanties voor de statistieken voor elke tab
    public CompareScreenStatistics testStatistics;
    public CompareScreenStatistics courseStatistics;
    public CompareScreenStatistics blockStatistics;

    private DatabaseConn d;

    public CompareScreen() {
        /**
         * Hoofdfunctie.
         * Roept de functies aan om het tabmenu, keuzemenu en statistieken
         * gedeelte aan te maken.
         */
        d = new DatabaseConn();
        createTabs(); // Initieert de tabs
        createChoiceMenus(); // Maakt instanties voor keuzemenu
        createStatistics(); // Maakt instanties voor statistieken
        fillTabs(); // Maakt boxjes voor elke tab en stopt deze in de tabs
        selectionMenuEvent();
        choiceMenuAllButtonEvent();

        this.getChildren().add(tabPane);
    }

    private void selectionMenuEvent() {
        /**
         * Event handler voor als er data geselecteerd wordt in het
         * selectiemenu. Selectie wordt toegekend aan een variabele en
         * doorgegeven aan de functie welke de tabel in elke tab vult.
         */
        testChoiceMenu.selectionMenu.setOnMouseClicked(event -> {
            ObservableList<String> selection = testChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillTestTable(selection);
        });

        courseChoiceMenu.selectionMenu.setOnMouseClicked(event -> {
            ObservableList<String> selection = courseChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillModuleTable(selection);
        });

        blockChoiceMenu.selectionMenu.setOnMouseClicked(event -> {
            ObservableList<String> selection = blockChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillBlockTable(selection);
        });

    }

    private void choiceMenuAllButtonEvent() {
        /**
         * Event voor de alles knop. Zorgt ervoor dat alles in het
         * selectiemenu geselecteerd wordt, deze waarden toegekend worden aan
         * een variabele, en de tabel in de drie tabjes gevuld worden met de
         * data voor de selectie.
         */
        testChoiceMenu.allButton.setOnAction(event -> {
            testChoiceMenu.selectionMenu.getSelectionModel().selectAll();
            ObservableList<String> selection = testChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillTestTable(selection);
        });
        courseChoiceMenu.allButton.setOnAction(event -> {
            courseChoiceMenu.selectionMenu.getSelectionModel().selectAll();
            ObservableList<String> selection = courseChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillModuleTable(selection);
        });
        blockChoiceMenu.allButton.setOnAction(event -> {
            blockChoiceMenu.selectionMenu.getSelectionModel().selectAll();
            ObservableList<String> selection = blockChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillBlockTable(selection);
        });
    }

    private void fillTestTable(ObservableList<String> selection) {
        testStatistics.clearTable();
        /**
         * Deze functie voegt data toe aan de tabel voor de toetstab.
         * Als er een of meerdere waarden worden geselecteerd in het
         * selectiemenu, worden de toetsstatistieken hiervoor opgehaald en
         * toegevoegd aan de tabel. Als er geen data beschikbaar is voor de
         * toets, wordt er een melding weergegeven.
         */
        ObservableList<Row> data = FXCollections.observableArrayList();

        try {
            for (String s: selection) {
                String[] parts = s.split(" ");
                String course = parts[0];
                String type = parts[1];
                String attempt = parts[2];
                String year = parts[3];

                String testId = d.getTestID(year, course, type, attempt);
                Object[] testStatisticsObj = Statistics.examStats
                        (Integer.parseInt(testId));
                data.add(new Row(s.toString(),
                                (Double) testStatisticsObj[0],
                                (int) testStatisticsObj[1],
                                (int) testStatisticsObj[2],
                                (int) testStatisticsObj[3],
                                (Double) testStatisticsObj[4]));
            }
            testStatistics.fillTable(data);
        } catch(Exception e) {
            displayWarning("toets");
        }
    }

    private void fillModuleTable(ObservableList<String> selection) {
        /**
         * Deze functie voegt data toe aan de tabel voor de moduletab.
         * Als er een of meerdere waarden worden geselecteerd in het
         * selectiemenu, worden de modulestatistieken hiervoor opgehaald en
         * toegevoegd aan de tabel. Als er geen data beschikbaar is voor de
         * module, wordt er een melding weergegeven.
         */
        courseStatistics.clearTable();
        ObservableList<Row> data = FXCollections.observableArrayList();

        try {
            for (String s: selection) {
                String courseId = s.split(" ")[0];
                Object[] courseStatisticsObj = Statistics.courseStats(courseId);

                data.add(new Row(s.toString(),
                        (double) courseStatisticsObj[0],
                        (int) courseStatisticsObj[1],
                        (int) courseStatisticsObj[2],
                        (int) courseStatisticsObj[3],
                        (double) courseStatisticsObj[4]));
            }
            courseStatistics.fillTable(data);
        } catch (Exception e) {
            displayWarning("module");
        }
    }

    private void fillBlockTable(ObservableList<String> selection) {
        blockStatistics.clearTable();
        /**
         * Deze functie voegt data toe aan de tabel voor de periodetab.
         * Als er een of meerdere waarden worden geselecteerd in het
         * selectiemenu, worden de periodestatistieken hiervoor opgehaald en
         * toegevoegd aan de tabel. Als er geen data beschikbaar is voor de
         * periode, wordt er een melding weergegeven.
         */
        ObservableList<Row> data = FXCollections.observableArrayList();

        try {
            for (String s: selection) {
                String[] parts = s.split(" ");
                String year = parts[4];
                String schoolYear = parts[3];
                String block = parts[1];

                Object[] blockStatisticsObj = Statistics.periodStats(year,
                        schoolYear, block);

                data.add(new Row(s.toString(), (double) blockStatisticsObj[0],
                        (int) blockStatisticsObj[1],
                        (int) blockStatisticsObj[2],
                        (int) blockStatisticsObj[3],
                        (double) blockStatisticsObj[4]));
            }
            blockStatistics.fillTable(data);
        } catch (Exception e) {
            displayWarning("Periode");
        }
    }

    private void createTabs() {
        /**
         * Maakt het tabmenu met tabs aan
         */
        tabPane = new TabPane();
        testTab = new Tab();
        courseTab = new Tab();
        blockTab = new Tab();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        testTab.setText("Toets");
        courseTab.setText("Module");
        blockTab.setText("Periode");

        tabPane.getTabs().add(testTab);
        tabPane.getTabs().add(courseTab);
        tabPane.getTabs().add(blockTab);
        tabPane.setTabMinWidth(100);

        // Goedkope fix
        // Vergelijkscherm past zich niet aan aan de
        // verticale hoogte van de applet om een of andere reden.
        tabPane.setPrefHeight(1080);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }

    private void createChoiceMenus() {
        /**
         * Initieert instanties voor het keuzemenu
         */
        testChoiceMenu = new CompareScreenChoiceMenu(1);
        courseChoiceMenu = new CompareScreenChoiceMenu(2);
        blockChoiceMenu = new CompareScreenChoiceMenu(3);
    }

    private void createStatistics() {
        /**
         * Initieert instanties voor het statistiek gedeelte
         */
        testStatistics = new CompareScreenStatistics(1);
        testStatistics.setTestTableColumns();
        courseStatistics = new CompareScreenStatistics(2);
        courseStatistics.setModuleTableColumns();
        blockStatistics = new CompareScreenStatistics(3);
        blockStatistics.setPeriodTableColumns();
    }

    private void fillTabs() {
        /**
         * Maakt boxjes voor de tabs aan, vult deze, en past het toe op de tabs
         */
        testTabBox = new HBox();
        courseTabBox = new HBox();
        blockTabBox = new HBox();

        testTabBox.getChildren().addAll(testChoiceMenu.getTestChoiceMenu(),
                testStatistics.getCompareStatisticsBox());
        courseTabBox.getChildren().addAll(courseChoiceMenu
                        .getCourseChoiceMenu(), courseStatistics
                .getCompareStatisticsBox());
        blockTabBox.getChildren().addAll(blockChoiceMenu.getBlockChoiceMenu(),
                blockStatistics.getCompareStatisticsBox());

        testTabBox.setPadding(new Insets(5, 5, 5, 5));
        courseTabBox.setPadding(new Insets(5, 5, 5, 5));
        blockTabBox.setPadding(new Insets(5, 5, 5, 5));

        testTab.setContent(testTabBox);
        courseTab.setContent(courseTabBox);
        blockTab.setContent(blockTabBox);
    }

    private void displayWarning(String selected) {
        /**
         * Functie dat een waarschuwing weergeeft voor elke tab als er geen
         * data beschikbaar is voor de geselecteerde toets in het selectiemenu.
         */
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Fout");
        alert.setHeaderText("De geselecteerde " + selected + " kan niet " +
                "worden ingeladen.");
        alert.setContentText("De geselecteerde " + selected + " bevat geen of" +
                " te weinig waarden. Selecteer een andere.");
        ButtonType confirm = new ButtonType("OK");
        alert.getButtonTypes().setAll(confirm);
        alert.show();
    }
}
