package sample;

import javafx.scene.control.*;
import javafx.scene.layout.*;


public class Vergelijken extends Pane {
    TabPane tabPane;
    Tab testTab;
    Tab moduleTab;
    Tab periodTab;

    HBox testTabBox = new HBox();
    HBox moduleTabBox = new HBox();
    HBox periodTabBox = new HBox();


    public Vergelijken(){
        createTabs();

        Keuzemenu testChoiceMenu = new Keuzemenu();
        Keuzemenu moduleChoiceMenu = new Keuzemenu();
        Keuzemenu periodChoiceMenu = new Keuzemenu();

        BorderPane toetsMenuBox = testChoiceMenu.getTestMenu();
        BorderPane moduleMenuBox = moduleChoiceMenu.getModuleMenu();
        BorderPane periodMenuBox = periodChoiceMenu.getPeriodMenu();
        toetsMenuBox.setMinHeight(400);

        Statistiek testStatistics = new Statistiek();
        Statistiek moduleStatistics = new Statistiek();
        Statistiek periodStatistics = new Statistiek();

        testTabBox.getChildren().addAll(toetsMenuBox, testStatistics.returnStatisticsBox());
        moduleTabBox.getChildren().addAll(moduleMenuBox, moduleStatistics.returnStatisticsBox());
        periodTabBox.getChildren().addAll(periodMenuBox, periodStatistics.returnStatisticsBox());

        testTab.setContent(testTabBox);
        moduleTab.setContent(moduleTabBox);
        periodTab.setContent(periodTabBox);

        this.getChildren().add(tabPane);

        testStatistics.addStatistics(new int[] {40, 50, 50, 30});
        testStatistics.addStatistics(new int[] {30, 20, 60, 70});
        testStatistics.addStatistics(new int[] {50, 80, 60, 40});

        moduleStatistics.addStatistics(new int[] {11, 46, 23 ,77});
        moduleStatistics.addStatistics(new int[] {33, 6, 43, 6});
        moduleStatistics.addStatistics(new int[] {7, 23, 5, 77});

        periodStatistics.addStatistics(new int[] {33, 5, 72, 45});
        periodStatistics.addStatistics(new int[] {64, 45, 64, 44});
    }

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
        HBox.setHgrow(tabPane, Priority.ALWAYS);
    }

}