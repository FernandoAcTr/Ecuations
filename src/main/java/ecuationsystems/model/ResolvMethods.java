package ecuationsystems.model;

import java.text.DecimalFormat;

public class ResolvMethods {
    private double[][] matrix;
    private int numVariables;
    private DecimalFormat formatter;
    private String procedure;
    private String separator;
    private double errorPermited;

    public ResolvMethods() {
        procedure = "";
        separator = "";
        formatter = new DecimalFormat("0.00");
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setNumVariables(int numVariables) {
        this.numVariables = numVariables;
        separator = "";
        for (int i = 0; i < numVariables * 11; i++) {
            separator += "-";
        }
    }

    public void setErrorPermited(double errorPermited) {
        this.errorPermited = errorPermited;
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------------------------------
     *                                                   METODO GAUSS
     * ------------------------------------------------------------------------------------------------------------------------------------------
     */

    public boolean resolvByGauss() {
        int indexPivot, row, col;
        double pivot, factor;

        for (indexPivot = 0; indexPivot < numVariables; indexPivot++) { //recorre la diagonal de pivotes

            pivot = matrix[indexPivot][indexPivot];

            if(pivot == 0){
                int rowToChange = findChangeRow(indexPivot);
                if(rowToChange != -1) {
                    changeRows(indexPivot, rowToChange);
                    pivot = matrix[indexPivot][indexPivot];
                    procedure += "Cambiar la fila R" + (indexPivot+1) + " por la R" + (rowToChange+1);
                    concatProcedure();
                }
                else {
                    procedure = "Este sistemas de ecuaciones no tiene solucion por este método";
                    return false;
                }
            }

            procedure += "Hacer 1 el pivote " + formatter.format(pivot) + " dividiendo la fila " + (indexPivot + 1) + " sobre si mismo";
            concatProcedure();

            for (col = indexPivot; col < numVariables + 1; col++) { //recorre la fila del pivote para dividirla sobre el pivote
                matrix[indexPivot][col] = matrix[indexPivot][col] / pivot;
            }

            concatProcedure();
            if (indexPivot + 1 < numVariables)
                procedure += "Hacer 0 los índices bajo el pivote: \n";

            for (row = indexPivot + 1; row < numVariables; row++) {  //recorre la columna del pivote para hacer 0, los indices inferiores
                factor = matrix[row][indexPivot];

                if(factor != 0) {
                    procedure += "Hacer 0 el indice de la fila " + (row + 1) + " haciendo: R" + (row + 1) + " - " + formatter.format(factor) + "*R" + (indexPivot + 1);

                    for (col = indexPivot; col < numVariables + 1; col++) {
                        matrix[row][col] = matrix[row][col] - factor * matrix[indexPivot][col];
                    }

                    concatProcedure();
                }
            }
        }

        return true;
    }

    public double[] getGaussResults() {
        double results[] = new double[numVariables];
        int pivotRow, col;
        double tempResult;

        results[numVariables - 1] = matrix[numVariables - 1][numVariables]; //el primer resultado se sabe de facto

        //comenzamos a despejar desde una fila antes que el ultimo pivote que ya se sabe el resultado

        for (pivotRow = numVariables - 2; pivotRow >= 0; pivotRow--) {
            tempResult = matrix[pivotRow][numVariables];

            for (col = numVariables - 1; col > pivotRow; col--) {
                tempResult += (-1) * matrix[pivotRow][col] * results[col];
            }

            results[pivotRow] = tempResult;
        }

        return results;
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------------------------------
     *                                                  METODO GAUSS - JORDAN
     * ------------------------------------------------------------------------------------------------------------------------------------------
     */

    public boolean resolvByGauss_Jordan() {
        int indexPivot, row, col;
        double pivot, factor;

        for (indexPivot = 0; indexPivot < numVariables; indexPivot++) { //recorre la diagonal de pivotes
            pivot = matrix[indexPivot][indexPivot];

            if(pivot == 0){
                int rowToChange = findChangeRow(indexPivot);
                if(rowToChange != -1) {
                    changeRows(indexPivot, rowToChange);
                    pivot = matrix[indexPivot][indexPivot];
                    procedure += "Cambiar la fila R" + (indexPivot+1) + " por la R" + (rowToChange+1);
                    concatProcedure();
                }
                else {
                    procedure = "Este sistemas de ecuaciones no tiene solucion por este método";
                    return false;
                }
            }

            procedure += "Hacer 1 el pivote " + formatter.format(pivot) + " dividiendo la fila " + (indexPivot + 1) + " sobre si mismo";
            concatProcedure();

            for (col = indexPivot; col < numVariables + 1; col++) { //recorre la fila del pivote para dividirla sobre el pivote
                matrix[indexPivot][col] = matrix[indexPivot][col] / pivot;
            }

            concatProcedure();
            if (indexPivot + 1 < numVariables)
                procedure += "Hacer 0 los índices sobre y debajo del pivote: \n";

            for (row = 0; row < numVariables; row++) {  //recorre la columna del pivote para hacer 0, los indices inferiores y superiores

                if (row != indexPivot) {

                    factor = matrix[row][indexPivot];

                    if(factor != 0) {
                        procedure += "Hacer 0 el indice de la fila " + (row + 1) + " haciendo: R" + (row + 1) + " - " + formatter.format(factor) + "*R" + (indexPivot + 1);

                        for (col = indexPivot; col < numVariables + 1; col++) {
                            matrix[row][col] = matrix[row][col] - factor * matrix[indexPivot][col];
                        }

                        concatProcedure();
                    }
                }
            }
        }

        return true;
    }


    public double[] getGaussJordanResults() {
        double results[] = new double[numVariables];
        int pivotRow, col;

        for (int row = 0; row < numVariables; row++) {
            results[row] = matrix[row][numVariables];
        }

        return results;
    }


    /**
     * ------------------------------------------------------------------------------------------------------------------------------------------
     *                                               METODO JACOBI
     * ------------------------------------------------------------------------------------------------------------------------------------------
     */

    public double[] resolvByJacobi() {

        String tempExpresion;
        MultiVariableFuntion[] functions = new MultiVariableFuntion[numVariables];
        /*se usan 2 vectores porque en Jacobi, se usan siempre los mismos valores para evaluar cada funcion
          y necesito que esos valores prevalezcan hasta que se hayan evaluado todas las funciones.
         */
        double[] results = new double[numVariables];
        double[] values = new double[numVariables];

        double errors[] = new double[numVariables];

        int iteration = 1;

        //recorre los pivotes buscando que no sean 0 haciendo los intercambios de fila pertinentes, antes de hacer los despejes
        for (int indexPivot = 0; indexPivot < numVariables; indexPivot++) {
            if(matrix[indexPivot][indexPivot] == 0){
                int rowToChange = findChangeRowForGauss(indexPivot);
                if(rowToChange != -1)
                    changeRows(indexPivot, rowToChange);
                else {
                    procedure = "Este sistema de ecuaciones no tiene solución por este metodo";
                    return null;
                }
            }
        }

        for (int i = 0; i < functions.length; i++) {
            tempExpresion = getExpressionCleared(matrix[i], i);
            functions[i] = new MultiVariableFuntion(tempExpresion, numVariables);
            results[i] = 0;
            values[i] = 0;
            errors[i] = Double.POSITIVE_INFINITY;
        }

        try {

            initJacobiProcedure();
            concatJacobiProcedure(iteration, values, errors);

            while (!verifyErrors(errors)) {

                for (int currentFuntion = 0; currentFuntion < numVariables; currentFuntion++) {

                    results[currentFuntion] = functions[currentFuntion].evaluateFrom(values);
                    errors[currentFuntion] = Math.abs( (results[currentFuntion] - values[currentFuntion]) / results[currentFuntion] ) * 100.0;

                }

                iteration++;
                values = results.clone();

                concatJacobiProcedure(iteration, values, errors);

            }

        } catch (Exception ex) {
            System.out.println("Error en resolvByJacobi");
            ex.printStackTrace();
        }

        return results;
    }

    private boolean verifyErrors(double errors[]) {
        boolean stop = true;

        for (double e : errors) {
            if (e > errorPermited)
                return false;
        }

        return stop;
    }

    private void initJacobiProcedure() {
        formatter = new DecimalFormat("0.000000");
        procedure = "";
        procedure += String.format("%-6s\t", "No");

        for (int i = 1; i <= numVariables; i++)
            procedure += String.format("%-14s\t%-14s\t", "X" + i, "EP" + i);

        procedure += "\n";
    }

    private void concatJacobiProcedure(int iteration, double[] values, double[] errors) {

        if(iteration < 10)
            procedure += String.format("%-6s\t", iteration);
        else
            procedure += String.format("%-5s\t", iteration);

        if (iteration == 1)

            for (int i = 0; i < numVariables; i++)
                procedure += String.format("%14s\t%16s\t", formatter.format(values[i]), " ");
        else
            for (int i = 0; i < numVariables; i++)
                procedure += String.format("%14s\t%14s\t", formatter.format(values[i]), formatter.format(errors[i]));

        procedure += "\n";
    }

    /**
     * Despeja un renglon de la matriz de valores para una cierta posicion
     *
     * @param values          El renglon de la matriz a despejar
     * @param positionToClear La variable de ese renglon que se quiere despejar
     * @return Espresion String despejada
     */
    public static String getExpressionCleared(double[] values, int positionToClear) {
        String expression = "";

        expression += "(" + values[values.length - 1] + " + ";

        for (int position = 0; position < values.length - 1; position++) {

            if (position != positionToClear) {
                expression += (values[position] * -1) + "x" + (position + 1) + " + ";
            }
        }

        expression = expression.substring(0, expression.length() - 2);

        if (values[positionToClear] != 0)
            expression += ") / " + values[positionToClear];
        else
            expression += ")";

        return expression;
    }

    //--------------------------------------------------------------------------------------------------------------------

    /**
     * ------------------------------------------------------------------------------------------------------------------------------------------
     *                                               METODO GAUSS-SEIDEL
     * ------------------------------------------------------------------------------------------------------------------------------------------
     */

    public double[] resolvByGauss_Seidel() {
        String tempExpresion;
        MultiVariableFuntion[] functions = new MultiVariableFuntion[numVariables];

        double[] results = new double[numVariables];
        double[] values = new double[numVariables];

        double errors[] = new double[numVariables];

        int iteration = 1;

        //recorre los pivotes buscando que no sean 0 haciendo los intercambios de fila pertinentes, antes de hacer los despejes
        for (int indexPivot = 0; indexPivot < numVariables; indexPivot++) {
            if(matrix[indexPivot][indexPivot] == 0){
                int rowToChange = findChangeRowForGauss(indexPivot);
                if(rowToChange != -1)
                    changeRows(indexPivot, rowToChange);
                else {
                    procedure = "Este sistema de ecuaciones no tiene solución por este metodo";
                    return null;
                }
            }
        }

        for (int i = 0; i < functions.length; i++) {
            tempExpresion = getExpressionCleared(matrix[i], i);
            functions[i] = new MultiVariableFuntion(tempExpresion, numVariables);
            results[i] = 0;
            values[i] = 0;
            errors[i] = Double.POSITIVE_INFINITY;
        }

        try {

            //se van a reutilizar los metodos de Jacobi porque es exactamente el mismo formato
            initJacobiProcedure();
            concatJacobiProcedure(iteration, values, errors);

            while (!verifyErrors(errors)) {

                for (int currentFuntion = 0; currentFuntion < numVariables; currentFuntion++) {

                    results[currentFuntion] = functions[currentFuntion].evaluateFrom(values);
                    errors[currentFuntion] = Math.abs( (results[currentFuntion] - values[currentFuntion]) / results[currentFuntion] ) * 100;

                    values[currentFuntion] = results[currentFuntion];

                }

                iteration++;
                values = results.clone();

                concatJacobiProcedure(iteration, values, errors);

            }

        } catch (Exception ex) {
            System.out.println("Error en resolvByJacobi");
            ex.printStackTrace();
        }

        return results;

    }

    private int findChangeRowForGauss(int indexPivot){
        for (int i = 0; i < numVariables; i++) {
            if(matrix[i][indexPivot] != 0)
                if(matrix[indexPivot][i] != 0)
                    return i;
        }

        return -1;
    }

    //--------------------------------------------------------------------------------------------------------------------


    public String printMatrix() {
        String mat = "";
        String aux;
        for (int i = 0; i < numVariables; i++) {
            for (int j = 0; j < numVariables + 1; j++) {
                aux = matrix[i][j] < 0 ? formatter.format(matrix[i][j]) : " " + formatter.format(matrix[i][j]);
                mat += " | " + aux;
            }
            mat += "\n";
            mat += separator;
            mat += "\n";
        }

        return mat;
    }

    private void concatProcedure() {
        procedure += "\n" + printMatrix() + "\n";
    }

    public String getProcedure() {
        return procedure;
    }

    public void reestartProcedure() {
        formatter = new DecimalFormat("0.00");
        procedure = "";
    }

    /**
     * Recorre las filas bajo el pivote en busca de alguno que no sea 0
     * @param indexPivot el indice del pivote que es 0
     * @return el indice de la primera fila encontrada bajo el pivote que no tiene 0
     */
    private int findChangeRow(int indexPivot){
        for (int i = indexPivot; i < numVariables; i++) {
            if(matrix[i][indexPivot] != 0)
                return i;
        }

        return -1;
    }

    /**
     * Cambia la fila del pivote en 0 por alguna otra fila abajo de el
     * @param indexPivot el indice del pivote que se quiere cambia
     * @param indexRowToChange el indice de la fira para intercambiar con la fila del pivote
     */
    private void changeRows(int indexPivot, int indexRowToChange){
        double rowPivot[] = new double[numVariables+1];
        double rowToChange[] = new double[numVariables+1];

        for (int i = 0; i < numVariables + 1; i++) {
            rowPivot[i] = matrix[indexPivot][i];
            rowToChange[i] = matrix[indexRowToChange][i];
        }

        for (int i = 0; i < numVariables+1; i++) {
            matrix[indexPivot][i] = rowToChange[i];
            matrix[indexRowToChange][i] = rowPivot[i];
        }
    }
}
