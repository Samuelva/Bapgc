package sample;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import java.util.HashMap;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;


public class Vergelijken extends Pane {
    HBox vergelijk = new HBox();
    VBox statistiek = new VBox();
    HBox info = new HBox();
    HBox infoButtons = new HBox();
    HBox grafiek = new HBox();
    ComboBox<String> grafiekSoort = new ComboBox<String>();
    Button opslaan = new Button("Opslaan");
    Button test1 = new Button("Test1");
    Button test2 = new Button("test2");
    HBox toetsBox = new HBox();


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

        Keuzemenu toetsMenu = new Keuzemenu("Jaartal", "Leerjaar", "Periode", "Module", "Toetsvorm", "Gelegenheid");
        Keuzemenu moduleMenu = new Keuzemenu("Jaartal", "Leerjaar", "Periode");
        Keuzemenu periodeMenu = new Keuzemenu("Jaartal", "Leerjaar");

        infoButtons.getChildren().addAll(grafiekSoort, opslaan);
        infoButtons.setMinWidth(900);
        opslaan.setAlignment(Pos.BASELINE_RIGHT);
        info.setMinWidth(900);
        info.setMinHeight(200);
        grafiek.getChildren().addAll();
        grafiek.setMinHeight(300);
        statistiek.getChildren().addAll(info, infoButtons, grafiek);
        toetsBox.getChildren().addAll(toetsMenu.returnMenu(), statistiek);

//        toets.setContent(toetsMenu.returnMenu());
        toets.setContent(toetsBox);
        module.setContent(moduleMenu.returnMenu());
        periode.setContent(periodeMenu.returnMenu());

        vergelijk.getChildren().addAll(tabPane);

        HashMap<String, Integer> hurdur = new HashMap<>();
        hurdur.put("Aantal deelnemers", 108);
        hurdur.put("Aantal Voldoendes", 70);
        hurdur.put("Aantal onvoldoendes", 31);

        tabPane.setMinWidth(1000);

        this.setMinWidth(1000);
        this.getChildren().add(vergelijk);

    }

    public HBox info(HashMap<String, Integer> data) {
        HBox infoBox = new HBox();
        for (HashMap.Entry<String, Integer> entry : data.entrySet()) {
            Label label = new Label(entry.getKey());
            infoBox.getChildren().add(label);
        }
        return infoBox;
    }



}