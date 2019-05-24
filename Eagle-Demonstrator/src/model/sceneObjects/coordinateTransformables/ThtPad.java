package model.sceneObjects.coordinateTransformables;

import javafx.scene.Node;
import javafx.scene.shape.*;
import model.BoundingBox;
import model.enums.PadShape;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;

/**
 * The type Tht pad.
 */
public class ThtPad extends Pad {

    private static final double MIN_BOARDER_WIDTH = 0.19; // with drill <= 1
    private static final double MAX_BOARDER_WIDTH = 0.29; // with drill >= 2

    // required
    private double drill;

    // optional
    private double diameter;
    private PadShape shape;
    private boolean first;

    private boolean isSetDiameter;
    private boolean isSetShape;
    private boolean isSetFirst;

    /**
     * Instantiates a new Tht pad.
     *
     * @param board the board
     * @param name  the name
     * @param x     the x
     * @param y     the y
     * @param drill the drill
     */
    public ThtPad(EagleBoard board, String name, double x, double y, double drill) {
        super(board, x, y, name);

        this.drill = drill;

        this.diameter = 0;
        this.shape = PadShape.Round;
        this.first = false;

        this.isSetDiameter = false;
        this.isSetShape = false;
        this.isSetFirst = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        // first, thermals and stop ignored

        double x = getX1() + referenceX;
        double y = getY1() + referenceY;
        double drillRadius = getDrill() / 2;
        double padRadius = getPadRadius();

        Circle hole = new Circle(x, y, drillRadius);

        Shape viaShape = null;

        switch(getShape()) {
            case Round:
                viaShape = new Circle(x, y, padRadius);
                break;
            case Square:
                viaShape = getSquareShape(x, y, padRadius);
                break;
            case Octagon:
                viaShape = getOctagonShape(x, y, padRadius);
                break;
            case Long:
                viaShape = getLongShape(x, y, padRadius);
                break;
            case Offset:
                viaShape = getOffsetShape(x, y, padRadius);
                break;
        }

        Shape sceneNode = Shape.subtract(viaShape, hole);
        sceneNode.setRotate(getRot());
        sceneNode.setFill(ColorProvider.THT_PAD_COLOR);

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = null;

        switch (getShape()) {
            case Round:
            case Octagon:
            case Square:
                bbox = new BoundingBox(getX1() + referenceX - getPadRadius(),
                                       getY1() + referenceY - getPadRadius(),
                                       getX1() + referenceX + getPadRadius(),
                                       getY1() + referenceY + getPadRadius()).rotate(getRot());
                break;
            case Offset:
            case Long:
                bbox = new BoundingBox(getX1() + referenceX - getPadRadius() * 2,
                                       getY1() + referenceY - getPadRadius(),
                                       getX1() + referenceX + getPadRadius() * 2,
                                       getY1() + referenceY + getPadRadius()).rotate(getRot());
                break;
        }

        return bbox;
    }

    /**
     * Gets the pad radius
     *
     * @return  the pad radius
     */
    private double getPadRadius() {
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
        Rectangle rec = new Rectangle();
        rec.setX(x - radius);
        rec.setY(y - radius);
        rec.setHeight(radius * 2);
        rec.setWidth(radius * 2);

        return rec;
    }

    /**
     * Gets the long shape.
     *
     * @param x         x
     * @param y         y
     * @param radius    the radius
     * @return          the shape
     */
    private Shape getLongShape(double x, double y, double radius) {
        Shape square = getSquareShape(x, y, radius);

        Arc leftArc = new Arc(x - radius, y, radius, radius, 90, 180);
        leftArc.setType(ArcType.ROUND);

        Arc rightArc = new Arc(x + radius, y, radius, radius, 270, 180);
        rightArc.setType(ArcType.ROUND);

        Shape shape = Shape.union(square, leftArc);

        return Shape.union(shape, rightArc);
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
     * Gets the offset shape.
     *
     * @param x         x
     * @param y         y
     * @param radius    the radius
     * @return          the shape
     */
    private Shape getOffsetShape(double x, double y, double radius) {
        return getLongShape(x, y - radius, radius);
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
    public void setShape(PadShape shape) {
        this.shape = shape;
        this.isSetShape = true;
    }

    /**
     * Sets first.
     *
     * @param first the first
     */
    public void setFirst(boolean first) {
        this.first = first;
        this.isSetFirst = true;
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
    public PadShape getShape() {
        return shape;
    }

    /**
     * Gets first.
     *
     * @return the first
     */
    public boolean getFirst() {
        return first;
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
     * Gets is set first.
     *
     * @return the is set first
     */
    public boolean getIsSetFirst() {
        return isSetFirst;
    }

    @Override
    public int getLayerNumber() {
        return -1;
    }
}
