package interpolation.model;

public class Interpolator {

    public float getLinearInterpolation(XYPoint x0, XYPoint x1, float valueFor) {
        return x0.getY() + (x1.getY() - x0.getY()) / (x1.getX() - x0.getX()) * (valueFor - x0.getX());
    }

    public float getSquareInterpolation(XYPoint x0, XYPoint x1, XYPoint x2, float valueFor) {
        float b0 = x0.getY();
        float b1 = (x1.getY() - x0.getY()) / (x1.getX() - x0.getX());
        float b2 = (x2.getY() - x0.getY() - b1 * (x2.getX() - x0.getX())) / ((x2.getX() - x0.getX()) * (x2.getX() - x1.getX()));

        return b0 + b1 * (valueFor - x0.getX()) + b2 * (valueFor - x0.getX()) * (valueFor - x1.getX());
    }

    /**
     * Regresa una interpolacion por el metodo de diferencias divididas
     * @param Xn arreglo de puntos con el formato [X4, X3, X2, X1 ... X0]
     * @param grade El grado de la interpolacion que queremos usar f3, f4, f5,...,fn
     * @return
     */
    public float getDiffInterpolation(XYPoint[] Xn, int grade, float valueFor){
        float coefficients[] = new float[grade+1];
        coefficients[0] = Xn[grade].getY();
//        coefficients[1] = getDiff_b_coefficient(Xn, Xn.length-2, Xn.length-1, 2);
//        coefficients[2] = getDiff_b_coefficient(Xn, Xn.length-3, Xn.length-1, 2);

        for (int i = 1; i <= grade; i++)
            coefficients[i] = getDiff_b_coefficient(Xn, Xn.length-i-1, Xn.length-1, i+1);


        float interpolation = coefficients[0];
        float aux;

        for (int i = 1; i < coefficients.length; i++) {
            aux = coefficients[i];
            for (int j = 0, k = Xn.length-1; j < i; j++, k--) {
                aux *= (valueFor - Xn[k].getX());
            }

            interpolation += aux;
        }

        return interpolation;
    }

    /**
     * Regresa una interpolacion por el metodo de diferencias divididas
     * @param Xn arreglo de puntos con el formato [X4, X3, X2, X1 ... X0]
     * @param startIndex El indice mas a la izquierda para calcular una diferencia dividida. A la primer llamada siempre es 0
     * @param endIndex El indice mas a la derecha para calcular una diferencia dividida. A la primer llamada siempre es el ultimo indice
     * @param sizeDiff El numero de puntos de la diferencia que se quiere calcular
     * @return
     */
    private float getDiff_b_coefficient(XYPoint[] Xn, int startIndex, int endIndex, int sizeDiff) {
        if (sizeDiff == 2) {
            return (Xn[startIndex].getY() - Xn[endIndex].getY()) / (Xn[startIndex].getX() - Xn[endIndex].getX());
        } else {
            return (getDiff_b_coefficient(Xn, startIndex, endIndex - 1, sizeDiff - 1)
                    - getDiff_b_coefficient(Xn, startIndex + 1, endIndex, sizeDiff - 1)) / (Xn[startIndex].getX() - Xn[endIndex].getX());
        }
    }

    /**
     * Regresa una interpolacion por el metodo de Lagrange
     * @param Xn arreglo de puntos con el formato [X0, X1, X2...Xn]
     * @return
     */
    public float getLagrangeInterpolation(XYPoint[] Xn, float valueFor){
        float product;
        float denominator;
        float sum = 0;
        float aux;

        for (int i = 0; i < Xn.length; i++) {

            product = 1;
            denominator = 1;

            for (int j = 0; j < Xn.length; j++) {
                if( j != i) {
                    product *= (valueFor - Xn[j].getX());
                    denominator *= (Xn[i].getX() - Xn[j].getX());
                }
            }

            aux = product / denominator;
            sum += aux * Xn[i].getY();
        }

        return sum;
    }
}
