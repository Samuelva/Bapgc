
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


/*
Programmeur: Anne van Winzum
Datum: 08-12-2016 (door Davy Cats, layout aangepast)
Datum: 27-1-2017 (door Anne van Winzum, leegmaak knop werkend)
*/

final class Invoeren extends StackPane {
    /**
     * Er worden drie buttons geinitialiseerd:
     * - emptyButton, voor het leegmaken van de tabel.
     * - saveButton, voor het opslaan van de data.
     * - importCSV, voor hetr inladen van data.
     * <p>
     * Er worden twee labels, een TabelView en een Keuzemenu geinitialiseerd.
     * <p>
     * Vervolgens worden er een aantal variabelen geinitialiseerd:
     * - questionIDs: Een int array die de de IDs van de vragen bevat (zodra deze gevuld wordt
     * zullen de IDs dezelfde volgorde hebben als de kolomen).
     * - emptied: Een boolean die aanduid of de tabel leeggemaakt is.
     * - changes: Een HashMap die gebruikt wordt om de veranderingen in de tabel bij te houden.
     * De keys zijn de student nummers (als String) en de waardes zijn HashMaps die als key
     * de ID van vragen bevatten met als waarde de nieuwe waarde voor de vraag.
     */
    protected Button emptyButton;
    protected Button saveChanges;
    protected Button importCSV;
    protected Label lbl1;
    protected Label lbl2;
    protected TableView pointsTable;
    protected Keuzemenu choiceMenu;
    protected DatabaseConn d;

    private int[] questionIDs;
    private boolean emptied = false;
    private Map<String, Map> changes = new HashMap();

    public Invoeren() {
        /**
         *  De methode menuUnder wordt aangeroepen en maakt een HBox aan met
         *  drie knoppen.
         *  De methode MenuMaken wordt aangeroepen, deze maakt het keuzemenu aan.
         *  De methode makeTable maakt vervolgens de tabel aan.
         *  BozenVullen zet vervolgens het scherm in elkaar.
         *  Daarna worden er vier methodes gebruikt om de knoppen functies te geven.
         */
        HBox hbox = menuUnder();
        VBox vbox2 = MenuMaken();
        makeTable();
        BoxenVullen(vbox2, hbox);
        setLoadEvent();
        setSaveChangesEvent();
        setImportEvent();
        setEmptyEvent();

        d = new DatabaseConn();

    }

    private void setEmptyEvent() {
        /**
         * Als op emptyButton wordt geklikt,
         * wordt een Alert aangemaakt om een pop-up waarschuwing weer te geven.
         * De titel wordt op 'Waarschuwing' gezet.
         * De tekst van de header bevat een zin of je het bestand echt wilt maken.
         * Een tekst eronder geeft instructies, druk op OK als je het zeker weet,
         * anders druk je op cancel.
         * Er worden twee button types aangemaakt: OK en Cancel.
         * De button types worden toegevoegd aan alert.
         * De optionele buttontype krijgt de naam result,
         * als waarde krijgt hij showAndWait, dit betekent dat de pop-up wacht
         * tot er input van de gebruiker volgt.
         * Er volgt een if-else statement.
         * Als result gelijk is aan 'OK' moet alles gewist worden,
         * Dit wordt gedaan door de rijen leeg te maken.
         * Ook wordt de importCSV knop weer actief gemaakt,
         * zo kan er opnieuw een CSV worden geïmporteerd.
         * Ook wordt de globale boolean 'emptied' op true gezet.
         * Als result niet gelijk is aan 'OK' wordt de pop-up afgesloten.t.
         * Als result niet gelijk is aan OK wordt de pop-up afgesloten.
         */
        emptyButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Waarschuwing");
            alert.setHeaderText("Weet u zeker dat u het bestand leeg wilt maken?");
            alert.setContentText("Druk op OK als u het zeker weet, ander drukt u op Cancel");
            ButtonType OK = new ButtonType("OK");
            ButtonType Cancel = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(OK, Cancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == OK) {
                this.pointsTable.getItems().clear();
                this.importCSV.setDisable(false);
                this.emptied = true;
            } else {
                alert.close();
            }
        });
    }

    private void setImportEvent() {
        /**
         * Met FileChooser wordt de verkenner geopend in windows.
         */
        importCSV.setOnAction(e -> {
            List A = choiceMenu.getSelection();
            System.out.println(A);
            System.out.println(A.get(0));
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Toets Bestand");
            File file = fileChooser.showOpenDialog(new Stage());

            if (file != null) {

                //String[] list = getSelectionProperties();
                Integer ToetsID = d.GetToetsID(A.get(0).toString(),
                        A.get(1).toString(),
                        A.get(2).toString(),
                        A.get(3).toString(),
                        A.get(4).toString(),
                        A.get(5).toString());

                Object reader = new Reader(file.toString(), ToetsID);

                /*courseChoiceBox.getValue().toString(),
                        yearChoiceBox.getValue().toString(),
                        schoolYearChoiceBox.getValue().toString(),
                        blockChoiceBox.getValue().toString(),
                        attemptChoiceBox.getValue().toString(),
                        typeChoiceBox.getValue().toString()*/

                //btn4.setDisable(true);
            }
        });
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    protected void setupTable(String[] columns) {
        /**
         * Maak de kolommen aan voor de tabel.
         * Maak eerst de tabel leeg en verwijder alle kolomen. Voeg vervolgens de string "Student nr." toe aan de
         * columnsTotal op de eerste positie gevolgd door de waardes van columns. Maak voor iedere String in
         * columnsTotal een kolom aan. Geef deze kolomen een Cell Factory die een String accepteerd (anders kunnen de
         *  String arrays niet als waardes voor de tabel gebruikt worden). Maak de kolommen 40 breed en de eerste 80.
         * Geef alle kolommen een TextField als cell (op de eerste na) en  voeg de hantering toe voor als die gewijzigd
         * wordt. Voeg de kolommen toe aan de tabel.
         */
        this.pointsTable.getItems().clear();
        this.pointsTable.getColumns().clear();
        String[] columnsTotal = compileColumns(columns);
        for (int i = 0; i < columnsTotal.length; i++) {
            TableColumn column = makeColumn(i, columnsTotal[i]);
            this.pointsTable.getColumns().add(column);
        }
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private TableColumn makeColumn(int i, String columnLabel) {
        TableColumn column = new TableColumn(columnLabel);
        final int INDEX = i;
        column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>,
                ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> values) {
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

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void editEvent(TableColumn column, TableColumn.CellEditEvent event) {
        try {
            int newValue = Integer.parseInt(event.getNewValue().toString());
            if (newValue > 9999 || newValue < 0) {
                throw new NumberFormatException();
            }
            ObservableList items = pointsTable.getItems();
            String[] newRow = ((String[]) event.getRowValue());
            newRow[pointsTable.getColumns().indexOf(column)] = event.getNewValue().toString();
            items.set(event.getTablePosition().getRow(), newRow);
            pointsTable.setItems(items);
            storeChange(newRow[0], pointsTable.getColumns().indexOf(column), newValue);
        } catch (NumberFormatException e) {
            column.setVisible(false);
            column.setVisible(true);
        }
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private String[] compileColumns(String[] columns) {
        String[] columnsTotal = new String[columns.length + 1];
        columnsTotal[0] = "Student nr.";
        for (int i = 0; i < columns.length; i++) {
            columnsTotal[i + 1] = columns[i];
        }
        return columnsTotal;
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void storeChange(String studentID, int i, Integer newValue) {
        if (!this.changes.keySet().contains(studentID)) {
            this.changes.put(studentID, new HashMap<Integer, Integer>());
        }
        Map<Integer, Integer> map = this.changes.get(studentID);
        map.put(questionIDs[i - 1], newValue);
        this.changes.put(studentID, map);
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /* Deze functie vult de tabel in.
     * Er wordt connectie gemaakt met de database. De vraagnummers en ids worden opgehaald en opgeslagen onder
     * eerder geinitialiseerde variabelen. setupTable wordt gebruikt om de kolomen aan te maken.
     * De punten die de studenten per vraag hebben worden oopgehaald en toegevoegd aan een ObservableList die
     * aan de tabel gegeven wordt.
     */
    protected void fillTable(int examID) {
        DatabaseConn d = new DatabaseConn();
        Object[][] questionData = d.GetVragenVanToets(examID);
        if (Arrays.deepToString(questionData).equals("[]")) {
            warnNoQuestions();
        } else {
            this.questionIDs = Statistics.stringToIntArray(Statistics.getColumn(0, questionData), 0);
            String[] questionLabels = Statistics.getColumn(1, questionData);
            setupTable(questionLabels);
            String[][] pointsArray = d.GetStudentScores(examID);
            if (pointsArray[0][0] != null) {
                ObservableList<String[]> data = FXCollections.observableArrayList();
                data.addAll(Arrays.asList(pointsArray));
                pointsTable.setItems(data);
                this.emptied = false;
            }
            d.CloseConnection();
        }
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void warnNoQuestions() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Waarschuwing!");
        alert.setHeaderText("Er zijn geen vragen bekend voor deze toets!");
        alert.setContentText("U kunt alleen scores invoeren voor toetsen die vragen bevatten.");
        alert.showAndWait();
    }


    public HBox menuUnder() {
        /*
        Er wordt een regio gemaakt voor centreren van de knoppen.
        emptyButton krijgt de label Leeg maken en de afmetingen 150 bij 30 worden
        meegegeven aan de methode maakObject.
        saveChanges krijgt de label Wijzigingen opslaan en de afmetingen 150 bij 30
        worden meegegeven aan de methode maakObject.
        importCSV krijgt de label Import CSV en de ametingen 150 bij 30 worden
        meegegeven aan de methode Import CSV.
        Er wort nog een regio gemaakt voor het centreren van de knoppen.
        Er wordt een HBox aangemaakt met de naam hbox.
        Vervolgens worden alle knoppen en regios toegevoegd aan de hbox.
        De spacing in hbox wordt op 20 gezet.
        hbox wordt terug gegeven.
        */
        Region leftFill = new Region();
        HBox.setHgrow(leftFill, Priority.ALWAYS);
        emptyButton = maakObject(new Button(), "Leeg maken", 30, 150);
        saveChanges = maakObject(new Button(), "Wijzigingen opslaan", 30, 150);
        importCSV = maakObject(new Button(), "Import CSV", 30, 150);
        Region rightFill = new Region();
        HBox.setHgrow(rightFill, Priority.ALWAYS);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(rightFill, emptyButton, saveChanges, importCSV, leftFill);
        hbox.setSpacing(20);
        return hbox;

    }

    public Button maakObject(Button btn, String tekst, double hoogte,
                             double breedte) {
        /*
        Deze functie zet de naam, hoogte en breedte van de knoppen.
        */
        btn.setText(tekst);
        btn.setPrefHeight(hoogte);
        btn.setPrefWidth(150);

        return btn;

    }

    public Label maakObject(Label lbl, String tekst) {
        /*
        Deze functie zet de naam van de labels, laat ze centreren en
        geeft ze het font Arial met grootte 18.
        */
        lbl.setText(tekst);
        lbl.setAlignment(Pos.CENTER);
        lbl.setFont(new Font("Arial", 18));
        return lbl;
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
    public VBox MenuMaken() {
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


        VBox vbox2 = new VBox();

        vbox2.getChildren().addAll(lbl1, choiceMenu.getChoiceMenuBox());

        vbox2.setSpacing(20);
        return vbox2;

    }

    public void BoxenVullen(VBox vbox2, HBox hbox) {
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

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    protected void setSaveChangesEvent() {
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

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void saveChanges() {
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
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void clearScores() {
        System.out.println("hey");
        DatabaseConn d = new DatabaseConn();
        for (Integer id : this.questionIDs) {
            d.DeleteScoresForQuestion(id);
        }
        d.CloseConnection();
    }

    //DOCUMENTATIE AANPASSEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void setLoadEvent() {
        this.choiceMenu.examLoadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                emptied = false;
                changes.clear();
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