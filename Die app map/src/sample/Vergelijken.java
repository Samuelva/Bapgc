package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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
    public Keuzemenu testChoiceMenu;
    public Keuzemenu moduleChoiceMenu;
    public Keuzemenu periodChoiceMenu;

    // Instanties voor de statistieken voor elke tab
    public Statistiek testStatistics;
    public Statistiek moduleStatistics;
    public Statistiek periodStatistics;


    public Vergelijken(){
        /**
         * Hoofdfunctie.
         * Roept de functies aan om het tabmenu, keuzemenu en statistieken gedeelte aan te maken.
         */
        createTabs(); // Initieerd de tabs
        createChoiceMenus(); // Maakt instanties voor keuzemenu
        createStatistics(); // Maakt instanties voor statistieken
        fillTabs(); // Maakt boxjes voor elke tab en stopt deze in de tabs

        this.getChildren().add(tabPane);

        // Voeg zo inhoud toe aan de dropdown menu's
        testChoiceMenu.setYearContent("2016", "2017", "2018");
//        testChoiceMenu.setModuleContent("Bapgc", "Bacf");
        moduleChoiceMenu.setYearContent("2016", "207");

        // Voeg zo items toe aan het menu in het keuzemenu
        testChoiceMenu.setSelectionMenuItems("Binp 2016 opdracht 1", "Binp 2016 opdracht 2", "Binp 2015 opdracht 1", "Binp 2015 odpracht 2", "Binp 2014 opdracht 1", "Binp 2014 opdracht 2", "Binp 2013 opdracht 1", "Binp 2013 opdracht 2");
        moduleChoiceMenu.setSelectionMenuItems("Binp 2016", "Binp 2015", "Binp 2014", "Binp 2013", "Binp 2012");
        periodChoiceMenu.setSelectionMenuItems("1", "2", "3", "4");

        testStatistics.setTestTableContent(testStatistics.testData);
        moduleStatistics.setModuleTableContent(moduleStatistics.moduleData);
        periodStatistics.setPeriodTableContent(moduleStatistics.moduleData);

        // Voeg zo statistieken toe

        // Voeg zo een grafiek toe
        testGraphButtonEvent();
        moduleGraphButtonEvent();
        periodGraphButtonEvent();

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
        // Vergelijkscherm past zich niet aan aan de veticale hoogte van de applet voor
        // een of andere reden.
        tabPane.setPrefHeight(1080);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }

    private void createChoiceMenus() {
        /**
         * Initieerd instanties voor het keuzemenu
         */
        testChoiceMenu = new Keuzemenu();
        moduleChoiceMenu = new Keuzemenu();
        periodChoiceMenu = new Keuzemenu();
    }

    private void createStatistics() {
        /**
         * Initieerd instanties voor het statistiek gedeelte
         */
        testStatistics = new Statistiek(1);
        moduleStatistics = new Statistiek(2);
        periodStatistics = new Statistiek(3);
    }

    private void fillTabs() {
        /**
         * maakt boxjes voor de tabs aan, vult deze, en past het toe op de tabs
         */
        testTabBox = new HBox();
        moduleTabBox = new HBox();
        periodTabBox = new HBox();

        testTabBox.getChildren().addAll(testChoiceMenu.getTestMenu(), testStatistics.returnStatisticsBox());
        moduleTabBox.getChildren().addAll(moduleChoiceMenu.getModuleMenu(), moduleStatistics.returnStatisticsBox());
        periodTabBox.getChildren().addAll(periodChoiceMenu.getPeriodMenu(), periodStatistics.returnStatisticsBox());

        testTabBox.setPadding(new Insets(5, 5, 5, 5));
        moduleTabBox.setPadding(new Insets(5, 5, 5, 5));
        periodTabBox.setPadding(new Insets(5, 5, 5, 5));

        testTab.setContent(testTabBox);
        moduleTab.setContent(moduleTabBox);
        periodTab.setContent(periodTabBox);
    }

    public void testGraphButtonEvent() {
        testStatistics.graphButton.setOnAction(event -> {
            if (testStatistics.graphButton.getValue() == "Histogram") {
//                testStatistics.setBarChart();
                testStatistics.activeGraphInt = 1;
            } else if (testStatistics.graphButton.getValue() == "Lijngrafiek") {
//                testStatistics.setLineChart();
                testStatistics.activeGraphInt = 2;
            } else if (testStatistics.graphButton.getValue() == "Boxplot") {
//                testStatistics.setBoxPlot();
                testStatistics.activeGraphInt = 3;
            }
        });
    }

    public void moduleGraphButtonEvent() {
        moduleStatistics.graphButton.setOnAction(event -> {
            if (moduleStatistics.graphButton.getValue() == "Histogram") {
//                moduleStatistics.setBarChart();
                moduleStatistics.activeGraphInt = 1;
            } else if (moduleStatistics.graphButton.getValue() == "Lijngrafiek") {
//                moduleStatistics.setLineChart();
                moduleStatistics.activeGraphInt = 2;
            } else if (moduleStatistics.graphButton.getValue() == "Boxplot") {
//                moduleStatistics.setBoxPlot();
                moduleStatistics.activeGraphInt = 3;
            }
        });
    }

    public void periodGraphButtonEvent() {
        periodStatistics.graphButton.setOnAction(event -> {
            if (periodStatistics.graphButton.getValue() == "Histogram") {
//                periodStatistics.setBarChart();
                periodStatistics.activeGraphInt = 1;
            } else if (periodStatistics.graphButton.getValue() == "Lijngrafiek") {
//                periodStatistics.setLineChart();
                periodStatistics.activeGraphInt = 2;
            } else if (periodStatistics.graphButton.getValue() == "Boxplot") {
//                periodStatistics.setBoxPlot();
                periodStatistics.activeGraphInt = 3;
            }
        });
    }
}