package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.converter.NumberStringConverter;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Diego Staphorst on 5-12-2016.
 * Diego: 07-12-2016,  Toevgoegscherm is af
 * Diego: 09-12-2016, Documenteren van het script
 */
public class Toevoegscherm extends BorderPane{

    public Button showExamBtn;
    private Button newExamBtn;
    public Button saveExamBtn;
    public Button showModuleBtn;
    private Button newModuleBtn;
    public Button saveModuleBtn;
    private Button moduleScreenBtn;
    private Button examScreenBtn;
    private Button pointDistributionButton;
    private Button importCsvButton;
    private Button resetPointDistributionButton;

    private BorderPane examPane;
    private BorderPane modulePane;

    public ChoiceBox yearChoiceBox; //Jaartal
    public ChoiceBox schoolYearChoiceBox; //Schooljaar
    public ChoiceBox blockChoiceBox; //Periode
    public ChoiceBox courseChoiceBox; //Modules
    public ChoiceBox typeChoiceBox; //Toetsvorm
    public ChoiceBox attemptChoiceBox; //Gelegenheid

    public ChoiceBox choiceBox7; //Module
    public ChoiceBox choiceBox8; //Toetsvorm
    public ChoiceBox choiceBox9; //Gelegenheid
    public ChoiceBox choiceBox10; //Leerjaar
    public ChoiceBox choiceBox11; //Periode
    public DatePicker datePicker; //Datum

    public Boolean logPresent = true;
    public Boolean theoryPresent = true;
    public Boolean practicePresent = false;
    public Boolean presencePresent = false;


    public TextField textField1; //cesuur
    public TextField textField2; //beheeersgraad
    public TextField textField3; //Module
    public TextField textField4; //Omschrijving

    public ListView examOptionsList;
    public ListView moduleOptionsList;

    public LinkedList<TextField> scoreDistributionArray; //puntenverdeling
    private CheckBox questionCheckBox; //Aanwezige vragen
    private Slider slider; //Aantal vragen
    private FlowPane pointBox;

    public Toevoegscherm() {
        /**
         * Verschillende methoden aanroepen die nodig zijn voor het juist initialiseren van elementen, panes en events.
         */
        initButtons();
        initCenterPane();
        createTopPane();
        events();
    }


    private void events() {
        /**
         * Funtionaliteiten binden aan de knoppen, de acties die heermee worden gestart zijn:
         *      -Ontbruikbaar maken van elementen
         *      -Functies aanroepen voor het toevoegen van elementen
         *      -Functies aanroepen die gegevens inladen
         *      -Waarden uit elementen verkrijgen die van belang zijn voor de database
         */
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
        pointDistributionButton.setOnAction(e -> {
            addPointFields();
            pointDistributionButton.setDisable(true);
            resetPointDistributionButton.setDisable(false);
        });
        importCsvButton.setOnAction(e -> {
            System.out.println("Hey");
        });
        resetPointDistributionButton.setOnAction(e -> {
            pointBox.getChildren().clear();
            resetPointDistributionButton.setDisable(true);
            pointDistributionButton.setDisable(false);
        });

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

        for (int i = 1; i<=slider.getValue(); i++) {
            Region fill = new Region();
            HBox questionInput = new HBox();
            Label lbl = new Label(Integer.toString(i)+ "->");
            lbl.setMinWidth(20);
            TextField textfield = new TextField();
            textfield.setPrefWidth(25);
            scoreDistributionArray.add(i - 1, textfield);
            questionInput.getChildren().addAll(lbl, scoreDistributionArray.get(i - 1), fill);
            pointBox.getChildren().add(questionInput);
        }
        pointBox.setVgap(20);
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

    private void initButtons() {
        /**
         * Initialiseren en styling geven aan de verschillende knoppen die aanwezig zijn op het scherm
         */
        showExamBtn = new Button("Toets weergeven");
        newExamBtn = new Button("Nieuwe Toets");
        saveExamBtn = new Button("Toets opslaan");

        showModuleBtn = new Button("Module weergeven");
        newModuleBtn = new Button("Nieuwe Module");
        saveModuleBtn = new Button("Module opslaan");

        examScreenBtn = new Button("Toetsen");
        moduleScreenBtn = new Button("Modulen");

        pointDistributionButton = new Button("Weergeef vragen");
        importCsvButton = new Button("Importeer van csv");
        resetPointDistributionButton = new Button("Reset aantal vragen");

        resetPointDistributionButton.setDisable(true);

        examScreenBtn.setMaxWidth(Double.MAX_VALUE);
        moduleScreenBtn.setMaxWidth(Double.MAX_VALUE);
    }

    private void initCenterPane() {
        /**
         * Verschillende panes initialiseren die van belang zijn voor de centerpane van de borderpane.
         */
        initExamPane();
        initModulePane();
    }

    private void initModulePane() {
        /**
         * Initialiseren van het scherm die dient voor het toevoegen van de module. Dit scherm maakt het
         * scherm aan die zorgt voor het creeren van de module selectie scherm.
         */
        modulePane = new BorderPane();
        createModuleSelectionScreen();
    }

    private void createModuleSelectionScreen() {
        /**
         * Aanmaken van het selectiemenu in het module scherm. Deze bevat minder elementen dus worden er een
         * aantal uit verwijderd (ten opzichte van het toetsscherm). Voor de vbox zijn nog meerdere elementen van
         * belang deze worden ook toegoevd aan de VBOX. Als laatst wordt de VBOX in de leftPane van de borderpane
         * geplaatst.
         */
        VBox vboxSelectionMenu = new VBox();

        VBox choiceBoxesInSelectionMenu = createChoiceBoxes();
        choiceBoxesInSelectionMenu.getChildren().remove(typeChoiceBox);
        choiceBoxesInSelectionMenu.getChildren().remove(attemptChoiceBox);

        vboxSelectionMenu.getChildren().addAll(createLabel("Keuzemenu"),choiceBoxesInSelectionMenu, createModuleSelectionList(), addModuleButtons());
        modulePane.setLeft(vboxSelectionMenu);

    }

    private VBox addModuleButtons() {
        /**
         * Knoppen toevoegden die van belang zijn voor het weergeven van de module of creeren van een
         * nieuwe module. Styling wordt hier meegegeven aan de knoppen
         */
        VBox vbox = new VBox();
        vbox.getChildren().addAll(showModuleBtn, newModuleBtn);

        showModuleBtn.setMaxWidth(Double.MAX_VALUE);
        newModuleBtn.setMaxWidth(Double.MAX_VALUE);
        return vbox;
    }

    private ListView createModuleSelectionList() {
        /**
         * Maken van een selectie lijst voor het kiezen van de module die er moet worden weergeven.
         */
        moduleOptionsList = new ListView();
        moduleOptionsList.setPrefWidth(150);
        return moduleOptionsList;
    }

    private VBox createChoiceBoxes() {
        /**
         * Aanmaken van de verschillende dropdown menu's waarin de gebruiker de juistte module of toets kan kiezen.
         */
        VBox vbox = new VBox();

        yearChoiceBox = new ChoiceBox(); //Jaartal
        schoolYearChoiceBox = new ChoiceBox(); //Schooljaar
        blockChoiceBox = new ChoiceBox(); //Periode
        courseChoiceBox = new ChoiceBox(); //Modules
        typeChoiceBox = new ChoiceBox(); //Toetsvorm
        attemptChoiceBox = new ChoiceBox(); //Gelegenheid

        yearChoiceBox.getItems().addAll("Jaar", new Separator(), "placeholder");
        schoolYearChoiceBox.getItems().addAll("Leerjaar", new Separator(), "Jaar 1", "Jaar 2", "Jaar 3",
                "Jaar 4");
        blockChoiceBox.getItems().addAll("Periode", new Separator(), "Periode 1", "Periode 2", "Periode 3",
                "Periode 4", "Periode 5");
        courseChoiceBox.getItems().addAll("Module", new Separator(), "placeholder");
        typeChoiceBox.getItems().addAll("Toetsvorm", new Separator(), "Theorietoets", "Praktijktoets",
                "Logboek", "Aanwezigheid", "Project");
        attemptChoiceBox.getItems().addAll("Gelegenheid", new Separator(), "1e kans", "2e kans");

        yearChoiceBox.setPrefSize(150, 30);
        schoolYearChoiceBox.setPrefSize(150, 30);
        blockChoiceBox.setPrefSize(150, 30);
        courseChoiceBox.setPrefSize(150, 30);
        typeChoiceBox.setPrefSize(150, 30);
        attemptChoiceBox.setPrefSize(150, 30);


        yearChoiceBox.setValue("Jaar");
        schoolYearChoiceBox.setValue("Leerjaar");
        blockChoiceBox.setValue("Periode");
        courseChoiceBox.setValue("Module");
        typeChoiceBox.setValue("Toetsvorm");
        attemptChoiceBox.setValue("Gelegenheid");

        vbox.getChildren().addAll(yearChoiceBox, schoolYearChoiceBox, blockChoiceBox, courseChoiceBox, typeChoiceBox, attemptChoiceBox);
        vbox.setSpacing(20);

        return vbox;
    }

    private VBox createModulePropertiesScreen() {
        /**
         * Scherm waar de eigenschappen van de modulen op staan, waaronder de module informatie en een knop
         * om gemaakte aanpassingen aan de info op te slaan.
         */
        VBox vbox = new VBox();
        vbox.getChildren().addAll(getModuleInformation(), getSaveModuleButton());
        vbox.setVgrow(vbox.getChildren().get(0), Priority.ALWAYS);
        vbox.setPadding(new Insets(0, 20, 0, 20));
        return vbox;
    }

    private HBox getModuleInformation() {
        /**
         * Module informatie en de mogelijkheid om te zien welke toetsen er aanwezig zijn in de module.
         */
        HBox hbox = new HBox();
        hbox.getChildren().addAll(createModuleDataBox(), getExamsInModule());
        hbox.setHgrow(hbox.getChildren().get(0), Priority.ALWAYS);
        hbox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);
        return hbox;
    }

    private HBox getSaveModuleButton() {
        /**
         * Box die de knop voor het opslaan bevat en waarmee sstyling aan de knop wordt gegeven
         */
        HBox hbox = new HBox();
        hbox.getChildren().add(saveModuleBtn);
        saveExamBtn.setPrefWidth(250);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        return hbox;
    }

    private Node getExamsInModule() {
        /**
         * Weergeven welke toetsen er aanwezig zijn in de module.
         */
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createLabel("Toetsen in deze module:"), getModuleExams());
        vbox.setAlignment(Pos.CENTER);

        vbox.setPrefHeight(200);
        return vbox;
    }

    private HBox getModuleExams() {
        /**
         * Labels die informatie weergeven of er bepaalde toetsen aan wezig zijn in de module. In de methode
         * checkIfPresent wordt eer waarde teruggegven die laat zien of de toets aanwezig is.
         */
        HBox hbox1 = new HBox();

        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();

        Label lbl1 = new Label("Aanwezigheid");
        Label lbl2 = new Label("Praktijk");
        Label lbl3 = new Label("Theorie");
        Label lbl4 = new Label("Logboek");

        vbox1.getChildren().addAll(lbl1, lbl2, lbl3, lbl4);
        vbox2.getChildren().addAll(checkIfPresent(presencePresent), checkIfPresent(practicePresent), checkIfPresent(theoryPresent), checkIfPresent(logPresent));
        hbox1.getChildren().addAll(vbox1, vbox2);
        hbox1.setAlignment(Pos.CENTER);

        return hbox1;

    }

    private Label checkIfPresent(Boolean present) {
        /**
         * Label teruggegven die weergeeft of een toets aanwezig is door de label groen te maken anders is ie grijs.
         */
        Label lbl = new Label("✔");
        if (present) {
            lbl.setTextFill(Color.GREEN);
        } else {
            lbl.setTextFill(Color.GREY);
        }
        return lbl;
    }

    private VBox createModuleDataBox() {
        /**
         * Box die de module gegevens/eigenschappen bevat.
         */
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createLabel("Module Gegevens:"), getModuleData());
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefHeight(200);
        return vbox;
    }

    private HBox getModuleData() {
        /**
         * Box met Labels en textfields die de waarden bevatten die behoren tot de module en omscrhijving.
         */
        HBox hbox = new HBox();

        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();

        Label lbl1 = new Label("Module");
        Label lbl2 = new Label("Omschrijving");

        lbl1.setPrefSize(100, 25);
        lbl2.setPrefSize(100, 25);

        textField3 = new TextField();
        textField4 = new TextField();


        vbox1.getChildren().addAll(lbl1, lbl2);
        vbox2.getChildren().addAll(textField3, textField4);
        hbox.getChildren().addAll(vbox1, vbox2);

        hbox.setAlignment(Pos.CENTER);


        return hbox;
    }


    private void initExamPane() {
        /**
         * initialiseren van het scherm voor het toevoegen van de toetsen en aanroepen van de methode voor het
         * maken van de examen selectie menu
         */
        examPane = new BorderPane();
        createExamSelectionMenu();
    }

    private VBox createExamPropertiesScreen() {
        /**
         * aanmaken van het toetsscherm. Deze bevat de examen informatie, de puntenverderling en de knop voor het
         * opslaan van de toets.
         */
        VBox vbox = new VBox();
        vbox.getChildren().addAll(getExamInformation(), getPointDistribution(), getSaveExamButton());
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
        saveExamBtn.setPrefWidth(250);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        return hbox;
    }



    private ListView createExamSelection() {
        /**
         * Aanmaken van de selectie scherm voor de toetsen.
         */
        examOptionsList = new ListView();
        examOptionsList.setPrefWidth(150);
        return examOptionsList;
    }

    private VBox getPointDistribution() {
        /**
         * Maken van de punten verdeling gedeelte op het scherm.
         */
        VBox vbox = new VBox();
        pointBox = new FlowPane();
        vbox.getChildren().addAll(createLabel("Puntenverdeling:"), getAmountOfQuestions(), pointBox);
        vbox.setPadding(new Insets(20, 0, 0, 0));

        return vbox;
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
        slider.setMax(50);
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
        hbox.getChildren().addAll(lbl1, slider, lbl2,new Region(), new HBox(resetPointDistributionButton, pointDistributionButton, importCsvButton));
        hbox.setHgrow(hbox.getChildren().get(3), Priority.ALWAYS);
        return hbox;
    }


    private void createExamSelectionMenu() {
        /**
         * Selectie menu voor het kiezen van de juistte toets.
         */
        VBox vboxSelectionMenu = new VBox();
        vboxSelectionMenu.getChildren().addAll(createLabel("Keuzemenu"),createChoiceBoxes(),createExamSelection(), addExamButtons());
        examPane.setLeft(vboxSelectionMenu);
    }

    private VBox addExamButtons() {
        /**
         * Toevoegen van knoppen voor het weergeven van de geselecteerde toets en de knop voor het aanmmaken van een
         * nieuwe examen.
         */
        VBox vbox = new VBox();
        vbox.getChildren().addAll(showExamBtn, newExamBtn);

        showExamBtn.setMaxWidth(Double.MAX_VALUE);
        newExamBtn.setMaxWidth(Double.MAX_VALUE);
        return vbox;
    }

    private HBox getExamInformation() {
        /**
         * Aanmaken van de box die informatie bevat over de toets en cesuur die daarbij hoort.
         */
        HBox hbox = new HBox();
        hbox.getChildren().addAll(createExamData(), getExamGrader());

        hbox.setHgrow(hbox.getChildren().get(0), Priority.ALWAYS);
        hbox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);
        return hbox;

    }

    private VBox getExamGrader() {
        /**
         * Gegevens over het bepalen van een cijfer voor de toets.
         */
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createLabel("Cijfer Gegevens: "), getGradeData());
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    private HBox getGradeData()  {
        /**
         * Gegevens over het bepalen van een cijfer voor de toets.
         * DE FORMATTER WERKT NIET OVERAL!! Waarschijnlijk mist er een library!
         */
        HBox hbox = new HBox();

        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();

        Label lbl1 = new Label("Cesuur");
        Label lbl2 = new Label("Beheersgraad");

        lbl1.setPrefSize(100, 25);
        lbl2.setPrefSize(100, 25);

        textField1 = new TextField();
        textField2 = new TextField();

//        textField1.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
//        textField2.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

        vbox1.getChildren().addAll(lbl1, lbl2);
        vbox2.getChildren().addAll(textField1, textField2);
        hbox.getChildren().addAll(vbox1, vbox2);

        hbox.setAlignment(Pos.CENTER);


        return hbox;
    }

    private VBox createExamData() {
        /**
         * Box met die data bevat over de toets
         */
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createLabel("Toets Gegevens: "), getExamData());
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private VBox getExamData() {
        /**
         * Dropdown menu's van de toets gegevens. Bevat de mogelijkheden die kunnnen worden ingevoerd voor het
         * aanpassen van toetsgegevens en wijzigen van toetsgegevens, de styling aan de menu's wordt ook gedaan hier,
         */
        VBox vbox = new VBox();

        choiceBox7 = new ChoiceBox(); //module
        choiceBox8 = new ChoiceBox(); //Toetsvorm
        choiceBox9 = new ChoiceBox(); //Gelegenheid
        choiceBox10 = new ChoiceBox(); //Leerjaar
        choiceBox11 = new ChoiceBox(); //Periode
        datePicker = new DatePicker(); //Datum

        choiceBox7.getItems().addAll("Module", new Separator(), "placeholder");
        choiceBox8.getItems().addAll("Toetsvorm", new Separator(), "Theorietoets", "Praktijktoets",
                "Logboek", "Aanwezigheid", "Project");
        choiceBox9.getItems().addAll("Gelegenheid", new Separator(), "1e kans", "2e kans");
        choiceBox10.getItems().addAll("Jaar", new Separator(), "placeholder");
        choiceBox11.getItems().addAll("Periode", new Separator(), "Periode 1", "Periode 2", "Periode 3",
                "Periode 4", "Periode 5");

        choiceBox7.setPrefWidth(150);
        choiceBox8.setPrefWidth(150);
        choiceBox9.setPrefWidth(150);
        choiceBox10.setPrefWidth(150);
        choiceBox11.setPrefWidth(150);
        datePicker.setPrefWidth(150);

        choiceBox7.setValue("Module");
        choiceBox8.setValue("Toetsvorm");
        choiceBox9.setValue("Gelegenheid");
        choiceBox10.setValue("Jaar");
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

        examScreenBtn.setPrefWidth(500);
        moduleScreenBtn.setPrefWidth(500);

        this.setTop(hbox);
    }


}


