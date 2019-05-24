package model.sceneObjects;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import model.enums.GridStyle;
import model.enums.GridUnit;

/**
 * The type Grid.
 */
public class Grid {

    private final static double LINE_WIDTH = 0.01;

    // implied
    private double distance;
    private GridUnit unitDist;
    private GridUnit unit;
    private double altDistance;
    private GridUnit altUnitDist;
    private GridUnit altUnit;

    // optional
    private GridStyle style;
    private int multiple;
    private boolean display;

    /**
     * Instantiates a new Grid.
     */
    public Grid() {
        this.distance = 0.0;
        this.unitDist = null;
        this.unit = null;
        this.altDistance = 0.0;
        this.altUnitDist = null;
        this.altUnit = null;

        this.style = GridStyle.Lines;
        this.multiple = 1;
        this.display = false;
    }

    /**
     * Instantiates a new Grid.
     *
     * @param distance    the distance
     * @param unitDist    the unit dist
     * @param unit        the unit
     * @param altDistance the alt distance
     * @param altUnitDist the alt unit dist
     * @param altUnit     the alt unit
     */
    public Grid(double distance, GridUnit unitDist, GridUnit unit, double altDistance, GridUnit altUnitDist, GridUnit altUnit) {
        this.distance = distance;
        this.unitDist = unitDist;
        this.unit = unit;
        this.altDistance = altDistance;
        this.altUnitDist = altUnitDist;
        this.altUnit = altUnit;

        this.style = GridStyle.Lines;
        this.multiple = 1;
        this.display = false;
    }

    /**
     * Draw on node.
     *
     * @param root   the root
     * @param width  the width
     * @param height the height
     */
    public void drawOnNode(Node root, int width, int height) {
        // draw horizontal lines
        for(int i = -height; i*getDistanceInMm() < height; i++) {
            double y = i*getDistanceInMm() - (LINE_WIDTH / 2);
            Line line = new Line(-width, y, width, y);
            line.setStroke(Color.GREY);
            line.setStrokeWidth(LINE_WIDTH);
            ((Pane) root).getChildren().add(line);
        }

        // draw vertical lines
        for(int i = -width; i*getDistanceInMm() < width; i++) {
            double x = i*getDistanceInMm() - (LINE_WIDTH / 2);
            Line line = new Line(x, -height, x, height);
            line.setStroke(Color.GREY);
            line.setStrokeWidth(LINE_WIDTH);
            ((Pane) root).getChildren().add(line);
        }
    }

    /**
     * Gets distance in mm.
     *
     * @return the distance in mm
     */
    public double getDistanceInMm() {
        double dist = getDistance();

        switch(getUnitDist()) {
            case Mm:
                break;
            case Inch:
                dist *= 25.4;
                break;
            case Mic:
                dist /= 1000;
                break;
            case Mil:
                dist = dist / 1000 * 25.4;
                break;
        }

        return dist;
    }

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets distance.
     *
     * @param distance the distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Gets unit dist.
     *
     * @return the unit dist
     */
    public GridUnit getUnitDist() {
        return unitDist;
    }

    /**
     * Sets unit dist.
     *
     * @param unitDist the unit dist
     */
    public void setUnitDist(GridUnit unitDist) {
        this.unitDist = unitDist;
    }

    /**
     * Gets unit.
     *
     * @return the unit
     */
    public GridUnit getUnit() {
        return unit;
    }

    /**
     * Sets unit.
     *
     * @param unit the unit
     */
    public void setUnit(GridUnit unit) {
        this.unit = unit;
    }

    /**
     * Gets alt distance.
     *
     * @return the alt distance
     */
    public double getAltDistance() {
        return altDistance;
    }

    /**
     * Sets alt distance.
     *
     * @param altDistance the alt distance
     */
    public void setAltDistance(double altDistance) {
        this.altDistance = altDistance;
    }

    /**
     * Gets alt unit dist.
     *
     * @return the alt unit dist
     */
    public GridUnit getAltUnitDist() {
        return altUnitDist;
    }

    /**
     * Sets alt unit dist.
     *
     * @param altUnitDist the alt unit dist
     */
    public void setAltUnitDist(GridUnit altUnitDist) {
        this.altUnitDist = altUnitDist;
    }

    /**
     * Gets alt unit.
     *
     * @return the alt unit
     */
    public GridUnit getAltUnit() {
        return altUnit;
    }

    /**
     * Sets alt unit.
     *
     * @param altUnit the alt unit
     */
    public void setAltUnit(GridUnit altUnit) {
        this.altUnit = altUnit;
    }

    /**
     * Gets style.
     *
     * @return the style
     */
    public GridStyle getStyle() {
        return style;
    }

    /**
     * Sets style.
     *
     * @param style the style
     */
    public void setStyle(GridStyle style) {
        this.style = style;
    }

    /**
     * Gets multiple.
     *
     * @return the multiple
     */
    public int getMultiple() {
        return multiple;
    }

    /**
     * Sets multiple.
     *
     * @param multiple the multiple
     */
    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    /**
     * Gets display.
     *
     * @return the display
     */
    public boolean getDisplay() {
        return display;
    }

    /**
     * Sets display.
     *
     * @param display the display
     */
    public void setDisplay(boolean display) {
        this.display = display;
    }
}
