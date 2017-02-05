package sample;

import database.DatabaseConn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Samuel on 23-1-2017.
 */
public class ChoiceMenuDatabaseConnect {
    protected DatabaseConn db;

    protected List<String> year;
    protected List<String> schoolYear;
    protected List<String> block;
    protected List<String> course;
    protected List<String> type;
    protected List<String> attempt;

    //
    protected String selectedYear;
    protected String selectedSchoolYear;
    protected String selectedBlock;
    protected String selectedCourse;
    protected String selectedType;
    protected String selectedAttempt;

    protected List<String> selection;

    public ChoiceMenuDatabaseConnect() {
        db = new DatabaseConn();
    }

    public List<String> getYear() {
        year = db.getYears();
        return year;
    }

    public List<String> getSchoolYear() {
        schoolYear = db.getSchoolYears(selectedYear);
        return schoolYear;
    }

    public List<String> getBlock() {
        block = db.getBlocks(selectedYear, selectedSchoolYear);
        return block;
    }

    public List<String> getCourse() {
        course = db.getCourses(selectedYear, selectedSchoolYear, selectedBlock);
        return course;
    }

    public List<String> getType() {
        type = db.getTypes(selectedYear, selectedSchoolYear, selectedBlock, selectedCourse);
        return type;
    }

    public List<String> getAttempt() {
        attempt = db.getAttempts(selectedYear, selectedSchoolYear, selectedBlock, selectedCourse, selectedType);
        return attempt;
    }

    public void setYearSelection(String selectedYearI) {
        selectedYear = selectedYearI;
    }

    public void setSchoolYearSeleciton(String selectedSchoolYearI) {
        selectedSchoolYear = selectedSchoolYearI;
    }

    public void setBlockSelection(String selectedBlockI) {
        selectedBlock = selectedBlockI;
    }

    public void setCourseSelection(String selectedCourseI) {
        selectedCourse = selectedCourseI;
    }

    public void setTypeSelection(String selectedTypeI) {
        selectedType = selectedTypeI;
    }

    public void setAttemptSelection(String selectedAttemptI) {
        selectedAttempt = selectedAttemptI;
    }

    public List<String> getSelection() {
        selection = new ArrayList<String>();
        selection.addAll(Arrays.asList(selectedCourse, selectedYear, selectedSchoolYear, selectedBlock,selectedAttempt, selectedType));
        return selection;
    }
}
