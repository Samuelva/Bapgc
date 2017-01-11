package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Diego on 5-12-16.
 * 7-12-2016: Toevoegscherm is af
 * 9-12-2016: Documenteren van script
 * 11-12-2016: Layout aangepast
 * 12-12-2016: Script aangepast, constructor aangemaakt.
 * 15-12-2016: Vragen aanwezig functionaliteit toegevoegd
 * 17-12-2016: Importeren van punten toegevoegd
 * 19-12-2016: Module tab verwijderd, Modulen toevoegen tab toegevoegd.
 * 20-12-2016: Module variabelen verwijderd
 */
public class Toevoegen extends TabPane {
    //SELECTION MENU
    public ChoiceBox yearExamChoiceBox; //Jaartal
    public ChoiceBox schoolYearExamChoiceBox; //Schooljaar
    public ChoiceBox blockExamChoiceBox; //Periode
    public ChoiceBox courseExamChoiceBox; //Modules
    public ChoiceBox typeExamChoiceBox; //Toetsvorm
    public ChoiceBox attemptExamChoiceBox; //Gelegenheid

    //PROPERTY SCREENS
    public ChoiceBox yearPropertyChoiceBox;
    public ChoiceBox schoolYearPropertyChoiceBox;
    public ChoiceBox blockPropertyChoiceBox;
    public ChoiceBox coursePropertyChoiceBox;
    public ChoiceBox typePropertyChoiceBox;
    public ChoiceBox attemptExamPropertyChoiceBox;
    public DatePicker datePropertyDatePicker;
    public CheckBox questionPropertyCheckBox;
    public LinkedList<TextField> scoreDistributionArray = new LinkedList(); //puntenverdeling

    public ListView examOptionsList;

    public Button showExamBtn;
    public Button saveExamBtn;

    public TextField beheersGradTextfield;

    private FlowPane pointBox;
    private VBox pointDistributionBox;


    public Toevoegen(ObservableList yearElements, ObservableList schoolYearElements, ObservableList blockElements, ObservableList courseElements, ObservableList typeElements, ObservableList attemptElements, ListView examOptionsList, ListView moduleOptionsList) {
        this.yearExamChoiceBox = new ChoiceBox(yearElements);

        this.schoolYearExamChoiceBox = new ChoiceBox(schoolYearElements);

        this.blockExamChoiceBox = new ChoiceBox(blockElements);

        this.courseExamChoiceBox = new ChoiceBox(courseElements);

        this.typeExamChoiceBox = new ChoiceBox(typeElements);
        this.attemptExamChoiceBox = new ChoiceBox(attemptElements);
        this.examOptionsList = examOptionsList;
        setAppendingScreen();
    }

    public Toevoegen() {
        createSelectionMenu();
        createButtons();
        createAvailableOptions();
        setUpChangeListeners();

        setAppendingScreen();
    }

    private void setUpChangeListeners() {

        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value, Number oldWidth, Number newWidth) {
                Side side = getSide();
                int numTabs = getTabs().size();
                if ((side == Side.BOTTOM || side == Side.TOP) && numTabs != 0) {
                    setTabMinWidth(newWidth.intValue() / numTabs - (20));
                    setTabMaxWidth(newWidth.intValue() / numTabs - (20));
                }
            }
        });

        heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> value, Number oldHeight, Number newHeight) {
                Side side = getSide();
                int numTabs = getTabs().size();
                if ((side == Side.LEFT || side == Side.RIGHT) && numTabs != 0) {
                    setTabMinWidth(newHeight.intValue() / numTabs - (20));
                    setTabMaxWidth(newHeight.intValue() / numTabs - (20));
                }
            }
        });

        getTabs().addListener(new ListChangeListener<Tab>() {
            public void onChanged(ListChangeListener.Change<? extends Tab> change) {
                Side side = getSide();
                int numTabs = getTabs().size();
                if (numTabs != 0) {
                    if (side == Side.LEFT || side == Side.RIGHT) {
                        setTabMinWidth(heightProperty().intValue() / numTabs - (20));
                        setTabMaxWidth(heightProperty().intValue() / numTabs - (20));
                    }
                    if (side == Side.BOTTOM || side == Side.TOP) {
                        setTabMinWidth(widthProperty().intValue() / numTabs - (20));
                        setTabMaxWidth(widthProperty().intValue() / numTabs - (20));
                    }
                }
            }
        });
    }

    private void createAvailableOptions() {
        examOptionsList = new ListView();

        examOptionsList.setMaxWidth(150);
    }

    private void createButtons() {
        showExamBtn = new Button("Toets weergeven");
        saveExamBtn = new Button("Toets opslaan");

        showExamBtn.setMinWidth(150);
        saveExamBtn.setMinWidth(150);
    }

    private void createSelectionMenu() {
        /**
         * Aanmaken van de verschillende dropdown menu's waarin de gebruiker de juistte module of toets kan kiezen.
         */
        yearExamChoiceBox = new ChoiceBox(); //Jaartal
        schoolYearExamChoiceBox = new ChoiceBox(); //Schooljaar
        blockExamChoiceBox = new ChoiceBox(); //Periode
        courseExamChoiceBox = new ChoiceBox(); //Modules
        typeExamChoiceBox = new ChoiceBox(); //Toetsvorm
        attemptExamChoiceBox = new ChoiceBox(); //Gelegenheid

        yearExamChoiceBox.getItems().addAll("Jaar", new Separator(), "placeholder");
        schoolYearExamChoiceBox.getItems().addAll("Leerjaar", new Separator(), "Jaar 1", "Jaar 2", "Jaar 3", "Jaar 4");
        blockExamChoiceBox.getItems().addAll("Periode", new Separator(), "Periode 1", "Periode 2", "Periode 3", "Periode 4", "Periode 5");
        courseExamChoiceBox.getItems().addAll("Module", new Separator(), "placeholder");
        typeExamChoiceBox.getItems().addAll("Toetsvorm", new Separator(), "Theorietoets", "Praktijktoets", "Logboek", "Aanwezigheid", "Project");
        attemptExamChoiceBox.getItems().addAll("Gelegenheid", new Separator(), "1e kans", "2e kans");

        yearExamChoiceBox.setPrefSize(150, 30);
        schoolYearExamChoiceBox.setPrefSize(150, 30);
        blockExamChoiceBox.setPrefSize(150, 30);
        courseExamChoiceBox.setPrefSize(150, 30);
        typeExamChoiceBox.setPrefSize(150, 30);
        attemptExamChoiceBox.setPrefSize(150, 30);

        yearExamChoiceBox.setValue("Jaar");
        schoolYearExamChoiceBox.setValue("Leerjaar");
        blockExamChoiceBox.setValue("Periode");
        courseExamChoiceBox.setValue("Module");
        typeExamChoiceBox.setValue("Toetsvorm");
        attemptExamChoiceBox.setValue("Gelegenheid");

    }

    private Label createLabel(String header) {
        /**
         * Creeeren van label voor de verschillende headers. Geeft extra styling als de header gelijk is aan
         * Keuzemenu
         */
        Label label = new Label(header);
        label.setFont(new Font("Arial", 18));
        label.setAlignment(Pos.CENTER);
        setMaxWidth(Double.MAX_VALUE);
        label.setPadding(new Insets(0, 0, 20, 0));
        if (header == "Keuzemenu") {
            label.setPrefWidth(150);
        }
        return label;
    }

    private void setAppendingScreen() {
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.setTabMinWidth(477);
        this.getTabs().addAll(new ModuleTab(), new ExamTab());
    }

    public LinkedList getExamProperties() {
        LinkedList examProperties = new LinkedList();
        examProperties.add(yearPropertyChoiceBox.getValue());
        examProperties.add(schoolYearPropertyChoiceBox.getValue());
        examProperties.add(blockPropertyChoiceBox.getValue());
        examProperties.add(coursePropertyChoiceBox.getValue());
        examProperties.add(typePropertyChoiceBox.getValue());
        examProperties.add(attemptExamPropertyChoiceBox.getValue());
        if (datePropertyDatePicker.getValue() != null) {
            examProperties.add(Integer.toString(datePropertyDatePicker.getValue().getYear()));
        }
        examProperties.add(beheersGradTextfield.getText());
        if (!(scoreDistributionArray.isEmpty()) && questionPropertyCheckBox.isSelected()) {
            for (int i = 0; i < scoreDistributionArray.size(); i++) {
                examProperties.add(scoreDistributionArray.get(i).getText());
            }
        }
        return examProperties;
    }

    public LinkedList getAvailableExams() {
        LinkedList searchExamWithProperties = new LinkedList();
        searchExamWithProperties.add(yearExamChoiceBox.getValue());
        searchExamWithProperties.add(schoolYearExamChoiceBox.getValue());
        searchExamWithProperties.add(blockExamChoiceBox.getValue());
        searchExamWithProperties.add(courseExamChoiceBox.getValue());
        searchExamWithProperties.add(typeExamChoiceBox.getValue());
        searchExamWithProperties.add(attemptExamChoiceBox.getValue());
        return searchExamWithProperties;
    }



    private class ExamTab extends Tab {
        private BorderPane examPane;

        private Button newExamBtn;
        private Button pointDistributionButton;
        private Button importCsvButton;
        private Button resetPointDistributionButton;

        public Slider slider;

        private Desktop desktop = Desktop.getDesktop();

        public ExamTab() {
            this.setText("Toetsen");
            createButtons();
            examPane = new BorderPane();
            examPane.setLeft(getSelectionMenu());
            this.setContent(examPane);
        }

        private void createButtons() {
            pointDistributionButton = new Button("Weergeef vragen");
            importCsvButton = new Button("Importeer van csv");
            resetPointDistributionButton = new Button("Reset aantal vragen");

            resetPointDistributionButton.setDisable(true);

        }

        private void addPointFields(String header, String questions, String subQuestions, String points) {
            scoreDistributionArray = new LinkedList<TextField>();
            pointBox.setOrientation(Orientation.VERTICAL);

            String[] columnHeader = header.split(";");
            String[] columnQuestions = questions.split(";");
            String[] columnSubQuestions = subQuestions.split(";");
            String[] columnPoints = points.split(";");

            int total = 1;

            Integer questionNumber = 1;
            int index = 1;
            while (!columnHeader[index].contains("Tot")){
                try {
                    if (!columnQuestions[questionNumber].isEmpty() && false) {
                        System.out.println(columnQuestions[questionNumber] + "." + columnSubQuestions[index]);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Hey");
                }
                index++;
            }


            questionNumber = 1;
            for (String question : columnPoints) {
                try {
                    Integer point = Integer.parseInt(question);
                    if (total != point) {
                        total += point;
                        Label lbl = new Label(Integer.toString(questionNumber) + "->");
                        HBox questionInput = new HBox();
                        lbl.setMinWidth(30);
                        TextField textField = new TextField(question);
                        textField.setPrefWidth(38);
                        scoreDistributionArray.add(questionNumber - 1, textField);
                        questionInput.getChildren().addAll(lbl, scoreDistributionArray.get(questionNumber - 1));
                        pointBox.getChildren().add(questionInput);
                        questionNumber++;
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
            pointBox.setVgap(0);
        }

        private void addPointFields() {
            /**
             * Creeeren van een linkedlist waarin TextField elementen staan die nodig zijn voor de punten invoer.
             * In een loop afhankelijk van het aantal vragen die de toets heeft worden er textfields
             * toegevoegd aan de lijst. Ook wordt er een label gecreeerd zodat de gebruiker kan zien aan welke
             * vraag de gebruiker het aantal maximale punten toekent. Er wordt styling gegeven aan de elementen
             * Als laatst worden deze elementen toegevoegd aan de pointbox.
             */

            scoreDistributionArray = new LinkedList<TextField>();
            pointBox.setOrientation(Orientation.VERTICAL);


            for (int i = 1; i <= slider.getValue(); i++) {
                Region fill = new Region();
                HBox questionInput = new HBox();
                Label lbl = new Label(Integer.toString(i) + "->");
                lbl.setMinWidth(30);
                TextField textfield = new TextField();
                textfield.setPrefWidth(38);
                scoreDistributionArray.add(i - 1, textfield);
                questionInput.getChildren().addAll(lbl, scoreDistributionArray.get(i - 1));
                pointBox.getChildren().add(questionInput);
            }
            pointBox.setVgap(0);
        }

        private VBox getSelectionMenu() {
            VBox selectionBox = new VBox();
            createExamButtons();

            selectionBox.getChildren().addAll(createLabel("Keuzemenu"),
                    getExamChoiceBoxes(), examOptionsList, showExamBtn,
                    newExamBtn);
            selectionBox.setVgrow(examOptionsList, Priority.ALWAYS);
            selectionBox.setPadding(new Insets(0, 0, 0, 5));
            return selectionBox;
        }

        private VBox getExamChoiceBoxes() {
            VBox choiceBoxes = new VBox();
            choiceBoxes.getChildren().addAll(yearExamChoiceBox,
                    schoolYearExamChoiceBox, blockExamChoiceBox,
                    courseExamChoiceBox, typeExamChoiceBox,
                    attemptExamChoiceBox);
            choiceBoxes.setSpacing(20);
            return choiceBoxes;
        }

        private void createExamButtons() {
            newExamBtn = new Button("Toets aanmaken");

            newExamBtn.setMinWidth(150);
            setButtonEvents();
        }

        private void setButtonEvents() {
            newExamBtn.setOnAction(e -> {
                examPane.setCenter(createExamPropertiesScreen());
            });
            pointDistributionButton.setOnAction(e -> {
                addPointFields();
                pointDistributionButton.setDisable(true);
                importCsvButton.setDisable(true);
                resetPointDistributionButton.setDisable(false);
            });
            resetPointDistributionButton.setOnAction(e -> {
                pointBox.getChildren().clear();
                resetPointDistributionButton.setDisable(true);
                pointDistributionButton.setDisable(false);
                importCsvButton.setDisable(false);

            });
            importCsvButton.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Toets Bestand");
                File file = fileChooser.showOpenDialog(new Stage());
                if (file != null) {
                    importPointsFromCsv(file);
                    resetPointDistributionButton.setDisable(false);
                    pointDistributionButton.setDisable(true);
                    importCsvButton.setDisable(true);
                }
            });
        }

        private void importPointsFromCsv(File file) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String header = scanner.next();
                    String questions = scanner.next();
                    String subQuestions = scanner.next();
                    String points = scanner.next();
                    if (points.contains("Punten")) {
                        addPointFields(header,questions,subQuestions,points);
                        break;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("hey");

            }
        }

        private VBox createExamPropertiesScreen() {
            VBox vbox = new VBox();
            vbox.getChildren().addAll(getExamInformationBoxes(), getPointDistribution(), getSaveExamButton());
            vbox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            vbox.setPadding(new Insets(0, 20, 0, 20));

            return vbox;
        }

        private HBox getSaveExamButton() {
            /**
             * Aanmaken van de knop en stylen van de knop voor het opslaan van de examens.
             */
            HBox hbox = new HBox();
            hbox.getChildren().add(saveExamBtn);
            saveExamBtn.setPrefWidth(350);
            hbox.setAlignment(Pos.BOTTOM_CENTER);
            return hbox;
        }

        private VBox getPointDistribution() {
            pointDistributionBox = new VBox();
            pointBox = new FlowPane();
            ScrollPane scrollpane = new ScrollPane(pointBox);
            scrollpane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            pointDistributionBox.getChildren().addAll(createLabel("Puntenverdeling:"), getAmountOfQuestions(), scrollpane);
            return pointDistributionBox;
        }

        private HBox getAmountOfQuestions() {
            /**
             * Slider maken die het aantal vragen voor de toets bepaald. De styling aan de slider gebeurt ook in deze functie
             * Hierna volgt het toevoegen van een event aan de slider, zodat de gebruiker kan zien wat voor waarde de slider
             * nu heeft.
             */
            HBox hbox = new HBox();
            Label lbl1 = new Label("Aantal vragen: ");
            slider = new Slider();
            Label lbl2 = new Label("20");

            slider.setMin(0);
            slider.setMax(100);
            slider.setValue(20);
            slider.setShowTickLabels(true);
            slider.setPrefHeight(20);

            slider.valueProperty().addListener(new ChangeListener() {

                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                    lbl2.textProperty().setValue(
                            String.valueOf((int) slider.getValue()));

                }
            });
            hbox.getChildren().addAll(lbl1, slider, lbl2, new Region(), new HBox(resetPointDistributionButton, pointDistributionButton, importCsvButton));
            hbox.setHgrow(hbox.getChildren().get(3), Priority.ALWAYS);
            return hbox;
        }

        private HBox getExamInformationBoxes() {
            HBox hbox = new HBox();
            hbox.getChildren().addAll(createExamData(), getExamGrader());


            hbox.setHgrow(hbox.getChildren().get(0), Priority.ALWAYS);
            hbox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);
            hbox.setPadding(new Insets(0, 0, 0, 5));

            return hbox;

        }


        private VBox createExamData() {
            VBox vbox = new VBox();
            vbox.getChildren().addAll(createLabel("Toets Gegevens"), getExamDataBox());
            return vbox;
        }

        private VBox getExamDataBox() {
            VBox examDataVbox = new VBox();


            yearPropertyChoiceBox = new ChoiceBox();
            schoolYearPropertyChoiceBox = new ChoiceBox();
            blockPropertyChoiceBox = new ChoiceBox();
            coursePropertyChoiceBox = new ChoiceBox();
            typePropertyChoiceBox = new ChoiceBox();
            attemptExamPropertyChoiceBox = new ChoiceBox();
            datePropertyDatePicker = new DatePicker();

            yearPropertyChoiceBox.getItems().addAll("Jaar", new Separator(), "placeholder");
            schoolYearPropertyChoiceBox.getItems().addAll("Leerjaar", new Separator(), "Jaar 1", "Jaar 2", "Jaar 3", "Jaar 4");
            blockPropertyChoiceBox.getItems().addAll("Periode", new Separator(), "Periode 1", "Periode 2", "Periode 3", "Periode 4", "Periode 5");
            coursePropertyChoiceBox.getItems().addAll("Module", new Separator(), "placeholder");
            attemptExamPropertyChoiceBox.getItems().addAll("Gelegenheid", new Separator(), "1e", "2e", "3e");
            typePropertyChoiceBox.getItems().addAll("Toetsvorm", new Separator(), "Theorietoets", "Praktijktoets", "Logboek", "Aanwezigheid", "Project");

            yearPropertyChoiceBox.setPrefWidth(150);
            schoolYearPropertyChoiceBox.setPrefWidth(150);
            blockPropertyChoiceBox.setPrefWidth(150);
            coursePropertyChoiceBox.setPrefWidth(150);
            typePropertyChoiceBox.setPrefWidth(150);
            attemptExamPropertyChoiceBox.setPrefWidth(150);
            datePropertyDatePicker.setPrefWidth(150);

            yearPropertyChoiceBox.setValue("Jaar");
            schoolYearPropertyChoiceBox.setValue("Leerjaar");
            blockPropertyChoiceBox.setValue("Periode");
            coursePropertyChoiceBox.setValue("Module");
            attemptExamPropertyChoiceBox.setValue("Gelegenheid");
            typePropertyChoiceBox.setValue("Toetsvorm");

            examDataVbox.getChildren().addAll(yearPropertyChoiceBox, schoolYearPropertyChoiceBox, blockPropertyChoiceBox, coursePropertyChoiceBox, typePropertyChoiceBox, attemptExamPropertyChoiceBox, datePropertyDatePicker);
            examDataVbox.setSpacing(12);

            return examDataVbox;


        }

        private VBox getExamGrader() {
            /**
             * Gegevens over het bepalen van een cijfer voor de toets.
             */
            VBox vbox = new VBox();
            vbox.getChildren().addAll(createLabel("Cijfer Gegevens"), getGradeData());

            return vbox;
        }

        private HBox getGradeData() {
            /**
             * Gegevens over het bepalen van een cijfer voor de toets.
             * DE FORMATTER WERKT NIET OVERAL!! Waarschijnlijk mist er een library!
             */
            HBox hbox = new HBox();
            questionPropertyCheckBox = new CheckBox();
            questionPropertyCheckBox.setSelected(true);
            questionPropertyCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        pointDistributionBox.setVisible(true);
                    } else {
                        pointDistributionBox.setVisible(false);
                        resetPointDistributionButton.setDisable(true);
                        pointDistributionButton.setDisable(false);
                        importCsvButton.setDisable(false);
                    }
                }
            });


            VBox vbox1 = new VBox();
            VBox vbox2 = new VBox();

            Label lbl1 = new Label("Beheersgraad");
            Label lbl2 = new Label("Vragen aanwezig: ");

            lbl1.setPrefSize(100, 25);

            beheersGradTextfield = new TextField();

//        textField1.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
//        textField2.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

            vbox1.getChildren().addAll(lbl1, lbl2);
            vbox2.getChildren().addAll(beheersGradTextfield, questionPropertyCheckBox);
            hbox.getChildren().addAll(vbox1, vbox2);
            vbox1.setSpacing(20);
            vbox2.setSpacing(20);


            return hbox;
        }

    }


    private class ModuleTab extends Tab {
        private BorderPane modulePane;


        public ModuleTab() {
            this.setText("Modulen inladen");
        }
    }
}


