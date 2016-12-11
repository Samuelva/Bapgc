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
    private VBox statisticsBox;
    private ScrollPane statisticsScrollPane;
    private HBox statisticsGridBox;
    private BorderPane graphButtonBox;
    private StackPane graphPane;

    private ComboBox graphButton;
    private Button saveButton;
    private ImageView graph;

    Grafiek test = new Grafiek();

    public Statistiek() {
        // Box met toets statistieken, grafiekopties en de grafiek
        statisticsBox = new VBox();
        // Box waarin gridboxen met statistieken in zitten
        statisticsGridBox = new HBox();
        // Scrollpane met de gridbox waarin de statistieken in zitten
        statisticsScrollPane = new ScrollPane();
        // Box met grafiek optie knoppen
        graphButtonBox = new BorderPane();
        // Pane met de grafiek
        graphPane = new StackPane();

        graphButton = new ComboBox();
        graphButton.setPrefWidth(150);
        graphButton.setPrefHeight(30);
        graphButton.setPromptText("Grafiek Soort");
        graphButton.getItems().addAll("Boxplot", "Histogram");
        saveButton = new Button("Afbeelding opslaan");
        saveButton.setPrefWidth(150);
        saveButton.setPrefHeight(30);
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
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
//        graphPane.setCenter(graph);

        statisticsScrollPane.setContent(statisticsGridBox);
        statisticsScrollPane.setMinHeight(150);
        statisticsBox.getChildren().addAll(statisticsScrollPane, graphButtonBox, graphPane);

        HBox.setHgrow(statisticsBox, Priority.ALWAYS);
        VBox.setVgrow(graphPane, Priority.ALWAYS);
    }

    /**
     * Met deze functie kan er een boxje met statistieken toegevoegd worden aan de scrollbare pane.
     * Gebruik: klasseInstantie.addStatistics(10, 30, 40, 30, 40); voor als gemiddelde cijfer
     * erbij moet, anders klasseInstantie.addStatistics(30, 40, 50, 30);
     */
    public void addStatistics(int[] values) {
        GridPane statisticsGrid = new GridPane();

        Label averageGradeLbl = new Label("Gemiddelde cijfer: ");
        Label participantsLbl = new Label("Aantal Deelnemers: ");
        Label failedLbl = new Label("Aantal onvoldoendes: ");
        Label passedLbl = new Label("Aantal voldoendes: ");
        Label passRateLbl = new Label("Rendement: ");

        // Als er 5 getallen meegegeven worden, wordt er een grid met het gemiddelde cijfer gemaakt
        if (values.length == 5) {
            Label averageGrade = new Label(Integer.toString(values[0]));
            Label participants = new Label(Integer.toString(values[1]));
            Label failed = new Label(Integer.toString(values[2]));
            Label passed = new Label(Integer.toString(values[3]));
            Label passRate = new Label(Integer.toString(values[4]));

            statisticsGrid.add(averageGradeLbl, 1, 1);
            statisticsGrid.add(participantsLbl, 1, 2);
            statisticsGrid.add(failedLbl, 1, 3);
            statisticsGrid.add(passedLbl, 1, 4);
            statisticsGrid.add(passRateLbl, 1, 5);
            statisticsGrid.add(averageGrade, 2, 1);
            statisticsGrid.add(participants, 2, 2);
            statisticsGrid.add(failed, 2, 3);
            statisticsGrid.add(passed, 2, 4);
            statisticsGrid.add(passRate, 2, 5);
        }

        else if (values.length == 4) {
            Label participants = new Label(Integer.toString(values[0]));
            Label failed = new Label(Integer.toString(values[1]));
            Label passed = new Label(Integer.toString(values[2]));
            Label passRate = new Label(Integer.toString(values[3]));

            statisticsGrid.add(participantsLbl, 1, 1);
            statisticsGrid.add(failedLbl, 1, 2);
            statisticsGrid.add(passedLbl, 1, 3);
            statisticsGrid.add(passRateLbl, 1, 4);
            statisticsGrid.add(participants, 2, 1);
            statisticsGrid.add(failed, 2, 2);
            statisticsGrid.add(passed, 2, 3);
            statisticsGrid.add(passRate, 2, 4);
        }

        statisticsGrid.setVgap(5);
        statisticsGrid.setHgap(5);
        statisticsGrid.setPadding(new Insets(15, 15, 15, 15));
        statisticsGridBox.getChildren().add(statisticsGrid);
    }

    // Returned de box met toets statistieken, grafiekopties en de grafiek
    public VBox returnStatisticsBox() {
        statisticsBox.setPadding(new Insets(5, 5, 5, 10));
        return statisticsBox;
    }

    // Input is een link/path naar de grafiek
    public void setGraph() {
        graphPane.getChildren().add(test.returnGraph());
    }
}
