package sample;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.util.HashMap;

/**
 * Created by Samuel on 4-12-2016.
 */
public class Keuzemenu {
    HashMap<String, String[]> keuzeMenu = new HashMap<>();
    HashMap<String, ComboBox<String>> test = new HashMap<>();
    Button alles = new Button("Alles");
    Button reset = new Button("Reset");
    private VBox keuzeMenuBox = new VBox();
    private VBox choiceButtonBox = new VBox();
    private VBox menuBox = new VBox();

    private ComboBox year = new ComboBox();
    private ComboBox studyYear = new ComboBox();
    private ComboBox period = new ComboBox();
    private ComboBox module = new ComboBox();
    private ComboBox type = new ComboBox();
    private ComboBox chance = new ComboBox();

    public Keuzemenu() {
        setComboBoxStyle();
    }
    private void setComboBoxStyle() {
        year.setPromptText("Jaar");
        year.setPrefWidth(150);
        year.setPrefHeight(30);

        studyYear.setPromptText("Studiejaar");
        studyYear.getItems().addAll("1", "2", "3", "4");
        studyYear.setPrefWidth(150);
        studyYear.setPrefHeight(30);

        period.setPromptText("Periode");
        period.setPrefWidth(150);
        period.setPrefHeight(30);

        module.setPromptText("Module");
        module.setPrefWidth(150);
        module.setPrefHeight(30);

        type.setPromptText("Toetsvorm");
        type.getItems().addAll("Theorietoets", "Praktijktoets", "Opdracht", "Aanwezigheid",
                "Logboek", "Project");
        type.setPrefWidth(150);
        type.setPrefHeight(30);

        chance.setPromptText("Gelegenheid");
        chance.setPrefWidth(150);
        chance.setPrefHeight(30);

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
