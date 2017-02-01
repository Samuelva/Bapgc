package sample;

import database.DatabaseConn;
import database.ModuleReader;
import database.Reader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
 * 15-12-2016: Vragen aanwezig, functionaliteit toegevoegd
 * 17-12-2016: Importeren van punten toegevoegd
 * 19-12-2016: Module tab verwijderd, modulen toevoegen tab toegevoegd.
 * 20-12-2016: Module variabelen verwijderd
 * 13-01-2017: Toevoegscherm opnieuw geschreven
 * 15-01-2017: Verder met ontwikkelen
 * 16-01-2017: Documenteren van methoden
 * 17-01-2017: Toets data aanpassingen
 * 30-01-2016: Documenteren & pep toepassen
 */
public class Toevoegen extends TabPane{
    /**
     * Deze klasse maakt het scherm aan voor het toevoegen van
     * toetsen.
     *
     * Het selectiemenu is voor het kiezen van een bepaalde toets, die
     * dan kan worden gewijzigd. Deze bevat verschillende eigenschappen
     * om de juiste toets te selecteren.
     *
     * De tweede alinea bevat twee van de knoppen die aanwezig zijn 
     * op het scherm.
     *
     * Toets eigenschappen gaat over de eigenschappen die van de toets kunnen
     * worden aangepast. Deze staan in choiceboxes.
     */
    //SELECTION MENU
    public Keuzemenu choiceMenu;

    public ScreenButtons saveExamBtn = new ScreenButtons("Update cessuur/gokkans");

    //EXAM PROPERTIES
    public CheckBox questionPropertyCheckBox;
    public TextField thresholdTextfield;
    public TextField chanceByGamblingTextfield;

    public ExamTab examTab;

    public ModuleTab moduleTab;
    private Button emptyButton;

    private Button importCSV;
    private TableView pointsTable;

    public Integer examID;


    public Toevoegen() {
        /**
         * Dit is de constructor voor het maken van het scherm.
         *
         * Deze methode zorgt voor de juiste instellingen van het scherm.
         * Tabs kunnen niet gesloten worden, de tabs hebben een bepaalde
         * grootte. De laatste regel laat zien hoe er een nieuwe tab
         * wordt aangemaakt voor het scherm.
         */
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        this.setTabMinWidth(100);
        examTab = new ExamTab("Toetsen");
        moduleTab = new ModuleTab("Modulen");
        this.getTabs().add(moduleTab);
        this.getTabs().add(examTab);


    }


    public String[][] getQuestionInfo() {
        /**
         * Functie die het terughalen van de ingevoerde vragen naar
         * in een array zet zodat het in kan worden geladen in de
         * database
         *
         * In de eerste regel wordt er een 2d array aangemaakt met
         * de grootte van het aantal vragen dat er aanwezig is.
         * De lijst bevat alle elementen die zich in de questionAndCheckboxes
         * flowpane bevinden. Hier wordt overheen geloopt en wordt het
         * element omgevast naar een HBox. De elementen die zich in deze
         * HBOx bevinden worden weer naar hun oorspronkelijke element gecast
         * waarna de informatie eruit geextraheerd kan worden en het dan
         * kan worden toegevoed aan de 2d array.
         */
        String[][] questionInformation =
                new String[
                        examTab.questionAndCheckboxes.getChildren().size()][];
        ObservableList<Node> childsVB =
                examTab.questionAndCheckboxes.getChildren();
        for (int i = 0; i < childsVB.size() ; i++) {
            HBox hb = (HBox)childsVB.get(i);
            ObservableList<Node> childsHB = hb.getChildren();
            Label question = (Label)childsHB.get(0);
            CheckBox accountable = (CheckBox)childsHB.get(2);
            String[] info = new String[]{
                    question.getText().split(" ")[1].replace(":", ""),
                    question.getText().split(" ")[2],
                    String.valueOf(accountable.isSelected())};
            questionInformation[i] = info;
        }
        return  questionInformation;
    }

    public void resetWarning() {
        /**
         * Aanmaken van een een pop-up waarschuwing die zal aangeven wat
         * er zal gebeuren zodra de toets punten worden gereset.
         *
         * Er wordt een nieuwe alert aangemaakt met type confirmation
         * Verschillende strings worden toegevoegd om het scherm duidelijk te
         * maken. Er worden twee knoppen aan toegevoegd Ja en Nee.
         * Als er op de knop wordt gedrukt wordt de waarde van de gedrukte
         * knop opgeslagen in result. Als dit gelijk is aan de Ja knop
         * dan wordt de database vragen verwijderd die gelijk is aan de
         * toets id. Om opnieuw punten in te laden worden de knoppen
         * weer beschikbaar gezet.
         */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Waarschuwing!");
        alert.setHeaderText("Met deze actie worden alle vragen verwijderd! " +
                "Ook uit de database als ze er al in stonden.");
        alert.setContentText("Gaat u akkoord?");
        ButtonType buttonTypeOne = new ButtonType("Ja");
        ButtonType buttonTypeCancel = new ButtonType("Nee",
                ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            examTab.questionAndCheckboxes.getChildren().clear();
            examTab.pointDistributionBox.getChildren().remove(
                    examTab.questionAndCheckBoxesScrollpane);
            DatabaseConn databaseConn = new DatabaseConn();
            databaseConn.DeleteVragenToets(examID);
            databaseConn.CloseConnection();
            examTab.importCsvButton.setDisable(false);
            examTab.resetPointDistributionButton.setDisable(true);
        }
    }





    public class ExamTab extends Tab {
        /**
         * Inner klasse voor het aanmaken van de toetstab
         *
         * Borderpane voor het instellen van de elementen op de juiste
         * positie. Selectionmenu vbox zal de elementen bevatten die van
         * belang zijn voor het selectiemenu: een knop aanmaken van nieuwe
         * toetsen, een knop voor het importeren van de puntgegevens en een
         * knop voor het resetten van de ingeladen csv. Hierna volgen twee
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
             * belang zijn voor het toetsscherm.
             *
             * Als eerst wordt de toetstab voorzien van een naam. Hierna
             * wordt de methode aangeroepen die ervoor zorgt dat het
             * selectiemenu gevuld wordt. Ook worden door het aanroepen van
             * een methode de button events ingesteld. Links in de borderpane
             * wordt het selectiemenu gezet. Daarna volgen een paar layout
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
             * Het selectiemenu wordt hier gevuld met zijn elementen.
             *
             * Er wordt een vbox geïnitialiseerd. Hierin komen de verschillende
             * choiceboxes, headers en knoppen in te staan. Om alles goed op zijn
             * plek te houden wordt er een Region element aangemaakt.
             *
             * De verschillende elementen worden hierna toegevoegd aan de vbox
             * en de spacer wordt zo ingesteld dat deze de knoppen helemaal onder
             * aan het scherm zet.
             */
            selectionMenu = getChoiceBoxesSelectionMenu();
        }

        private VBox getChoiceBoxesSelectionMenu() {
            /**
             * Vbox wordt hier aangemaakt die de verschillende choiceboxes
             * goed zet. Ook wordt er een header aan toegevoegd. Hierna
             * volgt een layout stap met spacing, waarna de vbox teruggestuurd
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
             * ImportCsvButton actie zorgt er voor de csv op de juiste manier
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
             * andere methoden worden uitgelezen.
             */
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Toets Bestand");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                findLinesWithQuestionPointDistribution(file);
//                new Reader(file.toString(), examID);

            }
        }

        private void findLinesWithQuestionPointDistribution(File file) {
            /**
             * Zoeken naar de regels die de opgaven bevatten met de gegevens
             * die van belang zijn voor de puntenverdeling.
             *
             * Met een scanner wordt het bestand geopend. Met een while loop
             * wordt het bestand doorgegaan totdat er geen regels over zijn.
             * In de variabel questions worden de regels opgeslagen. Zodra deze
             * het woord Opgave bevat, worden de deelvragen en de punten die
             * daarbij horen gepakt. Deze regels worden gesplit op de ; en naar
             * de methode extractQuestionsFromLines gestuurd.
             */
            try {
                Scanner scanner = new Scanner(file);
                String subQuestions = scanner.nextLine();
                String accountAbility = scanner.nextLine();
                String subQuestionsPoints = scanner.nextLine();
                extractQuestionsFromLines(subQuestions.split(";"),
                        accountAbility.split(";"),
                        subQuestionsPoints.split(";"));

                new Reader(file.toString(), examID);
            } catch (FileNotFoundException e) {
            }
        }


        private void setQuestionAndCheckboxesFlowpaneSettings() {
            /**
             * Nieuwe flowpane aangemaakt met de juistte instellingen
             */
            questionAndCheckboxes = new FlowPane();
            questionAndCheckboxes.setMaxHeight(250);
            questionAndCheckboxes.setOrientation(Orientation.VERTICAL);
            questionAndCheckboxes.setVgap(4);
            questionAndCheckboxes.setHgap(10);
        }

        private void extractQuestionsFromLines(String[] subQuestions,String[] accountAbility,
                                               String[] subQuestionsPoints) {
            /**
             * Extraheren van de juiste gegevens uit de variabelen questions,
             * subquestion en subquestionsPoints.
             *
             * Er wordt een variabel aangemaakt die de questionAndCheckboxes
             * zal bevatten. Dit is een klasse hbox, die de vraag, behorende
             * punten en een checkbox bevat die aangeeft of de toets meetelt.
             *
             * In een loop wordt in de subQuestions variabel gezocht naar het
             * vraagnummer. Dit bevat de index, waarna de juiste gegevens
             * gepakt kunnen worden. Omdat de kolom van de vraagnummer en de
             * daaropvolgende niet van belang zijn wordt er +2 aan toegevoegd.
             * Zolang de vraag of subvraag een getal bevat staat de loop aan.
             * In deze loop wordt de hoofdvraag ingesteld in de variabel
             * currentquestion en worden de hboxen aangemaakt per vraag en
             * deelvraag.
             *
             * Hierna volgen een paar layout aanpassingen.
             */
            setQuestionAndCheckboxesFlowpaneSettings();
            questionAndCheckBoxesScrollpane = new ScrollPane();
            for (int i = 2; i < subQuestions.length; i++) {
                questionAndCheckboxes.getChildren().add(
                        new QuestionBoxWithCheck(subQuestions[i],
                                subQuestionsPoints[i],
                                accountAbility[i]));
            }
            questionAndCheckBoxesScrollpane.setContent(questionAndCheckboxes);
            pointDistributionBox.getChildren().add(2,questionAndCheckBoxesScrollpane);
        }


        public void setExamPropertiesScreen(String[] examProperties) {
            VBox vbox = new VBox();
            vbox.getChildren().addAll(getExamInformationBoxes(examProperties,
                    examID), getPointDistribution(examProperties));
            vbox.setVgrow(vbox.getChildren().get(1), Priority.ALWAYS);
            vbox.setPadding(new Insets(0, 20, 0, 20));
            saveExamBtn.setAlignment(Pos.CENTER);
            examPane.setCenter(vbox);
        }


        private VBox getPointDistribution(String[] examProperties) {
            /**
             * Aanmaken van de vbox die de puntenverdeling zal laten zien.
             */
            setQuestionAndCheckboxesFlowpaneSettings();
            questionAndCheckBoxesScrollpane = new ScrollPane();
            pointDistributionBox = new VBox();
            pointDistributionBox.getChildren().addAll(new BoxHeaders("Puntenverdeling/Meerekenen:"), getImportQuestionButtons());
            try {
                DatabaseConn databaseConn = new DatabaseConn();
                String[][] questionInfo  = databaseConn.GetTable("vraag", "toetsid = " +
                        databaseConn.GetToetsID(examProperties[0],examProperties[1], examProperties[2],
                                examProperties[3], examProperties[4], examProperties[5]));
                if (questionInfo.length == 0) {
                    throw new EmptyStackException();
                }
                for (String[] info: questionInfo) {
                    questionAndCheckboxes.getChildren().add(new QuestionBoxWithCheck(info[1], info[2],info[4]));
                }
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
             * Aanmaken van de hbox die de knoppen bevat voor het inladen
             * van de csv en resetten van de punten om een nieuwe csv in te
             * laden. Layout wordt hier ook toegevoegd aan de knoppen, zodat ze
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
             * Een vbox met informatie over de cijfer gegevens.
             */
            Region leftSpring    = new Region();
            Region rightSpring = new Region();
            VBox vbox = new VBox();
            HBox hbox = new HBox();
            hbox.getChildren().addAll(leftSpring, saveExamBtn, rightSpring);
            hbox.setHgrow(leftSpring, Priority.ALWAYS);
            hbox.setHgrow(rightSpring, Priority.ALWAYS);
            hbox.setMaxWidth(vbox.getMaxWidth());
            vbox.getChildren().addAll(new BoxHeaders("Cijfer Gegevens"), getGradeData(), hbox);
            vbox.setSpacing(20);
            return vbox;
        }

        private HBox getGradeData() {
            /**
             * Hbox die informatie bevat over de beheersgraad en of er
             * vragen aanwezig zijn bij de toets.
             */
            HBox hbox = new HBox();
            VBox vbox1 = getGradePropertyLabels();
            VBox vbox2 = getGradePropertyInputFields();
            hbox.getChildren().addAll(vbox1, vbox2);
            return hbox;
        }

        private VBox getGradePropertyInputFields() {
            /**
             * Vbox met elementen die informatie bevatten over de toets.
             * Er wordt een textfield toegevoegd die informatie bevat over de
             * beheersgraad. Deze mag alleen cijfers bevatten (eerste if) en
             * niet langer zijn dan twee cijfers (tweede if). Hetzelfde is gedaan
             * voor de gokkans textfield.
             * De questionPropertyCheckBox bevat een checkbox die de
             * puntenverdeling wel of niet verdeeld.
             */
            thresholdTextfield = new TextField();
            chanceByGamblingTextfield = new TextField();
            questionPropertyCheckBox = new CheckBox();
            DatabaseConn databaseConn = new DatabaseConn();
            try {
                String[][] gradeInfo = databaseConn.GetTable("toets",
                        "toetsID = " + String.valueOf(examID));
                thresholdTextfield.setText(gradeInfo[0][7]);
                chanceByGamblingTextfield.setText(gradeInfo[0][8]);
            } catch (Exception E) {

            }
            VBox vbox2 = new VBox();
            questionPropertyCheckBox.setSelected(true);
            showQuestionPropertiesCheckBoxEvent();
            vbox2.getChildren().addAll(questionPropertyCheckBox,
                    thresholdTextfield, chanceByGamblingTextfield);
            setSettingsThreshholdtextfield();
            setSettingsChanceByGamblingTextfield();
            vbox2.setSpacing(20);
            databaseConn.CloseConnection();
            return vbox2;
        }

        private void setSettingsChanceByGamblingTextfield() {
            /**
             * Textfield input instellingen.
             *
             * Getallen mogen niet langer zijn dan 4 cijfers. Letters invoeren
             * is niet mogelijk
             */
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
        }

        private void setSettingsThreshholdtextfield() {
            /**
             * Textfield input instellingen.
             *
             * Getallen mogen niet langer zijn dan 4 cijfers. Letters invoeren
             * is niet mogelijk
             */
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
        }

        private VBox getGradePropertyLabels() {
            /**
             * Vbox met labels over de twee eigenschappen beheersgraad en
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
             * Als deze aangevinkt staat wordt het puntenverdelingscherm
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
             * Bevat de choiceboxes en datepicker voor de juiste invoer
             * van de toetsen.
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
         * Inner klasse voor het aanmaken van labels met de juiste
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
         * Aanmaken van knoppen met de juiste layout
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
         * Vraagnummer, deelvraag nummer, punten en of de vraag meetelt.
         */
        String question;
        String subQuestionPoints;
        CheckBox accountable;

        public QuestionBoxWithCheck(String questionNumber, String subQuestionPoints, String accoubtAble) {
            /**
             * Constructor voor het juist aanmaken van een hbox, die van belang
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
             * Aanmaken van de juiste indeling van de box
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
            createModuleTabButtons();
            HBox hbox = new HBox();
            hbox.getChildren().addAll(fillRegion(), emptyButton, fillRegion(), importCSV, fillRegion());
            hbox.setSpacing(20);
            VBox.setMargin(hbox, new Insets(5));
            pointsTable = new TableView();
            VBox.setVgrow(pointsTable, Priority.ALWAYS);
            VBox.setMargin(pointsTable, new Insets(5));
            VBox vbox = new VBox();
            vbox.getChildren().addAll(pointsTable, hbox);
            this.setContent(vbox);
            events();
        }

        private void events() {
            fillTable();
            importModuleCSV();
            setEmptyDatabaseEvent();
        }

        private void setEmptyDatabaseEvent() {
            /**
             * Deze methode zorgt ervoor dat als er op "Database leeg maken" gedrukt
             * wordt, dat er een waarschuwing getoond wordt. Als er op OK gedrukt
             * wordt in die waarchuwing wordt de database leeg gemaakt.
             */
            emptyButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Waarschuwing");
                    alert.setHeaderText("Weet u zeker dat u alle data uit de database wilt verwijderen?");
                    alert.setContentText("Druk op OK als u het zeker weet, ander drukt u op Cancel.\n" +
                            "Dit kan niet ongedaan gemaakt worden!");
                    ButtonType OK = new ButtonType("OK");
                    ButtonType Cancel = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(OK, Cancel);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == OK) {
                        emptyDatabase();
                    } else {
                        alert.close();
                    }
                }
            });
        }

        private void emptyDatabase() {
            /**
             * Deze methode maakt een connctie met de database, maakt hem
             * leeg en update de tabel op het scherm.
             */
            DatabaseConn d = new DatabaseConn();
            d.ResetTables();
            d.CloseConnection();
            fillTable();
        }

        private Region fillRegion(){
            /**
             * Deze methode maakt een Region aan die zich uitstrekt over de
             * breedte van het scherm.
             */
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            return region;
        }

        private void createModuleTabButtons() {
            /**
             * Aanmaken van knoppen die van belang zijn voor het module scherm
             */
            emptyButton = new Button("Database leeg maken");
            importCSV = new Button("Import CSV");

            emptyButton.setPrefSize(150, 30);
            importCSV.setPrefSize(150, 30);
        }

        private void importModuleCSV() {
            /**
             * Importeren van module
             *
             * mist nog meer documentatie
             */
            importCSV.setOnAction( e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Toets Bestand");
                File file = fileChooser.showOpenDialog(new Stage());
                if (file != null) {
                    System.out.println(file);
                    Object moduleReader = new ModuleReader(file.toString());
                    fillTable();
                }
            });
        }

        protected void fillTable(){
            /**
             * Deze methode vult de tabel in de module tab van het 
             * toevoegscherm met data. Eerst wordt de tabel leeggemaakt en de 
             * kolommen wordem verwijderd. Daarna worden de kolommen opnieuw 
             * aangemaakt. Er wordt een connectie gemaakt met de database en de 
             * informatie van de toetsen wordt opgehaald. Vervolgens wordt
             * er voor iedere toets een instantie gemaakt van de DataForTable 
             * class. De connectie met de database wordt gesloten en de 
             * DataForTable instaties worden aan de tabel toegevoegd.
             */
            pointsTable.getItems().clear();
            pointsTable.getColumns().clear();
            makeColumn("Module Code", "code");
            makeColumn("Jaar", "year");
            makeColumn("Periode", "period");
            makeColumn("Leer Jaar", "studyYear");
            makeColumn("Toetsvorm", "type");
            DatabaseConn d = new DatabaseConn();
            String[][] list = d.GetToetsData();
            DataForTable[] rows = new DataForTable[list.length];
            for (int i = 0; i < list.length; ++i){
                List<String> types = d.getTypes(list[i][1], list[i][3], list[i][2], list[i][0]);
                rows[i] = new DataForTable(list[i][0], list[i][1], list[i][2], list[i][3], types);
            }
            d.CloseConnection();
            pointsTable.getItems().addAll(rows);
        }

        protected void makeColumn(String label, String value){
            /**
             * Deze methode maakt een nieuwe kolom aan voor pointsTable.
             * Eerst wordt er een kolom gemaakt met de naam die under label 
             * gedefinieerd is, de cellValueFactory wordt vervolgens zo 
             * gedifinieerd dat de waarde die getoond wordt automatich uit een 
             * klasse gehaald kan worden. De waarde in de klasse die
             * opgehaald zal worden is degene onder de variabele naam die under 
             * value staat. De kolom wordt op niet aanpasbaar gezet en 
             * toegevoegt aan de tabel.
             */
            TableColumn column = new TableColumn(label);
            column.setCellValueFactory(new PropertyValueFactory(value));
            column.setEditable(false);
            pointsTable.getColumns().add(column);
        }
    }
}