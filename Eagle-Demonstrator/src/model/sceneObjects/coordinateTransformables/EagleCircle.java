package model.sceneObjects.coordinateTransformables;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.BoundingBox;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;
import utils.CoordinateTransformer;

/**
 * The type Eagle circle.
 */
public class EagleCircle implements ICoordinateTransformable, ILayerable {

    // required
    private EagleBoard board;
    private double x1;
    private double y1;
    private int layerNumber;
    private double radius;
    private double width;

    /**
     * Instantiates a new Eagle circle.
     *
     * @param board       the board
     * @param x1          the x 1
     * @param y1          the y 1
     * @param layerNumber the layer number
     * @param radius      the radius
     * @param width       the width
     */
    public EagleCircle(EagleBoard board, double x1, double y1, int layerNumber, double radius, double width) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.layerNumber = layerNumber;
        this.radius = radius;
        this.width = width;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        double x = referenceX + getX1();
        double y = referenceY + getY1();

        Color circleColor = ColorProvider.getColorByLayer(getBoard(), getLayerNumber());

        Circle circle = new Circle(x, y, getRadius(), Color.TRANSPARENT);
        circle.setStroke(circleColor);
        circle.setStrokeWidth(getWidth());

        return circle;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = new BoundingBox(getX1() + referenceX - getRadius(),
                getY1() + referenceY - getRadius(),
                getX1() + referenceX + getRadius(),
                getY1() + referenceY + getRadius());

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        this.y1 = signTransformation ? ct.transformSign(getY1()) : ct.transformOrigin(getY1());
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
     * Gets board.
     *
     * @return the board
     */
    public EagleBoard getBoard() {
        return board;
    }

    /**
     * Gets y 1.
     *
     * @return the y 1
     */
    public double getY1() {
        return y1;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    /**
     * Gets radius.
     *
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return width;
    }
}
