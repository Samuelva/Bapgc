package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Samuel on 22-1-2017.
 * Deze klasse zorgt ervoor dat er makkelijk rijen toegevoegd kunnen worden
 * aan de tabel in de modulen en periode tab.
 */
public class Row {
    private SimpleStringProperty name;
    private SimpleDoubleProperty averageGrade;
    private SimpleIntegerProperty participants;
    private SimpleIntegerProperty failed;
    private SimpleIntegerProperty passed;
    private SimpleDoubleProperty passRate;

    public Row(String nameI, Double averageGradeI, Integer participantsI,
               Integer failedI, Integer passedI, Double passRateI) {
        /**
         * Main klasse, data voor elke kolom wordt opgegeven.
         */
        name = new SimpleStringProperty(nameI);
        averageGrade = new SimpleDoubleProperty(averageGradeI);
        participants = new SimpleIntegerProperty(participantsI);
        failed = new SimpleIntegerProperty(failedI);
        passed = new SimpleIntegerProperty(passedI);
        passRate = new SimpleDoubleProperty(passRateI);
    }

    public String getName() {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        return name.get();
    }

    public Double getAverageGrade() {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        return averageGrade.get();
    }

    public Integer getParticipants() {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        return participants.get();
    }

    public Integer getFailed() {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        return failed.get();
    }

    public Integer getPassed() {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        return passed.get();
    }

    public double getPassRate() {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        return passRate.get();
    }

    public void setName(String nameI) {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        name.set(nameI);
    }

    public void setParticipants(Integer participantsI) {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        participants.set(participantsI);
    }

    public void setFailed(Integer failedI) {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        failed.set(failedI);
    }

    public void setPassed(Integer passedI) {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        passed.set(passedI);
    }

    public void setPassRate(Integer passRateI) {
        /**
         * Nodig voor het kunnen toevoegen van rijen aan de tabel.
         */
        passRate.set(passRateI);
    }

}
