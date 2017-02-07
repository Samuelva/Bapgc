package sample;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Created by Samuel on 15-1-2017.
 * Deze klasse maakt een histogram.
 */
public class Barchart {
    private String xLabel;
    private String yLabel;
    private String title;
    private String name;
    private BarChart<String, Number> barChart;
    private VBox chartBox;
    private XYChart.Series series;

    public Barchart(String setxLabel, String setyLabel, String setTitle, String
            setName) {
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
        barChart.setLegendVisible(false);

        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);

        series = new XYChart.Series();
        barChart.getData().add(series);
    }

    public void addBar(String xValue, double yValue) {
        series.getData().add(new XYChart.Data(xValue, yValue));
    }

    public VBox getBarChartBox() {
        chartBox = new VBox();
        chartBox.getChildren().addAll(barChart);
        chartBox.setVgrow(barChart, Priority.ALWAYS);
        return chartBox;
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }
}