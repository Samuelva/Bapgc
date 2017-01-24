package sample;

import database.DatabaseConn;

import java.util.List;

/**
 * Created by Samuel on 23-1-2017.
 */
public class KeuzemenuDBVerbind {
    DatabaseConn db;

    List<String> year;
    List<String> schoolYear;
    List<String> block;
    List<String> course;
    List<String> type;
    List<String> attempt;

    //
    String selectedYear;
    String selectedSchoolYear;
    String selectedBlock;
    String selectedCourse;
    String selectedType;
    String selectedAttempt;

    public KeuzemenuDBVerbind() {
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

}
