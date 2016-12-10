package sample;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Vergelijken extends StackPane {
    private TabPane tabPane;
    private Tab testTab;
    private Tab moduleTab;
    private Tab periodTab;

    private HBox testTabBox = new HBox();
    private HBox moduleTabBox = new HBox();
    private HBox periodTabBox = new HBox();

    public Keuzemenu testChoiceMenu;
    public Keuzemenu moduleChoiceMenu;
    public Keuzemenu periodChoiceMenu;

    public Statistiek testStatistics;
    public Statistiek moduleStatistics;
    public Statistiek periodStatistics;


    public Vergelijken(){
        createTabs();
        createChoiceMenus();
        createStatistics();

        testTabBox.getChildren().addAll(testChoiceMenu.getTestMenu(), testStatistics.returnStatisticsBox());
        moduleTabBox.getChildren().addAll(moduleChoiceMenu.getModuleMenu(), moduleStatistics.returnStatisticsBox());
        periodTabBox.getChildren().addAll(periodChoiceMenu.getPeriodMenu(), periodStatistics.returnStatisticsBox());

        testTab.setContent(testTabBox);
        moduleTab.setContent(moduleTabBox);
        periodTab.setContent(periodTabBox);


        this.getChildren().add(tabPane);

        // Voeg zo inhoud toe aan de selectie menu's
        testChoiceMenu.setYearContent("2016", "2017", "2018");
        testChoiceMenu.setModuleContent("Bapgc", "Bacf");
        moduleChoiceMenu.setYearContent("2016", "207");

        // Voeg zo statistieken toe
        testStatistics.addStatistics(new int[] {40, 50, 50, 30, 30});
        testStatistics.addStatistics(new int[] {30, 20, 60, 70, 24});
        testStatistics.addStatistics(new int[] {50, 80, 60, 40, 56});
        testStatistics.addStatistics(new int[] {40, 50, 50, 30, 70});
        testStatistics.addStatistics(new int[] {30, 20, 60, 70, 40});
        testStatistics.addStatistics(new int[] {50, 80, 60, 40, 20});
        testStatistics.addStatistics(new int[] {40, 50, 50, 30, 50});
        testStatistics.addStatistics(new int[] {30, 20, 60, 70, 10});
        testStatistics.addStatistics(new int[] {50, 80, 60, 40, 50});

        moduleStatistics.addStatistics(new int[] {11, 46, 23 ,77});
        moduleStatistics.addStatistics(new int[] {33, 6, 43, 6});
        moduleStatistics.addStatistics(new int[] {7, 23, 5, 77});
        moduleStatistics.addStatistics(new int[] {11, 46, 23 ,77});
        moduleStatistics.addStatistics(new int[] {33, 6, 43, 6});
        moduleStatistics.addStatistics(new int[] {7, 23, 5, 77});

        // Voeg zo een grafiek toe
        testStatistics.setGraph("https://i.imgur.com/8Sqgh3M.png");

    }

    // Maakt de tabs
    private void createTabs() {
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

        // Goedkope fix
        // Vergelijkscherm past zich niet aan aan de veticale hoogte van de applet voor
        // een of andere reden.
        tabPane.setPrefHeight(1080);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }

    private void createChoiceMenus() {
        testChoiceMenu = new Keuzemenu();
        moduleChoiceMenu = new Keuzemenu();
        periodChoiceMenu = new Keuzemenu();
    }

    private void createStatistics() {
        testStatistics = new Statistiek();
        moduleStatistics = new Statistiek();
        periodStatistics = new Statistiek();
    }

}