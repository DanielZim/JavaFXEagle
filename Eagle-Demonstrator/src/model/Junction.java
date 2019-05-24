package model;

/**
 * The type Junction.
 */
public class Junction {

    // required
    private double x1;
    private double y1;

    /**
     * Instantiates a new Junction.
     *
     * @param x1 the x 1
     * @param y1 the y 1
     */
    public Junction(double x1, double y1) {
        this.x1 = x1;
        this.y1 = y1;
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
     * Gets y 1.
     *
     * @return the y 1
     */
    public double getY1() {
        return y1;
    }
}
