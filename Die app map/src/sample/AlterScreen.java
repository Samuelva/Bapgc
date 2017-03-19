package sample;

import java.io.File;
import java.util.*;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import database.DatabaseConn;
import database.Reader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Deze class maakt het aanpasscherm aan.
 */
final class AlterScreen extends StackPane {
    /**
     * Er worden drie buttons geïnitialiseerd:
     * - emptyButton, voor het leegmaken van de tabel.
     * - saveButton, voor het opslaan van de data.
     * - importCSV, voor het inladen van data.
     * Er worden twee labels, een TabelView, een Keuzemenu en database connectie
     * geïnitialiseerd.
     * Vervolgens worden er een aantal variabelen geïnitialiseerd:
     * - questionIDs: Een int array die de de IDs van de vragen bevat (zodra 
     * deze gevuld wordt zullen de IDs dezelfde volgorde hebben als de 
     * kolommen).
     * - emptied: Een boolean die aanduidt of de tabel leeggemaakt is.
     * - changes: Een HashMap die gebruikt wordt om de veranderingen in de tabel 
     * bij te houden.
     * De keys zijn de studentnummers (als String) en de waardes zijn HashMaps,
     * die als key de ID van vragen bevatten met als waarde de nieuwe waarde 
     * voor de vraag.
     */
    protected Button emptyButton;
    protected Button saveChanges;
    protected Label lbl1;
    protected Label lbl2;
    protected TableView pointsTable;
    protected ChoiceMenu choiceMenu;
    protected DatabaseConn d;

    private int[] questionIDs;
    private boolean emptied = false;
    private Map<String, Map> changes = new HashMap();

    public AlterScreen() {
        /**
         *  De methode menuUnder wordt aangeroepen en maakt een HBox aan met
         *  drie knoppen.
         *  De methode MenuMaken wordt aangeroepen, deze maakt het keuzemenu 
         *  aan.
         *  De methode makeTable maakt vervolgens de tabel aan.
         *  BoxenVullen zet vervolgens het scherm in elkaar.
         *  Daarna worden er vier methodes gebruikt om de knoppen functies te 
         *  geven.
         *  Er wordt een connectie geopend met de database.
         */
        HBox hbox = menuUnder();
        VBox vbox2 = MenuMaken();
        makeTable();
        BoxenVullen(vbox2, hbox);
        setLoadEvent();
        setSaveChangesEvent();
        setEmptyEvent();

        d = new DatabaseConn();

    }

    private void setEmptyEvent() {
        /**
         * Als op emptyButton wordt geklikt, wordt een Alert aangemaakt om een 
         * pop-up waarschuwing weer te geven.
         * De titel wordt op 'Waarschuwing' gezet.
         * De tekst van de header bevat een zin of je het bestand echt wilt 
         * maken. Een tekst eronder geeft instructies, druk op OK als je het 
         * zeker weet, anders druk je op cancel.
         * Er worden twee button types aangemaakt: OK en Cancel.
         * De button types worden toegevoegd aan alert.
         * De optionele buttontype krijgt de naam result,
         * als waarde krijgt hij showAndWait, dit betekent dat de pop-up wacht
         * tot er input van de gebruiker volgt.
         * Er volgt een if-else statement. Als result gelijk is aan 'OK' moet 
         * alles gewist worden. Dit wordt gedaan door de rijen leeg te maken.
         * Ook wordt de importCSV knop weer actief gemaakt, zo kan er opnieuw 
         * een CSV worden geïmporteerd. Ook wordt de globale boolean 'emptied'
         * op true gezet. Verder wordt de opslaanknop klikbaar gemaakt en de 
         * leegmaakknop onklikbaar. Als result niet gelijk is aan 'OK' wordt 
         * de pop-up afgesloten.
         */
        emptyButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Waarschuwing");
            alert.setHeaderText("Weet u zeker dat u het bestand leeg wilt " +
                    "maken?");
            alert.setContentText("Druk op OK als u het zeker weet, druk " +
                    "anders op cancel.");
            ButtonType OK = new ButtonType("OK");
            ButtonType Cancel = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(OK, Cancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == OK) {
                this.pointsTable.getItems().clear();
                this.pointsTable.getColumns().clear();
                this.emptied = true;
                this.saveChanges.setDisable(false);
                this.emptyButton.setDisable(true);
            } else {
                alert.close();
            }
        });
    }

    protected void setupTable(String[] columns) {
        /**
         * Deze methode zet de tabel in elkaar.
         * Eerst wordt de tabel geheel leeggemaakt. Vervolgens worden
         * de kolommen gemaakt en toegevoegd aan de tabel.
         */
        this.pointsTable.getItems().clear();
        this.pointsTable.getColumns().clear();
        String[] columnsTotal = compileColumns(columns);
        for (int i = 0; i < columnsTotal.length; i++) {
            TableColumn column = makeColumn(i, columnsTotal[i]);
            this.pointsTable.getColumns().add(column);
        }
    }

    private TableColumn makeColumn(int i, String columnLabel) {
        /**
         * Deze methode maakt een kolom aan.
         * Eerst wordt er een kolom gemaakt. Vervolgens wordt
         * de CellValueFactory zo gezet dat de rijen een waarde
         * als String[] kunnen accepteren.
         * Als het de eerste kolom is, krijgt die een breedte van
         * 80, anders van 40 en worden van de cellen textvelden gemaakt.
         * Ook wordt er dan een EventHandler toegevoegd voor als
         * het textveld gewijzigd wordt.
         */
        TableColumn column = new TableColumn(columnLabel);
        final int INDEX = i;
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>,
                ObservableValue<String>>() {
            @Override
            public ObservableValue<String>call(TableColumn.CellDataFeatures<String[], String> values) {
                return new SimpleStringProperty((values.getValue()[INDEX]));
            }
        });
        if (i == 0) {
            column.setMinWidth(80);
            column.setMaxWidth(80);
        } else {
            column.setMinWidth(40);
            column.setMaxWidth(40);
            column.setCellFactory(TextFieldTableCell.<String>forTableColumn());
            column.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
                @Override
                public void handle(TableColumn.CellEditEvent event) {
                    editEvent(column, event);
                }
            });
        }
        return column;
    }

    private void editEvent(TableColumn column, TableColumn.CellEditEvent event) {
        /**
         * Deze methode verwerkt de wijzigingen.
         * Eerst wordt de nieuwe waarde omgezet naar een int. Als deze groter
         * is dan 9999 of kleiner dan 0, wordt er een NumberFormatException
         * gegooid. Dit gebeurt ook als er iets anders dan cijfer zijn ingevuld.
         * Vervolgens wordt er voor gezorgd dat de nieuwe waarde in de tabel
         * komt te staan en wordt de wijziging in de Map gezet die de 
         * wijzigingen bijhoudt met behulp van de storeChange methode. 
         * Ook wordt de opslaan knop klikbaar gemaakt.
         *
         * Als er een NumberFormatException plaatsvindt gebeurt het 
         * bovenstaande niet. De kolom wordt dan onzichtbaar en weer zichtbaar 
         * gemaakt, zodat de oude waarde weer in de tabel staat.
         */
        try {
            int newValue = Integer.parseInt(event.getNewValue().toString());
            if (newValue > 9999 || newValue < 0) {
                throw new NumberFormatException();
            }
            ObservableList items = pointsTable.getItems();
            String[] newRow = ((String[]) event.getRowValue());
            newRow[pointsTable.getColumns().indexOf(column)] = event
                    .getNewValue().toString();
            items.set(event.getTablePosition().getRow(), newRow);
            pointsTable.setItems(items);
            storeChange(newRow[0], pointsTable.getColumns().indexOf(column),
                    newValue);
            saveChanges.setDisable(false);
        } catch (NumberFormatException e) {
            column.setVisible(false);
            column.setVisible(true);
        }
    }

    private String[] compileColumns(String[] columns) {
        /**
         * Deze methode voegt de String "Student nr." toe
         * aan de String met kolom labels.
         */
        String[] columnsTotal = new String[columns.length + 1];
        columnsTotal[0] = "Student nr.";
        for (int i = 0; i < columns.length; i++) {
            columnsTotal[i + 1] = columns[i];
        }
        return columnsTotal;
    }

    private void storeChange(String studentID, int i, Integer newValue) {
        /**
         * Deze methode houdt de wijzigingen bij.
         * Als er nog geen wijzigingen gemaakt zijn voor een student
         * wordt deze student aan de Map die de wijzigingen bijhoudt
         * toegevoegd. Vervolgens wordt de wijziging in de Map gezet.
         * In de Map staat per student een Map met als keys de vraag
         * IDs en als waarde de nieuwe waarde.
         */
        if (!this.changes.keySet().contains(studentID)) {
            this.changes.put(studentID, new HashMap<Integer, Integer>());
        }
        Map<Integer, Integer> map = this.changes.get(studentID);
        map.put(questionIDs[i - 1], newValue);
        this.changes.put(studentID, map);
    }

    protected void fillTable(int examID) {
        /**
         * Deze methode vult de tabel in.
         * Eerst wordt de informatie over de vragen opgehaald.
         * Als er geen informatie bekend is, wordt er een waarschuwing
         * getoond. Anders worden de IDs en labels van de vragen
         * uit de informatie gehaald en wordt de tabel in elkaar gezet
         * met setupTable. Vervolgens worden de behaalde scores
         * opgehaald en als deze bekend zijn toegevoegd aan de
         * tabel. Ook wordt de variabele emptied op false gezet.
         */
        DatabaseConn d = new DatabaseConn();
        Object[][] questionData = d.GetVragenVanToets(examID);
        if (Arrays.deepToString(questionData).equals("[]")) {
            warnNoData();
        } else {
            this.questionIDs = Statistics.stringToIntArray(Statistics
                    .getColumn( 0, questionData), 0);
            String[] questionLabels = Statistics.getColumn(1, questionData);
            setupTable(questionLabels);
            String[][] pointsArray = d.GetStudentScores(examID);
            if (pointsArray[0][0] != null) {
                ObservableList<String[]> data = FXCollections
                        .observableArrayList();
                data.addAll(Arrays.asList(pointsArray));
                pointsTable.setItems(data);
                this.emptied = false;
            }
            d.CloseConnection();
        }
    }

    private void warnNoData() {
        /**
         * Deze methode laadt een waarchuwing zien voor
         * als er geen data is voor een toets.
         */
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Waarschuwing!");
        alert.setHeaderText("Er is geen data bekend voor deze toets!");
        alert.setContentText("U kunt alleen scores aanpassen voor toetsen " +
                "waarbij er al scores ingevoerd zijn.");
        alert.showAndWait();
    }


    public HBox menuUnder() {
        /**
         * Er wordt een regio gemaakt voor centreren van de knoppen. 
         * emptyButton krijgt de label "Leeg maken" en de afmetingen 150 bij 30
         * worden meegegeven aan de methode maakObject. saveChanges krijgt de 
         * label "Wijzigingen opslaan" en de afmetingen 150 bij 30 worden 
         * meegegeven aan de methode maakObject. importCSV krijgt de label 
         * "Import CSV" en de afmetingen 150 bij 30 worden meegegeven aan de
         * methode ImportCSV. Er wordt nog een regio gemaakt voor het centreren 
         * van de knoppen. Er wordt een HBox aangemaakt met de naam hbox.
         * Vervolgens worden alle knoppen en regio's toegevoegd aan de hbox.
         * De spacing in hbox wordt op 20 gezet. hbox wordt teruggegeven.
        */
        Region leftFill = new Region();
        HBox.setHgrow(leftFill, Priority.ALWAYS);
        emptyButton = maakObject(new Button(), "Leeg maken", 30, 150);
        emptyButton.setDisable(true);
        Region midFill = new Region();
        HBox.setHgrow(midFill, Priority.ALWAYS);
        saveChanges = maakObject(new Button(), "Wijzigingen opslaan", 30, 150);
        saveChanges.setDisable(true);
        Region rightFill = new Region();
        HBox.setHgrow(rightFill, Priority.ALWAYS);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(rightFill, emptyButton, midFill,
                saveChanges, leftFill);
        hbox.setSpacing(20);
        return hbox;

    }

    public Button maakObject(Button btn, String tekst, double hoogte,
                             double breedte) {
        /**
         * Deze functie stelt de naam, hoogte en breedte in van de knoppen.
        */
        btn.setText(tekst);
        btn.setPrefHeight(hoogte);
        btn.setPrefWidth(150);

        return btn;

    }

    public Label maakObject(Label lbl, String tekst) {
        /**
         * Deze functie stelt de naam van de labels in, laat ze centreren en
         * geeft ze het font Arial met grootte 18.
        */
        lbl.setText(tekst);
        lbl.setAlignment(Pos.CENTER);
        lbl.setFont(new Font("Arial", 18));
        return lbl;
    }

    private void makeTable() {
        /**
         * Deze methode maakt de tabel aan.
         * De tabel wordt aangemaakt, aanpasbaar gemaakt en
         * wordt zo gezet dat deze het scherm verticaal vult.
         * Vervolgens wordt ervoor gezorgd dat de kolommen
         * niet van volgorde gewijzigd kunnen worden.
         */
        pointsTable = new TableView();
        pointsTable.setEditable(true);
        VBox.setVgrow(pointsTable, Priority.ALWAYS);
        pointsTable.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> source,
                                Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) pointsTable.lookup
                        ("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {

                    @Override
                    public void changed(ObservableValue<? extends Boolean>
                                                observable, Boolean oldValue,
                                        Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });
    }

    @SuppressWarnings("unchecked")
    public VBox MenuMaken() {
        /**
         * In deze functie wordt het menu aan de linkerkant gemaakt.
         * lbl1 krijgt de tekst Keuzemnu, de breedte wordt op 150 gezet.
         * De eerste ChoiceBox is year. De tweede ChoiceBox is studyyear. De 
         * items zijn: jaar 1, jaar 2, jaar 3 of jaar 4. De derde ChoiceBox is 
         * periode. De items zijn: periode 1, periode 2. periode 3, periode 4 
         * en periode 5. De vierde ChoiceBox is module, er zijn hier nog geen 
         * items, dit wordt nader bepaald. De vijfde ChoiceBox is toetsvorm. 
         * De items zijn: theorietoets, praktijktoets, opdracht, aanwezigheid, 
         * logboek en project. De laatste ChoiceBox is gelegenheid, de items 
         * zijn: 1e kans, 2e kans en 3e kans.
         * Alle ChoiceBoxes krijgen een hoogte van 30 en een breedte van 150.
         * Er wordt een Region gemaakt die (door VGrow) btn1 naar beneden duwt.
         * btn1 krijgt het label "Toets laden", een hoogte van 30 en een breedte
         * van 150. De choiceboxes en btn1 worden toegevoegd aan een vbox.
         * De spacing van de vbox wordt op 20 gezet. De vbox wordt teruggegeven.
        */
        lbl1 = maakObject(new Label(), "Keuzemenu");
        lbl1.setPrefWidth(150);

        choiceMenu = new ChoiceMenu();

        Region fill = new Region();
        VBox.setVgrow(fill, Priority.ALWAYS);


        VBox vbox2 = new VBox();

        vbox2.getChildren().addAll(lbl1, choiceMenu.getChoiceMenuBox());

        vbox2.setSpacing(20);
        return vbox2;

    }

    public void BoxenVullen(VBox vbox2, HBox hbox) {
        /**
        * Er wordt eerst een label aangemaakt met de naam "Vragen". Er wordt een 
        * HBox aangemaakt, de label wordt toegevoegd aan een hbox, samen met 
        * twee Regions die er voor zorgen dat het label gecentreerd wordt. 
        * Er wordt een VBox aangemaakt, de pointsTable wordt toegevoegd aan de 
        * vbox. Aan de vbox wordt ook de hbox met het label toegevoegd en de 
        * hbox die als parameter meegegeven wordt. De vbox krijgt een spacing 
        * van 10. Er wordt nog een HBox aangemaakt, aan deze hbox wordt de vbox 
        * met het keuzemenu toegevoegd. Ook wordt de vbox toegevoegd met de 
        * pointsTable, knoppen en label die net zijn gemaakt. Hiervoor worden 
        * margins van 5 om beide vboxes gezet. De laatste hbox krijgt een 
        * spacing van 20. Ten slotte wordt de hbox toegepast aan this, hierdoor 
        * kan deze klasse ook gebruikt worden in de main.
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

    protected void setSaveChangesEvent() {
        /**
         * Deze methode voegt de functionaliteit van de opslaanknop toe.
         * Er wordt eerst een waarschuwing getoond, die vraagt of er echt
         * opgeslagen moet worden. Als er op OK gedrukt wordt, worden
         * de wijzigingen opgeslagen.
         */
        this.saveChanges.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Waarschuwing");
                alert.setHeaderText("Weet u zeker dat u de wijzigingen op wilt slaan?");
                alert.setContentText("Druk op OK als u het zeker weet, ander drukt u op Cancel");
                ButtonType OK = new ButtonType("OK");
                ButtonType Cancel = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(OK, Cancel);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == OK) {
                    saveChanges();
                } else {
                    alert.close();
                }
            }
        });
    }

    private void saveChanges() {
        /**
         * Deze methode slaat de wijzigingen op. Als "emptied" true is, wordt 
         * clearScores aangeroepen. Anders wordt er door de wijzigingen heen 
         * geloopt. Voor iedere student wordt het ID bepaald (deze staat als
         * String in de Map) en wordt er door de veranderde scores van
         * die student heen geloopt. Deze wijzigingen worden opgeslagen.
         * Daarna wordt de Map die de wijzigingen bevat leeggemaakt,
         * emptied op "false" gezet en de opslaanknop onklikbaar gemaakt.
         */
        if (this.emptied) {
            clearScores();
        } else {
            DatabaseConn d = new DatabaseConn();
            for (String key : this.changes.keySet()) {
                Map<Integer, Integer> map = this.changes.get(key);
                Integer student = Integer.parseInt(key);
                for (Integer id : map.keySet()) {
                    d.UpdateScore(student, id, map.get(id));
                }
            }
            d.CloseConnection();
        }
        this.changes.clear();
        this.emptied = false;
        this.saveChanges.setDisable(true);
    }

    private void clearScores() {
        /**
         * Deze methode verwijdert de geselecteerde toets.
         */
        DatabaseConn d = new DatabaseConn();
        Integer examID = d.getExamID(this.questionIDs[0]);
        d.UpdateCesuurGok(examID, 0, 0);
        d.DeleteVragenToets(d.getExamID(this.questionIDs[0]));

        d.CloseConnection();
    }

    private void setLoadEvent() {
        /**
         * Deze methode voegt de functionaliteit van de laadknop toe.
         * Als er een toets geladen wordt, wordt de globale variabele
         * emptied op "false" gezet. De Map die de aangepaste waardes bevat
         * wordt leeggemaakt. Er wordt een connectie gemaakt met de
         * database. Het ID van de geselecteerde toets wordt bepaald en
         * meegegeven aan fillTable om de tabel te vullen.
         * De leegmaakknop wordt klikbaar gemaakt en de opslaanknop
         * onklikbaar.
         */
        this.choiceMenu.examLoadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                emptied = false;
                changes.clear();
                DatabaseConn d = new DatabaseConn();
                List<String> selection = choiceMenu.getSelection();
                int id = d.GetToetsID(selection.get(0), selection.get(1), 
                        selection.get(2), selection.get(3),
                        selection.get(4), selection.get(5));
                fillTable(id);
                d.CloseConnection();
                emptyButton.setDisable(false);
                saveChanges.setDisable(true);
            }
        });
    }
}