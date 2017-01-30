package sample;

import database.DatabaseConn;

import java.util.*;

/**
 * Deze class kan gebruikt worden voor het ophalen
 * van het gemiddelde cijfer, de deelnemers en degene
 * met een voldoende voor een module.
 */
public class CourseStatsCalculator {
    /**
     * De volgende globale variabelen worden gebruikt:
     * - attempts: de IDs van alle gelegenheden van alle
     *      toetsen van de module
     * - initialGrades: Alle cijfer voor de toetsen van de
     *      module voor alle deelnemers.
     * - finalGrades: De data van initialGrades, maar
     *      alleen voor degene die aan alle toetsen
     *      meegedaan hebben.
     * - d: Een database connectie.
     */
    Object[][] attempts;
    HashMap<String, Double[]> initialGrades = new HashMap();
    HashMap<String, Double[]> finalGrades = new HashMap();
    DatabaseConn d;

    CourseStatsCalculator(String courseID){
        /**
         * Er wordt een connectie gemaakt met de database. De
         * toetsen en hun kansen worden opgehaald. Voor de
         * module met het ID courseID worden alle cijfers
         * voor alle toetsen berekend. Hierbij worden
         * de gelegenheden samengevoegd, zodat alleen het
         * hoogste cijfer mee wordt genomen. Deze cijfers
         * komen in de map this.initialGrades te staan.
         * Deze wordt gefilterd, zodat alleen de studenten
         * die aan de gehele module mee hebben gedaan verder
         * mee worden genomen.
         */
        this.d = new DatabaseConn();
        this.attempts = d.GetToetsKansen(courseID);
        compileFinalGrades();
        determineInclusion();
        d.CloseConnection();
    }

    private void determineInclusion() {
        /**
         * Deze methode bepaalt welke studenten er aan de gehele
         * module mee gedaan hebben.
         * Er wordt door de map met de cijfers heen geloopt.
         * De map bevat per student een Double[] met al zijn cijfers.
         * Als hierin een null staat, betekent dat dat de student
         * niet aan een toets heeft deelgenomen. Als er geen null
         * gevonden wordt, heeft de student meegedaan aan de gehele
         * module en wordt zijn cijfer gekopiëerd naar de map
         * this.finalGrades.
         */
        for (String key: this.initialGrades.keySet()){
            boolean include = true;
            for (Double value: this.initialGrades.get(key)) {
                if (value == null){
                    include = false;
                    break;
                }
            }
            if (include) {
                this.finalGrades.put(key, this.initialGrades.get(key));
            }
        }
    }

    private void compileFinalGrades() {
        /**
         * Deze method bepaalt de cijfers die voor iedere toets door iedere
         * student gehaald is en zet ze in this.initialGrades.
         * Er wordt door de toetsen van de modulen geloopt. Voor iedere toets
         * wordt er door de gelegenheden geloopt. De scores, cesuur en maximum
         * aantal punten voor de gelegenheid worden opgehaald en de cijfers
         * worden berekend. Als er een EmptyStackException plaatsvindt, is er
         * geen data bekend voor de gelegenheid en wordt die overgeslagen.
         * Er wordt door de studenten en hun cijfers heen geloopt en de
         * methode addGradeToMap wordt gebruikt om ze toe te voegen aan de
         * map.
         */
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
        /**
         * Deze methode voegt een cijfer toe aan de map this.initialGrades.
         * Als de student al bekend is in de Map, wordt er gekeken of er
         * al een cijfer bekend is voor dezelfde toets. Als dit zo is,
         * word dit cijfer vervangen met het nieuwe cijfer.
         * Als er geen cijfer bekend is wordt het cijfer in de map gezet.
         * Als de student nog niet bekend is, wordt er een Double[] in de
         * map gezet met een positie voor iedere toets en wordt het cijfer
         * aan de map toegevoegd.
         * De cijfers voor dezelfde toets (maar verschillende gelegenheden)
         * overschrijven elkaar, als één hoger is dan de ander. De positie
         * in de Double[] staat verbonden met een toets.
         */
        if (this.initialGrades.containsKey(studentId)){
            Double[] studentGrades = this.initialGrades.get(studentId);
            if (studentGrades[examPos] != null){
                if (studentGrades[examPos] < grade){
                    studentGrades[examPos] = grade;
                }
            } else {
                studentGrades[examPos] = grade;
            }
            this.initialGrades.put(studentId, studentGrades);
        } else {
            Double[] studentGrades = new Double[this.attempts.length];
            studentGrades[examPos] = grade;
            this.initialGrades.put(studentId, studentGrades);
        }
    }

    protected Set<String> getParticipant(){
        /**
         * Geeft de keys van this.finalGrades terug. Dit zijn de student
         * IDs van alle studenten die aan de gehele modulen mee
         * gedaan hebben.
         */
        return this.finalGrades.keySet();
    }

    protected double getAverageGrade(){
        /**
         * Deze methode berekent het gemiddelde cijfer voor de studenten
         * die aan de gehele module mee gedaan hebben.
         */
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

    protected Set<String> getPasses(){
        /**
         * Deze methode maakt een set van student IDs die een voldoende hebben
         * gehaald voor de gehele modulen.
         * Eerst wordt een list gemaakt, vervolgens wordt er door de cijfer van
         * iedere student geloopt. Als er een cijfer lager is dan 5.5, wordt de
         * boolean include op false gezet en worden de rest van de cijfers voor
         * die student niet meer gecontroleerd.
         * Als alle cijfers gecontroleerd zijn wordt het ID van de student
         * toegevoegd aan de list, als include true is.
         * Van de list wordt een set gemaakt die teruggegeven wordt.
         */
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
        return new HashSet(out);
    }
}