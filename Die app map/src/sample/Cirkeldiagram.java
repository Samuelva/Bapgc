package sample;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Created by Samuel on 15-1-2017.
 */
public class Cirkeldiagram {
    private String title;
    private VBox chartBox;
    ObservableList<PieChart.Data> pieChartData;

    public Cirkeldiagram(String setTitle) {
        title = setTitle;
        pieChartData = observableArrayList();
    }

    public VBox makePieChart() {
        final PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Taartgrafiek");
        chartBox = new VBox();
        chartBox.getChildren().add(pieChart);
        chartBox.setVgrow(pieChart, Priority.ALWAYS);
        return chartBox;
    }

    public void addData(String[] names, int[] values) {
        for (int i=0; i<names.length; i++) {
            pieChartData.add(new PieChart.Data(names[i], values[i]));
        }
    }
}
