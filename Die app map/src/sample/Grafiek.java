package sample;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Created by Samuel on 11-12-2016.
 */
public class Grafiek {
    int DATA_SIZE = 1000;
    int data[] = new int[DATA_SIZE];
    int group[] = new int[10];

    VBox graphBox;
    VBox vBox;
    String xLabel;
    String yLabel;
    String title;

    public Grafiek() {

    }
    public VBox pieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Onvoldoendes", 30),
                new PieChart.Data("Voldoendes", 70)
        );

        final PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Taartgrafiek");
        vBox = new VBox();
        vBox.getChildren().add(pieChart);
        vBox.setVgrow(pieChart, Priority.ALWAYS);
        return vBox;
    }

    public VBox lineChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<String, Number> lineChart =
                new LineChart<String, Number>(xAxis, yAxis);

        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
        XYChart.Series series = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series.setName(title);

        String[] xValues = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        int[] yValues = new int[] {6, 5, 4, 6, 7, 5, 4, 6, 7};

        for (int i=0; i<xValues.length; i++) {
            series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
        }
        for (int i=0; i<xValues.length; i++) {
            series2.getData().add(new XYChart.Data(xValues[i], yValues[i]+2));
        }
//        series.getData().add(new XYChart.Data(1, 23));
//        series.getData().add(new XYChart.Data(2, 14));
//        series.getData().add(new XYChart.Data(3, 15));
//        series.getData().add(new XYChart.Data(4, 24));
//        series.getData().add(new XYChart.Data(5, 34));
//        series.getData().add(new XYChart.Data(6, 36));
//        series.getData().add(new XYChart.Data(7, 22));
//        series.getData().add(new XYChart.Data(8, 45));
//        series.getData().add(new XYChart.Data(9, 43));
//        series.getData().add(new XYChart.Data(10, 17));
//        series.getData().add(new XYChart.Data(11, 29));
//        series.getData().add(new XYChart.Data(12, 25));

        lineChart.getData().add(series);
        lineChart.getData().add(series2);
        vBox = new VBox();
        vBox.getChildren().addAll(lineChart);
        vBox.setVgrow(lineChart, Priority.ALWAYS);
        return vBox;
    }

    public VBox barChart() {
        prepareData();
        groupData();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> barChart =
                new BarChart<>(xAxis,yAxis);

        barChart.setCategoryGap(0);
        barChart.setBarGap(0);
        barChart.setAnimated(true);

        xAxis.setLabel("test x-as");
        yAxis.setLabel("test y-as");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Histogram");
        series1.getData().add(new XYChart.Data("0-10", group[0]));
        series1.getData().add(new XYChart.Data("10-20", group[1]));
        series1.getData().add(new XYChart.Data("20-30", group[2]));
        series1.getData().add(new XYChart.Data("30-40", group[3]));
        series1.getData().add(new XYChart.Data("40-50", group[4]));

        series1.getData().add(new XYChart.Data("50-60", group[5]));
        series1.getData().add(new XYChart.Data("60-70", group[6]));
        series1.getData().add(new XYChart.Data("70-80", group[7]));
        series1.getData().add(new XYChart.Data("80-90", group[8]));
        series1.getData().add(new XYChart.Data("90-100", group[9]));

        barChart.getData().addAll(series1);

        vBox = new VBox();
        vBox.getChildren().addAll(barChart);
        VBox.setVgrow(barChart, Priority.ALWAYS);
        return vBox;
    }

    public VBox returnGraph() {
        return vBox;
    }

    //generate dummy random data
    private void prepareData(){

        Random random = new Random();
        for(int i=0; i<DATA_SIZE; i++){
            data[i] = random.nextInt(100);

        }
    }

    //count data population in groups
    private void groupData(){
        for(int i=0; i<10; i++){
            group[i]=0;
        }
        for(int i=0; i<DATA_SIZE; i++){
            if(data[i]<=10){
                group[0]++;
            }else if(data[i]<=20){
                group[1]++;
            }else if(data[i]<=30){
                group[2]++;
            }else if(data[i]<=40){
                group[3]++;
            }else if(data[i]<=50){
                group[4]++;
            }else if(data[i]<=60){
                group[5]++;
            }else if(data[i]<=70){
                group[6]++;
            }else if(data[i]<=80){
                group[7]++;
            }else if(data[i]<=90){
                group[8]++;
            }else if(data[i]<=100){
                group[9]++;
            }
        }
    }

    private static double[][] data2 = new double[][]{
            {1,  25, 20, 32, 16, 20},
            {2,  26, 30, 33, 22, 25},
            {3,  30, 38, 40, 20, 32},
            {4,  24, 30, 34, 22, 30},
            {5,  26, 36, 40, 24, 32},
            {6,  28, 38, 45, 25, 34},
            {7,  36, 30, 44, 28, 39},
            {8,  30, 18, 36, 16, 31},
            {9,  40, 50, 52, 36, 41},
            {10, 30, 34, 38, 28, 36},
            {11, 24, 12, 30, 8,  32.4},
            {12, 28, 40, 46, 25, 31.6},
            {13, 28, 18, 36, 14, 32.6},
            {14, 38, 30, 40, 26, 30.6},
            {15, 28, 33, 40, 28, 30.6},
            {16, 25, 10, 32, 6,  30.1},
            {17, 26, 30, 42, 18, 27.3},
            {18, 20, 18, 30, 10, 21.9},
            {19, 20, 10, 30, 5,  21.9},
            {20, 26, 16, 32, 10, 17.9},
            {21, 38, 40, 44, 32, 18.9},
            {22, 26, 40, 41, 12, 18.9},
            {23, 30, 18, 34, 10, 18.9},
            {24, 12, 23, 26, 12, 18.2},
            {25, 30, 40, 45, 16, 18.9},
            {26, 25, 35, 38, 20, 21.4},
            {27, 24, 12, 30, 8,  19.6},
            {28, 23, 44, 46, 15, 22.2},
            {29, 28, 18, 30, 12, 23},
            {30, 28, 18, 30, 12, 23.2},
            {31, 28, 18, 30, 12, 22}
    };

    public VBox Boxplot() {
        CandleStickChart chart;
        NumberAxis xAxis;
        NumberAxis yAxis;

        xAxis = new NumberAxis(0,32,1);
        xAxis.setMinorTickCount(0);
        yAxis = new NumberAxis();
        chart = new CandleStickChart(xAxis,yAxis);
        // setup chart
        xAxis.setLabel("Day");
        yAxis.setLabel("Price");
        // add starting data
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        for (int i=0; i< data2.length; i++) {
            double[] day = data2[i];
            series.getData().add(
                    new XYChart.Data<Number,Number>(day[0],day[1],new CandleStickExtraValues(day[2],day[3],day[4],day[5]))
            );
        }
        ObservableList<XYChart.Series<Number,Number>> data2 = chart.getData();
        if (data2 == null) {
            data2 = FXCollections.observableArrayList(series);
            chart.setData(data2);
        } else {
            chart.getData().add(series);
        }
        vBox = new VBox();
        vBox.getChildren().addAll(chart);
        VBox.setVgrow(chart, Priority.ALWAYS);
        return vBox;
    }



//    @Override public void start(Stage primaryStage) throws Exception {
//        primaryStage.setScene(new Scene(createContent()));
//        primaryStage.show();
//    }

}
