package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javax.swing.*;

/**
 * Created by Samuel on 5-12-2016.
 * Deze klasse maakt het statistiek gedeelte aan en kan deze uitbreiden/aanpassen
 */
public class Statistiek {
    private VBox statisticsBox; // Box met toets statistieken, grafiekopties en de grafiek
    private HBox statisticsGridBox; // Box waarin gridboxen met statistieken in zitten
    private ScrollPane statisticsScrollPane; // Scrollpane met de gridbox waarin de statistieken in zitten
    private BorderPane graphButtonBox; // Box met grafiek optie knoppen
    private BorderPane graphPane; // Pane met de grafiek

    private ComboBox graphButton;
    private Button saveButton;
    private ImageView graph;  // Container met grafiek foto

    public Statistiek() {
        /**
         * Roept functies aan welke de boxjes aanmaken en vullen met de jusite inhoud (statistiek en grafiek)
         */
        createStatisticsScrollPane();
        createGraphPane();
    }

    public void createStatisticsScrollPane() {
        /**
         * Maakt het scrollbare paneel aan waar de statistiek in weergegeven zal worden.
         */
        statisticsBox = new VBox();
        statisticsGridBox = new HBox();
        statisticsScrollPane = new ScrollPane();

        statisticsScrollPane.setContent(statisticsGridBox);
    }

    public void createGraphPane() {
        /**
         * Maakt het grafiek gedeelte aan met de box voor de grafiek knoppen en de grafiek zelf.
         */
        graphButtonBox = new BorderPane();
        graphPane = new BorderPane();
        graph = new ImageView();
        graph.setFitHeight(300);
        graph.setPreserveRatio(true);

        graphButton = new ComboBox(); // ComboBox voor het instellen van de soort grafiek
        graphButton.setPrefWidth(150);
        graphButton.setMinHeight(30);
        graphButton.setPromptText("Grafiek Soort");
        graphButton.getItems().addAll("Boxplot", "Histogram"); // Inhoud comboBox

        saveButton = new Button("Afbeelding opslaan");
        saveButton.setPrefWidth(150);
        saveButton.setMinHeight(30);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                /**
                 * Functie voor het openen van een systeem menu om de grafiek op te slaan.
                 */
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedLookAndFeelException e1) {
                    e1.printStackTrace();
                }
                JFrame parentFrame = new JFrame();
                JFileChooser saveMenu = new JFileChooser();
                saveMenu.setDialogTitle("Opslaan als");
                saveMenu.showSaveDialog(parentFrame);
            }
        });

        graphButtonBox.setLeft(graphButton);
        graphButtonBox.setRight(saveButton);
        graphButtonBox.setPadding(new Insets(5, 5, 5, 5));
        graphPane.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, null, null)));
        graphPane.setCenter(graph);
        VBox.setVgrow(graphPane, Priority.ALWAYS);

    }

    public void addStatistics(String[] values) {
        /**
         * Met deze functie kan er een boxje met statistieken toegevoegd worden aan de scrollbare pane.
         * Gebruik: klasseInstantie.addStatistics(10, 30, 40, 30, 40); voor als gemiddelde cijfer
         * erbij moet, anders klasseInstantie.addStatistics(30, 40, 50, 30);
         */
        GridPane statisticsGrid = new GridPane();

        Label titleLbl = new Label(values[0]);

        // Als er 5 getallen meegegeven worden, wordt er een grid met het gemiddelde cijfer gemaakt
        if (values.length == 6) {
            Label averageGrade = new Label(values[1]);
            Label participants = new Label(values[2]);
            Label failed = new Label(values[3]);
            Label passed = new Label(values[4]);
            Label passRate = new Label(values[5]);

            statisticsGrid.add(titleLbl, 1, 1);
            statisticsGrid.add(averageGrade, 1, 2);
            statisticsGrid.add(participants, 1, 3);
            statisticsGrid.add(failed, 1, 4);
            statisticsGrid.add(passed, 1, 5);
            statisticsGrid.add(passRate, 1, 6);
            statisticsScrollPane.setMinHeight(160);
        }

        else if (values.length == 5) {
            Label participants = new Label(values[1]);
            Label failed = new Label(values[2]);
            Label passed = new Label(values[3]);
            Label passRate = new Label(values[4]);

            statisticsGrid.add(titleLbl, 1, 1);
            statisticsGrid.add(participants, 1, 2);
            statisticsGrid.add(failed, 1, 3);
            statisticsGrid.add(passed, 1, 4);
            statisticsGrid.add(passRate, 1, 5);
        }

        statisticsGrid.setVgap(5);
        statisticsGrid.setHgap(5);
        statisticsGrid.setPadding(new Insets(10, 10, 10, 10));
        statisticsGridBox.getChildren().add(statisticsGrid);
    }

    public VBox returnStatisticsBox() {
        /**
         * Returned de box met toets statistieken, grafiekopties en de grafiek
         */
        statisticsBox.setPadding(new Insets(0, 0, 0, 5));
        statisticsBox.getChildren().addAll(statisticsScrollPane, graphButtonBox, graphPane);
        HBox.setHgrow(statisticsBox, Priority.ALWAYS);
        return statisticsBox;
    }

    public void setGraph(String graphFile) {
        /**
         * Input is een link/path naar de grafiek
         */
        Image graphImage = new Image(graphFile);
        graph.setImage(graphImage);
    }
}
