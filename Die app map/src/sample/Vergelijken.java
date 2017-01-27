package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
    public VergelijkKeuzemenu testChoiceMenu;
    public VergelijkKeuzemenu moduleChoiceMenu;
    public VergelijkKeuzemenu periodChoiceMenu;

    // Instanties voor de statistieken voor elke tab
    public VergelijkStatistieken testStatistics;
    public VergelijkStatistieken moduleStatistics;
    public VergelijkStatistieken periodStatistics;


    public Vergelijken(){
        /**
         * Hoofdfunctie.
         * Roept de functies aan om het tabmenu, keuzemenu en statistieken
         * gedeelte aan te maken.
         */
        createTabs(); // Initieerd de tabs
        createChoiceMenus(); // Maakt instanties voor keuzemenu
        createStatistics(); // Maakt instanties voor statistieken
        fillTabs(); // Maakt boxjes voor elke tab en stopt deze in de tabs

        this.getChildren().add(tabPane);

        // Voeg zo inhoud toe aan de dropdown menu's
        // gaat later weg
        testChoiceMenu.setYearContent("2016", "2017", "2018");
//        testChoiceMenu.setModuleContent("Bapgc", "Bacf");
        moduleChoiceMenu.setYearContent("2016", "207");

        // Voeg zo items toe aan het menu in het keuzemenu
        // gaat later weg
        testChoiceMenu.setSelectionMenuItems("Binp 2016 opdracht 1", "Binp 2016 opdracht 2", "Binp 2015 opdracht 1", "Binp 2015 odpracht 2", "Binp 2014 opdracht 1", "Binp 2014 opdracht 2", "Binp 2013 opdracht 1", "Binp 2013 opdracht 2");
        moduleChoiceMenu.setSelectionMenuItems("Binp 2016", "Binp 2015", "Binp 2014", "Binp 2013", "Binp 2012");
        periodChoiceMenu.setSelectionMenuItems("1", "2", "3", "4");

        // Voeg zo statistieken toe
        // Gaat later weg
        testStatistics.setTestTableContent(FXCollections.observableArrayList(
                new TestRow("Binp 2018 Opdracht 1", 7, 6, 4, 9, 2),
                new TestRow("Binp 2017 Opdracht 1", 3, 9, 4, 7, 4),
                new TestRow("Binp 2016 Opdracht 1", 7, 6, 7, 6, 3),
                new TestRow("Binp 2015 Opdracht 1", 5, 4, 0, 4, 6),
                new TestRow("Binp 2014 Opdracht 1", 4, 3, 5, 9, 8),
                new TestRow("Binp 2013 Opdracht 1", 6, 8, 7, 7, 5),
                new TestRow("Binp 2012 Opdracht 1", 8, 5, 8, 6, 4),
                new TestRow("Binp 2011 Opdracht 1", 4, 8, 9, 5, 6)
        ));

        moduleStatistics.setModuleTableContent(FXCollections.observableArrayList(
                new Row("Binp 2018", 6, 4, 9, 2),
                new Row("Binp 2017", 9, 4, 7, 4),
                new Row("Binp 2016", 6, 7, 6, 3),
                new Row("Binp 2015", 4, 0, 4, 6),
                new Row("Binp 2014", 3, 5, 9, 8),
                new Row("Binp 2013", 8, 7, 7, 5),
                new Row("Binp 2012", 5, 8, 6, 4),
                new Row("Binp 2011", 8, 9, 5, 6)
        ));

        periodStatistics.setPeriodTableContent(FXCollections.observableArrayList(
                new Row("1", 6, 4, 9, 2),
                new Row("2", 9, 4, 7, 4),
                new Row("3", 6, 7, 6, 3),
                new Row("4", 4, 0, 4, 6),
                new Row("5", 3, 5, 9, 8)
        ));
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
        testChoiceMenu = new VergelijkKeuzemenu();
        moduleChoiceMenu = new VergelijkKeuzemenu();
        periodChoiceMenu = new VergelijkKeuzemenu();
    }

    private void createStatistics() {
        /**
         * Initieerd instanties voor het statistiek gedeelte
         */
        testStatistics = new VergelijkStatistieken(1);
        moduleStatistics = new VergelijkStatistieken(2);
        periodStatistics = new VergelijkStatistieken(3);
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
}