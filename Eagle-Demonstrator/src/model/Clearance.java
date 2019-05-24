package model;

/**
 * The type Clearance.
 */
public class Clearance {

    private String className;

    private double value;

    /**
     * Instantiates a new Clearance.
     *
     * @param className the class name
     */
    public Clearance(String className) {
        this.className = className;

        this.value = 0;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Gets class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public double getValue() {
        return value;
    }
}
