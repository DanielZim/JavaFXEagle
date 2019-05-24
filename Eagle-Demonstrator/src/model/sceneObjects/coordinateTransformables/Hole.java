package model.sceneObjects.coordinateTransformables;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.BoundingBox;
import utils.ColorProvider;
import utils.CoordinateTransformer;

/**
 * The type Hole.
 */
public class Hole implements ICoordinateTransformable, ILayerable {

    private static final double STROKE_WIDTH = 0.1;

    // required
    private double x1;
    private double y1;
    private double drill;

    /**
     * Instantiates a new Hole.
     *
     * @param x1    the x 1
     * @param y1    the y 1
     * @param drill the drill
     */
    public Hole(double x1, double y1, double drill) {
        this.x1 = x1;
        this.y1 = y1;
        this.drill = drill;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        // should we use all 16 drill types? Every type looks different
        // we use 0.019 drill as standard now

        double x = getX1() + referenceX;
        double y = getY1() + referenceY;
        double halfLineLength = getDrill() * 4;

        // create circle
        Circle circle = new Circle(x, y, getDrill());
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(STROKE_WIDTH);

        // create cross (we use 0.019 drill type here)
        Line vertical = new Line(x, y + halfLineLength, x, y - halfLineLength);
        vertical.setStrokeWidth(STROKE_WIDTH);
        vertical.setStroke(ColorProvider.HOLE_COLOR);

        Line horizontal = new Line(x - halfLineLength, y, x + halfLineLength, y);
        horizontal.setStrokeWidth(STROKE_WIDTH);
        horizontal.setStroke(ColorProvider.HOLE_COLOR);

        Group sceneNode = new Group();
        sceneNode.getChildren().addAll(circle, horizontal, vertical);

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        return new BoundingBox(getX1() + referenceX - getDrill() * 4, getY1() + referenceY - getDrill() * 4,
                getX1() + referenceX + getDrill() * 4, getY1() + referenceY + getDrill() * 4);
    }

    @Override
    public int getLayerNumber() {
        return -1;
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
     * Gets y 1.
     *
     * @return the y 1
     */
    public double getY1() {
        return y1;
    }

    /**
     * Gets drill.
     *
     * @return the drill
     */
    public double getDrill() {
        return drill;
    }
}
