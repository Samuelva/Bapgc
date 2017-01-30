package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Samuel on 22-1-2017.
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
        name = new SimpleStringProperty(nameI);
        averageGrade = new SimpleDoubleProperty(averageGradeI);
        participants = new SimpleIntegerProperty(participantsI);
        failed = new SimpleIntegerProperty(failedI);
        passed = new SimpleIntegerProperty(passedI);
        passRate = new SimpleDoubleProperty(passRateI);
    }

    public String getName() {
        return name.get();
    }

    public Double getAverageGrade() {
        return averageGrade.get();
    }

    public Integer getParticipants() {
        return participants.get();
    }

    public Integer getFailed() {
        return failed.get();
    }

    public Integer getPassed() {
        return passed.get();
    }

    public double getPassRate() {
        return passRate.get();
    }

    public void setName(String nameI) {
        name.set(nameI);
    }

    public void setParticipants(Integer participantsI) {
        participants.set(participantsI);
    }

    public void setFailed(Integer failedI) {
        failed.set(failedI);
    }

    public void setPassed(Integer passedI) {
        passed.set(passedI);
    }

    public void setPassRate(Integer passRateI) {
        passRate.set(passRateI);
    }

}
