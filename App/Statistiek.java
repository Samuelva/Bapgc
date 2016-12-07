package sample;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
/**
 * Created by Samuel on 5-12-2016.
 */
public class Statistiek {
    private VBox statisticsBox = new VBox();
    private String participants = "Aantal Deelnemers: ";
    private String failed = "Aantal onvoldoendes: ";
    private String passed = "Aantal voldoendes: ";
    private String passRate = "Rendement: ";


    public Statistiek(int[] values) {
        participants += Integer.toString(values[0]);
        failed += Integer.toString(values[1]);
        passed += Integer.toString(values[2]);
        passRate += Integer.toString(values[3]);

        Label participantsLbl = new Label(participants);
        Label failedLbl = new Label(failed);
        Label passedLbl = new Label(passed);
        Label passRateLbl = new Label(passRate);
        statisticsBox.getChildren().addAll(participantsLbl, failedLbl, passedLbl, passRateLbl);
    }

    public VBox returnStatisticBox() {
        return statisticsBox;
    }
}
