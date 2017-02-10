package sample;

import database.DatabaseConn;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * Main klasse. Deze creëert het venster met de tabs waarin alle andere
 * schermen in zitten.
 */
public class Main extends Application {
    private static Stage window;
    private static Scene scene;
    private static VBox frame;
    private static ViewScreen view;
    private static AddScreen toevoeg;
    private static AlterScreen invoer;
    private static CompareScreen vergelijk;
    private static TabPane tabPane;
    private static Tab toevoegen;
    private static Tab invoeren;
    private static Tab inzien;
    private static Tab vergelijken;

    public static void main(String[] args) {
        /**
         * Main klasse.
         */
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        /**
         * Roept functies aan welke de interface aanmaakt, het switchen
         * tussen de schermen mogelijk maakt en het scherm laat zien.
         */
        window = primaryStage;

        initLayout();
        initTabs();
        events();
        showScreen();
    }

    private void events() {
        /**
         * Events voor diverse knoppen van het toevoegscherm
         */
        toevoeg.examTab.resetPointDistributionButton.setOnAction(event -> {
            toevoeg.resetWarning();
        });
        toevoeg.saveExamBtn.setOnAction(event -> {
            putExamPropertiesInDatabase();
        });
        toevoeg.choiceMenu.examLoadButton.setOnAction(event -> {
            getExamPropertiesFromDatabase();
        });
        toevoeg.updateAccountAbility.setOnAction(event -> {
            updateAccountability();
        });
    }

    private void updateAccountability() {
        /**
         * Met behulp van de vraagnummer en toetsid
         * word het meerekenen op de juistte waarde ingesteld in de
         * database. Door middel van een loop wordt er over vragen heen
         * gelooped waarna met een ternary wordt bepaald of de
         * waarde true of false moet zijn.
         */
        if (toevoeg.questionPropertyCheckBox.isSelected()) {
            DatabaseConn databaseConn = new DatabaseConn();
            for (String[] questionInfoArray : toevoeg.getQuestionInfo()) {
                int vraagID = databaseConn.GetVraagID(questionInfoArray[0],
                        toevoeg.examID);
                databaseConn.UpdateMeereken(vraagID,
                        questionInfoArray[2].equals("true") ? true : false);
            }
            databaseConn.CloseConnection();
        }
    }

    private void putExamPropertiesInDatabase() {
        /**
         * Maakt database verbinding
         * past met het ingeladen toetsID de cesuur en gokkans aan
         *
         * Als er vragen voor de toetsaanwezig zijn worden de verschillende
         * vragen ook in de database geladen
         */
        DatabaseConn databaseConn = new DatabaseConn();
        databaseConn.UpdateCesuurGok(toevoeg.examID, Integer.parseInt(
                toevoeg.thresholdTextfield.getText()),
                Integer.parseInt(toevoeg.chanceByGamblingTextfield.getText()));
        databaseConn.CloseConnection();
    }

    private void getExamPropertiesFromDatabase() {
        /**
         * Als er op de toets weergeven knop wordt gedrukt zal de selectie
         * worden teruggehaald. Hiermee wordt de toetsID opgehaald waarna
         * de gegevens in de klasse toets hiermee kunnen worden opgehaald.
         *
         * Als er geen data wordt gevonden wordt er een pop-up weergeven
         * met een waarschuwing dat er geen data is gevonden
         */

        DatabaseConn databaseConn = new DatabaseConn();
        try {
            List<String> selection = toevoeg.choiceMenu.getSelection();
            Integer examID = databaseConn.GetToetsID(selection.get(0),
                    selection.get(1), selection.get(2), selection.get(3),
                    selection.get(4), selection.get(5));
            toevoeg.examID = examID;
            toevoeg.examTab.setExamPropertiesScreen(selection.toArray(
                    new String[0]));
        } catch (Exception e) {
            toevoeg.examTab.examPane.setCenter(new VBox());
            warnNoData();
        }
        databaseConn.CloseConnection();
    }

    private void warnNoData() {
        /**
         * Deze methode toont een pop-up met een waarschuwing als er geen
         * data bekend is voor een toets die geladen wordt.
         */
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Waarschuwing!");
        alert.setHeaderText("Er is geen data bekend voor deze toets!");
        alert.setContentText("U kunt alleen toetsen waarvoor scores bekend zijn inzien.");
        alert.showAndWait();
    }

    public static void updateSelectionMenu() {
        /**
         * Wordt aangeroepen als de database leeg wordt gemaakt of als er
         * nieuwe modulen worden toegevoegd. Het selectiemenu in het
         * vergelijkscherm wordt dan geupdate met nieuwe data, of geleegd.
         */
        vergelijk.testChoiceMenu.updateSelectionMenu();
        vergelijk.courseChoiceMenu.updateSelectionMenu();
        vergelijk.blockChoiceMenu.updateSelectionMenu();
    }

    private static void initLayout(){
        /**
         * Initiëren van de layout voor het hoofdscherm
         * Er wordt een policy gezet op de tabs dat deze niet kunnen worden
         * afgesloten. De verschillende tabs worden aangemaakt en krijgen namen
         *
         */
        frame = new VBox();

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabMinWidth(100);
        toevoegen = new Tab();
        invoeren = new Tab();
        inzien = new Tab();
        vergelijken = new Tab();
        toevoegen.setText("Toevoegen");
        invoeren.setText("Aanpassen");
        inzien.setText("Inzien");
        vergelijken.setText("Vergelijken");

        tabPane.getTabs().add(toevoegen);
        tabPane.getTabs().add(invoeren);
        tabPane.getTabs().add(inzien);
        tabPane.getTabs().add(vergelijken);
    }



    private static void initTabs(){
        /**
         * Creeert een instantie voor elk scherm en voegt deze toe aan hun
         * respectievelijke tabs.
         */
        toevoeg = new AddScreen();
        toevoegen.setContent(toevoeg);

        invoer = new AlterScreen();
        invoeren.setContent(invoer);

        view = new ViewScreen();
        inzien.setContent(view);

        vergelijk = new CompareScreen();
        vergelijken.setContent(vergelijk);

        frame.getChildren().addAll(tabPane);

    }

    private void showScreen(){
        /**
         * Creëert het venster voor de schermen met een standaard hoogte en
         * breedte en laat deze zien.
         */
        scene = new Scene(frame, 1000, 600);
        window.setScene(scene);
        window.setTitle("Bapgc");
        window.show();
    }

}