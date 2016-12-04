package bapgc;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class Invoerscherm extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Invoerscherm");
        
        Button btn1 = new Button("Toets laden");
        Button btn2 = new Button("Leeg maken");
        Button btn3 = new Button("Wijzigingen opslaan");
        Button btn4 = new Button("Import CSV");
        
        btn1.setPrefSize(250, 50);
        btn2.setPrefSize(250, 50);
        btn3.setPrefSize(250, 50);
        btn4.setPrefSize(250, 50);
       
        HBox hbox = new HBox();
        HBox.setHgrow(btn1,Priority.ALWAYS);
        HBox.setHgrow(btn2,Priority.ALWAYS);
        HBox.setHgrow(btn3,Priority.ALWAYS);
        HBox.setHgrow(btn4,Priority.ALWAYS);
        
        btn1.setMaxWidth(Double.MAX_VALUE);
        btn2.setMaxWidth(Double.MAX_VALUE);
        btn3.setMaxWidth(Double.MAX_VALUE);
        btn4.setMaxWidth(Double.MAX_VALUE);
        
        btn1.setMaxHeight(Double.MAX_VALUE);
        btn2.setMaxHeight(Double.MAX_VALUE);
        btn3.setMaxHeight(Double.MAX_VALUE);
        btn4.setMaxHeight(Double.MAX_VALUE);
        hbox.setPrefSize(1000, 100);
        hbox.getChildren().addAll(btn1, btn2, btn3, btn4);
        
        Label lbl1 = new Label("Keuzemenu");
        lbl1.setAlignment(Pos.CENTER);
        lbl1.setPrefSize(100,100);
        lbl1.setFont(Font.font(null, FontWeight.BOLD, 15));
        MenuItem jaartal1 = new MenuItem("2014-2015");
        MenuItem jaartal2 = new MenuItem("2015-2016");
        MenuItem jaartal3 = new MenuItem("2016-2017");
        MenuButton jaartal = new MenuButton("Jaartal", null, 
                jaartal1, jaartal2, jaartal3);
        
        MenuItem leerjaar1 = new MenuItem("Jaar 1");
        MenuItem leerjaar2 = new MenuItem("Jaar 2");
        MenuItem leerjaar3 = new MenuItem("Jaar 3");
        MenuItem leerjaar4 = new MenuItem("Jaar 4");
        MenuButton leerjaar = new MenuButton("Leerjaar", null, leerjaar1, 
                leerjaar2, leerjaar3, leerjaar4);
        
        MenuItem periode1 = new MenuItem("Periode 1");
        MenuItem periode2 = new MenuItem("Periode 2");
        MenuItem periode3 = new MenuItem("Periode 3");
        MenuItem periode4 = new MenuItem("Periode 4");
        MenuButton periode = new MenuButton("Periode", null, periode1, 
                periode2, periode3, periode4);
        
        MenuItem module1 = new MenuItem("Modules");
        MenuButton modules = new MenuButton("Module", null, module1);
        
        MenuItem theorietoets = new MenuItem("Theorietoets");
        MenuItem praktijktoets = new MenuItem("Praktijktoets");
        MenuItem opdracht = new MenuItem("Opdracht");
        MenuItem aanwezigheid = new MenuItem("Aanwezigheid");
        MenuItem logboek = new MenuItem("Logboek");
        MenuItem project = new MenuItem("Project"); 
        MenuButton toetsvorm = new MenuButton("Toetsvorm", null, theorietoets, 
                praktijktoets, opdracht, aanwezigheid, logboek, project);
        
        MenuItem gelegenheid1 = new MenuItem("Gelegenheid 1");
        MenuItem gelegenheid2 = new MenuItem("Gelegenheid 2");
        MenuButton gelegenheid = new MenuButton("Gelegenheid", null, 
                gelegenheid1, gelegenheid2);     
        
        
        VBox vbox2 = new VBox();
        vbox2.setPrefSize(150, 600);
        jaartal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        leerjaar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        periode.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        modules.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toetsvorm.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gelegenheid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vbox2.getChildren().addAll(lbl1, jaartal, leerjaar, periode, modules, 
                toetsvorm, gelegenheid);
        
        HBox hbox3 = new HBox();
        Button btn5 = new Button("Nieuwe Student");
        btn5.setPrefSize(150,50);
        Label lbl2 = new Label("Vragen");
        lbl2.setPrefSize(700, 50);
        lbl2.setAlignment(Pos.CENTER);
        lbl2.setFont(Font.font(null, FontWeight.BOLD, 15));
        hbox3.getChildren().addAll(btn5, lbl2);
       
        HBox hbox2 = new HBox();
        TextArea textArea = new TextArea();
        
        VBox vbox3 = new VBox();
        textArea.setPrefSize(850,500);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        vbox3.getChildren().addAll(hbox3, textArea);
        HBox.setHgrow(vbox3, Priority.ALWAYS);
        hbox2.getChildren().addAll(vbox2, vbox3);
        
        VBox vbox = new VBox();
        VBox.setVgrow(hbox2, Priority.ALWAYS);
        VBox.setVgrow(hbox, Priority.ALWAYS);
        vbox.getChildren().addAll(hbox2, hbox);
        Scene scene = new Scene(vbox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

   
    public static void main(String[] args) {
        launch(args);
    }
    
}
