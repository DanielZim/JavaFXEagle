package model;

/**
 * The type Vertex.
 */
public class Vertex {

    // required
    private double x1;
    private double y1;

    // optional
    private double curve;

    private boolean isSetCurve;

    /**
     * Instantiates a new Vertex.
     *
     * @param x1 the x 1
     * @param y1 the y 1
     */
    public Vertex(double x1, double y1) {
        this.x1 = x1;
        this.y1 = y1;

        this.curve = 0;
        this.isSetCurve = false;
    }

    /**
     * Sets curve.
     *
     * @param curve the curve
     */
    public void setCurve(double curve) {
        this.curve = curve;
        this.isSetCurve = true;
    }

    /**
     * Gets x 1.
     *
     * @return the x 1
     */
    public double getX1() {
        return x1;
    }

    /**
     * Sets x 1.
     *
     * @param x1 the x 1
     */
    public void setX1(double x1) {
        this.x1 = x1;
    }

    /**
     * Gets y 1.
     *
     * @return the y 1
     */
    public double getY1() {
        return y1;
    }

    /**
     * Sets y 1.
     *
     * @param y1 the y 1
     */
    public void setY1(double y1) {
        this.y1 = y1;
    }

    /**
     * Gets curve.
     *
     * @return the curve
     */
    public double getCurve() {
        return curve;
    }

    /**
     * Gets is set curve.
     *
     * @return the is set curve
     */
    public boolean getIsSetCurve() {
        return isSetCurve;
    }
}
