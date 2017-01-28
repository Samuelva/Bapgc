package sample;

import database.DatabaseConn;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class Main extends Application {

    private static Stage window;
    private static Scene scene;
    private static VBox frame;
    private static ViewScreen view;
    private static Toevoegen toevoeg;
    private static Invoeren invoer;
    private static Vergelijken vergelijk;
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
        toevoeg.examTab.resetPointDistributionButton.setOnAction(event -> {
            toevoeg.resetWarning();
        });
        toevoeg.saveExamBtn.setOnAction(event -> {
            DatabaseConn databaseConn = new DatabaseConn();
            databaseConn.UpdateCesuurGok(toevoeg.examID,Integer.parseInt(toevoeg.thresholdTextfield.getText()), Integer.parseInt(toevoeg.chanceByGamblingTextfield.getText()));
            if (toevoeg.questionPropertyCheckBox.isSelected()) {
                for (String[] questionInfoArray: toevoeg.getQuestionInfo()) {
                    databaseConn.DeleteVragenToets(toevoeg.examID);
                    databaseConn.InputVraag(questionInfoArray[0], Integer.parseInt(questionInfoArray[1]), toevoeg.examID, questionInfoArray[2].equals("true") ? true : false);
                }
            }
            databaseConn.CloseConnection();
        });
        toevoeg.choiceMenu.examLoadButton.setOnAction(event -> {
            DatabaseConn databaseConn = new DatabaseConn();
            List<String> selection = toevoeg.choiceMenu.getSelection();
            Integer examID = databaseConn.GetToetsID(selection.get(0), selection.get(1), selection.get(2), selection.get(3), selection.get(4), selection.get(5));
            toevoeg.examID = examID;
            toevoeg.examTab.setExamPropertiesScreen(selection.toArray(new String[0]));
            databaseConn.CloseConnection();
        });


        invoer.loadTest.setOnAction(event -> {
            String[] searchOnProperties = invoer.getSelectionProperties();
            if (searchOnProperties == null) {
                warning();
            }
            else {
                view.setSelection(searchOnProperties);
                toevoeg.setSelection(searchOnProperties);
                int examID = 1; //HIER MOET DE TOETS ID OPGEHAALD WORDEN MBV HET KEUZEMENU!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                invoer.fillTable(examID);
            }
        });

        view.loadBtn.setOnAction(event -> {
            String[] searchOnProperties = view.getSelectionProperties();
            if (searchOnProperties == null) {
                warning();
            }
            else {
                invoer.setSelection(searchOnProperties);
                toevoeg.setSelection(searchOnProperties);
                int examID = 1; //HIER MOET HER ID VAN DE IN HET KEUZEMENU GESELECTEERDE TOETS OPGEHAALD WORDEN!!!!!!
                view.fillTable(examID);

                view.updateQualityStats();
            }
        });
    }

    private void warning(String header, String text) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private void warning() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Niet alles is ingevoerd!");
        alert.setContentText("Voer de niet gevoerde keuzes in het "
                + "keuzemenu in om verder te gaan.");
        alert.showAndWait();
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
        invoeren.setText("Invoeren");
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


        invoer = new Invoeren();
        invoeren.setContent(invoer);

        view = new ViewScreen();
        inzien.setContent(view);

        vergelijk = new Vergelijken();
        vergelijken.setContent(vergelijk);

        frame.getChildren().addAll(tabPane);

    }

    private void showScreen(){
        scene = new Scene(frame, 1000, 600);
        window.setScene(scene);
        window.setTitle("Bapgc");
        window.show();
    }

}