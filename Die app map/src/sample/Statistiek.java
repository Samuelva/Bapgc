package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private VBox statisticsTableBox;
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

    TableView<TestRow> table;
    TableColumn testCol;
    TableColumn moduleCol;
    TableColumn periodCol;
    TableColumn averageGradeCol;
    TableColumn participantsCol;
    TableColumn failedCol;
    TableColumn passedCol;
    TableColumn passRateCol;

    public final ObservableList testData = FXCollections.observableArrayList(
            new TestRow("Binp 2018 Opdracht 1", 7, 6, 4, 9, 2),
            new TestRow("Binp 2017 Opdracht 1", 3, 9, 4, 7, 4),
            new TestRow("Binp 2016 Opdracht 1", 7, 6, 7, 6, 3),
            new TestRow("Binp 2015 Opdracht 1", 5, 4, 0, 4, 6),
            new TestRow("Binp 2014 Opdracht 1", 4, 3, 5, 9, 8),
            new TestRow("Binp 2013 Opdracht 1", 6, 8, 7, 7, 5),
            new TestRow("Binp 2012 Opdracht 1", 8, 5, 8, 6, 4),
            new TestRow("Binp 2011 Opdracht 1", 4, 8, 9, 5, 6)
    );

    public final ObservableList moduleData = FXCollections.observableArrayList(
            new Row("Binp 2018", 6, 4, 9, 2),
            new Row("Binp 2017", 9, 4, 7, 4),
            new Row("Binp 2016", 6, 7, 6, 3),
            new Row("Binp 2015", 4, 0, 4, 6),
            new Row("Binp 2014", 3, 5, 9, 8),
            new Row("Binp 2013", 8, 7, 7, 5),
            new Row("Binp 2012", 5, 8, 6, 4),
            new Row("Binp 2011", 8, 9, 5, 6)
    );


    public int activeGraphInt;

    public Statistiek() {
        /**
         * Roept functies aan welke de boxjes aanmaken en vullen met de jusite inhoud (statistiek en grafiek)
         */
        statisticsBox = new VBox();
        createStatisticsTableBox();
        createGraphPane();
    }

    public void createStatisticsTableBox() {
        /**
         * Maakt het scrollbare paneel aan waar de statistiek in weergegeven zal worden.
         */
        table = new TableView();
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getFocusModel().focusedCellProperty().addListener(new ChangeListener<TablePosition>() {

            /* Als er op een cel gedrukt wordt, wordt de gehele kolom geselecteerd en de statistiek geupdate.
             * Als een van de eerste drie kolomen geselecteerd wordt worden de statistieken voor de hele toets
             * weergegeven, anders voor de specifieke vraag.
             */
            @Override
            public void changed(ObservableValue<? extends TablePosition> observable, TablePosition oldValue,
                                TablePosition newValue) {
                if (newValue.getTableColumn() != null) {
                    table.getSelectionModel().selectRange(0, newValue.getTableColumn(),
                            table.getItems().size(), newValue.getTableColumn());

                }
            }
        });
        testCol = new TableColumn("Toets");
        moduleCol = new TableColumn("Module");
        periodCol = new TableColumn("Periode");
        averageGradeCol = new TableColumn("Gem. cijfer");
        participantsCol = new TableColumn("Aantal deelnemers");
        failedCol = new TableColumn("Onvoldoendes");
        passedCol = new TableColumn("Voldoendes");
        passRateCol = new TableColumn("Rendement");

        testCol.setCellValueFactory(new PropertyValueFactory<>("test"));
        moduleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        periodCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        averageGradeCol.setCellValueFactory(new PropertyValueFactory<>("averageGrade"));
        participantsCol.setCellValueFactory(new PropertyValueFactory<>("participants"));
        failedCol.setCellValueFactory(new PropertyValueFactory<>("failed"));
        passedCol.setCellValueFactory(new PropertyValueFactory<>("passed"));
        passRateCol.setCellValueFactory(new PropertyValueFactory<>("passRate"));

        statisticsTableBox = new VBox();
        statisticsTableBox.getChildren().add(table);
        statisticsTableBox.setMinHeight(175);
        statisticsTableBox.setMaxHeight(175);
        statisticsTableBox.setSpacing(5);
        VBox.setVgrow(statisticsTableBox, Priority.ALWAYS);
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

    public VBox returnStatisticsBox() {
        /**
         * Returned de box met toets statistieken, grafiekopties en de grafiek
         */
        statisticsBox.setPadding(new Insets(0, 0, 0, 5));
        statisticsBox.getChildren().addAll(statisticsTableBox, graphButtonBox, graphPane);
        HBox.setHgrow(statisticsBox, Priority.ALWAYS);
        return statisticsBox;
    }

    public void setTestTableContent(ObservableList data) {
        /**
         * Voor toetsen
         */
        table.setItems(data);
        table.getColumns().addAll(testCol, averageGradeCol, participantsCol, failedCol, passedCol, passRateCol);
    }

    public void setModuleTableContent(ObservableList data) {
        /**
         * Voor modules
         */
        table.setItems(data);
        table.getColumns().addAll(moduleCol, participantsCol, failedCol, passedCol, passRateCol);
    }

    public void setPeriodTableContent(ObservableList data) {
        table.setItems(data);
        table.getColumns().addAll(periodCol, participantsCol, failedCol, passedCol, passRateCol);
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

