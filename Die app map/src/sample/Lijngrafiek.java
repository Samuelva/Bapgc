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
public class Lijngrafiek {
    private String xLabel;
    private String yLabel;
    private String title;
    private LineChart<String, Number> lineChart;
    private VBox chartBox;

    public Lijngrafiek(String setxLabel, String setyLabel, String setTitle) {
        xLabel = setxLabel;
        yLabel = setyLabel;
        title = setTitle;
        makeLineChart();
    }

    public void makeLineChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<String, Number>(xAxis, yAxis);
        lineChart.setTitle(title);

        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
    }

    public void addLine(String[] xValues, int[] yValues, String name) {
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        for (int i=0; i<xValues.length; i++) {
            series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
        }

        lineChart.getData().add(series);
    }

    public VBox getLineChart() {
        chartBox = new VBox();
        chartBox.getChildren().addAll(lineChart);
        chartBox.setVgrow(lineChart, Priority.ALWAYS);
        return chartBox;
    }
}
