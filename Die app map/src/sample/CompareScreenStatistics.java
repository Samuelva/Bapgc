package sample;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


/**
 * Created by Samuel on 5-12-2016.
 * Deze klasse regelt het statistiek gedeelte van het vergelijkscherm.
 * Dus alles van het vergelijkscherm behalve het keuzemenu.
 */
public class CompareScreenStatistics {
    // Geeft aan voor welke tab de klasse wordt aangeroepen. 1 = toetstab,
    // 2 = moduletab, 3 = periodetab.
    private Integer instance;
    private VBox compareStatisticsBox;
    private VBox statisticsTableBox; // Box met statistieken tabel
    private BorderPane graphButtonPane; // Box met grafiek optie knoppen
    private StackPane graphPane; // Pane met de grafiek

    private ComboBox graphSelectionButton;
    private Button graphSaveButton;
    // Geeft aan welke grafiek geselecteerd is
    private String selectedGraph;

    private LineGraph lineChart;
    private Barchart barChart;
    private Boxplot boxplot;
    private WritableImage graphImage;

    private TableView<Row> table;
    private TableColumn testCol;
    private TableColumn moduleCol;
    private TableColumn periodCol;
    private TableColumn averageGradeCol;
    private TableColumn participantsCol;
    private TableColumn failedCol;
    private TableColumn passedCol;
    private TableColumn passRateCol;
    private TablePosition tablePos;
    private boolean colSelected;

    public CompareScreenStatistics(Integer instanceI) {
        /**
         * Roept functies aan welke de boxjes aanmaken en vullen met de
         * jusite inhoud (statistiek en grafiek)
         */
        instance = instanceI;
        createTable();
        createGraphButtons();
        createGraphPane();
    }

    private VBox statisticsTableBox() {
        /**
         * Maakt een box aan waar de tabel met statistieken in komt.
         */
        statisticsTableBox = new VBox();
        statisticsTableBox.getChildren().add(table);
        statisticsTableBox.setMinHeight(175);
        statisticsTableBox.setMaxHeight(175);
        statisticsTableBox.setSpacing(5);
        VBox.setVgrow(statisticsTableBox, Priority.ALWAYS);

        return statisticsTableBox;
    }

    private void createTable() {
        /**
         * Maakt de tabel met kolommen aan waar statistieken in komen. Als er
         * op een cell wordt gedrukt, wordt er een event handler geroepen.
         */
        table = new TableView();
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getFocusModel().focusedCellProperty().addListener((observable,
                                                                 oldValue,
                                                                 newValue) -> {
            if (newValue.getTableColumn() != null && newValue.getColumn() > 0) {
                colSelectEvent(newValue);
            }
        });

        createColumns();
    }

    public void clearTable() {
        /**
         * Maakt de tabel leeg.
         */
        table.getItems().removeAll();
        table.getColumns().removeAll();
    }

    private void colSelectEvent(TablePosition newValue) {
        /**
         * Event handler voor het selecteren van een kolom in de tabel. Als
         * er op een cel wordt gedrukt, wordt de hele kolom geselecteerd.
         * Slaat de positie van de geselecteerde kolom op voor later gebruik
         * voor de grafiek.
         */
        table.getSelectionModel().selectRange(0, newValue.getTableColumn(),
                table.getItems().size(), newValue.getTableColumn());
        tablePos = newValue;
        colSelected = true;
        if (selectedGraph != null) {
            graphSelection();
        }
    }

    private void graphSelection() {
        /**
         * Roept de functies aan om grafieken te maken, afhankelijk van welke
         * grafiek er gekozen is.
         */
        switch (instance) {
            case 1:
                fillGraph(testCol, " per toets");
                break;
            case 2:
                fillGraph(moduleCol, " per module");
                break;
            case 3:
                fillGraph(periodCol, " per periode");
                break;
        }
    }

    private void createColumns() {
        /**
         * Maakt de kolommen voor de statistieken tabel aan.
         */
        testCol = new TableColumn("Toets");
        moduleCol = new TableColumn("Module");
        periodCol = new TableColumn("Periode");
        averageGradeCol = new TableColumn("Gem. cijfer");
        participantsCol = new TableColumn("Aantal deelnemers");
        failedCol = new TableColumn("Onvoldoendes");
        passedCol = new TableColumn("Voldoendes");
        passRateCol = new TableColumn("Rendement");

        testCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        moduleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        periodCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        averageGradeCol.setCellValueFactory(new PropertyValueFactory<>
                ("averageGrade"));
        participantsCol.setCellValueFactory(new PropertyValueFactory<>
                ("participants"));
        failedCol.setCellValueFactory(new PropertyValueFactory<>("failed"));
        passedCol.setCellValueFactory(new PropertyValueFactory<>("passed"));
        passRateCol.setCellValueFactory(new PropertyValueFactory<>
                ("passRate"));
    }

    private StackPane createGraphPane() {
        /**
         * Maakt het grafiek gedeelte aan met de box voor de grafiek knoppen
         * en de grafiek zelf.
         */
        graphPane = new StackPane();
        graphPane.setPadding(new Insets(10, 10, 10, 10));
        graphPane.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID, null, null)));
        VBox.setVgrow(graphPane, Priority.ALWAYS);
        return graphPane;
    }

    private BorderPane graphButtonPane() {
        /**
         * Maakt een pane aan met de grafiek selectie knop en grafiek opslaan
         * knop en returned deze.
         */
        graphButtonPane = new BorderPane();
        graphButtonPane.setLeft(graphSelectionButton);
        graphButtonPane.setRight(graphSaveButton);
        graphButtonPane.setPadding(new Insets(5, 5, 5, 5));
        return graphButtonPane;
    }

    private void createGraphButtons() {
        /**
         * Maakt de grafiek selectie en grafiek opslaan knop aan.
         */
        setGraphSelectionButton();
        setGraphSaveButton();
    }

    private void setGraphSelectionButton() {
        /**
         * Maakt de grafiek selectie knop aan. Als er een grafiek gekozen
         * wordt, zonder dat er een kolom in de tabel geselecteerd is, wordt
         * de grafiek keuze alleen opgeslagen. Als er wel een kolom
         * geselecteerd is, wordt er een nieuwe grafiek gemaakt.
         */
        graphSelectionButton = new ComboBox();
        graphSelectionButton.setMinWidth(150);
        graphSelectionButton.setMinHeight(30);
        graphSelectionButton.setPromptText("Grafiek");
        graphSelectionButton.getItems().addAll("Histogram", "Lijngrafiek",
                "Boxplot");
        graphSelectionButton.setOnAction(event -> {
            selectedGraph = graphSelectionButton.getValue().toString();
            if (colSelected) {
                graphSelection();
            }
        });
    }

    private void setGraphSaveButton() {
        /**
         * Maakt de grafiek opslaan knop aan. Als er een grafiek gemaakt is,
         * wordt een opslaan venster getoond, zo niet, dan wordt er een
         * melding getoond.
         */
        graphSaveButton = new Button("Grafiek opslaan");
        graphSaveButton.setMinWidth(150);
        graphSaveButton.setMinHeight(30);
        graphSaveButton.setOnAction(event -> {
            if (selectedGraph != null && colSelected) {
                displaySaveDialog();
            }
            else {
                displayInformationDialog();
            }
        });
    }

    private void displaySaveDialog() {
        /**
         * Toont het opslaan scherm voor het opslaan van een grafiek met .png
         * als standaard extensie. Als er een opslaan locatie is gekozen,
         * wordt de functie voor het opslaan van een grafiek aangeroepen.
         */
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter
                ("PNG (*.png)", ".png"));
        fileChooser.setTitle("Opslaan Als");
        File savePath = fileChooser.showSaveDialog(new Stage());
        if (savePath != null) {
            saveGraph(savePath);
        }
    }

    private void displayInformationDialog() {
        /**
         * Toont een melding indien aangeroepen. Wordt gebruikt voor als er op
         * de grafiek opslaan knop wordt gedrukt, maar er een grafiek gemaakt
         * is.
         */
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Geen grafiek");
        alert.setHeaderText("Er kan geen grafiek worden opgeslagen omdat er " +
                "geen is aangemaakt.");
        alert.setContentText("Maak een grafiek om deze te kunnen opslaan.");
        ButtonType confirm = new ButtonType("OK");
        alert.getButtonTypes().setAll(confirm);
        alert.show();
    }

    public VBox getCompareStatisticsBox() {
        /**
         * Returned de box met toets statistieken, grafiekopties en de grafiek
         */
        compareStatisticsBox = new VBox();
        compareStatisticsBox.setPadding(new Insets(0, 0, 0, 5));
        compareStatisticsBox.getChildren().addAll(statisticsTableBox(),
                graphButtonPane(), createGraphPane());
        HBox.setHgrow(compareStatisticsBox, Priority.ALWAYS);
        return compareStatisticsBox;
    }

    public void setTestTableColumns() {
        /**
         * Voegt de kolommen die gebruikt worden in de toets tab toe aan het
         * statistieken tabje.
         */
        table.getColumns().addAll(testCol, averageGradeCol, participantsCol,
                failedCol, passedCol, passRateCol);
    }

    public void setModuleTableColumns() {
        /**
         * Voegt de kolommen die gebruikt worden in de modulen tab toe aan het
         * statistieken tabje.
         */
        table.getColumns().addAll(moduleCol, averageGradeCol,
                participantsCol, failedCol, passedCol, passRateCol);
    }

    public void setPeriodTableColumns() {
        /**
         * Voegt de kolommen die gebruikt worden in de periode tab toe aan het
         * statistieken tabje.
         */
        table.getColumns().addAll(periodCol, averageGradeCol,
                participantsCol, failedCol, passedCol, passRateCol);
    }

    public void fillTable(ObservableList data) {
        /**
         * Vult de tabel met de meegegeven data.
         */
        table.setItems(data);
    }

    private void fillGraph(TableColumn column, String
            title) {
        /**
         * Kijkt welke grafiek geselecteerd is, en roept de functies aan om
         * deze te vullen.
         */
        switch (selectedGraph) {
            case "Histogram":
                fillHistogram(column, title);
                break;
            case "Lijngrafiek":
                fillLineGraph(column, title);
                break;
            case "Boxplot":
                plotBoxplot(title);
        }
    }

    private void fillHistogram(TableColumn column,
                               String title) {
        /**
         * Maakt de histogram aan en vult deze met de geselecteerde data uit
         * de geselecteerde kolom. If statements kijken naar of de meegegeven
         * waardes integers of doubles zijn. Dit is nodig omdat de data dan
         * op een andere manier meegegeven moet worden.
         */
        setBarChart(column.getText(), tablePos.getTableColumn().getText(),
                tablePos.getTableColumn().getText() + title, tablePos
                        .getTableColumn().getText());
        for (int i = 0; i < table.getItems().size(); i++) {
            if (tablePos.getTableColumn().getCellObservableValue
                    (0).getValue().getClass().getName() == "java.lang" +
                    ".Double") {
                barChart.addBar(column.getCellObservableValue(i).getValue()
                        .toString(), (double) tablePos.getTableColumn()
                        .getCellObservableValue(i).getValue());
            } else if (tablePos.getTableColumn().getCellObservableValue
                    (0).getValue().getClass().getName() == "java.lang" +
                    ".Integer") {
                barChart.addBar(column.getCellObservableValue(i).getValue()
                        .toString(), (int) tablePos.getTableColumn()
                        .getCellObservableValue(i).getValue());
            }
        }
    }

    private void fillLineGraph(TableColumn column,
                               String title) {
        /**
         * Stopt de geselecteerde kolom data in arrays voor de lijngrafiek. If
         * statements kijken naar of de meegegeven
         * waardes integers of doubles zijn. Dit is nodig omdat de data dan
         * op een andere manier meegegeven moet worden.
         */
        setLineChart(column.getText(), tablePos.getTableColumn().getText(),
                tablePos.getTableColumn().getText() + title);
        String[] xValues = new String[table.getItems().size()];
        double[] yValues = new double[table.getItems().size()];

        if (tablePos.getTableColumn().getCellObservableValue
                (0).getValue().getClass().getName() == "java.lang.Double") {
        } else if (tablePos.getTableColumn().getCellObservableValue
                (0).getValue().getClass().getName() == "java.lang.Integer") {
        }

        for (int i = 0; i < table.getItems().size(); i++) {
            xValues[i] = column.getCellObservableValue(i).getValue()
                    .toString();
            if (tablePos.getTableColumn().getCellObservableValue
                    (0).getValue().getClass().getName() == "java.lang" +
                    ".Double") {
                yValues[i] = (double) tablePos.getTableColumn()
                        .getCellObservableValue(i).getValue();
            } else if (tablePos.getTableColumn().getCellObservableValue
                    (0).getValue().getClass().getName() == "java.lang" +
                    ".Integer") {
                yValues[i] = (int) tablePos.getTableColumn()
                        .getCellObservableValue(i).getValue();
            }
        }
        addLineChartLine(xValues, yValues, tablePos.getTableColumn().getText());
    }

    private void saveGraph(File savePath) {
        /**
         * Functie voor het opslaan van de grafieken. Maakt een snapshot van
         * de grafieken, en probeert deze weg te schrijven. Als het niet kan
         * worden weggeschreven, wordt er een foutmelding getoond.
         */
        BarChart<String, Number> barChartGraph;
        LineChart<String, Number> lineChartGraph;
        CandleStickChart boxplotGraph;
        if (selectedGraph == "Histogram") {
            barChartGraph = barChart.getBarChart();
            graphImage = barChartGraph.snapshot(new SnapshotParameters(),
                    null);
        } else if (selectedGraph == "Lijngrafiek") {
            lineChartGraph = lineChart.getLineChart();
            graphImage = lineChartGraph.snapshot(new SnapshotParameters()
                    , null);
        } else if (selectedGraph == "Boxplot") {
            boxplotGraph = boxplot.getBoxPlot();
            graphImage = boxplotGraph.snapshot(new SnapshotParameters(), null);
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(graphImage, null),
                    "png", savePath);
        } catch (IOException e) {
            displayErrorDialog();
        }
    }

    private void displayErrorDialog() {
        /**
         * Functie voor het tonen van een foutmelding als een grafiek niet
         * kan worden opgeslagen.
         */
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Grafiek kan niet worden opgeslagen door een " +
                "onbekende fout.");
        alert.setContentText("IOException, de grafiek kon niet worden " +
                "weggeschreven.");
        ButtonType confirm = new ButtonType("OK");
        alert.getButtonTypes().setAll(confirm);
        alert.show();
    }

    private void setLineChart(String xAxis, String yAxis, String title) {
        /**
         * Initieert de lijngrafiek met x-as, y-as en titel, maakt de pane
         * met de grafiek leeg voor als er al een andere hierin zit en stopt
         * er vervolgens de nieuwe grafiek in.
         */
        lineChart = new LineGraph(xAxis, yAxis, title);
        graphPane.getChildren().clear();
        graphPane.getChildren().add(lineChart.getLineChartBox());
    }

    private void addLineChartLine(String[] xValues, double[] yValues, String
            legend) {
        /**
         * Voegt een lijn toe aan de lijngrafiek met de opgegeven waarden.
         */
        lineChart.addLine(xValues, yValues, legend);
    }

    private void setBarChart(String xAxis, String yAxis, String title, String
            legend) {
        /**
         * Initieerd de barchart met x-as, y-as en title, maakt de grafiek
         * pane leeg en voegt de nieuwe grafiek er aan toe.
         */
        barChart = new Barchart(xAxis, yAxis, title, legend);
        graphPane.getChildren().clear();
        graphPane.getChildren().add(barChart.getBarChartBox());
    }

    private void plotBoxplot(String title) {
        /**
         * Maakt en vult de boxplot met geselecteerde data.
         * Voor alle data in de geselecteerde kolom wordt het minimum,
         * maximum, 1e kwartiel, 2e kwartiel en mediaan bepaald, welke
         * vervolgens geplot worden. If statements kijken naar of de meegegeven
         * waardes integers of doubles zijn. Dit is nodig omdat de data dan
         * op een andere manier meegegeven moet worden.
         */
        double[] points = new double[table.getItems().size()];
        for (int i = 0; i < table.getItems().size(); i++) {
            if (tablePos.getTableColumn().getCellObservableValue(0).getValue
                    ().getClass().getName() == "java.lang.Double") {
                points[i] = (double) tablePos.getTableColumn()
                        .getCellObservableValue(i).getValue();
            } else if (tablePos.getTableColumn().getCellObservableValue(0)
                    .getValue
                    ().getClass().getName() == "java.lang.Integer") {
                points[i] = (int) tablePos.getTableColumn()
                        .getCellObservableValue(i).getValue();
            }

        }

        double[][] boxplotData = new double[][]{
                {1, Statistics.kthQuartile(25, points), Statistics
                        .kthQuartile(75, points), Statistics.max(points),
                        Statistics.min(points), Statistics.median(points)}
        };
        boxplot = new Boxplot(boxplotData, tablePos.getTableColumn().getText
                () + title, "", tablePos.getTableColumn().getText(), false);
        graphPane.getChildren().clear();
        graphPane.getChildren().add(boxplot.makeBoxPlot());
        boxplot.addData();
    }
}

