package sample;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Samuel on 5-12-2016.
 * Deze klasse maakt het statistiek gedeelte aan en kan deze uitbreiden/aanpassen
 */
public class Statistiek {
    private VBox statisticsBox; // Box met toets statistieken, grafiekopties en de grafiek
    private HBox statisticsGridBox; // Box waarin gridboxen met statistieken in zitten
    private ScrollPane statisticsScrollPane; // Scrollpane met de gridbox waarin de statistieken in zitten
    private BorderPane graphButtonBox; // Box met grafiek optie knoppen
    public StackPane graphPane; // Pane met de grafiek

    public ComboBox graphButton;
    private Button saveButton;
    private ImageView graph;  // Container met grafiek foto

    private Lijngrafiek lineChart;
    private Histogram barChart;
    private Cirkeldiagram pieChart;
    private Boxplot boxplot;
    private WritableImage graphImage;
    public int activeGraphInt;

    public Statistiek() {
        /**
         * Roept functies aan welke de boxjes aanmaken en vullen met de jusite inhoud (statistiek en grafiek)
         */
        createStatisticsScrollPane();
        createGraphPane();
    }

    public void createStatisticsScrollPane() {
        /**
         * Maakt het scrollbare paneel aan waar de statistiek in weergegeven zal worden.
         */
        statisticsBox = new VBox();
        statisticsGridBox = new HBox();
        statisticsScrollPane = new ScrollPane();

        statisticsScrollPane.setContent(statisticsGridBox);
    }

    public void createGraphPane() {
        /**
         * Maakt het grafiek gedeelte aan met de box voor de grafiek knoppen en de grafiek zelf.
         */
        graphButtonBox = new BorderPane();
        graphPane = new StackPane();

        graphButton = new ComboBox(); // ComboBox voor het instellen van de soort grafiek
        graphButton.setPrefWidth(150);
        graphButton.setMinHeight(30);
        graphButton.setPromptText("Grafiek Soort");
        graphButton.getItems().addAll("Histogram", "Lijngrafiek", "Boxplot"); // Inhoud comboBox

        saveButton = new Button("Afbeelding opslaan");
        saveButton.setPrefWidth(150);
        saveButton.setMinHeight(30);
        saveButton.setOnAction(e -> {
            if (activeGraphInt > 0) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG (*.png)", "*.png"));
                fileChooser.setTitle("Opslaan Als");
                File file = fileChooser.showSaveDialog(new Stage());
                if (file != null) {
                    saveGraph();
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(graphImage, null), "png", file);
                    } catch (IOException ie) {
                        System.out.println("error");
                    }
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Geen grafiek");
                alert.setHeaderText("Grafiek kan niet worden opgeslagen.");
                alert.setContentText("Selecteer een grafiek.");
                ButtonType ok = new ButtonType("OK");
                alert.getButtonTypes().setAll(ok);
                alert.show();
            }
        });

        graphButtonBox.setLeft(graphButton);
        graphButtonBox.setRight(saveButton);
        graphButtonBox.setPadding(new Insets(5, 5, 5, 5));
        graphPane.setPadding(new Insets(10, 10, 10, 10));
        graphPane.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, null, null)));
        VBox.setVgrow(graphPane, Priority.ALWAYS);

    }

    public void addStatistics(String[] values) {
        /**
         * Met deze functie kan er een boxje met statistieken toegevoegd worden aan de scrollbare pane.
         * Gebruik: klasseInstantie.addStatistics(10, 30, 40, 30, 40); voor als gemiddelde cijfer
         * erbij moet, anders klasseInstantie.addStatistics(30, 40, 50, 30);
         */
        GridPane statisticsGrid = new GridPane();

        Label titleLbl = new Label(values[0]);

        // Als er 5 getallen meegegeven worden, wordt er een grid met het gemiddelde cijfer gemaakt
        if (values.length == 6) {
            Label averageGrade = new Label(values[1]);
            Label participants = new Label(values[2]);
            Label failed = new Label(values[3]);
            Label passed = new Label(values[4]);
            Label passRate = new Label(values[5]);

            statisticsGrid.add(titleLbl, 1, 1);
            statisticsGrid.add(averageGrade, 1, 2);
            statisticsGrid.add(participants, 1, 3);
            statisticsGrid.add(failed, 1, 4);
            statisticsGrid.add(passed, 1, 5);
            statisticsGrid.add(passRate, 1, 6);
            statisticsScrollPane.setMinHeight(160);
        }

        else if (values.length == 5) {
            Label participants = new Label(values[1]);
            Label failed = new Label(values[2]);
            Label passed = new Label(values[3]);
            Label passRate = new Label(values[4]);

            statisticsGrid.add(titleLbl, 1, 1);
            statisticsGrid.add(participants, 1, 2);
            statisticsGrid.add(failed, 1, 3);
            statisticsGrid.add(passed, 1, 4);
            statisticsGrid.add(passRate, 1, 5);
        }

        statisticsGrid.setVgap(5);
        statisticsGrid.setHgap(5);
        statisticsGrid.setPadding(new Insets(10, 10, 10, 10));
        statisticsGridBox.getChildren().add(statisticsGrid);
    }

    public VBox returnStatisticsBox() {
        /**
         * Returned de box met toets statistieken, grafiekopties en de grafiek
         */
        statisticsBox.setPadding(new Insets(0, 0, 0, 5));
        statisticsBox.getChildren().addAll(statisticsScrollPane, graphButtonBox, graphPane);
        HBox.setHgrow(statisticsBox, Priority.ALWAYS);
        return statisticsBox;
    }

    public void setGraph(VBox graphBox) {
        /**
         * Input is een link/path naar de grafiek
         */

        graphPane.getChildren().add(graphBox);
    }

    public void saveGraph() {
        BarChart<String, Number> barChartGraph;
        LineChart<String, Number> lineChartGraph;
        switch (activeGraphInt) {
            case 1:
                barChartGraph = barChart.getBarChart();
                graphImage = barChartGraph.snapshot(new SnapshotParameters(), null);
                break;
            case 2:
                lineChartGraph = lineChart.getLineChart();
                graphImage = lineChartGraph.snapshot(new SnapshotParameters(), null);
                break;
            case 3:
                lineChartGraph = lineChart.getLineChart();
                graphImage = lineChartGraph.snapshot(new SnapshotParameters(), null);
                break;
        }

    }

    public void setLineChart() {
        lineChart = new Lijngrafiek("test x-as", "test y-as", "test titel");
        graphPane.getChildren().clear();
        graphPane.getChildren().add(lineChart.getLineChartBox());
        String[] xValues = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        int[] yValues = new int[] {6, 5, 4, 6, 7, 5, 4, 6, 7};
        String[] xValues2 = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        int[] yValues2 = new int[] {4, 5, 8, 3, 2, 6, 8, 9, 5};
        addLineChartLine(xValues, yValues, "testlijn");
        addLineChartLine(xValues2, yValues2, "testlijn");
    }

    public void addLineChartLine(String[] xValues, int[] yValues, String title) {
        lineChart.addLine(xValues, yValues, "testLijn 1");
    }

    public void setBarChart() {
        barChart = new Histogram("test x-as", "test y-as", "test title", "test naam");
        graphPane.getChildren().clear();
        graphPane.getChildren().add(barChart.getBarChartBox());
        barChart.addBar("1", 8);
        barChart.addBar("2", 9);
        barChart.addBar("3", 5);
    }


    public void setBoxPlot() {
        boxplot = new Boxplot();
        graphPane.getChildren().clear();
        graphPane.getChildren().add(boxplot.makeBoxPlot());
        boxplot.addData();
    }
}
