package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Created by Samuel on 11-12-2016.
 * Deze klasse maakt een boxplot met de opgegeven data.
 */
public class Boxplot {
    private VBox vBox;
    private CandleStickChart chart;
    private double[][] data;
    private String graphTitle;
    private String xLabel;
    private String yLabel;
    private boolean setXAxis;

    public Boxplot(double[][] dataI, String graphTitleI, String xLabelI,
                   String yLabelI, boolean setXAxisI) {
        /**
         * Main functie. Slaat de meegegeven data, x-as label, y-as label en
         * titel op.
         */
        data = dataI;
        xLabel = xLabelI;
        yLabel = yLabelI;
        setXAxis = setXAxisI;
        graphTitle = graphTitleI;
    }

    public VBox makeBoxPlot() {
        /**
         * Deze functie maakt de boxplot en returned deze in een box.
         */
        NumberAxis xAxis;
        NumberAxis yAxis;

        xAxis = new NumberAxis(0,data.length+1,1);
        xAxis.setTickLabelsVisible(setXAxis);
        yAxis = new NumberAxis();
        chart = new CandleStickChart(xAxis,yAxis);
        chart.setTitle(graphTitle);

        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);

        vBox = new VBox();
        vBox.getChildren().addAll(chart);
        VBox.setVgrow(chart, Priority.ALWAYS);
        return vBox;
    }

    public void addData() {
        /**
         * Met deze functie kan er een boxplot toegevoegd worden aan de grafiek.
         */
        XYChart.Series<Number,Number> series = new XYChart.Series<Number,Number>();
        for (int i = 0; i < data.length; i++) {
            double[] day = data[i];
            series.getData().add(
                    new XYChart.Data<>(day[0], day[1] ,new
                            CandleStickExtraValues(day[2],day[3],day[4],day[5]))
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

    public CandleStickChart getBoxPlot() {
        /**
         * Returned de boxplot variabele. Wordt gebruikt om de grafiek te
         * kunnen opslaan.
         */
        return chart;
    }
}
