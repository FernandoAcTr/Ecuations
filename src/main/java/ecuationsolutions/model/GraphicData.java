package ecuationsolutions.model;

import javafx.scene.chart.XYChart;


public class GraphicData {

    public GraphicData() {

    }

    public XYChart.Series getSerie(String serieName, double xValues[], double yValues[]){

        int numValues = xValues.length;
        XYChart.Series<Double, Double> serie = new XYChart.Series<Double, Double>();
        serie.setName(serieName);

        for (int i = 0; i < numValues; i++)
            serie.getData().add(new XYChart.Data<Double, Double>(xValues[i], yValues[i]));

        return serie;
    }

}
