package sample;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Davy on 9-1-2017.
 */
public class Statistics {

    /* Bereken de som van een int array.
     */
    public static int sum(int[] values){
        int total = 0;
        for (int i: values){
            total += i;
        }
        return total;
    }

    /* Bereken de som van een double array.
     */
    public static double sum(double[] values){
        double total = 0;
        for (double i: values){
            total += i;
        }
        return total;
    }

    /* Bereken het gemiddelde van een int array.
     */
    public static double mean(int[] values){
        double total = (double) sum(values);
        return total/values.length;
    }

    /* Bereken het gemiddelde van een double array.
     */
    public static double mean(double[] values){
        double total = sum(values);
        return total/values.length;
    }

    /* Bereken de variantie van een int array.
     */
    public static double var(int[] values){
        double mean = mean(values);
        double diffsum = 0;
        for (int i: values) {
            double diff = i - mean;
            diff *= diff;
            diffsum += diff;
        }
        return diffsum/(values.length-1);
    }

    /* Bereken de variantie van een double array.
     */
    public static double var(double[] values){
        double mean = mean(values);
        double diffsum = 0;
        for (double i: values) {
            double diff = i - mean;
            diff *= diff;
            diffsum += diff;
        }
        return diffsum/(values.length-1);
    }

    /* Bereken de standaard deviatie van een int array.
     */
    public static double sd(int[] values){
        double var = var(values);
        return Math.sqrt(var);
    }

    /* Bereken de standaard deviate van een double array.
     */
    public static double sd(double[] values){
        double var = var(values);
        return Math.sqrt(var);
    }

    /* Bereken het percentage dat 'value' is van 'max' waarbij beide int zijn.
     */
    public static double percentage(int value, int max){
        if (!(max > 0)) {
            throw new IllegalArgumentException("max should be higher than 0.");
        }
        return value/ (double) max * 100;
    }

    /* Bereken het percentage dat 'value' is van 'max' waarbij 'value' een int is een 'max' een double.
     */
    public static double percentage(int value, double max){
        if (!(max > 0)) {
            throw new IllegalArgumentException("max should be higher than 0.");
        }
        return value/max * 100;
    }

    /* Bereken het percentage dat 'value' is van 'max' waarbij 'value' een double is een 'max' een int.
     */
    public static double percentage(double value, int max){
        if (!(max > 0)) {
            throw new IllegalArgumentException("max should be higher than 0.");
        }
        return value/max * 100;
    }

    /* Bereken het percentage dat 'value' is van 'max' waarbij beide double zijn.
     */
    public static double percentage(double value, double max){
        if (!(max > 0)) {
            throw new IllegalArgumentException("max should be higher than 0.");
        }
        return value/max * 100;
    }

    /* Rond een waarde 'value' af tot 'places' plaatsen.
     */
    public static double round(double value, int places) {
        BigDecimal value2 = new BigDecimal(value);
        value2 = value2.setScale(places, RoundingMode.HALF_UP);
        return value2.doubleValue();
    }

    /* Bereken het cijfer voor een int array van punten 'values' met behulp van een maximum aantal punten
       'max' en een censuur 'threshold'. Deze cijfer worden als doubles terug gegeven, afgerond op 1 cijfer
       na de komma.
     */
    public static double grade(int[] values, int threshold, int max){
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

    /* Bepaal de hoogste waarde in een int array 'values'.
     */
    public static int max(int[] values){
        int out = values[0];
        for (int i: values){
            if (i > out){
                out = i;
            }
        }
        return out;
    }

    /* Bepaal de hoogste waarde in een double array 'values'.
     */
    public static double max(double[] values){
        double out = values[0];
        for (double i: values){
            if (i > out){
                out = i;
            }
        }
        return out;
    }

    /* Bepaal de laagste waarde in een int array 'values'.
     */
    public static int min(int[] values){
        int out = values[0];
        for (int i: values){
            if (i < out){
                out = i;
            }
        }
        return out;
    }

    /* Bepaal de laagste waarde in een double array 'values'.
     */
    public static double min(double[] values){
        double out = values[0];
        for (double i: values){
            if (i < out){
                out = i;
            }
        }
        return out;
    }

    /* Verander een String[] naar en int[]. Skip de eerste 'skip' waardes.
     */
    public static int[] stringToIntArray(String[] values, int skip){
        int[] out = new int[values.length-skip];
        for (int i = 0; i < values.length-skip; i++){
            out[i] = Integer.parseInt(values[i+skip]);
        }
        return out;
    }

    /* Verander een String[] naar en double[]. Skip de eerste 'skip' waardes.
     */
    public static double[] stringToDoubleArray(String[] values, int skip){
        double[] out = new double[values.length-skip];
        for (int i = 0; i < values.length-skip; i++){
            out[i] = Double.parseDouble(values[i+skip]);
        }
        return out;
    }

    /* Voeg het cijfer en totaal aantal punten toe aan de Strig[][] die per student het aantal punten
       per vraag bevat.
     */
    public static String[][] updateGradeTableArray(String[][] original, int threshold, int max){
        String[][] out = new String[original.length][original[0].length+2];
        for (int i = 0; i < original.length; i++){
            out[i][0] = original[i][0];
            int[] points = stringToIntArray(original[i], 1);
            out[i][1] = Double.toString(grade(points, threshold, max));
            out[i][2] = Integer.toString(sum(points));
            for (int x = 1; x < original.length; x++){
                out[i][x+2] = original[i][x];
            }
        }
        return out;
    }

    /* Tel hoe vaak 'query' voorkomt in 'list'.
     */
    public static int count(int query, int[] list){
        int out = 0;
        for (int i: list){
            if (i == query){
                out++;
            }
        }
        return out;
    }

    /* Tel hoeveel mensen er een voldoende hebben in een lijst 'values' van cijfers.
     */
    public static int getPasses(double[] values){
        int out = 0;
        for (double i: values){
            if (i >= 5.5){
                out++;
            }
        }
        return out;
    }

    /* Tel hoeveel mensen er een onvoldoende hebben in een lijst 'values' van cijfers.
     */
    public static int getFails(double[] values) {
        int out = 0;
        for (double i : values) {
            if (i <= 5.5) {
                out++;
            }
        }
        return out;
    }

    /* Bepaal de kth percentiel in 'values'.
     */
    public static double kthPercentile(double k, int[] values){
        if (k < 0 || k > 1){
            throw new IllegalArgumentException("k must be between 0 and 1");
        }
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

    /* Bepaal de kth percentiel in 'values'.
     */
    public static double kthPercentile(double k, double[] values){
        if (k < 0 || k > 1){
            throw new IllegalArgumentException("k must be between 0 and 1");
        }
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

    /* Maak een int[] met alle waardes uit 'values' die hoger zijn dan 'value'.
     */
    public static int[] getBiggerThan(int value, int[] values){
        List<Integer> out = new ArrayList<Integer>();
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

    /* Maak een int[] met alle waardes uit 'values' die hoger zijn dan 'value'.
     */
    public static int[] getBiggerThan(double value, int[] values){
        List<Integer> out = new ArrayList<Integer>();
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

    /* Maak een double[] met alle waardes uit 'values' die hoger zijn dan 'value'.
     */
    public static double[] getBiggerThan(int value, double[] values){
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

    /* Maak een double[] met alle waardes uit 'values' die hoger zijn dan 'value'.
     */
    public static double[] getBiggerThan(double value, double[] values){
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

    /* Bereken de som van de varianties van de vragen.
     */
    public static double varianceQuestions(String[][] values){
        int num = values[0].length - 1;

        double[] vars = new double[num];

        for (int i = 0; i < num; i++){
            String[] points = new String[values.length];
            for (int x = 0; x < values.length; x++){
                points[x] = values[x][i+1];
            }
            System.out.println(Arrays.toString(points));
            vars[i] = var(stringToIntArray(points, 0));
        }
        System.out.println(Arrays.toString(vars));
        return sum(vars);
    }

    /* Bereken de correlatie tusen twee int[].
     */
    public static double correlation(int[] values1, int[] values2) {
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

    /* Bereken de gemiddelde score van een percentiel.
     */
    public static double percentileMean(int[] values, double percentile){
        double per = kthPercentile(percentile, values);
        int[] values2 = getBiggerThan(per, values);
        return mean(values2);
    }

    /* Bereken de cronbach alfa.
     */
    public static double cronbach(int n, double questionsVar, double examVar){
        return n / (n-1) * (1 - questionsVar/examVar);
    }

    /* Bereken de Cohen-Schotanus censuur.
     */
    public static double cohen(double meanPoints, double mastery, double chancePoints){
        return (meanPoints-chancePoints) * mastery + chancePoints;
    }
}
