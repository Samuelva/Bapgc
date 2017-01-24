package sample;

import database.DatabaseConn;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import database.DatabaseConn;
import database.ModuleReader;
import database.Reader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;


/*
Programmeur: Anne van Winzum
Datum laatste aanpassing: 08-12-2016 (door Davy Cats, layout aangepast)
Beschrijving: In dit script wordt de eerste layout van het invoerscherm getoond.
*/

final class Invoeren extends StackPane {
    /*
    De volgende globale variabelen worden gemaakt:
    btn1, btn2, btn3 en btn4, deze worden later gebruikt voor de knoppen
    onderaan het scherm
    Ook worden de labels lbl1 en lbl2 gemaakt en de pointsTable.
    */
    protected Button btn1;
    protected Button btn2;
    protected Button btn3;
    protected Button btn4;
    protected Label lbl1;
    protected Label lbl2;
    protected Button btn5;
    protected TableView pointsTable;
    protected ComboBox yearChoiceBox;
    protected ComboBox schoolYearChoiceBox;
    protected ComboBox blockChoiceBox;
    protected ComboBox courseChoiceBox;
    protected ComboBox typeChoiceBox;
    protected ComboBox attemptChoiceBox;

    protected Keuzemenu choiceMenu;

    private String[] questionLabels;
    private String[][] pointsArray;
    private int[] questionIDs;

    public Invoeren() {
        /*
        De methode menuUnder wordt aangeroepen en maakt een HBox aan met
        drie knoppen.
        De methode MenuMaken wordt aangeroepen, deze maakt het keuzemenu aan.
        BozenVullen zet vervolgens het scherm in elkaar.
        */
        HBox hbox = menuUnder();
        VBox vbox2 = MenuMaken();
        makeTable();
        BoxenVullen(vbox2, hbox);
        DatabaseConn d = new DatabaseConn();



        btn4.setOnAction(e -> {
        	/*
        	 *Met FileChooser wordt de verkenner geopend in windows. 
        	 */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Toets Bestand");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                //String[] list = getSelectionProperties();
                //Integer ToetsID = d.GetToetsID(list[0], list[2], list[1], list[3], list[5], list[4]);
                //Object moduleReader = new Reader(file.toString(), ToetsID);
                btn4.setDisable(true);
            }
        });
        
        btn2.setOnAction(e -> {
        	/*
        	 * Als op btn2 wordt geklikt,
        	 * wordt een Alert aangemaakt om een pop-up waarschuwing weer te geven.
        	 * De titel wordt op waarschuwing gezet.
        	 * De tekst van de header bevat een zin of je het bestand echt wilt maken.
        	 * Een tekst eronder geeft instructies, druk op OK als je het zeker weet, 
        	 * anders druk je op cancel.
        	 * Er worden twee button types aangemaakt: OK en Cancel.
        	 * De button types worden toegevoegd aan alert.
        	 * De optionele buttontype krijgt de naam result,
        	 * als waarde krijgt hij showAndWait, dit betekent dat de pop-up wacht
        	 * tot er input van de gebruiker volgt.
        	 * Er volgt een if-else statement.
        	 * Als result gelijk is aan OK moet alles gewist worden,
        	 * dit is nu nog een zin die naar de terminal wordt geprint.
        	 * Als result niet gelijk is aan OK wordt de pop-up afgesloten.
        	 */
        	Alert alert = new Alert(AlertType.WARNING);
        	alert.setTitle("Waarschuwing");
        	alert.setHeaderText("Weet u zeker dat u het bestand leeg wilt maken?");
        	alert.setContentText("Druk op OK als u het zeker weet, ander drukt u op Cancel");
        	ButtonType OK = new ButtonType("OK");
        	ButtonType Cancel = new ButtonType("Cancel");
        	
        	alert.getButtonTypes().setAll(OK, Cancel);
        	Optional<ButtonType> result = alert.showAndWait();
        	
        	if (result.get() == OK){
                this.pointsTable.getItems().clear();
                this.pointsTable.getColumns().clear();
        	} else {
        	    alert.close();
        	}
        });
    }

    /* Maak de kolommen aan voor de tabel.
     * Maak eerst de tabel leeg en verwijder alle kolomen. Voeg vervolgens de string "Student nr." toe aan de
     * columnsTotal op de eerste positie gevolgd door de waardes van columns. Maak voor iedere String in
     * columnsTotal een kolom aan. Geef deze kolomen een Cell Factory die een String accepteerd (anders kunnen de
     * String arrays niet als waardes voor de tabel gebruikt worden). Maak de kolommen 40 breed en de eerste 80.
     * Geef alle kolommen een TextField als cell (op de eerste na) en  voeg de hantering toe voor als die gewijzigd
     * wordt. Voeg de kolommen toe aan de tabel.
     */
    protected void setupTable(String[] columns) {
        this.pointsTable.getItems().clear();
        this.pointsTable.getColumns().clear();
        String[] columnsTotal = new String[columns.length + 1];
        columnsTotal[0] = "Student nr.";
        for (int i = 0; i < columns.length; i++){
            columnsTotal[i+1] = columns[i];
        }
        for (int i = 0; i < columnsTotal.length; i++) {
            TableColumn column = new TableColumn(columnsTotal[i]);
            final int index = i;
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> values) {
                    return new SimpleStringProperty((values.getValue()[index]));
                }
            });
            if (i == 0){
                column.setMinWidth(80);
                column.setMaxWidth(80);
            } else {
                column.setMinWidth(40);
                column.setMaxWidth(40);
                TextFieldTableCell cell = new TextFieldTableCell();
                column.setCellFactory(TextFieldTableCell.<String>forTableColumn());
                column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {

                    /* Wanneer de waarde van een cel gewijzigd wordt, wordt deze doorgevoerd naar de items van de
                     * tabel. Er wordt ook gecontroleerd of het een cijfer is.
                     */
                    @Override
                    public void handle(TableColumn.CellEditEvent event) {
                        try{
                            int x = Integer.parseInt(event.getNewValue().toString());
                            ObservableList items = pointsTable.getItems();
                            String[] newRow = ((String[]) event.getRowValue());
                            newRow[pointsTable.getColumns().indexOf(column)] = event.getNewValue().toString();
                            items.set(event.getTablePosition().getRow(), newRow);
                            pointsTable.setItems(items);
                        } catch (Exception e) {
                            column.setVisible(false);
                            column.setVisible(true);
                        }
                    }
                });
            }
            this.pointsTable.getColumns().add(column);
        }
    }

    /* Deze functie vult de tabel in.
     * Er wordt connectie gemaakt met de database. De vraagnummers en ids worden opgehaald en opgeslagen onder
     * eerder geinitialiseerde variabelen. setupTable wordt gebruikt om de kolomen aan te maken.
     * De punten die de studenten per vraag hebben worden oopgehaald en toegevoegd aan een ObservableList die
     * aan de tabel gegeven wordt.
     */
    protected void fillTable(int examID){
        DatabaseConn d = new DatabaseConn();
        Object[][] questionData = d.GetVragenVanToets(examID);
        this.questionIDs = Statistics.stringToIntArray(Statistics.getColumn(0, questionData), 0);
        this.questionLabels = Statistics.getColumn(1, questionData);
        setupTable(this.questionLabels);
        this.pointsArray = d.GetStudentScores(examID);
        ObservableList<String[]> data = FXCollections.observableArrayList();
        data.addAll(Arrays.asList(this.pointsArray));
        this.pointsTable.setItems(data);
        d.CloseConnection();

    }
    
    public HBox menuUnder(){
        /*
        Er wordt een regio gemaakt voor centreren van de knoppen.
        btn2 krijgt de label Leeg maken en de afmetingen 150 bij 30 worden
        meegegeven aan de methode maakObject.
        btn3 krijgt de label Wijzigingen opslaan en de afmetingen 150 bij 30
        worden meegegeven aan de methode maakObject.
        btn4 krijgt de label Import CSV en de ametingen 150 bij 30 worden
        meegegeven aan de methode Import CSV.
        Er wort nog een regio gemaakt voor het centreren van de knoppen.
        Er wordt een HBox aangemaakt met de naam hbox.
        Vervolgens worden alle knoppen en regios toegevoegd aan de hbox.
        De spacing in hbox wordt op 20 gezet.
        hbox wordt terug gegeven.
        */
        Region leftFill = new Region();
        HBox.setHgrow(leftFill, Priority.ALWAYS);
        btn2 = maakObject(new Button(), "Leeg maken", 30, 150);
        btn3 = maakObject(new Button(),"Wijzigingen opslaan", 30, 150);
        btn4 = maakObject(new Button(),"Import CSV", 30, 150);
        Region rightFill = new Region();
        HBox.setHgrow(rightFill, Priority.ALWAYS);
            
        HBox hbox = new HBox();
        hbox.getChildren().addAll(rightFill, btn2, btn3, btn4, leftFill);
        hbox.setSpacing(20);
        return hbox;
        
    }

    public Button maakObject(Button btn, String tekst, double hoogte, 
            double breedte){
        /*
        Deze functie zet de naam, hoogte en breedte van de knoppen.
        */
        btn.setText(tekst);
        btn.setPrefHeight(hoogte);
        btn.setPrefWidth(150);
        
        return btn;
        
    }
    
    public Label maakObject(Label lbl, String tekst){
        /*
        Deze functie zet de naam van de labels, laat ze centreren en
        geeft ze het font Arial met grootte 18.
        */
        lbl.setText(tekst);
        lbl.setAlignment(Pos.CENTER);
        lbl.setFont(new Font("Arial", 18));
        return lbl;
    }

    //maak de tabel aan
    private void makeTable() {
        pointsTable = new TableView();
        pointsTable.setEditable(true);
        VBox.setVgrow(pointsTable, Priority.ALWAYS);
        
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

    @SuppressWarnings("unchecked")
	public VBox MenuMaken(){
        /*
        In deze functie wordt het menu aan de linkerkant gemaakt.
        lbl1 krijgt de tekst Keuzemnu, de breedte wordt op 150 gezet.
        De eerste ChoiceBox is year.
        De tweede ChioceBox is studyyear: de items zijn: jaar 1, jaar 2, 
        jaar 3 of jaar 4.
        De derde ChoiceBox is periode: de items zijn: periode 1, periode 2.
        periode 3, periode 4 en periode 5.
        De vierde ChoiceBox is module, er zijn hier nog geen items, 
        dit wordt nader bepaald.
        De vijfde ChoiceBox is toetsvorm, de items zijn: theorietoets,
        praktijktoets, opdracht, aanwezigheid, logboek en project.
        De laatste ChoiceBox is gelegenheid, de items zijn: 1e kans en 
        2e kans.
        Alle ChoiceBoxes krijgen een hoogte van 30 en een breedte van 150.
        er wordt een Region gemaakt die (door VGrow) btn1 naar beneden duwt.
        btn1 krijgt het label Toets laden, een hoogte van 30 en een breedte
        van 150.
        De choiceboxes en btn1 worden toegevoegd aan een vbox.
        De spacing van de vbox wordt op 20 gezet.
        De vbox wordt terug gegeven.
        */
        lbl1 = maakObject(new Label(), "Keuzemenu");
        lbl1.setPrefWidth(150);

        choiceMenu = new Keuzemenu();

        Region fill = new Region();
        VBox.setVgrow(fill, Priority.ALWAYS);
        
        btn1 = maakObject(new Button(), "Toets laden", 30, 150);
        btn1.setPrefWidth(150);
        
        VBox vbox2 = new VBox();

        vbox2.getChildren().addAll(lbl1, choiceMenu.getChoiceMenuBox(), fill, btn1);
        
        vbox2.setSpacing(20);
        return vbox2;   
        
    }


    public void BoxenVullen(VBox vbox2, HBox hbox){
        /*
        Er wordt eerst een label aangemaakt met de naam: Vragen.
        Ook wordt er een knop aangemaakt met de label: nieuwe student.
        Er wordt een HBox aangemaakt,
        de label en de knop worden toegevoegd aan een hbox, same met twee
        Region die er voor zorgen dat het label gecentreerd wordt.
        Vervolgens wordt er een TableView (pointsTable) gemaakt.
        In deze ruimte komen later de studenten en hun punten te staan.
        Er wordt een VBox aangemaakt,
        de pointsTable wordt toegevoegd aan de vbox en hier wordt een Vgrow
        aan toegevoegd. Aan de vbox wordt ook de hbox met de label en knop
        toegevoegd en de hbox die als parameter meegegeven wordt.
        De vbox krijgt een spacing van 10.
        Er wordt nog een HBox aangemaakt,
        aan deze hbox wordt de vbox met het keuzemenu toegevoegd,
        ook wordt de vbox toegevoegd met de pointsTable, knoppen en label die
        net is gemaakt.
        Hiervoor worden margins van 5 om beide vboxes gezet.
        De laatste hbox krijget een spacing van 20.
        Ten slotte wordt de hbox toegepast aan this, hierdoor kan
        deze klasse ook gebruikt worden in de main.
        */
        
        Region fillLeft = new Region();
        HBox.setHgrow(fillLeft, Priority.ALWAYS);
        lbl2 = maakObject(new Label(), "Vragen");
        Region fillRight = new Region();
        HBox.setHgrow(fillRight, Priority.ALWAYS);

        
        HBox hbox3 = new HBox();
        hbox3.getChildren().addAll(fillLeft, lbl2, fillRight);
        
        VBox vbox3 = new VBox();
        
        vbox3.setSpacing(10);
        vbox3.getChildren().addAll(hbox3, pointsTable, hbox);
        
        
        HBox hbox2 = new HBox();
        HBox.setHgrow(vbox3, Priority.ALWAYS);
        
        HBox.setMargin(vbox2, new Insets(5));
        HBox.setMargin(vbox3, new Insets(5));
        
        hbox2.setSpacing(20);
        hbox2.getChildren().addAll(vbox2, vbox3);
        
        this.getChildren().add(hbox2);
        
    }
    
    public String[] getSelectionProperties() {
            String[] properties = new String[6];


            if (schoolYearChoiceBox.getValue().equals("Jaartal"))
                return null;
            if (yearChoiceBox.getValue().equals("Leerjaar"))
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
            properties[1] = (String) schoolYearChoiceBox.getValue();
            properties[2] = (String) yearChoiceBox.getValue();
            properties[3] = (String) blockChoiceBox.getValue();
            properties[4] = (String) typeChoiceBox.getValue();
            properties[5] = (String) attemptChoiceBox.getValue();


            return properties;
        }
    
    public void setSelection(String[] selection) {
        courseChoiceBox.setValue(selection[0]);
        schoolYearChoiceBox.setValue(selection[1]);
        yearChoiceBox.setValue(selection[2]);
        blockChoiceBox.setValue(selection[3]);
        typeChoiceBox.setValue(selection[4]);
        attemptChoiceBox.setValue(selection[5]);
    }



}