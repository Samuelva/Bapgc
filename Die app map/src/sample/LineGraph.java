package sample;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.chart.LineChart;

/**
 * Created by Samuel on 15-1-2017.
 */
public class LineGraph {
    private String xLabel;
    private String yLabel;
    private String title;
    private LineChart<String, Number> lineChart;
    private VBox chartBox;

    public LineGraph(String setxLabel, String setyLabel, String setTitle) {
        xLabel = setxLabel;
        yLabel = setyLabel;
        title = setTitle;
        makeLineChart();
    }

    public void makeLineChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setLegendVisible(false);

        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
    }

    public void addLine(String[] xValues, double[] yValues, String name) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        for (int i=0; i<xValues.length; i++) {
            series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
        }

        lineChart.getData().add(series);
    }

    public VBox getLineChartBox() {
        chartBox = new VBox();
        chartBox.getChildren().addAll(lineChart);
        chartBox.setVgrow(lineChart, Priority.ALWAYS);
        return chartBox;
    }

    public LineChart<String, Number> getLineChart() {
        return lineChart;
    }
}