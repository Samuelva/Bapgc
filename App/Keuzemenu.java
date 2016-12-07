package sample;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
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
    private HBox buttonBox = new HBox();
    private VBox menuBox = new VBox();

    public Keuzemenu(String... menus) {
        for (String i : menus) {
            keuzeMenu.put(i, new String[]{});
            test.put(i+"Ar", new ComboBox<>());
            test.get(i+"Ar").getItems().addAll(keuzeMenu.get(i));
            keuzeMenuBox.getChildren().addAll(test.get(i+"Ar"));
        }
    }

    public VBox returnMenu() {
        buttonBox.getChildren().addAll(alles, reset);
        menuBox.getChildren().addAll(keuzeMenuBox, buttonBox);
        keuzeMenuBox.setMinHeight(500);
        menuBox.setMinWidth(100);
        return menuBox;
    }

}
