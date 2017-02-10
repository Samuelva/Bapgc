package sample;

import database.DatabaseConn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Samuel on 23-1-2017.
 * Deze klasse zorgt voor de connectie tussen de database en het keuzemenu.
 * De geselecteerde waarden van de comboboxen in het keuzemenu worden hierin
 * opgeslagen, en waarden waarmee de comboboxen worden gevuld, worden hier
 * verkregen.
 */
public class ChoiceMenuDatabaseConnect {
    protected DatabaseConn db;

    protected List<String> year;
    protected List<String> schoolYear;
    protected List<String> block;
    protected List<String> course;
    protected List<String> type;
    protected List<String> attempt;

    protected String selectedYear;
    protected String selectedSchoolYear;
    protected String selectedBlock;
    protected String selectedCourse;
    protected String selectedType;
    protected String selectedAttempt;

    protected List<String> selection;

    public ChoiceMenuDatabaseConnect() {
        /**
         * Main klasse. Maakt verbinding met de database als de klasse wordt
         * aangeroepen.
         */
        db = new DatabaseConn();
    }
    public void setYearSelection(String selectedYearI) {
        /**
         * Slaat de jaar selectie uit het keuzemenu op.
         */
        selectedYear = selectedYearI;
    }

    public void setSchoolYearSeleciton(String selectedSchoolYearI) {
        /**
         * Slaat de schooljaar selectie uit het keuzemenu op.
         */
        selectedSchoolYear = selectedSchoolYearI;
    }

    public void setBlockSelection(String selectedBlockI) {
        /**
         * Slaat de periode selectie uit het keuzemenu op.
         */
        selectedBlock = selectedBlockI;
    }

    public void setCourseSelection(String selectedCourseI) {
        /**
         * Slaat de module selectie uit het keuzemenu op.
         */
        selectedCourse = selectedCourseI;
    }

    public void setTypeSelection(String selectedTypeI) {
        /**
         * Slaat de toetsvorm selectie uit het keuzemenu op.
         */
        selectedType = selectedTypeI;
    }

    public void setAttemptSelection(String selectedAttemptI) {
        /**
         * Slaat de gelegenheid selectie uit het keuzemenu op.
         */
        selectedAttempt = selectedAttemptI;
    }

    public List<String> getSelection() {
        /**
         * Returned alle opgeslagen selecties van het keuzemenu in een lijst.
         */
        selection = new ArrayList<>();
        selection.addAll(Arrays.asList(selectedCourse, selectedYear,
                selectedSchoolYear, selectedBlock,selectedAttempt,
                selectedType));
        return selection;
    }

    public List<String> getYear() {
        /**
         * Returned alle beschikbare jaartallen uit de database.
         */
        year = db.getYears();
        return year;
    }

    public List<String> getSchoolYear() {
        /**
         * Returned alle beschikbare schooljaren in de database welke bij het
         * het geselecteerde jaar hoort (leerjaar 1, 2, 3 en 4 meestal) uit
         * de database.
         */
        schoolYear = db.getSchoolYears(selectedYear);
        return schoolYear;
    }

    public List<String> getBlock() {
        /**
         * Returned alle beschikbare periodes welke bij het geselecteerde
         * jaar en schooljaar hoort (periode 1, 2, 3 en 4 meestal) uit de
         * database.
         */
        block = db.getBlocks(selectedYear, selectedSchoolYear);
        return block;
    }

    public List<String> getCourse() {
        /**
         * Returned alle beschikbare modulen welke bij het geselecteerde
         * jaar, schooljaar en periode hoort uit de database.
         */
        course = db.getCourses(selectedYear, selectedSchoolYear, selectedBlock);
        return course;
    }

    public List<String> getType() {
        /**
         * Returned alle beschikbare toetsvormen welke bij de geselecteerde
         * jaar, schooljaar, periode en module hoort uit de database.
         */
        type = db.getTypes(selectedYear, selectedSchoolYear, selectedBlock,
                selectedCourse);
        return type;
    }

    public List<String> getAttempt() {
        /**
         * Returned alle beschikbare gelegenheden welke bij de geselecteerde
         * jaar, schooljaar, periode, module en toetsvorm hoort.
         */
        attempt = db.getAttempts(selectedYear, selectedSchoolYear,
                selectedBlock, selectedCourse, selectedType);
        return attempt;
    }
}
