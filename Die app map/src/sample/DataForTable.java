package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

/**
 * Deze class bevat data voor in de tabel in het toevoeg scherm.
 */
public class DataForTable {
    /**
     * De globale variabelen bevatten de data die in de kolommen
     * van de tabel in het toevoeg scherm komen te staan als
     * SimpleStringProperties.
     */
    private SimpleStringProperty code;
    private SimpleStringProperty period;
    private SimpleStringProperty year;
    private SimpleStringProperty studyYear;
    private SimpleStringProperty type;

    public DataForTable(String codeIn, String yearIn, String periodIn, String
            studyYearIn, List<String> typeIn) {
        /**
         * De meegegeven waardes worden aan de bijbehorende globale variabelen gezet.
         * De '[' en ']' worden hierbij verwijderd bij het converteren van een List
         * van toetsenvormen naar een String.
         */
        code = new SimpleStringProperty(codeIn);
        year = new SimpleStringProperty(yearIn);
        period = new SimpleStringProperty(periodIn);
        studyYear = new SimpleStringProperty(studyYearIn);
        String typeIn2 = typeIn.toString().replace("[", "").replace("]", "");
        type = new SimpleStringProperty(typeIn2);
    }

    public String getCode() {
        /**
         * Geef de module code terug.
         */
        return code.get();
    }

    public String getYear() {
        /**
         * Geef het jaar terug.
         */
        return year.get();
    }

    public String getPeriod() {
        /**
         * Geef de periode terug.
         */
        return period.get();
    }

    public String getStudyYear() {
        /**
         * Geef het studiejaar terug.
         */
        return studyYear.get();
    }

    public String getType() {
        /**
         * Geef de toetsvormen terug.
         */
        return type.get();
    }

    public void setCode(String codeIn) {
        /**
         * Verander de module code.
         */
        code.set(codeIn);
    }

    public void setYear(String yearIn) {
        /**
         * Verander het jaar.
         */
        year.set(yearIn);
    }

    public void setPeriod(String periodIn) {
        /**
         * Verander de periode.
         */
        period.set(periodIn);
    }

    public void setStudyYear(String studyYearIn) {
        /**
         * Verander het studiejaar.
         */
        studyYear.set(studyYearIn);
    }

    public void setType(String typeIn) {
        /**
         * Verander de toetsvormen.
         */
        type.set(typeIn);
    }

}