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
        setUpChangeListeners();

        this.getChildren().add(tabPane);

        // Voeg zo inhoud toe aan de dropdown menu's
        testChoiceMenu.setYearContent("2016", "2017", "2018");
        testChoiceMenu.setModuleContent("Bapgc", "Bacf");
        moduleChoiceMenu.setYearContent("2016", "207");

        // Voeg zo items toe aan het menu in het keuzemenu
        testChoiceMenu.setSelectionMenuItems("Binp 2016 opdracht 1", "Binp 2016 opdracht 2", "Binp 2015 opdracht 1", "Binp 2015 odpracht 2", "Binp 2014 opdracht 1", "Binp 2014 opdracht 2");
        moduleChoiceMenu.setSelectionMenuItems("Binp 2016", "Binp 2015", "Binp 2014", "Binp 2013", "Binp 2012");
        periodChoiceMenu.setSelectionMenuItems("1", "2", "3", "4");

        // Voeg zo statistieken toe
        testStatistics.addStatistics(new String[] {"Toets:", "Gemiddelde cijfer:", "Aantal Deelnemers:", "Aantal onvoldoendes:", "Aantal voldoendes:", "Rendement:"});
        testStatistics.addStatistics(new String[] {"Binp 2016 opdracht 1", "4.0", "5.0", "5.0", "3.0", "3.0"});
        testStatistics.addStatistics(new String[] {"Binp 2016 opdracht 2", "3.0", "2.0", "6.0", "7.0", "2.4"});
        testStatistics.addStatistics(new String[] {"Binp 2015 opdracht 1", "5.0", "8.0", "6.0", "4.0", "5.6"});
        testStatistics.addStatistics(new String[] {"Binp 2015 opdracht 2", "4.0", "5.0", "5.0", "3.0", "7.0"});
        testStatistics.addStatistics(new String[] {"Binp 2014 opdracht 1", "3.0", "2.0", "6.0", "7.0", "4.0"});
        testStatistics.addStatistics(new String[] {"Binp 2014 opdracht 2", "5.0", "8.0", "6.0", "4.0", "2.0"});
        testStatistics.addStatistics(new String[] {"Binp 2013 opdracht 1", "4.0", "5.0", "5.0", "3.0", "5.0"});
        testStatistics.addStatistics(new String[] {"Binp 2013 opdracht 2", "3.0", "2.0", "6.0", "7.0", "1.0"});
        testStatistics.addStatistics(new String[] {"Binp 2012 opdracht 1", "5.0", "8.0", "6.0", "4.0", "5.0"});

        moduleStatistics.addStatistics(new String[] {"Module:", "Gemiddelde cijfer:", "Aantal Deelnemers:", "Aantal onvoldoendes:", "Rendement:"});
        moduleStatistics.addStatistics(new String[] {"Binp 2016", "1.1", "4.6", "2.3", "7.7"});
        moduleStatistics.addStatistics(new String[] {"Binp 2015", "3.3", "6.8", "4.3", "6.0"});
        moduleStatistics.addStatistics(new String[] {"Binp 2014", "7.7", "2.3", "5.5", "7.7"});
        moduleStatistics.addStatistics(new String[] {"Binp 2013", "1.1", "4.6", "2.3", "7.7"});
        moduleStatistics.addStatistics(new String[] {"Binp 2012", "3.3", "6.7", "4.3", "6.0"});
        moduleStatistics.addStatistics(new String[] {"Binp 2011", "7.8", "2.3", "5.4", "7.7"});

        periodStatistics.addStatistics(new String[] {"Periode:", "Gemiddelde cijfer:", "Aantal Deelnemers:", "Aantal onvoldoendes:", "Rendement:"});
        periodStatistics.addStatistics(new String[] {"1", "5.4", "6.7", "5.6", "4.6"});

        // Voeg zo een grafiek toe
        testStatistics.setGraph("https://i.imgur.com/8Sqgh3M.png");

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
        testStatistics = new Statistiek();
        moduleStatistics = new Statistiek();
        periodStatistics = new Statistiek();
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

    private void setUpChangeListeners() {
        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value, Number oldWidth, Number newWidth) {
                Side side = tabPane.getSide();
                int numTabs = tabPane.getTabs().size();
                if ((side == Side.BOTTOM || side == Side.TOP) && numTabs != 0) {
                    tabPane.setTabMinWidth(newWidth.intValue() / numTabs - (20));
                    tabPane.setTabMaxWidth(newWidth.intValue() / numTabs - (20));
                }
            }
        });

        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value, Number oldHeight, Number newHeight) {
                Side side = tabPane.getSide();
                int numTabs = tabPane.getTabs().size();
                if ((side == Side.LEFT || side == Side.RIGHT) && numTabs != 0) {
                    tabPane.setTabMinWidth(newHeight.intValue() / numTabs - (20));
                    tabPane.setTabMaxWidth(newHeight.intValue() / numTabs - (20));
                }
            }
        });

        tabPane.getTabs().addListener(new ListChangeListener<Tab>() {
            public void onChanged(ListChangeListener.Change<? extends Tab> change) {
                Side side = tabPane.getSide();
                int numTabs = tabPane.getTabs().size();
                if (numTabs != 0) {
                    if (side == Side.LEFT || side == Side.RIGHT) {
                        tabPane.setTabMinWidth(heightProperty().intValue() / numTabs - (20));
                        tabPane.setTabMaxWidth(heightProperty().intValue() / numTabs - (20));
                    }
                    if (side == Side.BOTTOM || side == Side.TOP) {
                        tabPane.setTabMinWidth(widthProperty().intValue() / numTabs - (20));
                        tabPane.setTabMaxWidth(widthProperty().intValue() / numTabs - (20));
                    }
                }
            }
        });
    }

}