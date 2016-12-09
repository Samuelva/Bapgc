package sample;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

/**
 * Created by Samuel on 4-12-2016.
 */
public class Keuzemenu {
    private VBox choiceButtonBox = new VBox();
    private ComboBox year;
    private ComboBox studyYear;
    private ComboBox period;
    private ComboBox module;
    private ComboBox type;
    private ComboBox chance;

    public Keuzemenu() {
        setComboBoxStyle();
    }
    private void setComboBoxStyle() {
        year = makeComboBox("Jaar");
        studyYear = makeComboBox("Studiejaar");
        period = makeComboBox("Periode");
        module = makeComboBox("Module");
        type = makeComboBox("Toetsvorm");
        chance = makeComboBox("Gelegenheid");

        studyYear.getItems().addAll("1", "2", "3", "4");
        type.getItems().addAll("Theorietoets", "Praktijktoets", "Opdracht", "Aanwezigheid",
                "Logboek", "Project");
    }

    private ComboBox makeComboBox(String promptText) {
        ComboBox comboBox = new ComboBox();
        comboBox.setPromptText(promptText);
        comboBox.setPrefWidth(150);
        comboBox.setPrefHeight(30);
        return comboBox;
    }

    public VBox getTestMenu() {
        choiceButtonBox.getChildren().addAll(year, studyYear, period, module, type, chance);
        return choiceButtonBox;
    }

    public VBox getModuleMenu() {
        choiceButtonBox.getChildren().addAll(year, studyYear, period);
        return choiceButtonBox;
    }

    public VBox getPeriodMenu() {
        choiceButtonBox.getChildren().addAll(year, studyYear);
        return choiceButtonBox;
    }

    public void setYearContent(String... values) {
        for (String value : values) {
            year.getItems().add(value);
        }
    }

    public void setPeriodContent(String... values) {
        for (String value : values) {
            period.getItems().add(value);
        }
    }

    public void setModuleContent(String... values) {
        for (String value : values) {
            module.getItems().add(value);
        }
    }

    public void setChanceContent(String... values) {
        for (String value : values) {
            chance.getItems().add(value);
        }
    }

}
