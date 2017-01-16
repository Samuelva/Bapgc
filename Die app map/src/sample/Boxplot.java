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
public class Boxplot {
    public VBox vBox;
    CandleStickChart chart;

    public Boxplot() {

    }

    private static double[][] data = new double[][]{
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
            {17, 26, 30, 42, 18, 27.3}
    };

    public VBox makeBoxPlot() {
        NumberAxis xAxis;
        NumberAxis yAxis;

        xAxis = new NumberAxis();
        xAxis.setMinorTickCount(0);
        yAxis = new NumberAxis();
        chart = new CandleStickChart(xAxis,yAxis);
        // setup chart
        xAxis.setLabel("Day");
        yAxis.setLabel("Price");
        // add starting data

        vBox = new VBox();
        vBox.getChildren().addAll(chart);
        VBox.setVgrow(chart, Priority.ALWAYS);
        return vBox;
    }

    public void addData() {
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        for (int i=0; i< data.length; i++) {
            double[] day = data[i];
            series.getData().add(
                    new XYChart.Data<>(day[0],day[1],new CandleStickExtraValues(day[2],day[3],day[4],day[5]))
            );
        }
        ObservableList<XYChart.Series<Number,Number>> data2 = chart.getData();
        if (data2 == null) {
            data2 = FXCollections.observableArrayList(series);
            chart.setData(data2);
        } else {
            chart.getData().add(series);
        }
    }
}
