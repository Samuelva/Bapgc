package sample;

import database.DatabaseConn;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    private static Stage window;
    private static Scene scene;
    private static VBox frame;
    private static ViewScreen view;
    private static Toevoegen toevoeg;
    private static AlterScreen invoer;
    private static CompareScreen vergelijk;
    private static TabPane tabPane;
    private static Tab toevoegen;
    private static Tab invoeren;
    private static Tab inzien;
    private static Tab vergelijken;

    private static DatabaseConn databaseConn;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
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
         * past met het ingeladen toetsID de cessuur en gokkans aan
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
         */
        DatabaseConn databaseConn = new DatabaseConn();
        List<String> selection = toevoeg.choiceMenu.getSelection();
        Integer examID = databaseConn.GetToetsID(selection.get(0),
                selection.get(1), selection.get(2), selection.get(3),
                selection.get(4), selection.get(5));
        toevoeg.examID = examID;
        toevoeg.examTab.setExamPropertiesScreen(selection.toArray(
                new String[0]));
        databaseConn.CloseConnection();
    }

    private static void initLayout(){
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
        toevoeg = new Toevoegen();
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
        scene = new Scene(frame, 1000, 600);
        window.setScene(scene);
        window.setTitle("Bapgc");
        window.show();
    }

    public static void updateSelectionMenu() {
        vergelijk.testChoiceMenu.updateSelectionMenu();
        vergelijk.courseChoiceMenu.updateSelectionMenu();
        vergelijk.blockChoiceMenu.updateSelectionMenu();
    }

}