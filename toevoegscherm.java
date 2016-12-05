package sample;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.converter.NumberStringConverter;

import java.util.ArrayList;

/**
 * Created by Diego Staphorst on 5-12-2016.
 */
public class toevoegscherm extends BorderPane{

    public Button showExamBtn;
    private Button newExamBtn;
    public Button saveExamBtn;
    public Button showModuleBtn;
    private Button newModuleBtn;
    public Button saveModuleBtn;
    private Button moduleScreenBtn;
    private Button examScreenBtn;

    private BorderPane examPane;
    private BorderPane modulePane;

    public ChoiceBox choiceBox1; //Jaartal
    public ChoiceBox choiceBox2; //Schooljaar
    public ChoiceBox choiceBox3; //Periode
    public ChoiceBox choiceBox4; //Modules
    public ChoiceBox choiceBox5; //Toetsvorm
    public ChoiceBox choiceBox6; //Gelegenheid

    public ChoiceBox choiceBox7; //Module
    public ChoiceBox choiceBox8; //Toetsvorm
    public ChoiceBox choiceBox9; //Gelegenheid
    public ChoiceBox choiceBox10; //Leerjaar
    public ChoiceBox choiceBox11; //Periode
    public DatePicker datePicker; //Datum

    public TextField textField1; //cesuur
    public TextField textField2; //beheeersgraad

    public ListView examOptionsList;
    public ListView moduleOptionsList;

    public ArrayList<TextField> scoreDistributionArray; //puntenverdeling

    public toevoegscherm() {
        initButtons();
        initCenterPane();
        createTopPane();
        events();
    }


    private void events() {
        examScreenBtn.setOnAction(e -> {
            this.setCenter(examPane);
            examScreenBtn.setDisable(true);
            moduleScreenBtn.setDisable(false);
        });
        moduleScreenBtn.setOnAction(e -> {
            this.setCenter(modulePane);
            moduleScreenBtn.setDisable(true);
            examScreenBtn.setDisable(false);
        });
        newModuleBtn.setOnAction(e -> {
            modulePane.setCenter(createModulePropertiesScreen());
        });
        newExamBtn.setOnAction(e -> {
            examPane.setCenter(createExamPropertiesScreen());
        });

    }

    private Label createLabel(String header) {
        Label label = new Label(header);
        label.setFont(new Font("Arial", 20));
        label.setAlignment(Pos.CENTER);
        setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private void initButtons() {
        showExamBtn = new Button("Toets weergeven");
        newExamBtn = new Button("Nieuwe Toets");
        saveExamBtn = new Button("Toets opslaan");

        showModuleBtn = new Button("Module weergeven");
        newModuleBtn = new Button("Nieuwe Module");
        saveModuleBtn = new Button("Module opslaan");

        examScreenBtn = new Button("Toetsen");
        moduleScreenBtn = new Button("Modulen");

        examScreenBtn.setMaxWidth(Double.MAX_VALUE);
        moduleScreenBtn.setMaxWidth(Double.MAX_VALUE);
    }



    private void initCenterPane() {
        initExamPane();
        initModulePane();
    }

    private void initModulePane() {
        modulePane = new BorderPane();
        createModuleSelectionScreen();
    }

    private void createModuleSelectionScreen() {
        VBox vboxSelectionMenu = new VBox();

        VBox choiceBoxesInSelectionMenu = createChoiceBoxes();
        choiceBoxesInSelectionMenu.getChildren().remove(choiceBox5);
        choiceBoxesInSelectionMenu.getChildren().remove(choiceBox6);

        vboxSelectionMenu.getChildren().addAll(createLabel("Keuzemenu"),choiceBoxesInSelectionMenu, createModuleSelectionList(), addModuleButtons());

        modulePane.setLeft(vboxSelectionMenu);

    }

    private VBox addModuleButtons() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(showModuleBtn, newModuleBtn);

        showModuleBtn.setMaxWidth(Double.MAX_VALUE);
        newModuleBtn.setMaxWidth(Double.MAX_VALUE);
        return vbox;
    }

    private ListView createModuleSelectionList() {
        moduleOptionsList = new ListView();
        moduleOptionsList.setPrefWidth(150);
        return moduleOptionsList;
    }

    private VBox createChoiceBoxes() {
        VBox vbox = new VBox();

        choiceBox1 = new ChoiceBox(); //Jaartal
        choiceBox2 = new ChoiceBox(); //Schooljaar
        choiceBox3 = new ChoiceBox(); //Periode
        choiceBox4 = new ChoiceBox(); //Modules
        choiceBox5 = new ChoiceBox(); //Toetsvorm
        choiceBox6 = new ChoiceBox(); //Gelegenheid

        choiceBox1.getItems().addAll("Jaartal","1", "2", "3", "4");
        choiceBox2.getItems().addAll("Schooljaar","1", "2", "3", "4");
        choiceBox3.getItems().addAll("Periode","1", "2", "3", "4");
        choiceBox4.getItems().addAll("Modules","?", "?", "?", "Via Database?");
        choiceBox5.getItems().addAll("Toetsvorm","praktijk", "theorie", "aanwezigheid", "logboek");
        choiceBox6.getItems().addAll("Gelegenheid","1", "2", "1 + 2");

        choiceBox1.setPrefWidth(150);
        choiceBox2.setPrefWidth(150);
        choiceBox3.setPrefWidth(150);
        choiceBox4.setPrefWidth(150);
        choiceBox5.setPrefWidth(150);
        choiceBox6.setPrefWidth(150);

        choiceBox1.setValue("Jaartal");
        choiceBox2.setValue("Schooljaar");
        choiceBox3.setValue("Periode");
        choiceBox4.setValue("Modules");
        choiceBox5.setValue("Toetsvorm");
        choiceBox6.setValue("Gelegenheid");

        vbox.getChildren().addAll(choiceBox1, choiceBox2, choiceBox3, choiceBox4, choiceBox5, choiceBox6);

        return vbox;
    }

    private VBox createModulePropertiesScreen() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(getModuleInformation(), getSaveModuleButton());
        return vbox;
    }

    private HBox getModuleInformation() {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(createModuleDataBox(), getExamsInModule());
        hbox.setHgrow(hbox.getChildren().get(0), Priority.ALWAYS);
        hbox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);
        return hbox;
    }

    private HBox getSaveModuleButton() {
        HBox hbox = new HBox();
        hbox.getChildren().add(saveModuleBtn);
        saveExamBtn.setPrefWidth(250);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        return hbox;
    }

    private Node getExamsInModule() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createLabel("Toetsen in deze module"));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        vbox.setPrefHeight(400);
        return vbox;
    }

    private VBox createModuleDataBox() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createLabel("Module Gegevens:"));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        vbox.setPrefHeight(400);
        return vbox;
    }


    private void initExamPane() {
        examPane = new BorderPane();
        createExamSelectionMenu();
    }

    private VBox createExamPropertiesScreen() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(getExamInformation(), getPointDistribution(), getSaveExamButton());
        vbox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
        return vbox;
    }

    private HBox getSaveExamButton() {
        HBox hbox = new HBox();
        hbox.getChildren().add(saveExamBtn);
        saveExamBtn.setPrefWidth(250);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        return hbox;
    }



    private ListView createExamSelection() {
        examOptionsList = new ListView();
        examOptionsList.setPrefWidth(150);
        return examOptionsList;
    }

    private VBox getPointDistribution() {
        VBox vbox = new VBox();
        vbox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        return vbox;
    }

    private void createExamSelectionMenu() {
        VBox vboxSelectionMenu = new VBox();
        vboxSelectionMenu.getChildren().addAll(createLabel("Keuzemenu"),createChoiceBoxes(),createExamSelection(), addExamButtons());
        examPane.setLeft(vboxSelectionMenu);
    }

    private VBox addExamButtons() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(showExamBtn, newExamBtn);

        showExamBtn.setMaxWidth(Double.MAX_VALUE);
        newExamBtn.setMaxWidth(Double.MAX_VALUE);
        return vbox;
    }

    private HBox getExamInformation() {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(createExamData(), getExamGrader());

        hbox.setHgrow(hbox.getChildren().get(0), Priority.ALWAYS);
        hbox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);
        return hbox;

    }

    private VBox getExamGrader() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createLabel("Cijfer Gegevens: "), getGradeData());
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    private HBox getGradeData()  {
        HBox hbox = new HBox();

        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();

        Label lbl1 = new Label("Cesuur");
        Label lbl2 = new Label("Beheersgraad");

        textField1 = new TextField();
        textField2 = new TextField();

        textField1.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        textField2.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

        vbox1.getChildren().addAll(lbl1, lbl2);
        vbox2.getChildren().addAll(textField1, textField2);
        hbox.getChildren().addAll(vbox1, vbox2);

        hbox.setAlignment(Pos.CENTER);


        return hbox;
    }

    private VBox createExamData() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createLabel("Toets Gegevens: "), getExamData());
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private VBox getExamData() {
        VBox vbox = new VBox();

        choiceBox7 = new ChoiceBox(); //module
        choiceBox8 = new ChoiceBox(); //Toetsvorm
        choiceBox9 = new ChoiceBox(); //Gelegenheid
        choiceBox10 = new ChoiceBox(); //Leerjaar
        choiceBox11 = new ChoiceBox(); //Periode
        datePicker = new DatePicker(); //Datum

        choiceBox7.getItems().addAll("Module","1", "2", "3", "4");
        choiceBox8.getItems().addAll("Toetsvorm","1", "2", "3", "4");
        choiceBox9.getItems().addAll("Gelegenheid","1", "2", "3", "4");
        choiceBox10.getItems().addAll("Leerjaar","?", "?", "?", "Via Database?");
        choiceBox11.getItems().addAll("Periode","praktijk", "theorie", "aanwezigheid", "logboek");

        choiceBox7.setPrefWidth(150);
        choiceBox8.setPrefWidth(150);
        choiceBox9.setPrefWidth(150);
        choiceBox10.setPrefWidth(150);
        choiceBox11.setPrefWidth(150);
        datePicker.setPrefWidth(150);

        choiceBox7.setValue("Module");
        choiceBox8.setValue("Toetsvorm");
        choiceBox9.setValue("Gelegenheid");
        choiceBox10.setValue("Leerjaar");
        choiceBox11.setValue("Periode");

        vbox.getChildren().addAll(choiceBox7, choiceBox8, choiceBox9, choiceBox10, choiceBox11, datePicker);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }



    private void createTopPane() {
        HBox hbox = new HBox();

        hbox.getChildren().addAll(examScreenBtn, moduleScreenBtn);

        hbox.setHgrow(examScreenBtn, Priority.ALWAYS);
        hbox.setHgrow(moduleScreenBtn, Priority.ALWAYS);

        this.setTop(hbox);
    }


}


