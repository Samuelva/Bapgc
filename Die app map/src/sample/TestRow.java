package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Samuel on 22-1-2017.
 */
public class TestRow {
    private SimpleStringProperty test;
    private SimpleIntegerProperty averageGrade;
    private SimpleIntegerProperty participants;
    private SimpleIntegerProperty failed;
    private SimpleIntegerProperty passed;
    private SimpleIntegerProperty passRate;

    public TestRow(String testI, Integer averageGradeI, Integer participantsI, Integer failedI, Integer passedI, Integer passRateI) {
        test = new SimpleStringProperty(testI);
        averageGrade = new SimpleIntegerProperty(averageGradeI);
        participants = new SimpleIntegerProperty(participantsI);
        failed = new SimpleIntegerProperty(failedI);
        passed = new SimpleIntegerProperty(passedI);
        passRate = new SimpleIntegerProperty(passRateI);
    }

    public String getTest() {
        return test.get();
    }

    public Integer getAverageGrade() {
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

    public Integer getPassRate() {
        return passRate.get();
    }

    public void setTest(String testI) {
        test.set(testI);
    }

    public void setAverageGrade(Integer averageGradeI) {
        averageGrade.set(averageGradeI);
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
