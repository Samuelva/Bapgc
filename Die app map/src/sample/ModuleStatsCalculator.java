package sample;

import database.DatabaseConn;

import java.util.*;

/**
 * Created by kwita_000 on 26-1-2017.
 */
public class ModuleStatsCalculator {
    Object[][] attempts;
    HashMap<String, Double[]> tempGrades = new HashMap();
    HashMap<String, Double[]> finalGrades = new HashMap();
    DatabaseConn d;

    ModuleStatsCalculator(String moduleID){
        this.d = new DatabaseConn();
        this.attempts = d.GetToetsKansen(moduleID);
        compileFinalGrades();
        determineInclusion();
        d.CloseConnection();
    }

    private void determineInclusion() {
        for (String key: this.tempGrades.keySet()){
            boolean include = true;
            for (Double value: this.tempGrades.get(key)) {
                if (value == null){
                    include = false;
                    break;
                }
            }
            if (include) {
                this.finalGrades.put(key, this.tempGrades.get(key));
            }
        }
    }

    private void compileFinalGrades() {
        for (int examPos = 0; examPos < this.attempts.length; ++examPos) { //loop door toetsen
            for (int chance = 1; chance < this.attempts[examPos].length; ++chance) { //loop door kansen
                String[][] grades;
                try {
                    Integer[] values = d.GetCesuurMaxGok((int) this.attempts[examPos][chance]);
                    grades = Statistics.updateGradeTableArray(d.GetStudentScores(
                            (int) this.attempts[examPos][chance]), values[0], values[1]);
                } catch (EmptyStackException e){
                    break;
                }
                for (int student = 0; student < grades.length; ++student) {
                    Double grade = Double.parseDouble(grades[student][1]);
                    addGradeToMap(grade, grades[student][0], examPos);
                }
            }
        }
    }

    private void addGradeToMap(Double grade, String studentId, int examPos){
        if (this.tempGrades.containsKey(studentId)){
            Double[] studentGrades = this.tempGrades.get(studentId);
            if (studentGrades[examPos] != null){
                if (studentGrades[examPos] < grade){
                    studentGrades[examPos] = grade;
                }
            } else {
                studentGrades[examPos] = grade;
            }
            this.tempGrades.put(studentId, studentGrades);
        } else {
            Double[] studentGrades = new Double[this.attempts.length];
            studentGrades[examPos] = grade;
            this.tempGrades.put(studentId, studentGrades);
        }
    }

    protected Set<String> getParticipant(){
        return this.finalGrades.keySet();
    }

    protected double getAverageGrade(){
        double sum = 0;
        int num = 0;
        for (String key: this.finalGrades.keySet()){
            for (Double grade: this.finalGrades.get(key)){
                sum += grade;
                ++num;
            }
        }
        return Statistics.round(sum/num, 1);
    }

    protected List<String> getPasses(){
        List<String> out = new ArrayList();
        for (String key: this.finalGrades.keySet()){
            boolean include = true;
            for (Double grade: this.finalGrades.get(key)){
                if (grade < 5.5){
                    include = false;
                    break;
                }
            }
            if (include){
                out.add(key);
            }
        }
        return out;
    }
}
