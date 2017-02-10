package sample;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * Created by Samuel on 24-1-2017.
 * Deze klasse maakt het keuzemenu aan voor het toevoeg, aanpas en
 * inzienscherm. De klasse zorgt ervoor dat de juiste waardes in de
 * comboboxen worden getoond bij de verschillende selecties.
 */
public class ChoiceMenu {
    protected ComboBox yearChoiceBox;
    protected ComboBox schoolYearChoiceBox;
    protected ComboBox blockChoiceBox;
    protected ComboBox courseChoiceBox;
    protected ComboBox typeChoiceBox;
    protected ComboBox attemptChoiceBox;
    public Button examLoadButton;
    protected ChoiceMenuDatabaseConnect choiceMenuDB;
    protected List<String> selection;

    protected VBox choiceMenuBox; // Box met het hele keuzemenu

    public ChoiceMenu() {
        /**
         * Initieerd klasse welke voor database connectie zorgt en maakt de
         * knoppen voor het vergelijkscherm.
         */
        choiceMenuDB = new ChoiceMenuDatabaseConnect();
        createButtons();
    }

    private void createButtons() {
        /**
         * De comboboxen en toets weergeven knop worden hier geinitieerd en
         * aangemaakt met een functie.
         */
        yearChoiceBox = new ComboBox();
        schoolYearChoiceBox = new ComboBox();
        blockChoiceBox = new ComboBox();
        courseChoiceBox = new ComboBox();
        typeChoiceBox = new ComboBox();
        attemptChoiceBox = new ComboBox();
        examLoadButton = new Button("Toets weergeven");

        examLoadButton.setMinWidth(150);
        examLoadButton.setMinHeight(30);
        examLoadButton.setDisable(true);

        setButtons(yearChoiceBox, "Jaar");
        setButtons(schoolYearChoiceBox, "Leerjaar");
        setButtons(blockChoiceBox, "Periode");
        setButtons(courseChoiceBox, "Module");
        setButtons(typeChoiceBox, "Toetsvorm");
        setButtons(attemptChoiceBox, "Gelegenheid");
    }

    private void setButtons(ComboBox choiceBox, String promptText) {
        /**
         * Deze functie geeft de comboboxen een weergave waarde, breedte en
         * hoogte. Verder wordt de event handling hier geregeld.
         */
        choiceBox.setPromptText(promptText);
        choiceBox.setMinWidth(150);
        choiceBox.setMinHeight(30);
        choiceBox.setDisable(true);
        choiceBox.setOnMouseClicked(event -> {
            boxClickEvent(choiceBox);
        });
        choiceBox.valueProperty().addListener((observable, oldValue,
                                               newValue) -> {
            boxSelectedEventCheck(observable, choiceBox);
        });
    }

    private void boxClickEvent(ComboBox comboBox) {
        /**
         * Als er op een combobox wordt geklikt, zonder dat er een waarde
         * wordt geselecteerd, wordt er gekeken welke weergave waarde deze
         * combobox heeft, en aan de hand daarvan worden de juiste waarde
         * verkregen uit de database waarmee de box wordt gevuld.
         */
        comboBox.getItems().clear();
        switch (comboBox.getPromptText()) {
            case "Jaar":
                comboBox.getItems().addAll(choiceMenuDB.getYear());
                break;
            case "Leerjaar":
                comboBox.getItems().addAll(choiceMenuDB.getSchoolYear());
                break;
            case "Periode":
                comboBox.getItems().addAll(choiceMenuDB.getBlock());
                break;
            case "Module":
                comboBox.getItems().addAll(choiceMenuDB.getCourse());
                break;
            case "Toetsvorm":
                comboBox.getItems().addAll(choiceMenuDB.getType());
                break;
            case "Gelegenheid":
                comboBox.getItems().addAll(choiceMenuDB.getAttempt());
        }
    }

    private void boxSelectedEventCheck(ObservableValue observable, ComboBox
            comboBox) {
        /**
         * Als er een waarde in de combobox wordt geselecteerd, wordt er
         * eerst gekeken of deze niet null is (voor een of andere reden wordt
         * de geselecteerde waarde soms als null gezien wat problemen
         * veroorzaakt).
         */
        if (observable.getValue() == null) {
        } else {
            boxSelectedEvent(observable, comboBox);
        }
    }

    private void boxSelectedEvent(ObservableValue observable, ComboBox
            comboBox) {
        /**
         * Als de geselecteerde waarde in de combobox niet null is, wordt
         * deze functie aangeroepen. Er wordt gekeken welke weergave waarde
         * de box heeft (daarmee wordt bepaald welke box het is) en aan de
         * hand daarvan wordt de juiste functie aangeroepen welke de
         * comboboxen boven de geselecteerde combobox leegt en inactief maakt.
         */
        switch (comboBox.getPromptText()) {
            case "Jaar":
                yearBoxEvent(observable);
                break;
            case "Leerjaar":
                schoolYearBoxEvent(observable);
                break;
            case "Periode":
                blockBoxEvent(observable);
                break;
            case "Module":
                courseBoxEvent(observable);
                break;
            case "Toetsvorm":
                typeBoxEvent(observable);
                break;
            case "Gelegenheid":
                attemptBoxEvent(observable);
                break;
        }
    }


    private void disable(boolean bool1, boolean bool2, boolean bool3, boolean
            bool4, boolean bool5, boolean bool6, boolean bool7) {
        /**
         * Deze functie accepteerd een serie van booleans welke de comboboxen
         * en weergeef knop actief en inactief maken.
         */
        yearChoiceBox.setDisable(bool1);
        schoolYearChoiceBox.setDisable(bool2);
        blockChoiceBox.setDisable(bool3);
        courseChoiceBox.setDisable(bool4);
        typeChoiceBox.setDisable(bool5);
        attemptChoiceBox.setDisable(bool6);
        examLoadButton.setDisable(bool7);
    }


    public void yearBoxEvent(ObservableValue observable) {
        /**
         * Deze functie maakt alle comboboxen boven de jaar combobox leeg en
         * inactief en slaat de jaar combobox selectie op.
         */
        choiceMenuDB.setYearSelection(observable.getValue().toString());

        schoolYearChoiceBox.getItems().clear();
        blockChoiceBox.getItems().clear();
        courseChoiceBox.getItems().clear();
        typeChoiceBox.getItems().clear();
        attemptChoiceBox.getItems().clear();

        disable(false, false, true, true, true, true, true);
    }

    public void schoolYearBoxEvent(ObservableValue observable) {
        /**
         * Deze functie maakt alle comboboxen boven de leerjaar combobox leeg
         * en inactief en slaat de leerjaar combobox selectie op.
         */
        choiceMenuDB.setSchoolYearSeleciton(observable.getValue().toString());

        blockChoiceBox.getItems().clear();
        courseChoiceBox.getItems().clear();
        typeChoiceBox.getItems().clear();
        attemptChoiceBox.getItems().clear();

        disable(false, false, false, true, true ,true, true);
    }

    public void blockBoxEvent(ObservableValue observable) {
        /**
         * Deze functie maakt alle comboboxen boven de periode combobox leeg
         * en inactief en slaat de periode combobox selectie op.
         */
        choiceMenuDB.setBlockSelection(observable.getValue().toString());

        courseChoiceBox.getItems().clear();
        typeChoiceBox.getItems().clear();
        attemptChoiceBox.getItems().clear();

        disable(false, false, false, false, true, true, true);
    }

    public void courseBoxEvent(ObservableValue observable) {
        /**
         * Deze functie maakt alle comboboxen boven de module combobox leeg
         * en inactief en slaat de module combobox selectie op.
         */
        choiceMenuDB.setCourseSelection(observable.getValue().toString());

        typeChoiceBox.getItems().clear();
        attemptChoiceBox.getItems().clear();

        disable(false, false, false, false, false, true, true);
    }

    public void typeBoxEvent(ObservableValue observable) {
        /**
         * Deze functie maakt alle comboboxen boven de toetsvorm combobox
         * leeg en inactief en slaat de selectie van de toetsvorm combobox op.
         */
        choiceMenuDB.setTypeSelection(observable.getValue().toString());

        attemptChoiceBox.getItems().clear();

        disable(false, false, false, false, false, false, true);
    }

    public void attemptBoxEvent(ObservableValue observable) {
        /**
         * Slaat de selectie van de gelegenheid combobox op.
         */
        choiceMenuDB.setAttemptSelection(observable.getValue().toString());

        disable(false, false, false, false, false, false, false);
    }

    public VBox getChoiceMenuBox() {
        /**
         * Stopt alle componenten voor het keuzemenu in een box en returned
         * deze.
         */
        VBox fillBox = new VBox();
        VBox.setVgrow(fillBox, Priority.ALWAYS);

        yearChoiceBox.setDisable(false);

        choiceMenuBox = new VBox();
        choiceMenuBox.getChildren().addAll(yearChoiceBox,
                schoolYearChoiceBox, blockChoiceBox, courseChoiceBox,
                typeChoiceBox, attemptChoiceBox, fillBox, examLoadButton);
        choiceMenuBox.setSpacing(20);
        VBox.setVgrow(choiceMenuBox, Priority.ALWAYS);
        return choiceMenuBox;
    }

    public List<String> getSelection() {
        /**
         * Returned de selectie van alle comboboxen in een lijst.
         */
        selection = choiceMenuDB.getSelection();
        return selection;
    }
}
