package sample;

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
        int total = sum(values);
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

    public static void test(){
        int[] testArray1 = {0, 10};
        double[] testArray2 = {0.0, 10.0};
        System.out.println(sum(testArray1));
        System.out.println(sum(testArray2));
        System.out.println(mean(testArray1));
        System.out.println(mean(testArray2));
        System.out.println(var(testArray1));
        System.out.println(var(testArray2));
        System.out.println(sd(testArray1));
        System.out.println(sd(testArray2));
    }
}
