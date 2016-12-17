package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage window;
    private static Scene scene;
    private static VBox frame;
    private static ViewScreen view;
    private static Toevoegscherm toevoeg;
    private static Invoeren invoer;
    private static Vergelijken vergelijk;
    private static TabPane tabPane;
    private static Tab toevoegen;
    private static Tab invoeren;
    private static Tab inzien;
    private static Tab vergelijken;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        initLayout();

        initTabs();

        showScreen();
    }

    private static void initLayout(){
        frame = new VBox();

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
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
        toevoeg = new Toevoegscherm();
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