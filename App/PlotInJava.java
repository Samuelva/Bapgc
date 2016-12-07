package plotinjava;

import java.io.File;
import javafx.scene.layout.StackPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.rosuda.REngine.*;
import org.rosuda.REngine.Rserve.*;

public class PlotInJava extends Application {
    
        public static void main(String[] args) throws REXPMismatchException {
            RConnection connection = null;

            try {
                connection = new RConnection();

                connection.eval("source('C:/Users/Aaricia/Documents/School/Jaar 4/Bapgc/Test.R')");
                connection.eval("histogram('Jaar')");
            } catch (RserveException e) {
                e.printStackTrace();
            }
            launch(args);
    }
    
    public void start(Stage primaryStage) {
        
        File file = new File("C:/Users/Aaricia/Documents/School/Jaar 4/Bapgc/Histogram.jpg");
        Image image = new Image(file.toURI().toString());
        ImageView iv = new ImageView(image);
        
        StackPane root = new StackPane();
        root.getChildren().add(iv);
        
        Scene scene = new Scene(root, 600, 500);
        
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
}
