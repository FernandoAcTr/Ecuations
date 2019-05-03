package polynomialregresion.model;

public class TableUtils {

    public static String[] getColumnHeaders(int grade){
        int numColumns;
        numColumns = grade * 2 + (grade -1); //para un polinomio grado m necesito m * 2 + (m-1) columnas
        numColumns += 2; //agregar columna Sr y St;

        String headers[] = new String[numColumns];

        //Ciclo para agregar las X^2, X^3, X^4...
        int index;
        for (index = 0; index < (grade * 2 - 1); index++)
            headers[index] = "Xi^" + (index+2);

        //Ciclo para agregar las X^2Yi, X^3Yi, X^4Yi...
        int potence;
        for ( potence = 1; index < numColumns - 2; index++, potence++) {
            if(potence < 2)
                headers[index] = "XiYi";
            else
                headers[index] = "X^" + potence + "Yi";
        }

        headers[index] = "Sr";
        headers[index+1] = "St";

        return headers;

    }
}
