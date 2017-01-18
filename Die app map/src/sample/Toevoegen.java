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
 * 16-01-2017: Documenteren van methoden
 */
public class Toevoegen extends TabPane{
    /**
     * Deze klasse maakt het scherm aan voor het toevoegen van
     * toetsen.
     *
     * De selectie menu is voor het aankiezen van een bepaalde toets die
     * dan kan worden gewijzigd. Deze bevat verschillende eigenschappen
     * om de juistte toets te selecteren.
     *
     * De 2e alinea bevat twee van de knoppen die aanwezig zijn op het scherm.
     *
     * toets eigenschappen gaat over de eigenschappen die van de toets kunnen
     * worden aangepast. Deze staan in choiceboxes.
     */
    //SELECTION MENU
    public ChoiceBoxes yearExamChoiceBox; //Jaartal
    public ChoiceBoxes schoolYearExamChoiceBox; //Schooljaar
    public ChoiceBoxes blockExamChoiceBox; //Periode
    public ChoiceBoxes courseExamChoiceBox; //Modules
    public ChoiceBoxes typeExamChoiceBox; //Toetsvorm

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
        /**
         * Dit is de constructor voor het maken van de scherm.
         *
         * Deze methode zorgt voor de juistte instellingen van het scherm.
         * Tabs kunnen niet gesloten worden, de tabs hebben een bepaalde
         * grootte. De laatste regel laat zien hoe er een nieuwe tab
         * wordt aangemaakt voor het scherm.
         */
        createSelectionMenuElements();
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.setTabMinWidth(100);
        this.getTabs().add(new examTab("Toetsen"));
    }


    private void createSelectionMenuElements(){
        /**
         * Aan maken van de verschillende elementen die van belang zijn voor
         * het selectie menu. Onderandere verschillende choiceboxes die
         * aanwezig zijn voor het selecteren. Maar ook de knoppen die van
         * belang zijn voor het inladen of het aanmaken van een nieuwe toets
         */
        createSelectionMenuChoiceBoxes();
        createSelectionMenuButtons();
    }

    private void createSelectionMenuChoiceBoxes() {
        /**
         * Creeeren van choiceboxes voor de selectie menu.
         */
        yearExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Jaar", "placeholder")));
        schoolYearExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Leerjaar", "Jaar 1", "Jaar 2", "Jaar 3", "Jaar 4")));
        blockExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Periode", "Periode 1", "Periode 2", "Periode 3", "Periode 4", "Periode 5")));
        courseExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Module",  "placeholder")));
        typeExamChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Toetsvorm",  "Theorietoets", "Praktijktoets", "Logboek", "Aanwezigheid", "Project")));
    }

    private void createSelectionMenuButtons() {
        /**
         * Aanmaken van knoppen voor de selectie menu
         */
        showExamBtn = new ScreenButtons("Toets weergeven");
        saveExamBtn = new ScreenButtons("Toets opslaan");
    }

    private class examTab extends Tab {
        /**
         * Inner klasse voor het aanmaken van de toetstab
         *
         * Borderpane voor het instellen van de elementen op juistte
         * positie. Selectionmenu vbox zal de elementen bevatten die van
         * belang zijn voor het selectiemenu. Een knop Aanmaken van nieuwe
         * toetsen een knop voor het importeren van de puntgegevens en een
         * knop voor het resetten van de ingeladen csv. Hierna volgen 2
         * boxen die van belang zijn voor het juist weergeven van de punten
         * die zijn ingeladen.
         *
         */
        private BorderPane examPane = new BorderPane();

        private VBox selectionMenu;


        private ScreenButtons newExamBtn = new ScreenButtons("Nieuwe Toets");
        private ScreenButtons importCsvButton = new ScreenButtons("Importeer CSV");
        private ScreenButtons resetPointDistributionButton = new ScreenButtons("Reset");
        private VBox pointDistributionBox;
        private FlowPane questionAndCheckboxes;



        public examTab(String text) {
            /**
             * Maken van de parentTab waar alle elementen inkomen die van
             * belang zijn voor het toets scherm.
             *
             * Als eerst wordt de toetstab voorzien van een naam. Hierna
             * wordt de methode aangeroepen die er voor zorgt dat de
             * selectiemenu gevuld wordt. Ook worden door het aanroepen van
             * een methode de button events ingesteld. Links in de borderpane
             * wordt de selectiemenu gezet. Daarna volgen een paar layout
             * functies. Als laatst wordt de content van de tab ingesteld op de
             * borderpane.
             */
            super(text);
            fillSelectionMenu();
            setButtonEventsExamTab();
            examPane.setLeft(selectionMenu);
            examPane.setPadding(new Insets(5, 5, 5, 5));
            selectionMenu.setPrefHeight(1081);
            this.setContent(examPane);
        }

        private void fillSelectionMenu() {
            /**
             * De selectiemenu wordt hier gevuld met zijn elementen
             *
             * Er wordt een vbox geinitialiseerd hierin komen de verschillende
             * choiceboxes, headers en knoppen te staan. Om alles goed op zijn
             * plek te houden wordt er een Region element aangemaakt.
             *
             * De verschillende elementen worden hierna toegevoegd aan de VBOX
             * en de spacer wordt zo ingesteld dat ie de knoppen helemaal onder
             * aan het scherm zet.
             */
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
            /**
             * VBOX wordt hier aangemaakt die de verschillende choiceboxes
             * goed zet ook wordt er een header aan toegevoegd. Hierna
             * Volgt eene layout stap met spacing waarna de VBOX teruggestuurd
             * wordt.
             */
            VBox labelAndChoiceBoxesBox = new VBox();
            labelAndChoiceBoxesBox.getChildren().addAll(
                    new BoxHeaders("Keuzemenu"),
                    yearExamChoiceBox,
                    schoolYearExamChoiceBox,
                    blockExamChoiceBox,
                    courseExamChoiceBox,
                    typeExamChoiceBox
            );
            labelAndChoiceBoxesBox.setSpacing(20);
            return labelAndChoiceBoxesBox;
        }

        private void setButtonEventsExamTab() {
            /**
             * Genereren van events na klikken op knop.
             *
             * Na het drukken van newExamBtn wordt het scherm aangemaakt voor
             * het maken van de elementen die nodig zijn voor het invullen
             * van de eigenschappen voor de toets.
             *
             * ImportCsvButton actie zorgt er voor de csv op de jusitte manier
             * wordt ingelezen. De reset knop wordt hierna de mogelijkheid
             * gegeven om te activeren.
             *
             * resetPointDistributionButton actie zorgt voor het resetten
             * van de punten distributie. Hij wordt volledig uit het scherm
             * verwijderd zodat er nieuwe mogelijkheid wordt gegeven om een
             * csv opnieuw in te laden.
             */
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
            /**
             * Bestand inladen voor het importeren van de punten.
             *
             * Als het bestand goed is gekozen kan hij door middel van een
             * andere methode worden uitgelezen.
             */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Toets Bestand");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                findLinesWithQuestionPointDistribution(file);
            }
        }

        private void findLinesWithQuestionPointDistribution(File file) {
            /**
             * Zoeken naar de regels die de opgaven bevatten met de gegevens
             * die van belang zijn voor de puntenverdeling
             *
             * Met een scanner wordt het bestand geopend. Met een while loop
             * wordt het bestand door gegeaan totdat er geen regels over zijn.
             * In de variabel questions worden de regeld opgeslagen. Zodra deze
             * het woord Opgave bevat. Worden de deelvragen en de punten die
             * daarbij horen gepakt. Deze regels worden gesplit op de ; en naar
             * de methode extractQuestionsFromLines gestuurd.
             */
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
            /**
             * Extraheren van de juisste gegevens uit de variabelen questions,
             * subquestion en subquestionsPoints.
             *
             * Er wordt een variabel aangemaakt die de questionAndCheckboxes
             * zal bevatten dit is een klasse HBOX, die de vraag, behorende
             * punten en een checkbox bevat die aangeeft of de toets meetelt.
             *
             * In een loop wordt in de subQuestions variabel gezocht naar de
             * vraagnummer. Dit bevat de index waarna de juistte gegevens
             * gepakt kunnen worden. Omdat de kolom van de vraagnummer en de
             * daaropvolgende niet van belang zijn wordt er  +2 aan toegevoegd.
             * Zolang de vraag of subvraag een getal bevat staat de loop aan
             * in deze loop wordt de Hoofdvraag ingesteld in de variabel
             * currentquestion en worden de HBOXen aangemaakt per vraag en
             * deelvraag.
             *
             * Hierna volgen een paar layout aanpassingen.
             */
//            int indexVraagnummer;
            questionAndCheckboxes = new FlowPane();
            questionAndCheckboxes.setMaxHeight(140);
            for (int i = 0; i < subQuestions.length; i++) {
                if (subQuestions[i].contains("Vraagnummer:")) {
//                    indexVraagnummer = i;
                    int index = i+2; //Remove Vraagnummer and whitespaces
                    String currentQuestion = "1";
                    while (((questions[index].length() != 0 || subQuestions[index].length() != 0) && subQuestionsPoints[index].length() != 0)) {// || index-1 == indexVraagnummer) {
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
            /**
             * Verschillende eigenschappen elementen die van belang zijn voor
             * het juist toevoegen van een toets aan de database.
             */
            yearPropertyChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Jaar", "placeholder")));
            schoolYearPropertyChoiceBox =  new ChoiceBoxes(new ArrayList<>(Arrays.asList("Leerjaar", "Jaar 1", "Jaar 2", "Jaar 3", "Jaar 4")));
            blockPropertyChoiceBox = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Periode", "Periode 1", "Periode 2", "Periode 3", "Periode 4", "Periode 5")));
            coursePropertyChoiceBox  = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Module",  "placeholder")));
            attemptExamPropertyChoiceBox  = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Gelegenheid",  "1e kans", "2e kans", "3e kans")));
            typePropertyChoiceBox  = new ChoiceBoxes(new ArrayList<>(Arrays.asList("Toetsvorm",  "Theorietoets", "Praktijktoets", "Logboek", "Aanwezigheid", "Project")));
            datePropertyDatePicker = new DatePicker();
            datePropertyDatePicker.setPrefWidth(150);
        }

        private VBox createExamPropertiesScreen() {
            /**
             * Juistte layout voor het weergeven van de verschillende elementen
             * voor de toets. De onderdelen zijn examen eigenschappen met
             * choiceboxes, de punten verdeling en de buttonbox voor het
             * opslaan van de toetsgegevens.
             */
            VBox vbox = new VBox();
            VBox buttonBox = new VBox(saveExamBtn);
            vbox.getChildren().addAll(getExamInformationBoxes(), getPointDistribution(), buttonBox);
            buttonBox.setAlignment(Pos.CENTER);
            vbox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            vbox.setPadding(new Insets(0, 20, 0, 20));

            return vbox;
        }

        private VBox getPointDistribution() {
            /**
             * Aanmaken van de VBOX die de puntenverdeling zal laten zien.
             */
            pointDistributionBox = new VBox();
            pointDistributionBox.getChildren().addAll(new BoxHeaders("Puntenverdeling/Meerekenen:"), getImportQuestionButtons());
            return pointDistributionBox;
        }


        private HBox getImportQuestionButtons() {
            /**
             * Aanmaken van de HBOX die de knoppen bevat voor het inladen
             * van de csv en resetten van de punten om een nieuwe csv in te
             * laden. Layout wordt hier ook toegevoegd aan de knoppen zodat ze
             * rechts in de hoek staan.
             */
            HBox questionButtonBox = new HBox();
            questionButtonBox.getChildren().addAll(importCsvButton, resetPointDistributionButton);
            resetPointDistributionButton.setDisable(true);
            questionButtonBox.setAlignment(Pos.TOP_RIGHT);
            return questionButtonBox;
        }

        public HBox getExamInformationBoxes() {
            /**
             * Aanmaken van de box die eigenschappen bevat over de toets.
             *
             * Deze kunnen worden ingevoerd met choiceboxes, een datepicker
             * en een textfield die van belang is voor de beheersgraad.
             */
            HBox hbox = new HBox();
            hbox.getChildren().addAll(createExamData(), getExamGrader());
            hbox.setHgrow(hbox.getChildren().get(0), Priority.ALWAYS);
            hbox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);
            hbox.setPadding(new Insets(0, 0, 0, 5));

            return hbox;        }

        private Node getExamGrader() {
            /**
             * Een VBOX met informatie over de cijfer gegevens.
             */
            VBox vbox = new VBox();
            vbox.getChildren().addAll(new BoxHeaders("Cijfer Gegevens"), getGradeData());
            vbox.setSpacing(20);
            return vbox;
        }

        private HBox getGradeData() {
            /**
             * HBOX die informatie bevat over de beheersgraad en of er
             * wel vragen aanwezig zijn bij de toets.
             */
            HBox hbox = new HBox();
            VBox vbox1 = getGradePropertyLabels();
            VBox vbox2 = getGradePropertyInputFields();
            hbox.getChildren().addAll(vbox1, vbox2);
            return hbox;
        }

        private VBox getGradePropertyInputFields() {
            /**
             * VBOX met elementen die informatie bevattene over de toets.
             * Er wordt een textfield toegevoegd die informatie bevat over de
             * beheersgraad. Deze mag alleen cijfers bevatten(eerste if) en
             * niet langer zijn dan 2 cijfers (tweede if). De
             * questionPropertyCheckBox bevat een checkbox die de
             * puntenverdeling wel of niet verdeelt.
             */
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
                    if (thresholdTextfield.getText().length() > 4) {
                        String s = thresholdTextfield.getText().substring(0, 2);
                        thresholdTextfield.setText(s);
                    }
                }
            });
            vbox2.setSpacing(20);
            return vbox2;
        }
//////PUNTEN DOOR GOKKANS TOEVOEGEN
        private VBox getGradePropertyLabels() {
            /**
             * VBOX met labels over de 2 eigenschappen beheersgraad en
             * vragen aanwezig.
             */
            VBox vbox1 = new VBox();
            Label lbl1 = new Label("Censuur:");
            Label lbl2 = new Label("Vragen aanwezig:");
            lbl1.setPrefSize(125, 25);
            lbl2.setPrefSize(125, 25);
            vbox1.getChildren().addAll(lbl1, lbl2);
            vbox1.setSpacing(20);
            return vbox1;
        }

        private void setQuestionPropertyEvent() {
            /**
             * Funtionaliteit aan checkbox gegeven.
             *
             * Als deze aangevinkt staat wordt de puntenverdeling scherm
             * weergeven. Anders staat deze verstopt.
             */
            questionPropertyCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        pointDistributionBox.setVisible(true);
                    } else {
                        pointDistributionBox.setVisible(false);
                        resetPointDistributionButton.setDisable(true);
                        importCsvButton.setDisable(false);
                    }
                }
            });
        }

        private VBox createExamData() {
            /**
             * Box met toetsgegevens.
             */
            VBox vbox = new VBox();
            vbox.getChildren().addAll(new BoxHeaders("Toets Gegevens"), getExamDataBox());
            vbox.setSpacing(20);
            return vbox;
        }

        private VBox getExamDataBox() {
            /**
             * VBox met verschilllende eigenschappen over de toets.
             *
             * Bevat de choiceboxes en datepicker voor de juistte invoer
             * van toetsen.
             */
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
        /**
         * Inner klasse voor het aanmaken van choiceboxes met de juistte
         * layout
         * @param items: Mogelijkheden die zich in de choicebox bevinden
         */
        public ChoiceBoxes(ArrayList<String> items) {
            this.setItems(FXCollections.observableArrayList(items));
            this.setPrefSize(150, 30);
            this.setValue(items.get(0));
        }
    }

    private class BoxHeaders extends Label {
        /**
         * Inner klasse voor het aanmaken van labels met de juistte
         * layout
         * @param text: Label text
         */
        public BoxHeaders(String text) {
            super(text);
            this.setFont(new Font("Arial", 18));
            this.setAlignment(Pos.CENTER);
            this.setMaxWidth(Double.MAX_VALUE);
            this.setPrefWidth(150);
        }
    }

    private class ScreenButtons extends Button {
        /**
         * Aanmaken van knoppen met de juistte layout
         * @param text: Button text
         */
        public ScreenButtons(String text) {
            super(text);
            this.setMinWidth(150);

        }
    }

    private class QuestionBoxWithCheck extends HBox{
        /**
         * Inner klasse met gegevens over de punten.
         *
         * Vraagnummer, Deelvraag nummer, Punten en of de vraag meetelt
         */
        String questionNumber;
        String subQuestionNumber;
        String subQuestionPoints;
        CheckBox accountable;

        public QuestionBoxWithCheck(String questionNumber, String subQuestionNumber, String subQuestionPoints) {
            /**
             * Constructor voor het juistaanmaken van een HBOX die van belang
             * is voor het juist weergeven van de punten.
             */
            this.questionNumber = questionNumber;
            this.subQuestionNumber = subQuestionNumber;
            this.subQuestionPoints = subQuestionPoints;
            this.accountable = new CheckBox();
            this.accountable.setSelected(true);
            getQuestionAndCheckBox();
        }

        public void getQuestionAndCheckBox() {
            /**
             * Aanmaken van de juistte indeling van de box
             *
             * Label met gegevens en een checkbox die informatie geeft
             * over het meerekenen van de vraag
             */
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
        public Boolean getAccountability() {
            return this.accountable.isSelected();
        }
    }
}
