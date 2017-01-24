package sample;

import database.DatabaseConn;
import database.ModuleReader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import java.util.*;


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
 * 17-01-2017: Toets Data aanpassingen
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
    public Keuzemenu choiceMenu;

    public ScreenButtons showExamBtn;
    public ScreenButtons saveExamBtn;

    //EXAM PROPERTIES
    public CheckBox questionPropertyCheckBox;
    public TextField thresholdTextfield;
    public TextField chanceByGamblingTextfield;

    public ExamTab examTab;

    public ModuleTab moduleTab;
    private  Button emptyButton;
    private  Button saveButton;

    private  Button importCSV;
    private TableView pointsTable;

    public Integer examID;


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
        examTab = new ExamTab("Toetsen");
        moduleTab = new ModuleTab("Modulen");
        this.getTabs().add(moduleTab);
        this.getTabs().add(examTab);


    }

    private void warning() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Waarschuwing!");
        alert.setHeaderText("Niet alles is ingevoerd!");
        alert.setContentText("Voer de niet gevoerde keuzes in het "
                + "keuzemenu in om verder te gaan.");
        alert.showAndWait();
    }

    public String[][] getQuestionInfo() {
        String[][] questionInformation = new String[examTab.questionAndCheckboxes.getChildren().size()][];
        ObservableList<Node> childsVB = examTab.questionAndCheckboxes.getChildren();

        for (int i = 0; i < childsVB.size() ; i++) {
            HBox hb = (HBox)childsVB.get(i);

            ObservableList<Node> childsHB = hb.getChildren();

            Label question = (Label)childsHB.get(0);
            CheckBox accountable = (CheckBox)childsHB.get(2);

            System.out.println(question.getText().split(" ")[1].replace(":", ""));
            System.out.println(question.getText().split(" ")[2]);
            System.out.println(accountable.isSelected());

            String[] info = new String[]{question.getText().split(" ")[1].replace(":", ""), question.getText().split(" ")[2],String.valueOf(accountable.isSelected())};
            questionInformation[i] = info;
        }
        for (String[] info: questionInformation) {
            System.out.println(Arrays.asList(info));
        }
        return  questionInformation;
    }

    public void resetWarning() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Waarschuwing!");
        alert.setHeaderText("Met deze actie worden alle vragen verwijderd! Ook uit de database als ze er al in stonden.");
        alert.setContentText("Gaat u akkoord?");

        ButtonType buttonTypeOne = new ButtonType("Ja");
        ButtonType buttonTypeCancel = new ButtonType("Nee", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            examTab.questionAndCheckboxes.getChildren().clear();
            examTab.pointDistributionBox.getChildren().remove(examTab.questionAndCheckBoxesScrollpane);
            DatabaseConn databaseConn = new DatabaseConn();
            databaseConn.DeleteVragenToets(examID);
            databaseConn.CloseConnection();
            examTab.importCsvButton.setDisable(false);
            examTab.resetPointDistributionButton.setDisable(true);

        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }


    private void createSelectionMenuElements(){
        /**
         * Aan maken van de verschillende elementen die van belang zijn voor
         * het selectie menu. Onderandere verschillende choiceboxes die
         * aanwezig zijn voor het selecteren. Maar ook de knoppen die van
         * belang zijn voor het inladen of het aanmaken van een nieuwe toets
         */
        createSelectionMenuButtons();
    }




    private void createSelectionMenuButtons() {
        /**
         * Aanmaken van knoppen voor de selectie menu
         */
        showExamBtn = new ScreenButtons("Toets weergeven");
        saveExamBtn = new ScreenButtons("Toets opslaan");
    }

    public void setSelection(String[] selection) {
    }


    public class ExamTab extends Tab {
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
        public BorderPane examPane = new BorderPane();

        private VBox selectionMenu;

        public ScreenButtons importCsvButton = new ScreenButtons("Importeer CSV");
        public ScreenButtons resetPointDistributionButton = new ScreenButtons("Reset");
        private VBox pointDistributionBox;
        private ScrollPane questionAndCheckBoxesScrollpane;
        public FlowPane questionAndCheckboxes;

        private Label lbl2;
        private Label lbl3;



        public ExamTab(String text) {
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
            choiceMenu = new Keuzemenu();
            VBox labelAndChoiceBoxesBox = new VBox();
            labelAndChoiceBoxesBox.getChildren().addAll(
                    new BoxHeaders("Keuzemenu"),
                    choiceMenu.getChoiceMenuBox()
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
            importCsvButton.setOnAction( e -> {
                importQuestionsFromCSV();
                importCsvButton.setDisable(true);
                resetPointDistributionButton.setDisable(false);
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
            questionAndCheckBoxesScrollpane = new ScrollPane();
            questionAndCheckboxes = new FlowPane();
            for (int i = 0; i < subQuestions.length; i++) {
                if (subQuestions[i].contains("Vraagnummer:")) {
                    int index = i+2; //Remove Vraagnummer and whitespaces
                    String currentQuestion = "1";
                    while (((questions[index].length() != 0 || subQuestions[index].length() != 0) && subQuestionsPoints[index].length() != 0)) {// || index-1 == indexVraagnummer) {
                        if (questions[index].length() != 0) {
                            currentQuestion = questions[index];
                        }
                        questionAndCheckboxes.getChildren().add(new QuestionBoxWithCheck(String.valueOf(currentQuestion) + "." + subQuestions[index], subQuestionsPoints[index], "true"));
                        index++;
                    }
                }
            }
            questionAndCheckboxes.setMaxHeight(250);
            questionAndCheckboxes.setOrientation(Orientation.VERTICAL);
            questionAndCheckboxes.setVgap(4);
            questionAndCheckboxes.setHgap(10);
            questionAndCheckBoxesScrollpane.setContent(questionAndCheckboxes);
            pointDistributionBox.getChildren().add(2,questionAndCheckBoxesScrollpane);
        }


        public void setExamPropertiesScreen(String[] examProperties) {
            System.out.println(examProperties[0]);
            VBox vbox = new VBox();
            VBox buttonBox = new VBox(saveExamBtn);
            vbox.getChildren().addAll(getExamInformationBoxes(examProperties, examID), getPointDistribution(examProperties), buttonBox);
            buttonBox.setAlignment(Pos.CENTER);
            vbox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            vbox.setPadding(new Insets(0, 20, 0, 20));
            saveExamBtn.setAlignment(Pos.CENTER);
            examPane.setCenter(vbox);
        }


        private VBox getPointDistribution(String[] examProperties) {
            /**
             * Aanmaken van de VBOX die de puntenverdeling zal laten zien.
             */
            questionAndCheckBoxesScrollpane = new ScrollPane();
            pointDistributionBox = new VBox();
            pointDistributionBox.getChildren().addAll(new BoxHeaders("Puntenverdeling/Meerekenen:"), getImportQuestionButtons());
            try {
                DatabaseConn databaseConn = new DatabaseConn();
                String[][] questionInfo  = databaseConn.GetTable("vraag", "toetsid = " + databaseConn.GetToetsID(examProperties[0],examProperties[1], examProperties[2], examProperties[3], examProperties[4], examProperties[5]));
                if (questionInfo.length == 0) {
                    throw new EmptyStackException();
                }
                questionAndCheckboxes = new FlowPane();
                for (String[] info: questionInfo) {
                    questionAndCheckboxes.getChildren().add(new QuestionBoxWithCheck(info[1], info[2],info[4]));
                }
                questionAndCheckboxes.setOrientation(Orientation.VERTICAL);
                questionAndCheckboxes.setMaxHeight(250);
                questionAndCheckboxes.setVgap(4);
                questionAndCheckboxes.setHgap(10);
                databaseConn.CloseConnection();
                importCsvButton.setDisable(true);
                resetPointDistributionButton.setDisable(false);
                questionAndCheckBoxesScrollpane.setContent(questionAndCheckboxes);
                pointDistributionBox.getChildren().add(2,questionAndCheckBoxesScrollpane);

            } catch (EmptyStackException e) {
                importCsvButton.setDisable(false);
                resetPointDistributionButton.setDisable(true);
            }
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

        public HBox getExamInformationBoxes(String[] examProperties, Integer examID) {
            /**
             * Aanmaken van de box die eigenschappen bevat over de toets.
             *
             * Deze kunnen worden ingevoerd met choiceboxes, een datepicker
             * en een textfield die van belang is voor de beheersgraad.
             */
            HBox hbox = new HBox();
            hbox.getChildren().addAll(createExamData(examProperties), getExamGrader());
            hbox.setHgrow(hbox.getChildren().get(0), Priority.ALWAYS);
            hbox.setHgrow(hbox.getChildren().get(1), Priority.ALWAYS);
            hbox.setPadding(new Insets(0, 0, 0, 5));

            return hbox;
        }

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
             * niet langer zijn dan 2 cijfers (tweede if). Het zelfde is gedaan
             * voor de gokkans textfield.
             * De questionPropertyCheckBox bevat een checkbox die de
             * puntenverdeling wel of niet verdeelt.
             */
            thresholdTextfield = new TextField();
            chanceByGamblingTextfield = new TextField();
            questionPropertyCheckBox = new CheckBox();
            DatabaseConn databaseConn = new DatabaseConn();
            try {
                String[][] gradeInfo = databaseConn.GetTable("toets", "toetsID = " + String.valueOf(examID));
                thresholdTextfield.setText(gradeInfo[0][7]);
                chanceByGamblingTextfield.setText(gradeInfo[0][8]);
            } catch (Exception E) {

            }
            VBox vbox2 = new VBox();
            questionPropertyCheckBox.setSelected(true);
            showQuestionPropertiesCheckBoxEvent();
            vbox2.getChildren().addAll(questionPropertyCheckBox, thresholdTextfield, chanceByGamblingTextfield);
            thresholdTextfield.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{1,3}")) {
                        thresholdTextfield.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (thresholdTextfield.getText().length() > 4) {
                        String s = thresholdTextfield.getText().substring(0, 4);
                        thresholdTextfield.setText(s);
                    }
                }
            });
            chanceByGamblingTextfield.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{1,3}")) {
                        chanceByGamblingTextfield.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (chanceByGamblingTextfield.getText().length() > 4) {
                        String s = chanceByGamblingTextfield.getText().substring(0, 4);
                        chanceByGamblingTextfield.setText(s);
                    }
                }
            });
            vbox2.setSpacing(20);
            databaseConn.CloseConnection();
            return vbox2;
        }

        private VBox getGradePropertyLabels() {
            /**
             * VBOX met labels over de 2 eigenschappen beheersgraad en
             * vragen aanwezig.
             */
            VBox vbox1 = new VBox();
            Label lbl1 = new Label("Vragen aanwezig:");
            lbl2 = new Label("Cessuur:");
            lbl3 = new Label("Punten door gokkans:");
            lbl1.setPrefSize(160, 25);
            lbl2.setPrefSize(160, 25);
            lbl3.setPrefSize(160, 25);
            vbox1.getChildren().addAll(lbl1, lbl2, lbl3);
            vbox1.setSpacing(20);
            return vbox1;
        }

        private void showQuestionPropertiesCheckBoxEvent() {
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
                        thresholdTextfield.setVisible(true);
                        chanceByGamblingTextfield.setVisible(true);
                        lbl2.setVisible(true);
                        lbl3.setVisible(true);
                    } else {
                        pointDistributionBox.setVisible(false);
                        resetPointDistributionButton.setDisable(true);
                        importCsvButton.setDisable(false);
                        thresholdTextfield.setVisible(false);
                        chanceByGamblingTextfield.setVisible(false);
                        lbl2.setVisible(false);
                        lbl3.setVisible(false);
                    }
                }
            });
        }

        private VBox createExamData(String[] examProperties) {
            /**
             * Box met toetsgegevens.
             */
            VBox vbox = new VBox();
            vbox.getChildren().addAll(new BoxHeaders("Toets Gegevens"), getExamDataBox(examProperties));
            vbox.setSpacing(20);
            return vbox;
        }

        private VBox getExamDataBox(String[] examProperties) {
            /**
             * VBox met verschilllende eigenschappen over de toets.
             *
             * Bevat de choiceboxes en datepicker voor de juistte invoer
             * van toetsen.
             */
            VBox examDataVbox = new VBox();
            examDataVbox.getChildren().addAll(
                    new labelPropertyWithValue("Module", examProperties[0]),
                    new labelPropertyWithValue("Jaar", examProperties[1]),
                    new labelPropertyWithValue("Schooljaar", examProperties[2]),
                    new labelPropertyWithValue("Periode", examProperties[3]),
                    new labelPropertyWithValue("Gelegenheid", examProperties[4]),
                    new labelPropertyWithValue("Toetsvorm", examProperties[5])
            );
            examDataVbox.setSpacing(12);
            return examDataVbox;
        }

        private class labelPropertyWithValue extends Label {

            public labelPropertyWithValue(String property, String value) {
                this.setText(property + ": " + value);
                this.setFont(new Font("Arial", 14));
                this.setAlignment(Pos.CENTER);
                this.setMaxWidth(Double.MAX_VALUE);
            }
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

    public class ScreenButtons extends Button {
        /**
         * Aanmaken van knoppen met de juistte layout
         * @param text: Button text
         */
        public ScreenButtons(String text) {
            super(text);
            this.setMinWidth(150);

        }

        public String[] getSelectionProperties() {
            return new String[0];
        }
    }

    public class QuestionBoxWithCheck extends HBox{
        /**
         * Inner klasse met gegevens over de punten.
         *
         * Vraagnummer, Deelvraag nummer, Punten en of de vraag meetelt
         */
        String question;
        String subQuestionPoints;
        CheckBox accountable;

        public QuestionBoxWithCheck(String questionNumber, String subQuestionPoints, String accoubtAble) {
            /**
             * Constructor voor het juistaanmaken van een HBOX die van belang
             * is voor het juist weergeven van de punten.
             */
            this.question = questionNumber;
            this.subQuestionPoints = subQuestionPoints;
            this.accountable = new CheckBox();
            if (accoubtAble.equals("true")) {
                this.accountable.setSelected(true);
            }
            getQuestionAndCheckBox();
        }

        public void getQuestionAndCheckBox() {
            /**
             * Aanmaken van de juistte indeling van de box
             *
             * Label met gegevens en een checkbox die informatie geeft
             * over het meerekenen van de vraag
             */
            Label question = new Label("Vraag " + this.question+ ": " + subQuestionPoints);
            Region spacer = new Region();
            this.getChildren().addAll(question, spacer, accountable);
            this.setHgrow(spacer, Priority.ALWAYS);
            this.setPrefHeight(20);
            this.setPrefWidth(125);
            this.setStyle("-fx-border-color: lightgrey");
        }

        public String getQuestion() {
            return question;
        }

        public String getSubQuestionPoints() {
            return subQuestionPoints;
        }

        public Boolean getAccountability() {
            return this.accountable.isSelected();
        }
    }

    private class ModuleTab extends Tab{
        public ModuleTab(String text) {
            super(text);
            Region leftFill = new Region();
            HBox.setHgrow(leftFill, Priority.ALWAYS);

            emptyButton = new Button("Leeg maken");
            saveButton = new Button("Wijzigingen opslaan");
            importCSV = new Button("Import CSV");

            emptyButton.setPrefSize(150, 30);
            saveButton.setPrefSize(150, 30);
            importCSV.setPrefSize(150, 30);
            Region rightFill = new Region();
            HBox.setHgrow(rightFill, Priority.ALWAYS);

            HBox hbox = new HBox();
            hbox.getChildren().addAll(rightFill, emptyButton, saveButton, importCSV, leftFill);
            hbox.setSpacing(20);

            pointsTable = new TableView();
            VBox.setVgrow(pointsTable, Priority.ALWAYS);

            VBox vbox = new VBox();

            vbox.setSpacing(20);
            vbox.getChildren().addAll(pointsTable, hbox);


            this.setContent(vbox);

            importCSV.setOnAction( e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Toets Bestand");
                File file = fileChooser.showOpenDialog(new Stage());
                if (file != null) {
                    System.out.println(file);
                    Object moduleReader = new ModuleReader(file.toString());
                }
            });
        }
    }
}
