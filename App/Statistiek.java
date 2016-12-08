package sample;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
/**
 * Created by Samuel on 5-12-2016.
 */
public class Statistiek {
    GridPane statisticsGrid = new GridPane();
    private Label participantsLbl = new Label("Aantal Deelnemers: ");
    private Label failedLbl = new Label("Aantal onvoldoendes: ");
    private Label passedLbl = new Label("Aantal voldoendes: ");
    private Label passRateLbl = new Label("Rendement: ");


    public Statistiek(int[] values) {
        Label participants = new Label(Integer.toString(values[0]));
        Label failed = new Label(Integer.toString(values[1]));
        Label passed = new Label(Integer.toString(values[2]));
        Label passRate = new Label(Integer.toString(values[3]));

        statisticsGrid.add(participantsLbl, 1, 1);
        statisticsGrid.add(failedLbl, 1, 2);
        statisticsGrid.add(passedLbl, 1, 3);
        statisticsGrid.add(passRateLbl, 1, 4);
        statisticsGrid.add(participants, 2, 1);
        statisticsGrid.add(failed, 2, 2);
        statisticsGrid.add(passed, 2, 3);
        statisticsGrid.add(passRate, 2, 4);

        statisticsGrid.setPrefHeight(100);
        statisticsGrid.setPrefWidth(150);
    }

    public GridPane returnStatisticBox() {
        return statisticsGrid;
    }
}
