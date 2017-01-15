package sample;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Created by Samuel on 15-1-2017.
 */
public class Histogram {
    private String xLabel;
    private String yLabel;
    private String title;
    private String name;
    private BarChart<String, Number> barChart;
    private VBox chartBox;
    private XYChart.Series series;

    public Histogram(String setxLabel, String setyLabel, String setTitle, String setName) {
        xLabel = setxLabel;
        yLabel = setyLabel;
        title = setTitle;
        name = setName;
        makeBarChart();
    }

    public void makeBarChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);

        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);

        series = new XYChart.Series();
        series.setName(name);
        barChart.getData().add(series);
    }

    public void addBar(String xValue, int yValue) {
        series.getData().add(new XYChart.Data(xValue, yValue));
    }

    public VBox getBarChart() {
        chartBox = new VBox();
        chartBox.getChildren().addAll(barChart);
        chartBox.setVgrow(barChart, Priority.ALWAYS);
        return chartBox;
    }

    public void addLine(String[] xValues, int[] yValues, String lineName) {
        XYChart.Series series = new XYChart.Series();
        series.setName(lineName);

        for (int i=0; i<xValues.length; i++) {
            series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
        }

        barChart.getData().add(series);
    }


}
