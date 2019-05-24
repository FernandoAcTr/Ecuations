package multiplyregresion.model;

import java.util.ArrayList;
import java.util.List;

public class Summation {

    private List<MultiplePoint> listPoints;
    private List<Results> listResults;
    private float YSum;
    private float X1Sum;
    private float X2Sum;
    private float X1SquareSum;
    private float X2SquareSum;
    private float X1X2Sum;
    private float X1YSum;
    private float X2YSum;
    private float Ym;

    public Summation(List<MultiplePoint> listPoints) {
        this.listPoints = listPoints;
        listResults = new ArrayList<>();

        YSum = X1Sum = X2Sum = X1SquareSum = X2SquareSum = X1X2Sum = X1YSum = X2YSum = 0;

        float y, x1, x2, x1square, x2square, x1x2, x1y, x2y;

        for (MultiplePoint point : listPoints){
            y = point.getY();
            x1 = point.getX1();
            x2 = point.getX2();
            x1square = (float)Math.pow(x1, 2);
            x2square = (float)Math.pow(x2, 2);
            x1x2 = x1 * x2;
            x1y = x1 * y;
            x2y = x2 * y;

            YSum += y;
            X1Sum += x1;
            X2Sum += x2;
            X1SquareSum += x1square;
            X2SquareSum += x2square;
            X1X2Sum += x1x2;
            X1YSum += x1y;
            X2YSum += x2y;

            Results results = new Results();
            results.setX1Square(x1square);
            results.setX2Square(x2square);
            results.setX1X2(x1x2);
            results.setX1Y(x1y);
            results.setX2Y(x2y);

            listResults.add(results);
        }

        Ym = YSum/listPoints.size();
    }


    public List<MultiplePoint> getListPoints() {
        return listPoints;
    }

    public List<Results> getListResults() {
        return listResults;
    }

    public float getYSum() {
        return YSum;
    }

    public float getX1Sum() {
        return X1Sum;
    }

    public float getX2Sum() {
        return X2Sum;
    }

    public float getX1SquareSum() {
        return X1SquareSum;
    }

    public float getX2SquareSum() {
        return X2SquareSum;
    }

    public float getX1X2Sum() {
        return X1X2Sum;
    }

    public float getX1YSum() {
        return X1YSum;
    }

    public float getX2YSum() {
        return X2YSum;
    }

    public float getYm() {
        return Ym;
    }

    public float getR(float a0, float a1, float a2){
        float sr = 0, st = 0;

        float y, x1, x2;
        for (MultiplePoint p : listPoints){
            y = p.getY();
            x1 = p.getX1();
            x2 = p.getX2();

            sr += Math.pow(y - a0 - a1*x1 - a2*x2, 2);
            st += Math.pow(y - Ym, 2);
        }

        return (float)Math.sqrt((st-sr)/st);
    }



    public class Results{
        private float X1Square;
        private float X2Square;
        private float X1X2;
        private float X1Y;
        private float X2Y;

        // <editor-fold defaultstate="collapsed" desc=" getters() and setters() ">

        public float getX1Square() {
            return X1Square;
        }

        public void setX1Square(float x1Square) {
            X1Square = x1Square;
        }

        public float getX2Square() {
            return X2Square;
        }

        public void setX2Square(float x2Square) {
            X2Square = x2Square;
        }

        public float getX1X2() {
            return X1X2;
        }

        public void setX1X2(float x1X2) {
            X1X2 = x1X2;
        }

        public float getX1Y() {
            return X1Y;
        }

        public void setX1Y(float x1Y) {
            X1Y = x1Y;
        }

        public float getX2Y() {
            return X2Y;
        }

        public void setX2Y(float x2Y) {
            X2Y = x2Y;
        }
        // </editor-fold>
    }
}
