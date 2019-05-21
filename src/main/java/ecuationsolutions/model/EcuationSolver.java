package ecuationsolutions.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.MyUtils;

public class EcuationSolver {

    private double errorPermited;
    private double Xr;
    private ObservableList<ValuesBean> listValues;
    private Function function;   

    public EcuationSolver() {
        listValues = FXCollections.observableArrayList();
    }

    public EcuationSolver(Function function) {
        this.function = function;
        listValues = FXCollections.observableArrayList();
    }

    /**
     * Verifica que exista un cambio de signo en la evualacion para punto A y punto B
     * @return true si existe cambio de signo.
     */
    public boolean verifyCloseMethods(double pointA, double pointB){
        try {
            double x1 = function.evaluateFrom(pointA);
            double x2 = function.evaluateFrom(pointB);

            if(x1 > 0 && x2 < 0)
                return true;
            else if(x2 > 0 && x1 < 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Resuelve la funcion por medio del metodo de biseccion
     */
    public void solveByBiseccion(double pointA, double pointB){
        double error = Double.POSITIVE_INFINITY;
        int i = 1;
        double funA, funB, funXr;
        double previousXr = 0;

        try {
            funA = function.evaluateFrom(pointA);
            funB = function.evaluateFrom(pointB);
            Xr = (pointA + pointB)/2;
            funXr = function.evaluateFrom(Xr);
            concatCloseProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            do {

                if(funA * funXr == 0)
                    break;
                else if(funA*funXr > 0)
                    pointA = Xr;
                else if(funA * funXr < 0)
                    pointB = Xr;

                previousXr = Xr;

                funA = function.evaluateFrom(pointA);
                funB = function.evaluateFrom(pointB);
                Xr = (pointA + pointB)/2;
                funXr = function.evaluateFrom(Xr);

                error = Math.abs((Xr-previousXr)/Xr) * 100.0;
                i++;

                concatCloseProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            } while (error > errorPermited);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Resuelve la funcion cpor el metodo de falsa regla
     */
    public void solveByFalseRule(double pointA, double pointB){
        double error = Double.POSITIVE_INFINITY;
        int i = 1;
        double funA, funB, funXr;
        double previousXr = 0;

        try {
            funA = function.evaluateFrom(pointA);
            funB = function.evaluateFrom(pointB);
            Xr = pointB - (funB*(pointA-pointB)) / (funA - funB);
            funXr = function.evaluateFrom(Xr);
            concatCloseProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            do {

                if(funA * funXr == 0)
                    break;
                else if(funA*funXr > 0)
                    pointA = Xr;
                else if(funA * funXr < 0)
                    pointB = Xr;

                previousXr = Xr;

                funA = function.evaluateFrom(pointA);
                funB = function.evaluateFrom(pointB);
                Xr = pointB - (funB*(pointA-pointB)) / (funA - funB);
                funXr = function.evaluateFrom(Xr);

                error = Math.abs((Xr-previousXr)/Xr) * 100.0;
                i++;

                concatCloseProcedure(i, pointA,pointB,funA,funB,Xr,funXr,error);

            } while (error > errorPermited);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Resuelve la funcion por el metodo de Punto Fijo
     * @param gFunction funcion g(x)
     * @param pointC punto X arbitrario inicial para comenzar a evaluar
     */
    public void solveByFixedPoint(Function gFunction, double pointC){

        double error;
        int i = 1;

        try {

            Xr = gFunction.evaluateFrom(pointC);
            error = Math.abs((Xr-pointC)/Xr) * 100.0;

            concatFixedPointProcedure(i,pointC,Xr,error);

            do {

                pointC = Xr;
                Xr = gFunction.evaluateFrom(pointC);
                error = Math.abs((Xr-pointC)/Xr) * 100.0;
                i++;

                concatFixedPointProcedure(i,pointC,Xr,error);

            } while (error > errorPermited);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Resuelve la funcion por el metodo de Newton-Raphson
     * @param dFunction la derivada de la funcion
     * @param pointC punto X arbitrario para comenzae a evaluar
     */
    public void solveByNewtonRaphson(Function dFunction, double pointC){

        double error;
        int i = 1;
        double funC, dfunC;

        try {

            funC = function.evaluateFrom(pointC);
            dfunC = dFunction.evaluateFrom(pointC);
            Xr = pointC-(funC/dfunC);
            error = Math.abs((Xr-pointC)/Xr) * 100.0;

            concatNewtonProcedure(i,pointC,funC,dfunC,Xr,error);

            do {

                pointC = Xr;
                funC = function.evaluateFrom(pointC);
                dfunC = dFunction.evaluateFrom(pointC);
                Xr = pointC-(funC/dfunC);
                error = Math.abs((Xr-pointC)/Xr) * 100.0;
                i++;
                concatNewtonProcedure(i,pointC,funC,dfunC,Xr,error);
            } while (error > errorPermited);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void solveBySecant(double a, double b){
        double error;
        int i = 1;
        double valueForA, valueForB;

        try {
            valueForA = function.evaluateFrom(a);
            do {
                valueForB = function.evaluateFrom(b);
                Xr = b - (valueForB * (a - b) / (valueForA - valueForB));
                error = Math.abs((Xr - b) / Xr) * 100;

                concatSecantMethod(i,a,b,valueForA,valueForB,Xr,error);

                a = b;
                b = Xr;
                valueForA = valueForB;
                i++;

            }while (error > errorPermited);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Concatena al procedimiento un renglon más del procedimiento, para metodos cerrados
     */
    private void concatCloseProcedure(int i,double pointA, double pointB, double funA, double funB, double Xr, double funXr, double error){
        String a = MyUtils.format(pointA);
        String b = MyUtils.format(pointB);
        String fa = MyUtils.format(funA);
        String fb = MyUtils.format(funB);
        String xr = MyUtils.format(Xr);
        String fxr = MyUtils.format(funXr);
        String err = (i>1) ? MyUtils.format(error) : "------";

        ValuesBean values = new ValuesBean();
        values.setValue1(i+"");
        values.setValue2(a);
        values.setValue3(b);
        values.setValue4(fa);
        values.setValue5(fb);
        values.setValue6(xr);
        values.setValue7(fxr);
        values.setValue8(err);

        listValues.add(values);
    }

    /**
     * Concatena al procedimiento un renglon más del procedimiento, para el metodo de punto fijo
     */
    private void concatFixedPointProcedure(int i, double pointC, double Xr, double error){
        String c = MyUtils.format(pointC);
        String xr = MyUtils.format(Xr);
        String err = MyUtils.format(error);

        ValuesBean values = new ValuesBean();
        values.setValue1(i+"");
        values.setValue2(c);
        values.setValue3(xr);
        values.setValue4(err);

        listValues.add(values);
    }

    /**
     * Concatena al procedimiento un renglon más del procedimiento, para el metodo de Newthon-Rapson
     */
    private void concatNewtonProcedure(int i, double pointC, double valueF,double valueFPrime, double Xr, double error){
        String c = MyUtils.format(pointC);
        String f = MyUtils.format(valueF);
        String df = MyUtils.format(valueF);
        String xr = MyUtils.format(Xr);
        String err = MyUtils.format(error);

        ValuesBean values = new ValuesBean();
        values.setValue1(i+"");
        values.setValue2(c);
        values.setValue3(f);
        values.setValue4(df);
        values.setValue5(xr);
        values.setValue6(err);

        listValues.add(values);
    }

    /**
     * Concatena al procedimiento un renglon más del procedimiento, para el metodo de la secante
     */
    private void concatSecantMethod(int i, double pointA, double pointB, double valueForA, double valueForB, double Xr, double error){
        String a = MyUtils.format(pointA);
        String b = MyUtils.format(pointB);
        String valA = MyUtils.format(valueForA);
        String valB = MyUtils.format(valueForB);
        String xr = MyUtils.format(Xr);
        String err = MyUtils.format(error);

        ValuesBean values = new ValuesBean();
        values.setValue1(i+"");
        values.setValue2(a);
        values.setValue3(b);
        values.setValue4(valA);
        values.setValue5(valB);
        values.setValue6(xr);
        values.setValue7(err);

        listValues.add(values);
    }

    public double getRoot(){
        return Xr;
    }

    public ObservableList<ValuesBean> getProcedure(){
        return listValues;
    }

    public void setErrorPermited(double errorPermited) {
        this.errorPermited = errorPermited;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public void restartProcedure() {
        listValues.clear();
    }
}
