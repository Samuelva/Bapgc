package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

/**
 * Created by kwita_000 on 24-1-2017.
 */
public class dataForTable {
    private SimpleStringProperty code;
    private SimpleStringProperty period;
    private SimpleStringProperty year;
    private SimpleStringProperty studyYear;
    private SimpleStringProperty type;

    public dataForTable(String codeIn, String yearIn,  String periodIn, String studyYearIn, List<String> typeIn) {
        code = new SimpleStringProperty(codeIn);
        year = new SimpleStringProperty(yearIn);
        period = new SimpleStringProperty(periodIn);
        studyYear = new SimpleStringProperty(studyYearIn);
        String typeIn2 = typeIn.toString().replace("[", "").replace("]", "");
        type = new SimpleStringProperty(typeIn2);
    }

    public String getCode() {
        return code.get();
    }

    public String getYear() {
        return year.get();
    }

    public String getPeriod() {
        return period.get();
    }

    public String getStudyYear() {
        return studyYear.get();
    }

    public String getType() {
        return type.get();
    }

    public void setCode(String codeIn) {
        code.set(codeIn);
    }

    public void setYear(String yearIn) {
        year.set(yearIn);
    }

    public void setPeriod(String periodIn) {
        period.set(periodIn);
    }

    public void setStudyYear(String studyYearIn) {
        studyYear.set(studyYearIn);
    }

    public void setType(String typeIn) {
        type.set(typeIn);
    }

}
