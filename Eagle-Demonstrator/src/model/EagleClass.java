package model;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Eagle class.
 */
public class EagleClass {

    private int number;
    private String name;

    private double width;
    private double drill;

    private List<Clearance> clearances;

    /**
     * Instantiates a new Eagle class.
     *
     * @param number the number
     * @param name   the name
     */
    public EagleClass(int number, String name) {
        this.number = number;
        this.name = name;

        this.width = 0;
        this.drill = 0;

        this.clearances = new ArrayList<>();
    }

    /**
     * Add clearance.
     *
     * @param clearance the clearance
     */
    public void addClearance(Clearance clearance) {
        this.clearances.add(clearance);
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Sets drill.
     *
     * @param drill the drill
     */
    public void setDrill(double drill) {
        this.drill = drill;
    }

    /**
     * Gets number.
     *
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets drill.
     *
     * @return the drill
     */
    public double getDrill() {
        return drill;
    }

    /**
     * Gets clearances.
     *
     * @return the clearances
     */
    public List<Clearance> getClearances() {
        return clearances;
    }
}
