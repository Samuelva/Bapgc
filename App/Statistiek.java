package sample;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;

/**
 * Created by Samuel on 5-12-2016.
 */
public class Statistiek {
    VBox statisticsBox;
    ScrollPane statisticsScrollPane;
    HBox statisticsGridBox;
    BorderPane graphMenuBox;
    HBox graphBox;

    ComboBox graphButton;
    Button saveButton;

    public Statistiek() {
        // Box met toets statistieken, grafiekopties en de grafiek
        statisticsBox = new VBox();
        // Box waarin gridboxen met statistieken in zitten
        statisticsGridBox = new HBox();
        // Scrollpane met de gridbox waarin de statistieken in zitten
        statisticsScrollPane = new ScrollPane();
        // Box met grafiek optie knoppen
        graphMenuBox = new BorderPane();
        // Box met de grafiek
        graphBox = new HBox();

        graphButton = new ComboBox();
        graphButton.setPromptText("Grafiek Soort");
        graphButton.getItems().addAll("Histogram", "Pie chart", "placeholder");
        saveButton = new Button("Afbeelding opslaan");
        graphMenuBox.setLeft(graphButton);
        graphMenuBox.setRight(saveButton);

        statisticsScrollPane.setContent(statisticsGridBox);
        statisticsBox.getChildren().addAll(statisticsScrollPane, graphMenuBox, graphBox);
        HBox.setHgrow(statisticsBox, Priority.ALWAYS);
    }

    /**
     * Met deze functie kan er een boxje met statistieken toegevoegd worden aan de scrollbare pane.
     * Gebruik: klasseInstantie.addStatistics(10, 30, 40, 30);
     */
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
//        statisticsGrid.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, null, null)));
        statisticsGrid.setPadding(new Insets(10, 10, 10, 10));
        statisticsGridBox.getChildren().add(statisticsGrid);

    }

    // Returned de box met toets statistieken, grafiekopties en de grafiek
    public VBox returnStatisticsBox() {
        return statisticsBox;
    }

    public void setGraph() {

    }
}
