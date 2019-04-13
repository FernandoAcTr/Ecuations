package nolinearsystems.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileFunction {
    private RandomAccessFile randomFile;
    private File functionFile;

    public FileFunction() {
        randomFile = null;
        functionFile = null;
    }

    /**
     * Abre un archivo de acceso directo con la ruta especificada
     *
     * @param file
     */
    public void openFile(File file) {
        try {
            functionFile = file;
            randomFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierrra el archivo
     */
    public void closeFile() {
        try {
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Escribe una funcion y los datos necesarios para resolverla en un archivo binario,
     *
     * @param beanFunction objeto de tipo FileFunction.BeanFucntion
     */
    public void saveFunction(BeanFunction beanFunction) {
        try {
            randomFile.seek(0);
            randomFile.writeByte(beanFunction.typeMethod);
            randomFile.writeUTF(beanFunction.f1);
            randomFile.writeUTF(beanFunction.f2);
            randomFile.writeUTF(beanFunction.f1x);
            randomFile.writeUTF(beanFunction.f1y);
            randomFile.writeUTF(beanFunction.f2x);
            randomFile.writeUTF(beanFunction.f2y);
            randomFile.writeUTF(beanFunction.x);
            randomFile.writeUTF(beanFunction.y);
            randomFile.writeUTF(beanFunction.errorPermited);
            randomFile.writeUTF(beanFunction.graphicFunction1);
            randomFile.writeUTF(beanFunction.from1);
            randomFile.writeUTF(beanFunction.to1);
            randomFile.writeUTF(beanFunction.graphicFunction2);
            randomFile.writeUTF(beanFunction.from2);
            randomFile.writeUTF(beanFunction.to2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retorna una instancia de FileFunction.BeanFunction
     */
    public BeanFunction readFunction() {
        BeanFunction bean = null;
        try {
            byte type = randomFile.readByte();
            String f1 = randomFile.readUTF();
            String f2 = randomFile.readUTF();
            String f1x = randomFile.readUTF();
            String f1y = randomFile.readUTF();
            String f2x = randomFile.readUTF();
            String f2y = randomFile.readUTF();
            String x = randomFile.readUTF();
            String y = randomFile.readUTF();
            String errorPermited = randomFile.readUTF();
            String graphicFunction1 = randomFile.readUTF();
            String from1 = randomFile.readUTF();
            String to1 = randomFile.readUTF();
            String graphicFunction2 = randomFile.readUTF();
            String from2 = randomFile.readUTF();
            String to2 = randomFile.readUTF();


            bean = new BeanFunction(type, f1, f2, f1x, f1y, f2x, f2y, x, y, errorPermited);
            bean.setGraphicFunction1(graphicFunction1, from1, to1);
            bean.setGraphicFunction2(graphicFunction2, from2, to2);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * Pierde la referencia al archivo que se estaba apuntando
     */
    public void restartFile() {
        functionFile = null;
    }

    public File getFunctionFile() {
        return functionFile;
    }

    /**
     * Clase que sirve de contenedor de los valores de la interfaz grafica
     */
    public static class BeanFunction {
        private byte typeMethod;
        private String f1;
        private String f2;
        private String f1x;
        private String f1y;
        private String f2x;
        private String f2y;
        private String x, y;
        private String errorPermited;
        private String graphicFunction1;
        private String graphicFunction2;
        private String from1, to1, from2, to2;

        //estas constantes deben coincidir con los indices del ComboBox de metodos de solucion
        public static final byte NEWTON_RAPHSON_MULTIVARIABLE = 6;


        public BeanFunction(byte typeMethod, String f1, String f2, String f1x, String f1y, String f2x, String f2y,
                            String x, String y, String errorPermited) {
            this.typeMethod = typeMethod;
            this.f1 = f1;
            this.f2 = f2;
            this.f1x = f1x;
            this.f1y = f1y;
            this.f2x = f2x;
            this.f2y = f2y;
            this.x = x;
            this.y = y;
            this.errorPermited = errorPermited;
        }

        public byte getTypeMethod() {
            return typeMethod;
        }

        public String getF1() {
            return f1;
        }

        public String getF2() {
            return f2;
        }

        public String getF1x() {
            return f1x;
        }

        public String getF1y() {
            return f1y;
        }

        public String getF2x() {
            return f2x;
        }

        public String getF2y() {
            return f2y;
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }

        public String getErrorPermited() {
            return errorPermited;
        }

        public void setGraphicFunction1(String graphicFunction1, String from1, String to1){
            this.graphicFunction1 = graphicFunction1;
            this.from1 = from1;
            this.to1 = to1;
        }

        public void setGraphicFunction2(String graphicFunction2, String from2, String to2){
            this.graphicFunction2 = graphicFunction2;
            this.from2 = from2;
            this.to2 = to2;
        }

        public String getGraphicFunction1() {
            return graphicFunction1;
        }

        public String getGraphicFunction2() {
            return graphicFunction2;
        }

        public String getFrom1() {
            return from1;
        }

        public String getTo1() {
            return to1;
        }

        public String getFrom2() {
            return from2;
        }

        public String getTo2() {
            return to2;
        }
    }
}
