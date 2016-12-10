package sample;

import javafx.scene.control.*;
import javafx.scene.layout.*;

import javax.tools.Tool;
import java.awt.*;
import java.applet.*;
import java.util.PrimitiveIterator;


public class Vergelijken extends StackPane {
    TabPane tabPane;
    Tab testTab;
    Tab moduleTab;
    Tab periodTab;

    HBox testTabBox = new HBox();
    HBox moduleTabBox = new HBox();
    HBox periodTabBox = new HBox();

    Keuzemenu testChoiceMenu;
    Keuzemenu moduleChoiceMenu;
    Keuzemenu periodChoiceMenu;

    Statistiek testStatistics;
    Statistiek moduleStatistics;
    Statistiek periodStatistics;


    public Vergelijken(){
        createTabs();
        createChoiceMenus();
        createStatistics();

        BorderPane test1 = new BorderPane();
        test1.setTop(testChoiceMenu.getTestMenu());
        test1.setBottom(testChoiceMenu.getAnderMenu());

        testTabBox.getChildren().addAll(test1, testStatistics.returnStatisticsBox());
        moduleTabBox.getChildren().addAll(moduleChoiceMenu.getModuleMenu(), moduleStatistics.returnStatisticsBox());
        periodTabBox.getChildren().addAll(periodChoiceMenu.getPeriodMenu(), periodStatistics.returnStatisticsBox());

        testTab.setContent(testTabBox);
        moduleTab.setContent(moduleTabBox);
        periodTab.setContent(periodTabBox);

//        VBox.setVgrow(test1, Priority.ALWAYS);
//        VBox.setVgrow(testTabBox, Priority.ALWAYS);
//        VBox.setVgrow(test2, Priority.ALWAYS);

//        testje.getChildren().add(tabPane);
//        HBox.setHgrow(testTabBox, Priority.ALWAYS);
//        testTabBox.setMaxWidth(Double.MAX_VALUE);
//        HBox.setHgrow(tabPane, Priority.ALWAYS);
//        tabPane.setMaxWidth(Double.MAX_VALUE);
//        HBox.setHgrow(testje, Priority.ALWAYS);
//        testje.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().add(tabPane);

        testStatistics.addStatistics(new int[] {40, 50, 50, 30});
        testStatistics.addStatistics(new int[] {30, 20, 60, 70});
        testStatistics.addStatistics(new int[] {50, 80, 60, 40});
//        testStatistics.addStatistics(new int[] {40, 50, 50, 30});
//        testStatistics.addStatistics(new int[] {30, 20, 60, 70});
//        testStatistics.addStatistics(new int[] {50, 80, 60, 40});
//        testStatistics.addStatistics(new int[] {40, 50, 50, 30});
//        testStatistics.addStatistics(new int[] {30, 20, 60, 70});
//        testStatistics.addStatistics(new int[] {50, 80, 60, 40});

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