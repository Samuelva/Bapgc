package sample;


import database.DatabaseConn;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Deze class bevat een methodes voor het bereken van statistieken.
 */
public class Statistics {


    public static int sum(int[] values){
        /**
         * Bereken de som van een int array.
         */
        int total = 0;
        for (int i: values){
            total += i;
        }
        return total;
    }

    public static double sum(double[] values){
        /**
         * Bereken de som van een double array.
         */
        double total = 0;
        for (double i: values){
            total += i;
        }
        return total;
    }

    public static double mean(int[] values){
        /**
         * Bereken het gemiddelde van een int array.
         */
        double total = (double) sum(values);
        return total/values.length;
    }

    public static double mean(double[] values){
        /**
         * Bereken het gemiddelde van een double array.
         */
        double total = sum(values);
        return total/values.length;
    }
    
    public static double median(double[] values) {
        /**
         * Bereken het mediaan van een double array.
         */
        Arrays.sort(values);
        double median;

        if (values.length % 2 == 0) {
            median = (values[values.length / 2] +
                    values[values.length / 2 - 1]) / 2;
        } else {
            median = values[values.length/2];
        }
        return median;
    }

    public static double var(int[] values){
        /**
         * Bereken de variantie van een int array.
         */
        double mean = mean(values);
        double diffsum = 0;
        for (int i: values) {
            double diff = i - mean;
            diff *= diff;
            diffsum += diff;
        }
        return diffsum/(values.length-1);
    }

    public static double var(double[] values){
        /**
         * Bereken de variantie van een double array.
         */
        double mean = mean(values);
        double diffsum = 0;
        for (double i: values) {
            double diff = i - mean;
            diff *= diff;
            diffsum += diff;
        }
        return diffsum/(values.length-1);
    }

    public static double sd(int[] values){
        /**
         * Bereken de standaard deviatie van een int array.
         */
        double var = var(values);
        return Math.sqrt(var);
    }

    public static double sd(double[] values){
        /**
         * Bereken de standaard deviate van een double array.
         */
        double var = var(values);
        return Math.sqrt(var);
    }

    public static double percentage(int value, int max){
        /**
         * Bereken het percentage dat 'value' is van 'max' waarbij beide int zijn.
         */
        if (!(max > 0)) {
            throw new IllegalArgumentException("max should be higher than 0.");
        }
        return value/ (double) max * 100;
    }

    public static double percentage(int value, double max){
        /**
         * Bereken het percentage dat 'value' is van 'max' waarbij 'value' een int is een 'max' een double.
         */
        if (!(max > 0)) {
            throw new IllegalArgumentException("max should be higher than 0.");
        }
        return value/max * 100;
    }

    public static double percentage(double value, int max){
        /**
         * Bereken het percentage dat 'value' is van 'max' waarbij 'value' een double is een 'max' een int.
         */
        if (!(max > 0)) {
            throw new IllegalArgumentException("max should be higher than 0.");
        }
        return value/max * 100;
    }

    public static double percentage(double value, double max){
        /**
         * Bereken het percentage dat 'value' is van 'max' waarbij beide double zijn.
         */
        if (!(max > 0)) {
            throw new IllegalArgumentException("max should be higher than 0.");
        }
        return value/max * 100;
    }

    public static double round(double value, int places) {
        /**
         * Rond een waarde 'value' af tot 'places' plaatsen.
         */
        if (Double.isFinite(value)) {
            BigDecimal value2 = new BigDecimal(value);
            value2 = value2.setScale(places, RoundingMode.HALF_UP);
            return value2.doubleValue();
        } else {
            return value;
        }
    }

    public static double grade(int[] values, int threshold, int max){
        /**
         * Bereken het cijfer voor een int array van punten 'values' met behulp van een maximum aantal punten
         * 'max' en een censuur 'threshold'. Deze cijfer worden als doubles terug gegeven, afgerond op 1 cijfer
         * na de komma.
         */
        int total = sum(values);
        double threshold2 = (double) threshold;
        if (total > max) {
            return 10;
        } else if (total < threshold){
            return round(1 + total / threshold2 * 4.5, 1);
        } else {
            return round(5.5 + (total - threshold2) / (max - threshold2) * 4.5, 1);
        }
    }

    public static int max(int[] values){
        /**
         * Bepaal de hoogste waarde in een int array 'values'.
         */
        int out = values[0];
        for (int i: values){
            if (i > out){
                out = i;
            }
        }
        return out;
    }

    public static double max(double[] values){
        /**
         * Bepaal de hoogste waarde in een double array 'values'.
         */
        double out = values[0];
        for (double i: values){
            if (i > out){
                out = i;
            }
        }
        return out;
    }

    public static int min(int[] values){
        /**
         * Bepaal de laagste waarde in een int array 'values'.
         */
        int out = values[0];
        for (int i: values){
            if (i < out){
                out = i;
            }
        }
        return out;
    }

    public static double min(double[] values){
        /**
         * Bepaal de laagste waarde in een double array 'values'.
         */
        double out = values[0];
        for (double i: values){
            if (i < out){
                out = i;
            }
        }
        return out;
    }

    public static int[] stringToIntArray(String[] values, int skip){
        /**
         * Verander een String[] naar en int[]. Skip de eerste 'skip' waardes.
         */
        int[] out = new int[values.length-skip];
        for (int i = 0; i < values.length-skip; i++){
            out[i] = Integer.parseInt(values[i+skip]);
        }
        return out;
    }

    public static double[] stringToDoubleArray(String[] values, int skip){
        /**
         * Verander een String[] naar en double[]. Skip de eerste 'skip' waardes.
         */
        double[] out = new double[values.length-skip];
        for (int i = 0; i < values.length-skip; i++){
            out[i] = Double.parseDouble(values[i+skip]);
        }
        return out;
    }

    public static String[][] updateGradeTableArray(String[][] original, int threshold, int max){
        /**
         *  Voeg het cijfer en totaal aantal punten toe aan de Strig[][] die per student het aantal punten
         *  per vraag bevat.
         */
        String[][] out = new String[original.length][original[0].length+2];
        for (int i = 0; i < original.length; i++){
            out[i][0] = original[i][0];
            int[] points = stringToIntArray(original[i], 1);
            out[i][1] = Double.toString(grade(points, threshold, max));
            out[i][2] = Integer.toString(sum(points));
            for (int x = 1; x < original[0].length; x++){
                out[i][x+2] = original[i][x];
            }
        }
        return out;
    }

    public static int count(int query, int[] list){
        /**
         * Tel hoe vaak 'query' voorkomt in 'list'.
         */
        int out = 0;
        for (int i: list){
            if (i == query){
                out++;
            }
        }
        return out;
    }

    public static int getPasses(double[] values){
        /**
         * Tel hoeveel mensen er een voldoende hebben in een lijst 'values' van cijfers.
         */
        int out = 0;
        for (double i: values){
            if (i >= 5.5){
                out++;
            }
        }
        return out;
    }

    public static int getFails(double[] values) {
        /**
         * Tel hoeveel mensen er een onvoldoende hebben in een lijst 'values' van cijfers.
         */
        int out = 0;
        for (double i : values) {
            if (i <= 5.5) {
                out++;
            }
        }
        return out;
    }

    public static double kthPercentile(double k, int[] values){
        /**
         * Bepaal de kth percentiel in 'values'.
         */
        Arrays.sort(values);
        double index = k*(values.length - 1);
        int i = (int) Math.floor(index);

        if (i < 0){
            return 0;
        } else if (i >= values.length-1) {
            return values[values.length-1];
        } else {
            double fraction = index-i;
            return values[i] + fraction*(values[i+1]-values[i]);
        }
    }

    public static double kthPercentile(double k, double[] values){
        /**
         * Bepaal de kth percentiel in 'values'.
         */
        Arrays.sort(values);
        double index = k*(values.length - 1);
        int i = (int) Math.floor(index);

        if (i < 0){
            return 0;
        } else if (i >= values.length-1) {
            return values[values.length-1];
        } else {
            double fraction = index-i;
            return values[i] + fraction*(values[i+1]-values[i]);
        }
    }

    public static double kthQuartile(int k, double[] values) {
        /**
         * Berekend 1, 2, of 3 kwartiel van de opgegeven in array.
         * 25 = 1e kwartiel, 50 = 2e kwartiel, 75 = 3e kwartiel.
         */
        Arrays.sort(values);
        int index = Math.round(values.length * k /100);
        return values[index];
    }

    public static int[] getBiggerThan(int value, int[] values){
        /**
         * Maak een int[] met alle waardes uit 'values' die hoger zijn dan 'value'.
         */
        List<Integer> out = new ArrayList();
        for (int i: values){
            if (i > value) {
                out.add(i);
            }
        }
        int[] out2 = new int[out.size()];
        for(int i = 0; i < out.size(); i++) {
            out2[i] = out.get(i);
        }
        return out2;
    }

    public static int[] getBiggerThan(double value, int[] values){
        /**
         * Maak een int[] met alle waardes uit 'values' die hoger zijn dan 'value'.
         */
        List<Integer> out = new ArrayList();
        for (int i: values){
            if (i > value) {
                out.add(i);
            }
        }
        int[] out2 = new int[out.size()];
        for(int i = 0; i < out.size(); i++) {
            out2[i] = out.get(i);
        }
        return out2;
    }

    public static double[] getBiggerThan(int value, double[] values){
        /**
         *  Maak een double[] met alle waardes uit 'values' die hoger zijn dan 'value'.
         */
        List<Double> out = new ArrayList();
        for (double i: values){
            if (i > value) {
                out.add(i);
            }
        }
        double[] out2 = new double[out.size()];
        for(int i = 0; i < out.size(); i++) {
            out2[i] = out.get(i);
        }
        return out2;
    }

    public static double[] getBiggerThan(double value, double[] values){
        /**
         *  Maak een double[] met alle waardes uit 'values' die hoger zijn dan 'value'.
         */
        List<Double> out = new ArrayList<Double>();
        for (double i: values){
            if (i > value) {
                out.add(i);
            }
        }
        double[] out2 = new double[out.size()];
        for(int i = 0; i < out.size(); i++) {
            out2[i] = out.get(i);
        }
        return out2;
    }

    public static double varianceQuestions(String[][] values){
        /**
         *  Bereken de som van de varianties van de vragen.
         */
        int num = values[0].length - 3;
        double[] vars = new double[num];
        for (int i = 0; i < num; i++){
            String[] points = new String[values.length];
            for (int x = 0; x < values.length; x++){
                points[x] = values[x][i+3];
            }
            vars[i] = var(stringToIntArray(points, 0));
        }
        return sum(vars);
    }

    public static double correlation(int[] values1, int[] values2) {
        /**
         * Bereken de correlatie tusen twee int[].
         */
        if (values1.length != values2.length){
            throw new IllegalArgumentException("Arrays zhould be of equal size.");
        }
        double sum1 = sum(values1);
        double sum2 = sum(values2);
        double sum11 = 0;
        double sum22 = 0;
        double sum12 = 0;
        for(int i = 0; i < values1.length; i++) {
            double val1 = values1[i];
            double val2 = values2[i];
            sum11 += val1 * val1;
            sum22 += val2 * val2;
            sum12 += val1 * val2;
        }
        double cov = sum12/values1.length - sum1*sum2/values1.length/values1.length;
        double error1 = Math.sqrt(sum11/values1.length -  sum1*sum1/values1.length/values1.length);
        double error2 = Math.sqrt(sum22/values1.length -  sum2*sum2/values1.length/values1.length);
        return cov / error1 / error2;
    }

    public static double percentileMean(int[] values, double percentilePoints){
        /**
         * Bereken de gemiddelde score van een percentiel.
         */
        int[] values2 = getBiggerThan(percentilePoints, values);
        return mean(values2);
    }

    public static double cronbach(int n, double questionsVar, double examVar){
        /**
         * Bereken de cronbach alfa.
         */
        return n / (n-1) * (1 - questionsVar/examVar);
    }

    public static double cohen(double meanPoints, double mastery, double chancePoints){
        /**
         * Bereken de Cohen-Schotanus censuur.
         */
        return (meanPoints-chancePoints) * mastery + chancePoints;
    }

    public static String[] getColumn(int index, Object[][] matrix) {
        /**
         * Haal de positie index van alle Arrays in matrix op en
         * geef ze terug als String[].
         */
        String[] out = new String[matrix.length];
        for (int i = 0; i < matrix.length; ++i){
            out[i] = matrix[i][index].toString();
        }
        return out;
    }


    public static Object[] examStats(int examID){
        /**
         * Bereken de statistieken voor een toets voor gebruik in het vergelijk scherm.
         * Open een connectie met de database en haal de behaalde scores, cesuur en maximum van de
         * meegegeven toets op. Sluit de connectie. Bereken de cijfer met behulp van de updateGradeTableArray
         * functie en haal de cijfers eruit met behulp van de getColumn functie. Maak een Object array aan
         * en vul deze met de volgende waardes:
         *  - het gemiddelde cijfer, afgerond op een plaats na de komma (double)
         *  - het aantal deelnemers (int)
         *  - het aantal onvoldoendes (int)
         *  - het aantal voldoendes (int)
         *  - het rendement, afgerond op twee plaatsen na de komma (double)
         * Geef de Object array terug.
         */
        DatabaseConn d = new DatabaseConn();
        String[][] points = d.GetStudentScores(examID);
        Integer[] thresholdMaxGeuss = d.GetCesuurMaxGok(examID);
        d.CloseConnection();
        String[][] gradesTable = updateGradeTableArray(points, thresholdMaxGeuss[0], thresholdMaxGeuss[1]);
        double[] grades = stringToDoubleArray(getColumn(1, gradesTable),0);
        Object[] out = new Object[5];
        out[0] = round(mean(grades), 1); //gemiddelde cijfer
        out[1] = grades.length; //aantal deelnemers
        out[2] = getFails(grades); //aantal onvoldoendes
        out[3] = getPasses(grades); //aamtal voldoendes
        out[4] = round(percentage((int) out[3], (int) out[1]), 2); //rendement
        return out;
    }

    public static Object[] courseStats(String courseID){
        /**
         * Deze methode berekend de statistieken van een module.
         * Er wordt een CourseStatsCalculator instantie gemaakt voor de module
         * die het gemiddelde cijfer berekend. Ook wordt er dit object gebruikt
         * om lijsten van deelnemers en voldoendes op te halen, waarvan de
         * lengtes gebruikt wordt om de aantallen deelnemers, voldoendes en
         * onvoldoendes te bepalen. De volgende waardes worden in de volgende
         * volgorde als Object[] terug gegeven.
         * - het gemiddelde cijfer, afgerond op een plaats na de komma (double)
         * - het aantal deelnemers (int)
         * - het aantal onvoldoendes (int)
         * - het aantal voldoendes (int)
         * - het rendement, afgerond op twee plaatsen na de komma (double)
         */
        CourseStatsCalculator calculator = new CourseStatsCalculator(courseID);
        Object[] out = new Object[5];
        out[0] = calculator.getAverageGrade();
        out[1] = calculator.getParticipant().size();
        out[3] = calculator.getPasses().size();
        out[2] = (int) out[1] - (int) out[3];
        System.out.println(Arrays.deepToString(out));
        out[4] = round(percentage((int) out[3], (int) out[1]), 2);
        return out;
    }

    public static Object[] periodStats(String selectedYear, String selectedSchoolYear, String selectedBlock){
        /**
         * Deze methode bereken de statistieken voor en periode.
         * De modules van de periode worden opgehaald met behulp van de de meegegeven
         * waardes. Vervolgens wordt er voor iedere module een instantie aangemaakt
         * van CourseStatsCalculator die de statistieken van de module bepaald.
         * De student IDs van degene die aan de gehele module mee hebben gedaan
         * worden als Set toegevoegd aan een List van Sets genaamd participants.
         * Hetzelfde gebeurt voor degene die de gehele module gehald hebben in de List
         * van Sets passes. Tegelijkertijd worden het aantal cijfer dat behaald zijn
         * in de periode en de som van de cijfers bepaald.
         * All deze waardes worden aal determinePeriodStats meegegeven. Deze methode
         * berekend de getallen uit en geeft ze terug in een Object[] die ook door
         * deze methode terug gegeven wordt.
         * De output bevat de volgende waardes in de volgende volgorde:
         * - het gemiddelde cijfer, afgerond op een plaats na de komma (double)
         * - het aantal deelnemers (int)
         * - het aantal onvoldoendes (int)
         * - het aantal voldoendes (int)
         * - het rendement, afgerond op twee plaatsen na de komma (double)
         */
        DatabaseConn d = new DatabaseConn();
        List<String> courses = d.getCourses(selectedYear, selectedSchoolYear, selectedBlock);
        d.CloseConnection();
        List<Set<String>> passes = new ArrayList();
        List<Set<String>> participants = new ArrayList();
        double sum = 0;
        int number = 0;
        for (String course: courses){
            CourseStatsCalculator courseStats = new CourseStatsCalculator(course);
            passes.add(courseStats.getPasses());
            participants.add(courseStats.getParticipant());
            for (String key: courseStats.finalGrades.keySet()){
                for (Double value: courseStats.finalGrades.get(key)){
                    sum += value;
                    ++number;
                }
            }
        }
        return determinePeriodStats(sum, number, passes, participants);
    }

    private static Object[] determinePeriodStats(double sum, int number, List<Set<String>> passes,
                                                 List<Set<String>> participants) {
        /**
         * Deze methode bepaald de statistieken voor een periode met de meegegeven waardes.
         * De volgende waardes worden bepaald en terug gegeven:
         * - gemiddelde cijfer: sum wordt door number gedeelt en afgerond op 1 plaats.
         * - aantal deelnemers: de grootte van de Set die multipleIntersect terug geeft voor
         *      de List van Sets participants.
         * - aantal voldoendes (deze waarde wordt op index 3 gezet aangezien dit de volgorde
         *      is van de waardes in het scherm en wordt voor index 2 berekend omdat de uitkomst
         *      nodig is voor het bereken van index 2): hier wordt hetzelfde uitgevoerd als
         *      bij het aantal deelnemers, maar met passes.
         * - aantal onvoldoendes: aantal deelnemers min het aantal voldoendes
         * - rendement: het percentage van voldoendes, afegerond op 2 plaatsen.
         */
        Object[] out = new Object[5];
        out[0] = round(sum/number, 1);
        out[1] = multipleSetIntersect(participants).size();
        out[3] = multipleSetIntersect(passes).size();
        out[2] = (int) out[1] - (int) out[3];
        out[4] = round(percentage((int) out[3], (int) out[1]), 2);
        return out;
    }

    public static <T> Set<T> multipleSetIntersect(List<Set<T>> setList){
        /**
         * Deze methode bepaald welke waardes er in iedere Set in setList voorkomen.
         * Eerst wordt de eerste Set in setList apart genomen en een Set out aangemaakt.
         * Als firstSet leeg is wordt er een lege Set terug gegeven.
         * Vervolgens wordt er door die eerste Set heen geloopd en voor iedere andere set
         * gecontroleerd of de waarde ook in iedere andere Set zit. Als de waarde niet in
         * een van de Sets zit wordt de boolean include op false bezet wordt er gestopt
         * met controleren. Zodra alle Sets gecontroleerd zijn wordt er gekeken of include
         * op true of false staat, als die true is wordt de waarde aan de Set out
         * toegevoegd. Zodra alle waardes gecontroleerd zijn wordt out terug gegeven, deze
         * Set bevat nu alle waardes die in iedere Set in setList zat.
         */
        Set<T> firstSet;
        Set<T> out = new HashSet();
        try {
            firstSet = setList.get(0);
        } catch (IndexOutOfBoundsException e) {
            return out;
        }
        for (T value: firstSet) {
            boolean include = true;
            for (int i = 1; i < setList.size(); ++i) {
                if (! (setList.get(i).contains(value))) {
                    include = false;
                    break;
                }
            }
            if (include) {
                out.add(value);
            }
        }
        return out;
    }
}
