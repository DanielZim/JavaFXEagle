package model.sceneObjects.coordinateTransformables;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import model.BoundingBox;
import model.enums.ViaShape;
import utils.ColorProvider;
import utils.CoordinateTransformer;

/**
 * The type Via.
 */
public class Via implements ICoordinateTransformable {

    private static final double MAX_DRILL = 0.1259;
    private static final double MIN_BOARDER_WIDTH = 0.19; // with drill <= 1
    private static final double MAX_BOARDER_WIDTH = 0.29; // with drill >= 2

    // required
    private double x1;
    private double y1;
    private String extent;
    private double drill;

    // optional
    private double diameter;
    private ViaShape shape;
    private boolean alwaysStop;

    private boolean isSetDiameter;
    private boolean isSetShape;
    private boolean isSetAlwaysStop;

    /**
     * Instantiates a new Via.
     *
     * @param x1     the x 1
     * @param y1     the y 1
     * @param extent the extent
     * @param drill  the drill
     */
    public Via(double x1, double y1, String extent, double drill) {
        this.x1 = x1;
        this.y1 = y1;
        this.extent = extent;
        this.drill = drill;

        this.diameter = 0;
        this.shape = ViaShape.Round;
        this.alwaysStop = false;

        this.isSetDiameter = false;
        this.isSetShape = false;
        this.isSetAlwaysStop = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        // extent is ignored at the moment

        double x = getX1() + referenceX;
        double y = getY1() + referenceY;
        double drillRadius = getDrill() / 2;
        double viaRadius = getViaRadius();

        Circle hole = new Circle(x, y, drillRadius);
        Shape viaShape = null;

        switch (getShape()) {
            case Round:
                viaShape = new Circle(x, y, viaRadius);
                break;
            case Square:
                viaShape = getSquareShape(x, y, viaRadius);
                break;
            case Octagon:
                viaShape = getOctagonShape(x, y, viaRadius);
                break;
        }

        Shape sceneNode = Shape.subtract(viaShape, hole);
        sceneNode.setFill(ColorProvider.VIA_COLOR);

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = new BoundingBox(getX1() + referenceX - getViaRadius(), getY1() + referenceY - getViaRadius(),
                getX1() + referenceX + getViaRadius(), getY1() + referenceY + getViaRadius());

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        this.y1 = signTransformation ? ct.transformSign(getY1()) : ct.transformOrigin(getY1());
    }

    /**
     * Gets the via radius
     *
     * @return  the via radius
     */
    private double getViaRadius() {

        if(getIsSetDiameter() && getDiameter() != 0.0) {
            return  getDiameter() / 2;
        }

        double drill = getDrill();

        if(drill <= 1) {
            return drill / 2 + MIN_BOARDER_WIDTH;
        }

        if(drill >= 2) {
            return drill / 2 + MAX_BOARDER_WIDTH;
        }

        return drill / 2 + MIN_BOARDER_WIDTH + ((drill - 1) * (MAX_BOARDER_WIDTH - MIN_BOARDER_WIDTH));
    }

    /**
     * Gets the square shape.
     *
     * @param x         x
     * @param y         y
     * @param radius    the radius
     * @return          the shape
     */
    private Shape getSquareShape(double x, double y, double radius) {
        Rectangle square = new Rectangle();
        square.setX(x - radius);
        square.setY(y - radius);
        square.setHeight(radius * 2);
        square.setWidth(radius * 2);

        return square;
    }

    /**
     * Gets the octagon shape.
     *
     * @param x         x
     * @param y         y
     * @param radius    the radius
     * @return          the shape
     */
    private Shape getOctagonShape(double x, double y, double radius) {
        Polygon octagon = new Polygon();

        double angleStep = Math.PI * 2 / 8;
        double angle = angleStep / 2;

        for (int i = 0; i < 8; i++, angle += angleStep) {
            octagon.getPoints().addAll(
                    Math.sin(angle) * radius + x,
                              Math.cos(angle) * radius + y
            );
        }

        return octagon;
    }

    /**
     * Sets diameter.
     *
     * @param diameter the diameter
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
        this.isSetDiameter = true;
    }

    /**
     * Sets shape.
     *
     * @param shape the shape
     */
    public void setShape(ViaShape shape) {
        this.shape = shape;
        this.isSetShape = true;
    }

    /**
     * Sets always stop.
     *
     * @param alwaysStop the always stop
     */
    public void setAlwaysStop(boolean alwaysStop) {
        this.alwaysStop = alwaysStop;
        this.isSetAlwaysStop = true;
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

    /**
     * Gets extent.
     *
     * @return the extent
     */
    public String getExtent() {
        return extent;
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
     * Gets diameter.
     *
     * @return the diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Gets shape.
     *
     * @return the shape
     */
    public ViaShape getShape() {
        return shape;
    }

    /**
     * Gets always stop.
     *
     * @return the always stop
     */
    public boolean getAlwaysStop() {
        return alwaysStop;
    }

    /**
     * Gets is set diameter.
     *
     * @return the is set diameter
     */
    public boolean getIsSetDiameter() {
        return isSetDiameter;
    }

    /**
     * Gets is set shape.
     *
     * @return the is set shape
     */
    public boolean getIsSetShape() {
        return isSetShape;
    }

    /**
     * Gets is set always stop.
     *
     * @return the is set always stop
     */
    public boolean getIsSetAlwaysStop() {
        return isSetAlwaysStop;
    }
}
