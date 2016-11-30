package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


/**
 * Voorbeeld script voor Panes.
 */
public class Toevoegen extends Pane {

    public Toevoegen(){
        HBox keuze = new HBox();
        Label lbl = new Label();
        lbl.setText("Toevoegscherm");


        keuze.getChildren().addAll(lbl);

        this.getChildren().add(keuze);
    }

}
