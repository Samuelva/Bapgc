package sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import database.DatabaseConn;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.imageio.ImageIO;

/* Deze class maakt een StackPane dat het inzage scherm bevat.
 */
public class ViewScreen extends StackPane{
    protected ChoiceBox plotChoiceBox;
    protected Button calculateBtn;
    protected Button plotBtn;
    protected Button savePlotBtn;
    protected Button exportBtn;
    protected Text qualityText;
    protected Text cohenText;
    protected Text resultsText;
    protected Text statisticsText;
    protected TableView<String[]> pointsTable;
    protected TablePosition tablePos;
    protected Slider percentileSlider;
    protected Label percentileLabel;
    protected StackPane graphPane;
    protected Histogram barChart;
    protected Lijngrafiek lineGraph;
    protected Boxplot boxplot;
    protected Keuzemenu choiceMenu;
    protected String selectedGraph;
    protected WritableImage graphImage;
    protected boolean plotted;

    private String[][] gradeTable = null;
    private String[] questionLabels = null;
    private Object[][] questionData;
    private Integer[] examPoints;



    /* Deze functie zet het scherm in elkaar. Eerst het selectie gedeelte,
     * met een margin van 5 en een breedte van 150. Daarnaast wordt het
     * rechter gedeelte gezet dat de rest van het scherm opvult.
     */
    protected ViewScreen(){
        VBox selectionBox = makeSelectionBox();
        selectionBox.setMinWidth(150);
        selectionBox.setSpacing(20);
        HBox.setMargin(selectionBox, new Insets(5));
        VBox rightBox = makeRightBox();
        rightBox.setSpacing(10);
        HBox.setHgrow(rightBox, Priority.ALWAYS);
        HBox.setMargin(rightBox, new Insets(5));
        HBox mainBox = new HBox(selectionBox, rightBox);
        mainBox.setSpacing(20);
        setLoadEvent();
        setExportEvent();
        this.getChildren().add(mainBox);
    }

    /* Deze functie maakt een de dropdown menu's aan die gebruikt kunnen
     * worden voor het selecteren van een toets. Ook wordt er een knop
     * aangemaakt die gebruikt wordt voor het laden van een toets.
     */
    private VBox makeSelectionBox(){
        Label label = new Label("Keuzemenu");
        label.setFont(new Font("Arial", 18));
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(150);
        choiceMenu = new Keuzemenu();
        Region fill = new Region();
        VBox.setVgrow(fill, Priority.ALWAYS);
        return new VBox(label, choiceMenu.getChoiceMenuBox());
    }
    
    /* Deze functie maakt de HBox waarin het percentiel gekozen kan worden.
     * Eerst wordt er een label "percentiel" neergezet, vervolgens een
     * slider en als laatst een label dat het percentiel weergeeft. 
     * Er wordt daarna nog een Listener toegevoegd aan de slider die
     * het percentiel label veranderd als de slider verandered.
     */
    private HBox makePercentileBox(){
        Label label = new Label("Percentiel:");
        this.percentileSlider = new Slider(0, 1, 0.95);
        this.percentileSlider.setShowTickLabels(true);
        this.percentileSlider.setMajorTickUnit(0.25);
        this.percentileLabel = new Label("0.95");
        this.percentileSlider.valueProperty().addListener(
                new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, 
                    Number oldValue, Number newValue) {
                percentileLabel.setText(String.format("%.2f", newValue));
            }
        });
        return new HBox(label, this.percentileSlider, this.percentileLabel);
    }
    
    /* Deze functie maakt de VBox aan waarin de text neergezet wordt
     * met statistieken over de betrouwbaarheid van de toets.
     * Eerst wordt er een kop geplaatst dan een stuk text, vervolgens
     * nog een kop en dan nog een stuk text.
     * Deze stukken text moeten vervangen worden met de berekende 
     * statistieken.
     * Onder de text staat een HBox waarbinnen een percentiel gekozen kan
     * worden en daaronder staat een knop om het berkeken uit te voeren.
     */
    private VBox makeTopLeftBox(){
        Label firstLabel = new Label("Betrouwbaarheid Toets");
        firstLabel.setFont(new Font("Arial", 18));
        this.qualityText = new Text("Variantie vragen:\nVariantie Toets:\n"
                + "Cronbach alfa:\n");
        Label secondLabel = new Label("Cohen-Schotanus");
        secondLabel.setFont(new Font("Arial", 18));
        this.cohenText = new Text("Punten percentiel:\nGemiddelde punten "
                + "percentiel:\nCohen-Schotanus cesuur:\n");
        HBox percentileBox = makePercentileBox();
        this.calculateBtn = new Button("Bereken");
        this.calculateBtn.setPrefHeight(30);
        this.calculateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateCohen();
            }
        });
        return new VBox(firstLabel, this.qualityText, secondLabel, 
                this.cohenText, percentileBox, this.calculateBtn);
    }
    
    /* Deze functie maakt een VBox aan waarin statistieken van de gemaakte
     * toets weergegeven worden. Twee maal staat er een kop met een stuk
     * test eronder. Deze text moet veranderd worden naar de berekende
     * statistieken voor de toets of een vraag als er een vraag
     * geselecteerd word.
     */
    private VBox makeTopMiddleBox(){
        Label statisticsLabel = new Label("Statistieken");
        statisticsLabel.setFont(new Font("Arial", 18));
        this.statisticsText = new Text("Aantal vragen:\nMaximum punten:\n"
                + "Punten door gokkans:\nTotaal te verdienen:\nBeheersgraad:\n"
                + "Cesuur:\n");
        Label resultsLabel = new Label("Resultaten");
        resultsLabel.setFont(new Font("Arial", 18));
        this.resultsText = new Text("Aantal deelnemers:\nAantal voldoendes:\n"
                + "Aantal onvoldoendes:\nRendement:\nGemiddelde cijfer:");
        return new VBox(statisticsLabel, this.statisticsText, resultsLabel, 
                this.resultsText);
    }
    
    /* Deze functie maakt een VBox aan die een afbeelding weergeeft van 
     * 400x250. Daaronder staan dropdown menu's om mee te kiezen welke
     * afbeelding weergegeven moet worden. Een voor het soort plot en een
     * voor de data die gebruikt moet worden (een vraag, cijfer of totaal).
     * Naast de dropdowns staat een knop om de afbeelding op te slaan.
     */
    private VBox makeTopRightBox(){
        this.graphPane = new StackPane();
        this.graphPane.setPrefWidth(400);
        this.graphPane.setPrefHeight(250);
        this.graphPane.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID, null, null)));

        return new VBox(this.graphPane, makeGraphButtons());
    }

    /* Maakt de grafiek keuze en opslaan knop, plaatst deze in een box, en
     * returned ze.
     */
    private HBox makeGraphButtons() {
        this.plotChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                "Histogram", "Lijngrafiek", "Boxplot"));
        this.plotChoiceBox.setOnAction(event -> {
            this.selectedGraph = (String) plotChoiceBox.getValue();
            if (tablePos != null) {
                updateGraph();
            }
        });
        this.plotChoiceBox.setValue("Histogram");
        this.selectedGraph = "Histogram";
        this.plotChoiceBox.setMaxWidth(150);
        this.plotChoiceBox.setMinHeight(30);

        this.savePlotBtn = new Button("Grafiek opslaan");
        this.savePlotBtn.setMaxWidth(150);
        this.savePlotBtn.setMinHeight(30);
        this.savePlotBtn.setOnAction(e -> {
            if (selectedGraph != null && plotted) {
                displaySaveDialog();
            }
            else {
                displayInformationDialog();
            }
        });
        HBox fillBox = new HBox();
        HBox.setHgrow(fillBox, Priority.ALWAYS);

        HBox hBox = new HBox(this.plotChoiceBox, fillBox, this.savePlotBtn);
        hBox.setPadding(new Insets(5, 0, 0, 0));

        return hBox;
    }
    
    /* Deze functie zet het bovenste gedeelte van het scherm in elkaar
     * hierbij worden de twee statistiek stukken (TopLeft en TopMiddle)
     * gevruikt om de hele breedte te vullen.
     * Het bovenste gedeelte wordt als HBox terug gegeven.
     */
    private HBox makeTopBox(){
        VBox leftBox = makeTopLeftBox();
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        VBox middleBox = makeTopMiddleBox();
        HBox.setHgrow(middleBox, Priority.ALWAYS);
        VBox rightBox = makeTopRightBox();
        return new HBox(leftBox, middleBox, rightBox);
    }
    
    /* Deze functie maakt het stuk tussen de statistieken en de tabel in
     * een HBox. Eerst wordt er een knop aangemaakt met een breedte van 235.
     * Daarnaast wordt een label met "vragen" neergezet. Om deze label heen
     * staan twee regio's die er voor zorgen dat het label gecentreerd staat.
     */
    private HBox makeMiddleBox(){
        this.exportBtn = new Button("Exporteer CSV");
        this.exportBtn.setPrefWidth(240);
        this.exportBtn.setPrefHeight(30);
        Region leftFill = new Region();
        HBox.setHgrow(leftFill, Priority.ALWAYS);
        Label questionLabel = new Label("Vragen");
        questionLabel.setFont(new Font("Arial", 18));
        Region rightFill = new Region();
        HBox.setHgrow(rightFill, Priority.ALWAYS);
        return new HBox(this.exportBtn, leftFill, questionLabel, rightFill);
    }

    private void setExportEvent(){
        this.exportBtn.setOnAction(e -> {
            if (this.gradeTable != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV (*.csv)",
                        "*.csv"));
                fileChooser.setTitle("Opslaan Als");
                File file = fileChooser.showSaveDialog(new Stage());
                if (file != null) {
                    csvExport(this.gradeTable, questionLabels, file);
                }
            }
        });
    }

    /* Deze functie schrijft maakt een CSV bestand van de labels en scores die meegegeven worden
       in het bestand dat meegegeven wordt.
     */
    private void csvExport(String[][] scores, String[] labels, File file){
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("Studentnr;Cijfer;Totaal;" + String.join(";", labels) + "\n");
            for (String[] student: scores){
                writer.write(String.join(";", student) + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /* Deze functie maakt de tabel waar de punten in gezet moeten worden.
     * Deze kan niet gewijzigd worden en vuld de rest van het scherm.
     *     
     * Aangezien ik nog niet weet wat voor soort data (een class, string[],
     * of iets anders?) gebruikt gaat worden en omdat het aantal kolomen,
     * afhangt van de toets zijn er nog geen kolomen in de tabel gezet.
     *
     * Er staat een stuk onder voor het testen van het scherm. Hier worden
     * kolomen toegevoegd en een aantal rijen. Dit is vrijwel leterlijk
     * overgenomen van het internet en moet dus aangepast worden!
     */
    private void makeTable(){
        this.pointsTable = new TableView<>();
        VBox.setVgrow(this.pointsTable, Priority.ALWAYS);
        this.pointsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.pointsTable.getSelectionModel().setCellSelectionEnabled(true);
        this.pointsTable.getFocusModel().focusedCellProperty().addListener(new ChangeListener<TablePosition>() {
            @Override
            public void changed(ObservableValue<? extends TablePosition> observable, TablePosition oldValue,
                                TablePosition newValue) {
                columnSelectionChange(newValue);
            }
        });
        pointsTable.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) pointsTable.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                        Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });
    }

    private void columnSelectionChange(TablePosition newValue) {
        if (newValue.getTableColumn() != null) {
            pointsTable.getSelectionModel().selectRange(0, newValue.getTableColumn(),
                    pointsTable.getItems().size(), newValue.getTableColumn());
            tablePos = newValue;
            if (newValue.getColumn() < 3){
                if (newValue.getColumn() == 1) {
                    graphUpdateGrades(newValue);
                } else if (newValue.getColumn() == 2) {
                    graphUpdateTotal(newValue);
                }
                examSelectedUpdate();
            } else {
                graphUpdateQuestions(newValue);
                questionSelectedUpdate(newValue.getColumn());
            }
        }
    }

    /*
     * Deze functie zorgt ervoor dat de grafieksoort gelijk veranderd wordt
     * als deze wordt aangepast in de grafiek keuze combobox.
     */
    private void updateGraph() {
        if (tablePos.getColumn() < 3) {
            if (tablePos.getColumn() == 1) {
                graphUpdateGrades(tablePos);
            } else if (tablePos.getColumn() == 2) {
                graphUpdateTotal(tablePos);
            }
        } else {
            graphUpdateQuestions(tablePos);
        }
    }

    /*
     * Roept de functies aan om de grafieken te maken als de cijfer kolom
     * geselecteerd is.
     */
    private void graphUpdateGrades(TablePosition newValue) {
        if (selectedGraph == "Histogram") {
            plotHistogram(newValue, "Cijfer", "Cijfer per student");
        } else if (selectedGraph == "Lijngrafiek") {
            plotLineGraph(newValue, "Cijfer", "Cijfer per student");
        } else if (selectedGraph == "Boxplot") {
            plotBoxplot(newValue, "", "Cijfer", "Boxplot ");
        }
    }

    /*
     * Roept de functies aan om de grafieken te maken als de totaal kolom
     * geselecteerd is.
     */
    private void graphUpdateTotal(TablePosition newValue) {
        if (selectedGraph == "Histogram") {
            plotHistogram(newValue, "Punten", "Totaal aantal punten per " +
                    "student");
        } else if (selectedGraph == "Lijngrafiek") {
            plotLineGraph(newValue, "Punten", "Totaal aantal punten per " +
                    "student");
        } else if (selectedGraph == "Boxplot") {
            plotBoxplot(newValue, "", "Punten", "Boxplot punten ");
        }
    }

    /*
     * Roept de functies aan om de grafieken te maken als een vraag kolom
     * geselecteerd is.
     */
    private void graphUpdateQuestions(TablePosition newValue) {
        if (selectedGraph == "Histogram") {
            plotHistogram(newValue, "Punten", "Vraagpunten per student");
        } else if (selectedGraph == "Lijngrafiek") {
            plotLineGraph(newValue, "Punten", "Vraagpunten per student");
        } else if (selectedGraph == "Boxplot") {
            plotBoxplot(newValue, "Vraag ", "Punten", "Boxplot vraag ");
        }
    }

    /* Bereken de statistieken voor de geselecteerde vraag en update de weergave.
     */
    private void questionSelectedUpdate(int i) {
        int[] points = Statistics.stringToIntArray(Statistics.getColumn(i, gradeTable), 0);
        int[] total = Statistics.stringToIntArray(Statistics.getColumn(2, gradeTable), 0);
        double average = Statistics.mean(points);
        String counts;
        updateStats(questionData[i-3][2].toString(), (boolean) questionData[i-3][3],
                Integer.toString(Statistics.max(points)), Integer.toString(Statistics.min(points)),
                Integer.toString(Statistics.count((int) questionData[i-3][2], points)),
                Integer.toString(Statistics.count(0, points)),
                Double.toString(Statistics.round(average, 2)),
                Double.toString(Statistics.round(Statistics.var(points), 2)),
                Double.toString(Statistics.round(Statistics.correlation(points, total), 2)),
                Double.toString(Statistics.round(average /((int) questionData[i-3][2]), 2)));
    }

    /* Bereken de statistieken voor de geselecteerde toets en update de weergave.
     */
    private void examSelectedUpdate() {
        double[] grades = Statistics.stringToDoubleArray(Statistics.getColumn(1, gradeTable), 0);
        int passes = Statistics.getPasses(grades);
        int fails = gradeTable.length - passes;
        double performance = Statistics.percentage(passes, gradeTable.length);
        updateStats(Integer.toString(questionLabels.length),
                Integer.toString(this.examPoints[1]), Integer.toString(this.examPoints[2]),
                Integer.toString(this.examPoints[1]-this.examPoints[2]),
                Double.toString(Statistics.round(Statistics.percentage(examPoints[0]-this.examPoints[2],
                        this.examPoints[1]),2)),
                Integer.toString(examPoints[0]), Integer.toString(gradeTable.length),
                Integer.toString(passes), Integer.toString(fails), Double.toString(performance),
                Double.toString(Statistics.round(Statistics.mean(grades), 2)));
    }

    /* Deze functie zet het rechter gedeelte van het scherm in elkaar.
     * Eerst het statistieken gedeelte, daaronder het tussenstuk en dan
     * de tabel. Het statistieken gedeelte heeft een vaste grote (300).
     * Dit alles wordt als VBox terug gegeven.
     */
    private VBox makeRightBox(){
        HBox topBox = makeTopBox();
        topBox.setPrefHeight(280);
        HBox middleBox = makeMiddleBox();
        makeTable();
        return new VBox(topBox, middleBox, this.pointsTable);
    }

    /* Deze functie update de statistieken voor de weergegeven toets.
     */
    protected void updateStats(String questions, String maxPoints, String guessPoints, String earnablePoints,
                               String degree, String threshold, String participants, String passes, String fails,
                               String performance, String average) {
        this.statisticsText.setText("Aantal vragen: " + questions + "\nMaximum punten: " + maxPoints +
                "\nPunten door gokkans: " + guessPoints + "\nTotaal te verdienen: " + earnablePoints + "" +
                "\nBeheersgraad: " + degree + "%\nCensuur: " + threshold + "\n");
        this.resultsText.setText("Aantal deelnemers: " + participants + "\nAantal voldoendes: " + passes +
                "\nAantal onvoldoendes: " + fails + "\nRendement: " + performance + "\nGemiddelde cijfer: " +
                average + "\n");
    }

    /* Deze functie update de statistieken voor de geselecteerde vraag.
     */
    protected void updateStats(String maxPoints,  boolean countsBoolean, String highGiven, String lowGiven,
                               String maxGiven, String noneGiven, String average, String varPoints, String r, String p){
        String counts;
        if (countsBoolean){
            counts = "Ja";
        } else {
            counts = "Nee";
        }
        this.statisticsText.setText("Maximum punten: " + maxPoints + "\nMeerekenen: " + counts + "\n");
        this.resultsText.setText("Hoogste behaald: " + highGiven +
                "\nLaagste behaald: " + lowGiven + "\nAantal met maximum punten: " + maxGiven +
                "\nAantal met nul punten: " + noneGiven + "\nGemiddelde punten behaald: " + average +
                "\nVariantie gehaalde punten: "+ varPoints + "\nR(item-rest): " + r + "\np-waarde: " + p + "\n\n");
    }

    /* Deze functie update het kwaliteits gedeelte van de statistieken.
     */
    protected void updateQualityStats() {
        double varQuestions = Statistics.varianceQuestions(gradeTable);
        double varTest = Statistics.var(Statistics.stringToIntArray(Statistics.getColumn(2, gradeTable),
                0));
        double cronbach = Statistics.cronbach(questionData.length, varQuestions, varTest);
        this.qualityText.setText("Variantie vragen: " + Statistics.round(varQuestions, 2) +
                "\nVariantie Toets: " + Statistics.round(varTest, 2) + "\nCronbach alfa: " +
                Statistics.round(cronbach, 2) + "\n");
    }

    /* Deze functie update het Cohen-Schotanus gedeelte van de statistieken
     */
    protected void updateCohen() {
        if (! (this.gradeTable == null)) {
            int[] total = Statistics.stringToIntArray(Statistics.getColumn(2, gradeTable), 0);
            double percentilePoints = Statistics.kthPercentile(this.percentileSlider.getValue(), total);
            double average = Statistics.percentileMean(total, percentilePoints);
            double cohen = Statistics.cohen(average,
                    Statistics.percentage(this.examPoints[0] - this.examPoints[2],
                            this.examPoints[1]) / 100, this.examPoints[2]);
            this.cohenText.setText("Punten percentiel: " + Statistics.round(percentilePoints, 2) +
                    "\nGemiddelde punten percentiel: " + Statistics.round(average, 2) +
                    "\nCohen-Schotanus censuur: " + Statistics.round(cohen, 2) + "\n");
        }
    }

    /* Deze functie maakt de tabel leeg en de kolommen aan.
     */
    protected void setupTable(String[] columns) {
        this.pointsTable.getItems().clear();
        this.pointsTable.getColumns().clear();
        String[] columnsTotal = new String[columns.length + 3];
        columnsTotal[0] = "Student nr.";
        columnsTotal[1] = "Cijfer";
        columnsTotal[2] = "Totaal";
        for (int i = 0; i < columns.length; i++){
            columnsTotal[i+3] = columns[i];
        }
        for (int i = 0; i < columnsTotal.length; i++) {
            TableColumn column = makeColumn(i, columnsTotal[i]);
            this.pointsTable.getColumns().add(column);
        }
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private TableColumn makeColumn(int i, String columnLabel) {
        TableColumn column = new TableColumn(columnLabel);
        final int INDEX = i;
        column.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<String[], String> values) {
                return new SimpleStringProperty((values.getValue()[INDEX]));
            }
        });
        if (i < 3){
            column.setMinWidth(80);
            column.setMaxWidth(80);
        } else {
            column.setMinWidth(40);
            column.setMaxWidth(40);
        }
        return column;
    }

    protected void fillTable(int examID){
        /**
         * Deze methode vult de tabel.
         * De vragen, maximum aantal punten, punten door gok kans en cesuur
         * worden opgehaald voor de meegegeven toets. De scores die behaald
         * zijn voor de vragen worden ook opgehaald en de cijfer worden hier
         * direct berekend. Dit resulteerd in een matrix met alle data die
         * in de tabel moet komen. Als er hier een EcptyStackException of een
         * NumberFormatException plaatsvind, is er geen data bekend voor
         * de toets en wordt er een waarschuwing getoond. Anders
         * worden de label van de vragen uit de eerder opgehaald vraag data
         * gehaald en deze gebruikt voor het aanmaken van de kolomen met
         * de methode setupTable. Vervolgens wordt de matrix aan de tabel
         * toegevoegd om er data in te zetten en de de kwaliteits statistieken
         * geupdate.
         */
        DatabaseConn d = new DatabaseConn();
        try {
            this.questionData = d.GetVragenVanToets(examID);
            this.examPoints = d.GetCesuurMaxGok(examID);
            this.gradeTable = Statistics.updateGradeTableArray(d.GetStudentScores(examID), this.examPoints[0],
                    this.examPoints[1]);
        } catch (EmptyStackException | NumberFormatException e) {
            warnNoData();
            return;
        }
        this.questionLabels = Statistics.getColumn(1, questionData);
        setupTable(this.questionLabels);
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(this.gradeTable));
        this.pointsTable.setItems(data);
        updateQualityStats();
        d.CloseConnection();
    }

    private void warnNoData() {
        /**
         * Deze methode toont een pop-up met een waarchuwing als er geen
         * data bekend is voor een toets die geladen wordt.
         */
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Waarschuwing!");
        alert.setHeaderText("Er is geen data bekend voor deze toets!");
        alert.setContentText("U kunt alleen toetsen waarvoor scores bekend zijn inzien.");
        alert.showAndWait();
    }

    private void plotHistogram(TablePosition newValue, String yLabel, String
            title) {
        /**
         * Deze functie maakt en vult de histogram met de geselecteerde data
         * uit de kolom.
         */
        barChart = new Histogram("Student", yLabel, title, "");
        graphPane.getChildren().clear();
        graphPane.getChildren().add(barChart.getBarChartBox());

        for (int i = 0; i < pointsTable.getItems().size(); i++) {
            barChart.addBar(pointsTable.getColumns().get(0)
                    .getCellObservableValue(i).getValue().toString(), Double
                    .parseDouble((String) newValue.getTableColumn()
                            .getCellObservableValue(i).getValue()));
        }

        plotted = true;
    }

    private void plotLineGraph(TablePosition newValue, String yLabel, String
            title) {
        /**
         * Deze functie maakt en vult de lijngrafiek met de geselecteerde data
         * uit de kolom.
         */
        lineGraph = new Lijngrafiek("Student", yLabel, title);
        graphPane.getChildren().clear();
        graphPane.getChildren().add(lineGraph.getLineChartBox());

        String[] xValues = new String[pointsTable.getItems().size()];
        double[] yValues = new double[pointsTable.getItems().size()];

        for (int i = 0; i < pointsTable.getItems().size(); i++) {
            xValues[i] = pointsTable.getColumns().get(0)
                    .getCellObservableValue(i).getValue().toString();
            yValues[i] = Double.parseDouble((String) newValue.getTableColumn()
                    .getCellObservableValue(i).getValue());
        }

        lineGraph.addLine(xValues, yValues, "");
        plotted = true;
    }

    private void plotBoxplot(TablePosition newValue, String xLabelI, String
            yLabel, String title) {
        /**
         * Maakt en vult de boxplot met geselecteerde data.
         * Voor alle data in de geselecteerde kolom wordt het minimum,
         * maximum, 1e kwartiel, 2e kwartiel en mediaan bepaald, welke
         * vervolgens geplot worden.
         */
        double[] points = new double[pointsTable.getItems().size()];
        for (int i=0; i<pointsTable.getItems().size(); i++) {
            points[i] = Double.parseDouble((String) newValue.getTableColumn()
                    .getCellObservableValue(i).getValue());
        }

        double[][] boxplotData = new double[][] {
                {1, Statistics.kthQuartile(25, points), Statistics
                        .kthQuartile(75, points), Statistics.max(points),
                        Statistics.min(points), Statistics.median(points)}
        };

        String xLabel = new String(xLabelI + newValue.getTableColumn()
                .getText());
        String graphTitle = new String(title + newValue.getTableColumn()
                .getText());

        boxplot = new Boxplot(boxplotData, graphTitle, xLabel, yLabel,
                false);
        graphPane.getChildren().clear();
        graphPane.getChildren().add(boxplot.makeBoxPlot());
        boxplot.addData();
        plotted = true;
    }

    private void displaySaveDialog() {
        /**
         * Deze functie toont een scherm om de grafieken te kunnen opslaan.
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

    private void saveGraph(File savePath) {
        /**
         * Deze functie veranderd de geselecteerde grafiek in een image
         * bestand en schrijft deze weg als een png. Mocht dit wegschrijven
         * een error opleveren, dan wordt er een error getoond.
         */
        BarChart<String, Number> barChartGraph;
        LineChart<String, Number> lineChartGraph;
        CandleStickChart boxPlotGraph;
        if (selectedGraph == "Histogram") {
            barChartGraph = barChart.getBarChart();
            graphImage = barChartGraph.snapshot(new SnapshotParameters(),
                    null);
        } else if (selectedGraph == "Lijngrafiek") {
            lineChartGraph = lineGraph.getLineChart();
            graphImage = lineChartGraph.snapshot(new SnapshotParameters(),
                    null);
        } else if (selectedGraph == "Boxplot") {
            boxPlotGraph = boxplot.getBoxPlot();
            graphImage = boxPlotGraph.snapshot(new SnapshotParameters(),
                    null);
        }
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(graphImage, null),
                    "png", savePath);
        } catch (IOException e) {
            displayErrorDialog();
        }
    }

    private void displayInformationDialog() {
        /**
         * Als er geen grafiek aangemaakt is, wordt er een informatie scherm
         * getoond.
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

    private void displayErrorDialog() {
        /**
         * Als het wegschrijven van het png bestand fout gaat, wordt er een
         * error scherm getoond met wat informatie.
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

    public void setLoadEvent(){
        /**
         * Deze methode zorgt ervoor dat als er op de toetsweergave knop gedrukt wordt
         * het ID van de geselecteerde toets opgehaald wordt en dat de fillTable
         * aangeroepen wordt.
         */
        this.choiceMenu.examLoadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DatabaseConn d = new DatabaseConn();
                List<String> selection = choiceMenu.getSelection();
                int id = d.GetToetsID(selection.get(0), selection.get(1), selection.get(2), selection.get(3),
                        selection.get(4), selection.get(5));
                fillTable(id);
                d.CloseConnection();
            }
        });
    }
}