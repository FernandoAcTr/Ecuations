package interpolation.model;

public class Interpolator {

    public float getLinearInterpolation(XYPoint point0, XYPoint point1, float valueFor) {
        return point0.getY() + (point1.getY() - point0.getY()) / (point1.getX() - point0.getX()) * (valueFor - point0.getX());
    }

    public float getSquareInterpolation(XYPoint point0, XYPoint point1, XYPoint point2, float valueFor) {
        float b0 = point0.getY();
        float b1 = (point1.getY() - point0.getY()) / (point1.getX() - point0.getX());
        float b2 = (point2.getY() - point0.getY() - b1 * (point2.getX() - point0.getX())) / ((point2.getX() - point0.getX()) * (point2.getX() - point1.getX()));

        return b0 + b1 * (valueFor - point0.getX()) + b2 * (valueFor - point0.getX()) * (valueFor - point1.getX());
    }
}
