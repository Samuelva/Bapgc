package sample;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.chart.LineChart;

/**
 * Created by Samuel on 15-1-2017.
 * Deze klasse maakt een lijngrafiek voor de opgegeven data.
 */
public class LineGraph {
    private String xLabel;
    private String yLabel;
    private String title;
    private LineChart<String, Number> lineChart;
    private VBox chartBox;

    public LineGraph(String setxLabel, String setyLabel, String setTitle) {
        /**
         * Main functie, maakt de grafiek aan met de opgegeven x-as label,
         * y-as label en titel.
         */
        xLabel = setxLabel;
        yLabel = setyLabel;
        title = setTitle;
        makeLineChart();
    }

    public void makeLineChart() {
        /**
         * Maakt de lijngrafiek zonder lijn.
         */
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setLegendVisible(false);

        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
    }

    public void addLine(String[] xValues, double[] yValues, String name) {
        /**
         * Met deze functie kan er een lijn toegevoegd worden aan de grafiek
         * met legenda waarden.
         */
        XYChart.Series series = new XYChart.Series();
        series.setName(name);

        for (int i=0; i<xValues.length; i++) {
            series.getData().add(new XYChart.Data(xValues[i], yValues[i]));
        }

        lineChart.getData().add(series);
    }

    public VBox getLineChartBox() {
        /**
         * Returned de grafiek in een box.
         */
        chartBox = new VBox();
        chartBox.getChildren().addAll(lineChart);
        chartBox.setVgrow(lineChart, Priority.ALWAYS);
        return chartBox;
    }

    public LineChart<String, Number> getLineChart() {
        /**
         * Returned de variabele voor de lijngrafiek. Wordt gebruikt om de
         * grafiek op te slaan.
         */
        return lineChart;
    }
}
