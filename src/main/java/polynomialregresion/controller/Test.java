package polynomialregresion.controller;

import polynomialregresion.model.TableUtils;

public class Test {
    public static void main(String[] args){
        String[] headers = TableUtils.getColumnHeaders(10);

        for (int i = 0; i < headers.length; i++)
            System.out.print(headers[i] + " ");

    }
}
