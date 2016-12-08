package sample;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;

public class Vergelijken extends Pane {
    HBox vergelijk = new HBox();
    VBox statisticBox = new VBox();
    HBox info = new HBox(10);
    HBox infoButtons = new HBox();
    HBox grafiek = new HBox();
    ComboBox<String> grafiekSoort = new ComboBox<String>();
    Button opslaan = new Button("Opslaan");
    HBox testTabBox = new HBox();
    HBox moduleTabBox = new HBox();
    HBox periodTabBox = new HBox();

    public Vergelijken(){
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab toets = new Tab();
        Tab module = new Tab();
        Tab periode = new Tab();
        toets.setText("Toets");
        module.setText("Module");
        periode.setText("Periode");

        tabPane.getTabs().add(toets);
        tabPane.getTabs().add(module);
        tabPane.getTabs().add(periode);

        Keuzemenu toetsMenu = new Keuzemenu();
        Keuzemenu moduleMenu = new Keuzemenu();
        Keuzemenu periodMenu = new Keuzemenu();

        VBox toetsMenuBox = toetsMenu.getTestMenu();
        VBox moduleMenuBox = moduleMenu.getModuleMenu();
        VBox periodMenuBox = periodMenu.getPeriodMenu();

        infoButtons.getChildren().addAll(grafiekSoort, opslaan);
        infoButtons.setMinWidth(900);
        opslaan.setAlignment(Pos.BASELINE_RIGHT);
        info.getChildren().add(new Statistiek(new int[] {40, 50, 50, 30}).returnStatisticBox());
        info.getChildren().add(new Statistiek(new int[] {30, 20, 10, 44}).returnStatisticBox());
        grafiek.getChildren().addAll();
        grafiek.setMinHeight(300);
        statisticBox.getChildren().addAll(info, infoButtons, grafiek);
        testTabBox.getChildren().addAll(toetsMenuBox, statisticBox);
        moduleTabBox.getChildren().addAll(moduleMenuBox, statisticBox);
        periodTabBox.getChildren().addAll(periodMenuBox, statisticBox);

        toets.setContent(testTabBox);
        module.setContent(moduleTabBox);
        periode.setContent(periodTabBox);

        vergelijk.getChildren().addAll(tabPane);

        tabPane.setMinWidth(1000);

        this.setMinWidth(1000);
        this.getChildren().add(vergelijk);
    }

//    public HBox info(HashMap<String, Integer> data) {
//        HBox infoBox = new HBox();
//        for (HashMap.Entry<String, Integer> entry : data.entrySet()) {
//            Label label = new Label(entry.getKey());
//            infoBox.getChildren().add(label);
//        }
//        return infoBox;
//    }



}