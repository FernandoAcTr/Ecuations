package polynomialregresion.model;

import utils.MyUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Summation {
    private List<XYPoint> listPoints;
    
    public Summation(List<XYPoint> listPoints){
        this.listPoints = listPoints;
    }

    public double getXSum(){
        double acum = 0;
        for(XYPoint point : listPoints)
            acum += point.getX();

        return acum;
    }

    public double getYSum(){
        double acum = 0;
        for(XYPoint point : listPoints)
            acum += point.getY();

        return acum;
    }

    public double getYMedia(){
        double acum = 0;
        for(XYPoint point : listPoints)
            acum += point.getY();

        return acum / listPoints.size();
    }
    
    public double getXPotenceSum(int potence){
        double acum = 0;
        for(XYPoint point : listPoints)
            acum += Math.pow(point.getX(), potence);
        
        return acum;
    }
    
    public double getXYPotenceSum(int potence){
        double acum = 0;
        for(XYPoint point : listPoints) 
            acum += Math.pow(point.getX(), potence) * point.getY();
        
        return acum;
    }
    
    public ArrayList<String> getXPotenceValues(int potence){
        ArrayList<String> values = new ArrayList<>();
        double value;
        for(XYPoint point : listPoints) {
            value = Math.pow(point.getX(), potence);
            values.add(MyUtils.format(value));
        }
        
        return values;
    }

    public ArrayList<String> getXYPotenceValues(int potence){
        ArrayList<String> values = new ArrayList<>();
        double value;
        for(XYPoint point : listPoints) {
            double x = point.getX();
            double y = point.getY();
            value = Math.pow(point.getX(), potence) * point.getY();
            values.add(MyUtils.format(value));
        }

        return values;
    }

    public ArrayList<String> getSrValues(double[] coefficients){
        ArrayList<String> values = new ArrayList<>();
        float Yi;
        float Xi;

        for (XYPoint point : listPoints){
            Yi = point.getY();
            Xi = point.getX();
            for (int i = 0; i < coefficients.length; i++)
                Yi -= coefficients[i] * Math.pow(Xi, i);

            values.add(MyUtils.format(Math.pow(Yi, 2)));
        }
        return values;
    }


    public ArrayList<String> getStValues(double YMedia){
        ArrayList<String> values = new ArrayList<>();
        double value;
        for (XYPoint point : listPoints){
            value = point.getY() - YMedia;
            values.add(MyUtils.format(Math.pow(value, 2)));
        }

        return values;
    }

    public double getValuesSum(ArrayList<String> values){
        double acum = 0;
        for(String value : values)
            acum += Double.parseDouble(value);

        return acum;
    }

    public double getR(double St, double Sr){
        return Math.sqrt((St-Sr)/St);
    }

    public double[][] getEcuations(int grade){
        double ecuations[][] = new double[grade+1][grade+2];
        int column, row, potence;

        //formar la primera fila que es la unica diferente
        ecuations[0][0] = listPoints.size();
        for (column = 1; column < grade + 1; column++)
           ecuations[0][column] = getXPotenceSum(column);

        ecuations[0][column] = getYSum();

        //formar las siguientes filas que tienen un patron mas estable
        for (row = 1; row < grade+1; row++) {
            for (column = 0, potence = row; column < grade + 1; column++, potence++) {
                ecuations[row][column] = getXPotenceSum(potence);
            }
            ecuations[row][column] = getXYPotenceSum(row);
        }

        return ecuations;
    }

    public String getPolynomialEcuation(double[] coeficients){
        String ecuation = "";
        ecuation += MyUtils.format(coeficients[0]);
        ecuation += "+" + MyUtils.format(coeficients[1]) + "x";

        for (int i = 2; i < coeficients.length; i++)
            ecuation += "+" + MyUtils.format(coeficients[i]) + "x^" + i;

        return ecuation;
    }

    public String getPolynomialEcuation(double[] coeficients, DecimalFormat formatter){
        String ecuation = "";
        ecuation += formatter.format(coeficients[0]);
        ecuation += "+" + formatter.format(coeficients[1]) + "x";

        for (int i = 2; i < coeficients.length; i++)
            ecuation += "+" + formatter.format(coeficients[i]) + "x^" + i;

        return ecuation;
    }
}
