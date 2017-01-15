package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created by Diego on 05-12-16.
 * 7-12-2016: Toevoegscherm is af
 * 9-12-2016: Documenteren van script
 * 11-12-2016: Layout aangepast
 * 12-12-2016: Script aangepast, constructor aangemaakt.
 * 15-12-2016: Vragen aanwezig functionaliteit toegevoegd
 * 17-12-2016: Importeren van punten toegevoegd
 * 19-12-2016: Module tab verwijderd, Modulen toevoegen tab toegevoegd.
 * 20-12-2016: Module variabelen verwijderd
 * 13-01-2017: Toevoeg scherm opnieuw geschreven
 * 15-01-2017: Verder met ontwikkelen
 */
public class Toevoegen extends TabPane{
    //SELECTION MENU
    public ChoiceBoxes yearExamChoiceBox; //Jaartal
    public ChoiceBoxes schoolYearExamChoiceBox; //Schooljaar
    public ChoiceBoxes blockExamChoiceBox; //Periode
    public ChoiceBoxes courseExamChoiceBox; //Modules
    public ChoiceBoxes typeExamChoiceBox; //Toetsvorm
    public ChoiceBoxes attemptExamChoiceBox; //Gelegenheid
    public ScreenButtons showExamBtn;
    public ScreenButtons saveExamBtn;

    //EXAM PROPERTIES
    public ChoiceBox yearPropertyChoiceBox;
    public ChoiceBox schoolYearPropertyChoiceBox;
    public ChoiceBox blockPropertyChoiceBox;
    public ChoiceBox coursePropertyChoiceBox;
    public ChoiceBox typePropertyChoiceBox;
    public ChoiceBox attemptExamPropertyChoiceBox;
    public DatePicker datePropertyDatePicker;
    public CheckBox questionPropertyCheckBox;
    public TextField thresholdTextfield;

    public Toevoegen() {
        createSelectionMenuElements();
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.setTabMinWidth(100);
        this.getTabs().add(new examTab("Toetsen"));
    }


    private void createSelectionMenuElements(){
        createSelectionMenuChoiceBoxes();
        createSelectionMenuButtons();
    }

    private void createSelectionMenuChoiceBoxes() {
        yearExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Jaar", "placeholder")));
        schoolYearExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Leerjaar", "Jaar 1", "Jaar 2", "Jaar 3", "Jaar 4")));
        blockExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Periode", "Periode 1", "Periode 2", "Periode 3", "Periode 4", "Periode 5")));
        courseExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Module",  "placeholder")));
        typeExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Toetsvorm",  "Theorietoets", "Praktijktoets", "Logboek", "Aanwezigheid", "Project")));
        attemptExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Gelegenheid",  "1e kans", "2e kans")));
    }

    private void createSelectionMenuButtons() {
        showExamBtn = new ScreenButtons("Toets weergeven");
        saveExamBtn = new ScreenButtons("Toets opslaan");
    }

    private class examTab extends Tab {
        private BorderPane examPane = new BorderPane();

        private VBox selectionMenu;


        private ScreenButtons newExamBtn = new ScreenButtons("Nieuwe Toets");
        private ScreenButtons pointDistributionButton = new ScreenButtons("Weergeef vragen");
        private ScreenButtons importCsvButton = new ScreenButtons("Importeer CSV");
        private ScreenButtons resetPointDistributionButton = new ScreenButtons("Reset");
        private VBox pointDistributionBox;
        private FlowPane questionAndCheckboxes;



        public examTab(String text) {
            super(text);
            fillSelectionMenu();
            setButtonEventsExamTab();
            examPane.setLeft(selectionMenu);
            examPane.setPadding(new Insets(5, 5, 5, 5));

            selectionMenu.setPrefHeight(1081);
            this.setContent(examPane);
        }

        private void fillSelectionMenu() {
            selectionMenu = new VBox();
            Region spacer = new Region();
            selectionMenu.getChildren().addAll(
                    getChoiceBoxesSelectionMenu(),
                    spacer,
                    newExamBtn,
                    showExamBtn);
            selectionMenu.setVgrow(spacer, Priority.ALWAYS);
        }

        private VBox getChoiceBoxesSelectionMenu() {
            VBox labelAndChoiceBoxesBox = new VBox();
            labelAndChoiceBoxesBox.getChildren().addAll(
                    new BoxHeaders("Keuzemenu"),
                    yearExamChoiceBox,
                    schoolYearExamChoiceBox,
                    blockExamChoiceBox,
                    courseExamChoiceBox,
                    typeExamChoiceBox,
                    attemptExamChoiceBox
            );
            labelAndChoiceBoxesBox.setSpacing(20);
            return labelAndChoiceBoxesBox;
        }

        private void setButtonEventsExamTab() {
            newExamBtn.setOnAction(e -> {
                createDataChoiceBoxes();
                examPane.setCenter(createExamPropertiesScreen());
            });
            importCsvButton.setOnAction( e -> {
                importQuestionsFromCSV();
                importCsvButton.setDisable(true);
                resetPointDistributionButton.setDisable(false);
            });
            resetPointDistributionButton.setOnAction( event -> {
                questionAndCheckboxes.getChildren().clear();
                pointDistributionBox.getChildren().remove(questionAndCheckboxes);
                importCsvButton.setDisable(false);
                resetPointDistributionButton.setDisable(true);
            });
        }

        private void importQuestionsFromCSV() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Toets Bestand");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                findLinesWithQuestionPointDistribution(file);
            }
        }

        private void findLinesWithQuestionPointDistribution(File file) {
            try {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String questions = scanner.nextLine();
                    if (questions.contains("Opgave")){
                        String subQuestions = scanner.nextLine();
                        String subQuestionsPoints = scanner.nextLine();
                        extractQuestionsFromLines(questions.split(";"), subQuestions.split(";"), subQuestionsPoints.split(";"));
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("hey");

            }
        }

        private void extractQuestionsFromLines(String[] questions, String[] subQuestions, String[] subQuestionsPoints) {
            int indexVraagnummer;
            questionAndCheckboxes = new FlowPane();
            questionAndCheckboxes.setMaxHeight(140);
            for (int i = 0; i < subQuestions.length; i++) {
                if (subQuestions[i].contains("Vraagnummer:")) {
                    indexVraagnummer = i;
                    int index = i+2; //Remove Vraagnummer and whitespaces
                    String currentQuestion = "1";
                    while ((questions[index].length() != 0 || subQuestions[index].length() != 0 && subQuestionsPoints[index].length() != 0) || index-1 == indexVraagnummer) {
                        if (questions[index].length() != 0) {
                            currentQuestion = questions[index];
                        }
                        questionAndCheckboxes.getChildren().add(new QuestionBoxWithCheck(currentQuestion, subQuestions[index], subQuestionsPoints[index]));
                        index++;
                    }
                }
            }
            questionAndCheckboxes.setOrientation(Orientation.VERTICAL);
            questionAndCheckboxes.setVgap(4);
            questionAndCheckboxes.setHgap(10);
            questionAndCheckboxes.setPrefHeight(150);
            saveExamBtn.setAlignment(Pos.CENTER);
            pointDistributionBox.getChildren().add(2,questionAndCheckboxes);
        }

        private void createDataChoiceBoxes() {
            yearPropertyChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Jaar", "placeholder")));
            schoolYearPropertyChoiceBox =  new ChoiceBoxes(new ArrayList<>(Arrays.asList("Leerjaar", "Jaar 1", "Jaar 2", "Jaar 3", "Jaar 4")));
            blockPropertyChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Periode", "Periode 1", "Periode 2", "Periode 3", "Periode 4", "Periode 5")));
            coursePropertyChoiceBox  = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Module",  "placeholder")));
            attemptExamPropertyChoiceBox  = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Gelegenheid",  "1e kans", "2e kans")));
            typePropertyChoiceBox  = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Toetsvorm",  "Theorietoets", "Praktijktoets", "Logboek", "Aanwezigheid", "Project")));
            datePropertyDatePicker = new DatePicker();
            datePropertyDatePicker.setPrefWidth(150);
        }

        private VBox createExamPropertiesScreen() {
            VBox vbox = new VBox();
            VBox buttonBox = new VBox(saveExamBtn);
            vbox.getChildren().addAll(getExamInformationBoxes(), getPointDistribution(), buttonBox);
            buttonBox.setAlignment(Pos.CENTER);
            vbox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            vbox.setPadding(new Insets(0, 20, 0, 20));

            return vbox;
        }

        private VBox getPointDistribution() {
            pointDistributionBox = new VBox();
            pointDistributionBox.getChildren().addAll(new BoxHeaders("Puntenverdeling/Meerekenen:"), getImportQuestionButtons());
            return pointDistributionBox;
        }


        private HBox getImportQuestionButtons() {
            HBox questionButtonBox = new HBox();
            questionButtonBox.getChildren().addAll(importCsvButton, resetPointDistributionButton);
            resetPointDistributionButton.setDisable(true);
            questionButtonBox.setAlignment(Pos.TOP_RIGHT);
            return questionButtonBox;
        }

        public HBox getExamInformationBoxes() {
            HBox hbox = new HBox();
            hbox.getChildren().addAll(createExamData(), getExamGrader());


            hbox.setHgrow(hbox.getChildren().get(0), Priority.ALWAYS);
            hbox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);
            hbox.setPadding(new Insets(0, 0, 0, 5));

            return hbox;        }

        private Node getExamGrader() {
            VBox vbox = new VBox();
            vbox.getChildren().addAll(new BoxHeaders("Cijfer Gegevens"), getGradeData());
            vbox.setSpacing(20);
            return vbox;
        }

        private Node getGradeData() {
            HBox hbox = new HBox();

            VBox vbox1 = getGradePropertyLabels();
            VBox vbox2 = getPropertyInputFields();
            hbox.getChildren().addAll(vbox1, vbox2);

            return hbox;
        }

        private VBox getPropertyInputFields() {
            VBox vbox2 = new VBox();
            questionPropertyCheckBox = new CheckBox();
            questionPropertyCheckBox.setSelected(true);
            setQuestionPropertyEvent();
            thresholdTextfield = new TextField();
            vbox2.getChildren().addAll(thresholdTextfield, questionPropertyCheckBox);
            thresholdTextfield.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{1,3}")) {
                        thresholdTextfield.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (thresholdTextfield.getText().length() > 2) {
                        String s = thresholdTextfield.getText().substring(0, 2);
                        thresholdTextfield.setText(s);
                    }
                }
            });
            vbox2.setSpacing(20);
            return vbox2;
        }

        private VBox getGradePropertyLabels() {
            VBox vbox1 = new VBox();
            Label lbl1 = new Label("Beheersgraad:");
            Label lbl2 = new Label("Vragen aanwezig:");
            lbl1.setPrefSize(125, 25);
            lbl2.setPrefSize(125, 25);
            vbox1.getChildren().addAll(lbl1, lbl2);
            vbox1.setSpacing(20);
            return vbox1;
        }

        private void setQuestionPropertyEvent() {
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
        }

        private VBox createExamData() {
            VBox vbox = new VBox();
            vbox.getChildren().addAll(new BoxHeaders("Toets Gegevens"), getExamDataBox());
            vbox.setSpacing(20);
            return vbox;
        }

        private Node getExamDataBox() {
            VBox examDataVbox = new VBox();
            examDataVbox.getChildren().addAll(
                    yearPropertyChoiceBox,
                    schoolYearPropertyChoiceBox,
                    blockPropertyChoiceBox,
                    coursePropertyChoiceBox,
                    attemptExamPropertyChoiceBox,
                    typePropertyChoiceBox,
                    datePropertyDatePicker
            );
            examDataVbox.setSpacing(12);
            return examDataVbox;
        }
    }

    private class ChoiceBoxes extends ChoiceBox<String> {
        public ChoiceBoxes(ArrayList<String> items) {
            this.setItems(FXCollections.observableArrayList(items));
            this.setPrefSize(150, 30);
            this.setValue(items.get(0));
        }
    }

    private class BoxHeaders extends Label {
        public BoxHeaders(String text) {
            super(text);
            this.setFont(new Font("Arial", 18));
            this.setAlignment(Pos.CENTER);
            this.setMaxWidth(Double.MAX_VALUE);
            this.setPrefWidth(150);
        }
    }

    private class ScreenButtons extends Button {
        public ScreenButtons(String text) {
            super(text);
            this.setMinWidth(150);

        }
    }

    private class QuestionBoxWithCheck extends HBox{
        String questionNumber;
        String subQuestionNumber;
        String subQuestionPoints;
        CheckBox accountable;

        public QuestionBoxWithCheck(String questionNumber, String subQuestionNumber, String subQuestionPoints) {
            this.questionNumber = questionNumber;
            this.subQuestionNumber = subQuestionNumber;
            this.subQuestionPoints = subQuestionPoints;
            this.accountable = new CheckBox();
            this.accountable.setSelected(true);
            getQuestionAndCheckBox();
        }

        public void getQuestionAndCheckBox() {
            Label question = new Label("Vraag "+questionNumber + '.' + subQuestionNumber + ": " + subQuestionPoints);
            Region spacer = new Region();
            this.getChildren().addAll(question, spacer, accountable);
            this.setHgrow(spacer, Priority.ALWAYS);
            this.setPrefHeight(20);
            this.setPrefWidth(100);
            this.setStyle("-fx-border-color: lightgrey");
        }

        public String getQuestionNumber() {
            return questionNumber;
        }

        public String getSubQuestionNumber() {
            return subQuestionNumber;
        }

        public String getSubQuestionPoints() {
            return subQuestionPoints;
        }
    }
}
