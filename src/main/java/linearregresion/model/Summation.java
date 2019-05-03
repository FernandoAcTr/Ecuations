package linearregresion.model;

import java.util.List;

public class Summation {

    private List<XYPoint> listXY;
    private float xSum = 0;
    private float ySum = 0;
    private float xySum = 0;
    private float squareXSum = 0;
    private float Ym = 0;
    private float Xm = 0;
    private float STSum = 0;
    private float SRSum = 0;

    public Summation(List<XYPoint> listXY) {
        this.listXY = listXY;
        for(XYPoint xyPoint : listXY) {
            xSum += xyPoint.getX();
            ySum += xyPoint.getY();
            xySum += (xyPoint.getX() * xyPoint.getY());
            squareXSum += Math.pow(xyPoint.getX(), 2);
        }

        Xm = xSum / listXY.size();
        Ym = ySum / listXY.size();
    }

    public float getXSum(){
       return xSum;
    }

    public float getYSum(){
        return ySum;
    }

    public float getXYSum() {
        return xySum;
    }

    public float getSquareXSum() {
        return squareXSum;
    }

    public float getYm() {
        return Ym;
    }

    public float getXm() {
        return Xm;
    }

    public float getSTSum() {
        return STSum;
    }

    public float getSRSum() {
        return SRSum;
    }

    public float getA1(){
        int n = listXY.size();
        return (n * xySum - xSum * ySum) / (n * squareXSum - (float)Math.pow(xSum, 2));
    }

    public float getA0(){
        return Ym - getA1() * Xm;
    }

    public float getR(){
        return (float)Math.sqrt((STSum - SRSum) / STSum);
    }

    public void setSTSum(){
        STSum = 0;
        for(XYPoint xyPoint : listXY)
            STSum += xyPoint.getFloatST();
    }

    public void setSRSum(){
        SRSum = 0;
        for(XYPoint xyPoint : listXY)
            SRSum += xyPoint.getFloatSR();
    }
}
