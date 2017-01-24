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
import javafx.scene.input.MouseEvent;
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
    private Integer instance;
    private VBox statisticsBox; // Box met toets statistieken, grafiekopties en de grafiek
    private VBox statisticsTableBox;
    private BorderPane graphButtonBox; // Box met grafiek optie knoppen
    public StackPane graphPane; // Pane met de grafiek

    public ComboBox graphButton;
    private Button saveButton;

    private Lijngrafiek lineChart;
    private Histogram barChart;
    private Boxplot boxplot;
    private WritableImage graphImage;

    public int activeGraphInt; // Geeft aan welke grafiek geselecteerd is. 1 = histogram, 2 = lijngrafiek, 3 = boxplot.

    TableView<TestRow> table;
    TableColumn testCol;
    TableColumn moduleCol;
    TableColumn periodCol;
    TableColumn averageGradeCol;
    TableColumn participantsCol;
    TableColumn failedCol;
    TableColumn passedCol;
    TableColumn passRateCol;

    public Statistiek(Integer instanceI) {
        /**
         * Roept functies aan welke de boxjes aanmaken en vullen met de jusite inhoud (statistiek en grafiek)
         */
        instance = instanceI;
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
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getFocusModel().focusedCellProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getTableColumn() != null) {
                table.getSelectionModel().selectRange(0, newValue.getTableColumn(),
                        table.getItems().size(), newValue.getTableColumn());
                switch (instance) {
                    case 1:
                        fillGraph(newValue, testCol, " per toets");
                        break;
                    case 2:
                        fillGraph(newValue, moduleCol, " per module");
                        break;
                    case 3:
                        fillGraph(newValue, periodCol, " per periode");
                        break;
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
        graphButton.setPromptText("Grafiek");
        graphButton.getItems().addAll("Histogram", "Lijngrafiek", "Boxplot"); // Inhoud comboBox
        graphButton.setOnAction(event -> {
           switch (graphButton.getValue().toString()) {
               case "Histogram":
                   activeGraphInt = 1;
                   break;
               case "Lijngrafiek":
                   activeGraphInt = 2;
                   break;
               case "Boxplot":
                   activeGraphInt = 3;
                   break;
           }
        });

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

    public void fillGraph(TablePosition newValue, TableColumn column, String title) {
        switch (activeGraphInt) {
            case 1:
                setBarChart(column.getText(), newValue.getTableColumn().getText(), newValue.getTableColumn().getText() + title, newValue.getTableColumn().getText());
                for (int i = 0; i < table.getItems().size(); i++) {
                    barChart.addBar(column.getCellObservableValue(i).getValue().toString(), (int) newValue.getTableColumn().getCellObservableValue(i).getValue());
                }
                break;
            case 2:
                setLineChart(column.getText(), newValue.getTableColumn().getText(), newValue.getTableColumn().getText() + title);
                String[] xValues = new String[table.getItems().size()];
                int[] yValues = new int[table.getItems().size()];
                for (int i = 0; i < table.getItems().size(); i++) {
                    xValues[i] = column.getCellObservableValue(i).getValue().toString();
                    yValues[i] = (int) newValue.getTableColumn().getCellObservableValue(i).getValue();
                };
                addLineChartLine(xValues, yValues, newValue.getTableColumn().getText());
                break;
        }
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

    public void setLineChart(String xAxis, String yAxis, String title) {
        lineChart = new Lijngrafiek(xAxis, yAxis, title);
        graphPane.getChildren().clear();
        graphPane.getChildren().add(lineChart.getLineChartBox());
    }

    public void addLineChartLine(String[] xValues, int[] yValues, String legend) {
        lineChart.addLine(xValues, yValues, legend);
    }

    public void setBarChart(String xAxis, String yAxis, String title, String legend) {
        barChart = new Histogram(xAxis, yAxis, title, legend);
        graphPane.getChildren().clear();
        graphPane.getChildren().add(barChart.getBarChartBox());
    }

    public void setBoxPlot() {
        boxplot = new Boxplot();
        graphPane.getChildren().clear();
        graphPane.getChildren().add(boxplot.makeBoxPlot());
    }
}

