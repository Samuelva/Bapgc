package sample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
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
    protected ChoiceBox schoolYearChoiceBox;
    protected ChoiceBox yearChoiceBox;
    protected ChoiceBox blockChoiceBox;
    protected ChoiceBox courseChoiceBox;
    protected ChoiceBox typeChoiceBox;
    protected ChoiceBox attemptChoiceBox;
    protected ChoiceBox plotChoiceBox;
    protected Button loadBtn;
    protected Button calculateBtn;
    protected Button plotBtn;
    protected Button savePlotBtn;
    protected Button exportBtn;
    protected Text qualityText;
    protected Text cohenText;
    protected Text resultsText;
    protected Text statisticsText;
    protected TableView<String[]> pointsTable;
    protected Slider percentileSlider;
    protected Label percentileLabel;
    protected StackPane graphPane;
    protected Histogram barChart;
    protected Boxplot boxplot;
    protected Cirkeldiagram pieChart;
    protected int activeGraphInt;
    protected WritableImage graphImage;

    private String[][] gradeTable = null;
    private String[] questionLabels = null;
    private int threshold;
    private int maxPoints;
    private int guessPoints;
    private Object[][] questionData;

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
        this.getChildren().add(mainBox);
    }

    /* Deze functie maakt een de dropdown menu's aan die gebruikt kunnen
     * worden voor het selecteren van een toets. Ook wordt er een knop
     * aangemaakt die gebruikt wordt voor het laden van een toets.
     */
    private VBox makeSelectionBox(){
        //label
        Label label = new Label("Keuzemenu");
        label.setFont(new Font("Arial", 18));
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(150);
        //Dropdown voor jaar
        this.yearChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                "Jaar", new Separator(), "2016/2017"));
        this.yearChoiceBox.setValue("Jaar");
        this.yearChoiceBox.setPrefWidth(150);
        this.yearChoiceBox.setPrefHeight(30);
        //Dropdown voor school jaar
        this.schoolYearChoiceBox = new ChoiceBox(
                FXCollections.observableArrayList(
                        "Leerjaar", new Separator(), "Jaar 1", "Jaar 2", "Jaar 3",
                        "Jaar 4"));
        this.schoolYearChoiceBox.setValue("Leerjaar");
        this.schoolYearChoiceBox.setPrefWidth(150);
        this.schoolYearChoiceBox.setPrefHeight(30);
        //Dropdown voor periode
        this.blockChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                "Periode", new Separator(), "Periode 1", "Periode 2", "Periode 3", 
                "Periode 4", "Periode 5"));
        this.blockChoiceBox.setValue("Periode");
        this.blockChoiceBox.setPrefWidth(150);
        this.blockChoiceBox.setPrefHeight(30);
        //Dropdown voor module
        this.courseChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                "Module", new Separator(), "placeholder"));
        this.courseChoiceBox.setValue("Module");
        this.courseChoiceBox.setPrefWidth(150);
        this.courseChoiceBox.setPrefHeight(30);
        //Dropdown voor toetsvorm
        this.typeChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                "Toetsvorm", new Separator(), "Theorietoets", "Praktijktoets", 
                "Logboek", "Aanwezigheid", "Project"));
        this.typeChoiceBox.setValue("Toetsvorm");
        this.typeChoiceBox.setPrefWidth(150);
        this.typeChoiceBox.setPrefHeight(30);
        //Dropdown voor gelegenheid
        this.attemptChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                "Gelegenheid", new Separator(), "1e kans", "2e kans"));
        this.attemptChoiceBox.setValue("Gelegenheid");
        this.attemptChoiceBox.setPrefWidth(150);
        this.attemptChoiceBox.setPrefHeight(30);
        //leege ruimte
        Region fill = new Region();
        VBox.setVgrow(fill, Priority.ALWAYS);
        //laad knop
        this.loadBtn = new Button("Laad toets");

//        //HIER MOET DE CODE VOOR ALS ER TOETS GELADEN WORDT!!
//        this.loadBtn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                int examID = 1; //HIER MOET HER ID VAN DE IN HET KEUZEMENU GESELECTEERDE TOETS OPGEHAALD WORDEN!!!!!!
//                fillTable(examID);
//
//                updateQualityStats();
//            }
//        });

        this.loadBtn.setPrefWidth(150);
        this.loadBtn.setPrefHeight(30);
        return new VBox(label, this.yearChoiceBox, 
                this.schoolYearChoiceBox,this.blockChoiceBox, 
                this.courseChoiceBox, this.typeChoiceBox, this.attemptChoiceBox,
                fill, this.loadBtn);
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
                + "percentiel:\nCohen-Schotanus censuur:\n");
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
                + "Censuur:\n");
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
        this.graphPane.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, null, null)));
        this.plotChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                        "Boxplot", "Histogram", "Cirkeldiagram"));
        this.plotChoiceBox.setOnAction(event -> {
            if (plotChoiceBox.getValue() == "Boxplot") {
                makeBoxplot();
                activeGraphInt = 1;
            } else if (plotChoiceBox.getValue() == "Histogram") {
                makeHistogram();
                activeGraphInt = 2;
            } else if (plotChoiceBox.getValue() == "Cirkeldiagram") {
                makePieChart();
                activeGraphInt = 3;
            }
        });
        this.plotChoiceBox.setValue("Boxplot");
        this.plotChoiceBox.setPrefWidth(133);
        this.plotChoiceBox.setPrefHeight(30);
        this.plotBtn = new Button("Plotten");
        this.plotBtn.setPrefWidth(133);
        this.plotBtn.setPrefHeight(30);
        this.savePlotBtn = new Button("Afbeelding opslaan");
        this.savePlotBtn.setPrefWidth(133);
        this.savePlotBtn.setPrefHeight(30);
        this.savePlotBtn.setOnAction(e -> {
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
        HBox hBox = new HBox(this.plotChoiceBox,
                this.plotBtn, this.savePlotBtn);
        return new VBox(this.graphPane, hBox);
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

        this.exportBtn.setOnAction(e -> {
            if (this.gradeTable != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV (*.csv)", "*.csv"));
            fileChooser.setTitle("Opslaan Als");
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                csvExport(this.gradeTable, questionLabels, file);
            }}
        });
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

    /* Deze functie schrijft maakt een CSV bestand van de labels en scores die meegegeven worden
       in het bestand dat meegegeven wordt.
     */
    private void csvExport(String[][] scores, String[] labels, File file){
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("Studentnr,Cijfer,Totaal," + String.join(";", labels) + "\n");
            for (String[] student: scores){
                writer.write(String.join(",", student) + "\n");
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
        this.pointsTable.setEditable(false);
        VBox.setVgrow(this.pointsTable, Priority.ALWAYS);
        this.pointsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.pointsTable.getSelectionModel().setCellSelectionEnabled(true);
        this.pointsTable.getFocusModel().focusedCellProperty().addListener(new ChangeListener<TablePosition>() {

            /* Als er op een cel gedrukt wordt, wordt de gehele kolom geselecteerd en de statistiek geupdate.
             * Als een van de eerste drie kolomen geselecteerd wordt worden de statistieken voor de hele toets
             * weergegeven, anders voor de specifieke vraag.
             */
            @Override
            public void changed(ObservableValue<? extends TablePosition> observable, TablePosition oldValue,
                                TablePosition newValue) {
                if (newValue.getTableColumn() != null) {
                    pointsTable.getSelectionModel().selectRange(0, newValue.getTableColumn(),
                            pointsTable.getItems().size(), newValue.getTableColumn());
                    if (newValue.getColumn() < 3){
                        examSelectedUpdate();
                    } else {
                        questionSelectedUpdate(newValue.getColumn());
                    }
                }
            }
        });
        pointsTable.widthProperty().addListener(new ChangeListener<Number>() {
            /* Zorg ervoor dat de kolomen niet van volgorde veranderd kunnen worden.
             */
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
                Integer.toString(maxPoints), Integer.toString(guessPoints),
                Integer.toString(maxPoints-guessPoints),
                Double.toString(Statistics.round(Statistics.percentage(threshold-guessPoints, maxPoints),
                        2)),
                Integer.toString(threshold), Integer.toString(gradeTable.length),
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
                    Statistics.percentage(threshold - guessPoints, maxPoints) / 100, this.guessPoints);
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
            TableColumn column = new TableColumn(columnsTotal[i]);
            final int index = i;
            column.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> values) {
                    return new SimpleStringProperty((values.getValue()[index]));
                }
            });
            if (i < 3){
                column.setMinWidth(80);
                column.setMaxWidth(80);
            } else {
                column.setMinWidth(40);
                column.setMaxWidth(40);
            }
            this.pointsTable.getColumns().add(column);
        }
    }

    /* Deze functie vult de tabel in.
     */
    protected void fillTable(int examID){
        DatabaseConn d = new DatabaseConn();
        this.questionData = d.GetVragenVanToets(examID);
        this.questionLabels = Statistics.getColumn(1, questionData);
        setupTable(this.questionLabels);
        Integer[] examPoints = d.GetCesuurMaxGok(examID);
        this.threshold = examPoints[0];
        this.maxPoints = examPoints[1];
        this.guessPoints = examPoints[2];
        this.gradeTable = Statistics.updateGradeTableArray(d.GetStudentScores(examID), this.threshold, this.maxPoints);
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(this.gradeTable));
        this.pointsTable.setItems(data);
        d.CloseConnection();
    }


    protected void makeHistogram() {
        barChart = new Histogram("x-as", "y-as", "Titel", "Histogram");
        barChart.makeBarChart();
        graphPane.getChildren().clear();
        graphPane.getChildren().add(barChart.getBarChart());
        barChart.addBar("bar 1", 8);
        barChart.addBar("bar 2", 9);
        barChart.addBar("bar 3", 5);
        barChart.addBar("bar 4", 6);
        barChart.addBar("bar 5", 8);
    }

    protected void makeBoxplot() {
        boxplot = new Boxplot();
        graphPane.getChildren().clear();
        graphPane.getChildren().add(boxplot.makeBoxPlot());
        boxplot.addData();
    }
    protected void makePieChart() {
        String[] names = new String[] {"Voldoendes", "Onvoldoendes"};
        int[] values = new int[] {30, 59};

        pieChart = new Cirkeldiagram("test titel");
        graphPane.getChildren().clear();
        graphPane.getChildren().add(pieChart.makePieChart());
        pieChart.addData(names, values);
    }

    public void saveGraph() {
        BarChart<String, Number> barChartGraph;
        LineChart<String, Number> lineChartGraph;
        PieChart pieChartGraph;
        switch (activeGraphInt) {
            case 1:
                barChartGraph = barChart.getBarChart();
                graphImage = barChartGraph.snapshot(new SnapshotParameters(), null);
                break;
            case 2:
                barChartGraph = barChart.getBarChart();
                graphImage = barChartGraph.snapshot(new SnapshotParameters(), null);
                break;
            case 3:
                pieChartGraph = pieChart.getPieChart();
                graphImage = pieChartGraph.snapshot(new SnapshotParameters(), null);
                break;
        }
    }
    public String[] getSelectionProperties() {
            String[] properties = new String[6];


            if (yearChoiceBox.getValue().equals("Jaar"))
                return null;
            if (schoolYearChoiceBox.getValue().equals("Leerjaar"))
                return null;
            if (blockChoiceBox.getValue().equals("Periode"))
                return null;
            if (courseChoiceBox.getValue().equals("Module"))
                return null;
            if (typeChoiceBox.getValue().equals("Toetsvorm"))
                return null;
            if (attemptChoiceBox.getValue().equals("Gelegenheid"))
                return null;

            properties[0] = (String) courseChoiceBox.getValue();
            properties[1] = (String) yearChoiceBox.getValue();
            properties[2] = (String) schoolYearChoiceBox.getValue();
            properties[3] = (String) blockChoiceBox.getValue();
            properties[4] = (String) typeChoiceBox.getValue();
            properties[5] = (String) attemptChoiceBox.getValue();


            return properties;
        }
}