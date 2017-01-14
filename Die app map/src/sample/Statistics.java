package sample;


import java.math.BigDecimal;
import java.math.RoundingMode;

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
        return diffsum/values.length;
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
        return diffsum/values.length;
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
    public static void test(){
        int[] testArray1 = {0, 10, 20, -20};
        double[] testArray2 = {0.0, 10.0, 20.0, -20.0};
        System.out.println(sum(testArray1));
        System.out.println(sum(testArray2));
        System.out.println(mean(testArray1));
        System.out.println(mean(testArray2));
        System.out.println(var(testArray1));
        System.out.println(var(testArray2));
        System.out.println(sd(testArray1));
        System.out.println(sd(testArray2));
        System.out.println(percentage(50, 100));
        System.out.println(percentage(50, 100.0));
        System.out.println(percentage(50.5, 100));
        System.out.println(percentage(50.5, 100.0));
        System.out.println(round(3.141597653589793, 2));
        System.out.println(grade(new int[] {20}, 27, 100));
        System.out.println(grade(new int[] {80}, 27, 100));
        System.out.println(grade(new int[] {100}, 27, 100));
        System.out.println(max(testArray1));
        System.out.println(max(testArray2));
        System.out.println(min(testArray1));
        System.out.println(min(testArray2));
    }
}
