package sample;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;

/**
 * Created by Samuel on 5-12-2016.
 */
public class Statistiek {
    VBox statisticsBox;
    HBox statisticsGridBox;
    HBox graphMenuBox;
    HBox graphBox;

    ComboBox graphButton;
    Button saveButton;

    public Statistiek() {
        statisticsBox = new VBox();
        statisticsGridBox = new HBox();
        graphMenuBox = new HBox();
        graphBox = new HBox();

        graphButton = new ComboBox();
        saveButton = new Button("Afbeelding opslaan");
        graphMenuBox.getChildren().addAll(graphButton, saveButton);

        statisticsBox.getChildren().addAll(statisticsGridBox, graphMenuBox, graphBox);
    }

    public void addStatistics(int[] values) {
        GridPane statisticsGrid = new GridPane();

        Label participantsLbl = new Label("Aantal Deelnemers: ");
        Label failedLbl = new Label("Aantal onvoldoendes: ");
        Label passedLbl = new Label("Aantal voldoendes: ");
        Label passRateLbl = new Label("Rendement: ");

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

        statisticsGrid.setVgap(5);
        statisticsGrid.setHgap(5);

        statisticsGridBox.getChildren().add(statisticsGrid);

    }

    public VBox returnStatisticsBox() {
        return statisticsBox;
    }

    public void setGraph() {

    }
}
