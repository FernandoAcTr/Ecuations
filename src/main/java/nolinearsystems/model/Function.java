package nolinearsystems.model;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.ValidationResult;

public class Function {
    private String function;
    private Expression expression;

    public Function(String function) {
        this.function = function;
        expression = new ExpressionBuilder(function)
                    .implicitMultiplication(true)
                    .variables("x", "y")
                    .build();
    }

    /**
     * Evalua la funcion dada para un cierto valor
     * @param Xvalue El valor de X para el cual se requiere elvaluar la función
     * @param Yvalue El valor de Y para el cual se requiere elvaluar la función
     * @return double - El valor de la función para ese punto
     * @throws Exception
     */
    public double evaluateFrom(double Xvalue, double Yvalue) throws Exception {
        double result = Double.NaN;
        expression.setVariable("x", Xvalue);
        expression.setVariable("y", Yvalue);

        ValidationResult validation = expression.validate();
        if(!validation.isValid()){
            throw new Exception("La expresión es inválida");
        }

        result = expression.evaluate();
        return result;
    }


    public String getFunction() {
        return function;
    }
}
