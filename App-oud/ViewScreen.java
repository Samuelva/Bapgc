/* Changelog:
 * 01-12-2016    Davy Cats   Start basis script schrijven.
 * 04-12-2016    Davy Cats   Basis script af.
 * 08-12-2016    Davy Cats   Layout aangepast.
 * 09-12-2016    Davy Cats   ChoiceBox labels aangepast.
 *
 * Deze class maakt een StackPane dat het inzage scherm bevat.
 */
package sample;

import java.util.Arrays;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 *
 * @author davy
 */
public class ViewScreen extends StackPane{
    protected ChoiceBox schoolYearChoiceBox;
    protected ChoiceBox yearChoiceBox;
    protected ChoiceBox blockChoiceBox;
    protected ChoiceBox courseChoiceBox;
    protected ChoiceBox typeChoiceBox;
    protected ChoiceBox attemptChoiceBox;
    protected ChoiceBox plotChoiceBox;
    protected ChoiceBox questionChoiceBox;
    protected Button loadBtn;
    protected Button calculateBtn;
    protected Button savePlotBtn;
    protected Button exportBtn;
    protected Text qualityText;
    protected Text cohenText;
    protected Text resultsText;
    protected Text statisticsText;
    protected TableView<String[]> pointsTable;
    protected Slider percentileSlider;
    protected Label percentileLabel;
    protected ImageView plotImage;
    
    
    /* Deze functie maakt een de dropdown menu's aan die gebruikt kunnen
     * worden voor het selecteren van een toets. Ook wordt er een knop
     * aangemaakt die gebruikt wordt voor het laden van een toets.
     */
    private VBox makeSelectionBox(){
        //label
        Label label = new Label("Keuzemenu");
        label.setFont(new Font("Arial", 20));
        label.setAlignment(Pos.CENTER);
        label.setPrefWidth(150);
        //Dropdown voor jaar
        this.yearChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                "Jaar", new Separator(), "placeholder"));
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
        this.loadBtn.setPrefWidth(150);
        this.loadBtn.setPrefHeight(30);
        return new VBox(label, this.yearChoiceBox, 
                this.schoolYearChoiceBox,this.blockChoiceBox, 
                this.courseChoiceBox, this.typeChoiceBox, this.attemptChoiceBox,
                fill, this.loadBtn);
    };
    
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
        this.qualityText = new Text("Variantie vragen:\nVariantie Toets\n"
                + "Cronbach alfa:\n");
        Label secondLabel = new Label("Cohen-Schotanus");
        secondLabel.setFont(new Font("Arial", 18));
        this.cohenText = new Text("Punten percentiel:\nGemiddelde punten "
                + "percentiel:\nCohen-Schotanus censuur:\n");
        HBox percentileBox = makePercentileBox();
        this.calculateBtn = new Button("Bereken");
        this.calculateBtn.setPrefHeight(30);
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
        this.plotImage = new ImageView("/placeholder.PNG");
        this.plotImage.setFitWidth(400);
        this.plotImage.setFitHeight(250);
        this.plotChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                        "Boxplot", "Histogram"));
        this.plotChoiceBox.setValue("Boxplot");
        this.plotChoiceBox.setPrefWidth(133);
        this.plotChoiceBox.setPrefHeight(30);
        this.questionChoiceBox = new ChoiceBox(FXCollections.observableArrayList(
                        "Cijfer", "Totaal", new Separator(), "placeholder"));
        this.questionChoiceBox.setValue("Cijfer");
        this.questionChoiceBox.setPrefWidth(133);
        this.questionChoiceBox.setPrefHeight(30);
        this.savePlotBtn = new Button("Afbeelding opslaan");
        this.savePlotBtn.setPrefWidth(133);
        this.savePlotBtn.setPrefHeight(30);
        HBox hBox = new HBox(this.questionChoiceBox, this.plotChoiceBox,
                this.savePlotBtn);
        return new VBox(this.plotImage, hBox);
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
        
        //test
        String[][] staffArray = {{"Student nr.", "Cijfer", "Totaal", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"},
            {"s0000000", "1", "2","1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}};
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(staffArray));
        data.remove(0);//remove titles from data
        for (int i = 0; i < staffArray[0].length; i++) {
            TableColumn tc = new TableColumn(staffArray[0][i]);
            final int colNo = i;
            tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[colNo]));
                }
            });
            if (i < 3){
                tc.setMinWidth(80);
                tc.setMaxWidth(80);
            } else {
                tc.setMinWidth(25);
                tc.setMaxWidth(25);
            }
            tc.setSortable(false);
            this.pointsTable.getColumns().add(tc);
        }
        this.pointsTable.setItems(data);
        //end test
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

}