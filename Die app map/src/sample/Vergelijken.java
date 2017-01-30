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
 * Klasse voor het vergelijkscherm.
 */
public class Vergelijken extends StackPane {

    private TabPane tabPane; // paneel met de tabs
    private Tab testTab; // Toets tab
    private Tab moduleTab; // Module tab
    private Tab periodTab; // PeriodeTab

    private HBox testTabBox; // Box met inhoud voor de toetstab
    private HBox moduleTabBox; // Box met inhoud voor de moduletab
    private HBox periodTabBox; // Box met inhoud voor periodetab

    // Instanties voor de keuzemenu's voor elke tab
    public VergelijkKeuzemenu testChoiceMenu;
    public VergelijkKeuzemenu moduleChoiceMenu;
    public VergelijkKeuzemenu periodChoiceMenu;

    // Instanties voor de statistieken voor elke tab
    public VergelijkStatistieken testStatistics;
    public VergelijkStatistieken moduleStatistics;
    public VergelijkStatistieken periodStatistics;

    private DatabaseConn d;

    public Vergelijken() {
        /**
         * Hoofdfunctie.
         * Roept de functies aan om het tabmenu, keuzemenu en statistieken
         * gedeelte aan te maken.
         */
        d = new DatabaseConn();
        createTabs(); // Initieerd de tabs
        createChoiceMenus(); // Maakt instanties voor keuzemenu
        createStatistics(); // Maakt instanties voor statistieken
        fillTabs(); // Maakt boxjes voor elke tab en stopt deze in de tabs
        selectionMenuEvent();
        choiceMenuAllButtonEvent();

        this.getChildren().add(tabPane);
    }

    private void selectionMenuEvent() {
        testChoiceMenu.selectionMenu.setOnMouseClicked(event -> {
            ObservableList<String> selection = testChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillTestTable(selection);
        });

        moduleChoiceMenu.selectionMenu.setOnMouseClicked(event -> {
            ObservableList<String> selection = moduleChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillModuleTable(selection);
        });

        periodChoiceMenu.selectionMenu.setOnMouseClicked(event -> {
            ObservableList<String> selection = periodChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillBlockTable(selection);
        });

    }

    private void choiceMenuAllButtonEvent() {
        testChoiceMenu.allButton.setOnAction(event -> {
            testChoiceMenu.selectionMenu.getSelectionModel().selectAll();
            ObservableList<String> selection = testChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillTestTable(selection);
        });
        moduleChoiceMenu.allButton.setOnAction(event -> {
            moduleChoiceMenu.selectionMenu.getSelectionModel().selectAll();
            ObservableList<String> selection = moduleChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillModuleTable(selection);
        });
        periodChoiceMenu.allButton.setOnAction(event -> {
            periodChoiceMenu.selectionMenu.getSelectionModel().selectAll();
            ObservableList<String> selection = periodChoiceMenu.selectionMenu
                    .getSelectionModel().getSelectedItems();
            fillBlockTable(selection);
        });
    }

    private void fillTestTable(ObservableList<String> selection) {
        Random r = new Random();
        testStatistics.clearTable();
        ObservableList<TestRow> data = FXCollections.observableArrayList();

        try {
            for (String s : selection) {
//                String[] parts = s.split(" ");
//                String course = parts[0];
//                String type = parts[1];
//                String attempt = parts[2];
//                String year = parts[3];
//
//                Integer testId = d.getTestIdTestTab(year, course, type, attempt);
//                Object[] testStatisticsObj = Statistics.examStats(testId);
//
//                data.add(new TestRow(s.toString(),
//                                (Double) testStatisticsObj[0],
//                                (int) testStatisticsObj[1],
//                                (int) testStatisticsObj[2],
//                                (int) testStatisticsObj[3],
//                                (Double) testStatisticsObj[4]));
                data.add(new TestRow(s.toString(), r.nextDouble(), r.nextInt
                        (), r.nextInt(), r.nextInt(), r.nextDouble()));
            }
            testStatistics.fillTable(data);
        } catch(Exception e) {
            displayWarning("toets");
        }
    }

    private void fillModuleTable(ObservableList<String> selection) {
        moduleStatistics.clearTable();
        ObservableList<Row> data = FXCollections.observableArrayList();

        try {
            for (String s : selection) {
                String courseId = s.split(" ")[0];
                Object[] courseStatisticsObj = Statistics.courseStats(courseId);

                data.add(new Row(s.toString(), (double) courseStatisticsObj[0],
                        (int) courseStatisticsObj[1], (int) courseStatisticsObj[2],
                        (int) courseStatisticsObj[3],
                        (double) courseStatisticsObj[4]));
            }
            moduleStatistics.fillTable(data);
        } catch (Exception e) {
            displayWarning("module");
        }
    }

    private void fillBlockTable(ObservableList<String> selection) {
        periodStatistics.clearTable();
        ObservableList<Row> data = FXCollections.observableArrayList();

        try {
            for (String s : selection) {
                String[] parts = s.split(" ");
                String year = parts[4];
                String schoolYear = parts[3];
                String block = parts[1];

                Object[] blockStatisticsObj = Statistics.periodStats(year,
                        schoolYear, block);

                data.add(new Row(s.toString(), (double) blockStatisticsObj[0],
                        (int) blockStatisticsObj[1], (int) blockStatisticsObj[2],
                        (int) blockStatisticsObj[3], (double)
                        blockStatisticsObj[4]));
            }
            periodStatistics.fillTable(data);
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
        moduleTab = new Tab();
        periodTab = new Tab();

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        testTab.setText("Toets");
        moduleTab.setText("Module");
        periodTab.setText("Periode");

        tabPane.getTabs().add(testTab);
        tabPane.getTabs().add(moduleTab);
        tabPane.getTabs().add(periodTab);
        tabPane.setTabMinWidth(100);

        // Goedkope fix
        // Vergelijkscherm past zich niet aan aan de
        // veticale hoogte van de applet voor een of andere reden.
        tabPane.setPrefHeight(1080);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }

    private void createChoiceMenus() {
        /**
         * Initieerd instanties voor het keuzemenu
         */
        testChoiceMenu = new VergelijkKeuzemenu(1);
        moduleChoiceMenu = new VergelijkKeuzemenu(2);
        periodChoiceMenu = new VergelijkKeuzemenu(3);
    }

    private void createStatistics() {
        /**
         * Initieerd instanties voor het statistiek gedeelte
         */
        testStatistics = new VergelijkStatistieken(1);
        testStatistics.setTestTableColumns();
        moduleStatistics = new VergelijkStatistieken(2);
        moduleStatistics.setModuleTableColumns();
        periodStatistics = new VergelijkStatistieken(3);
        periodStatistics.setPeriodTableColumns();
    }

    private void fillTabs() {
        /**
         * maakt boxjes voor de tabs aan, vult deze, en past het toe op de tabs
         */
        testTabBox = new HBox();
        moduleTabBox = new HBox();
        periodTabBox = new HBox();

        testTabBox.getChildren().addAll(testChoiceMenu.getTestChoiceMenu(),
                testStatistics.getCompareStatisticsBox());
        moduleTabBox.getChildren().addAll(moduleChoiceMenu
                        .getCourseChoiceMenu(), moduleStatistics
                .getCompareStatisticsBox());
        periodTabBox.getChildren().addAll(periodChoiceMenu.getBlockChoiceMenu(),
                periodStatistics.getCompareStatisticsBox());

        testTabBox.setPadding(new Insets(5, 5, 5, 5));
        moduleTabBox.setPadding(new Insets(5, 5, 5, 5));
        periodTabBox.setPadding(new Insets(5, 5, 5, 5));

        testTab.setContent(testTabBox);
        moduleTab.setContent(moduleTabBox);
        periodTab.setContent(periodTabBox);
    }

    private void displayWarning(String selected) {
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