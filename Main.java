package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class Main extends Application {

    private static Stage window;
    private static Scene scene;
    private static VBox frame;
    private static HBox choice;
    private static Button btn1;
    private static Button btn2;
    private static Button btn3;
    private static Button btn4;
    private static Toevoegen toevoegen;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;

        initBoxes();
        initButtons();
        events();
        makeScene();
        showScreen();
    }

    private static void initBoxes(){
        frame = new VBox();
        choice = new HBox();
    }

    private void initButtons(){
        btn1 = new Button ("Toevoegen");
        btn2 = new Button ("Invoeren");
        btn3 = new Button ("Inzien");
        btn4 = new Button ("Vergelijken");

        btn1.setPrefSize(250, 30);
        btn2.setPrefSize(250, 30);
        btn3.setPrefSize(250, 30);
        btn4.setPrefSize(250, 30);

        HBox.setHgrow(btn1, Priority.ALWAYS);
        HBox.setHgrow(btn2, Priority.ALWAYS);
        HBox.setHgrow(btn3, Priority.ALWAYS);
        HBox.setHgrow(btn4, Priority.ALWAYS);

        btn1.setMaxWidth(Double.MAX_VALUE);
        btn2.setMaxWidth(Double.MAX_VALUE);
        btn3.setMaxWidth(Double.MAX_VALUE);
        btn4.setMaxWidth(Double.MAX_VALUE);
    }

    private void events(){
        btn1.setOnAction(e -> toevoegen.setVisible(true));
        btn2.setOnAction(e -> toevoegen.setVisible(false));
    }

    private void makeScene(){
        choice.getChildren().addAll(btn1, btn2, btn3, btn4);
        toevoegen = new Toevoegen();
        frame.getChildren().addAll(choice, toevoegen);
        toevoegen.setVisible(false);
        scene = new Scene(frame, 1000, 600);
    }

    private void showScreen(){
        window.setScene(scene);
        window.setTitle("Bapgc");
        window.show();
    }


}