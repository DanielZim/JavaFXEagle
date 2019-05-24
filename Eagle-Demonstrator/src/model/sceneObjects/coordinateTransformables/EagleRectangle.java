package model.sceneObjects.coordinateTransformables;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.BoundingBox;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;
import utils.CoordinateTransformer;

/**
 * The type Eagle rectangle.
 */
public class EagleRectangle implements ICoordinateTransformable, ILayerable {

    // required
    private EagleBoard board;
    private double x1;
    private double y1;
    private int layerNumber;
    private double x2;
    private double y2;

    // optional
    private int rot;

    private boolean isSetRot;

    /**
     * Instantiates a new Eagle rectangle.
     *
     * @param board       the board
     * @param x1          the x 1
     * @param y1          the y 1
     * @param layerNumber the layer number
     * @param x2          the x 2
     * @param y2          the y 2
     */
    public EagleRectangle(EagleBoard board, double x1, double y1, int layerNumber, double x2, double y2) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.layerNumber = layerNumber;
        this.x2 = x2;
        this.y2 = y2;

        this.rot = 0;
        this.isSetRot = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        double x1 = referenceX + getX1();
        // need to get the smallest y because of the possible sign flip
        double y1 = referenceY + Math.min(getY1(), getY2());

        Color recColor = ColorProvider.getColorByLayer(getBoard(), getLayerNumber());

        Rectangle rectangle = new Rectangle(x1, y1, getWidth(), getHeight());
        rectangle.setFill(recColor);
        rectangle.setRotate(getRot());

        return rectangle;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = new BoundingBox(getX1() + referenceX, getY1() + referenceY,
                getX2() + referenceX, getY2() + referenceY).rotate(getRot());

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        if (signTransformation) {
            this.y1 = ct.transformSign(getY1());
            this.y2 = ct.transformSign(getY2());
        }
        else {
            this.y1 = ct.transformOrigin(getY1());
            this.y2 = ct.transformOrigin(getY2());
        }
    }

    /**
     * Sets rot.
     *
     * @param rot the rot
     */
    public void setRot(int rot) {
        this.rot = rot;
        this.isSetRot = true;
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

    public int getLayerNumber() {
        return layerNumber;
    }

    /**
     * Gets x 2.
     *
     * @return the x 2
     */
    public double getX2() {
        return x2;
    }

    /**
     * Gets y 2.
     *
     * @return the y 2
     */
    public double getY2() {
        return y2;
    }

    /**
     * Gets rot.
     *
     * @return the rot
     */
    public int getRot() {
        return rot;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public double getHeight() {
        return Math.abs(getY2() - getY1());
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public double getWidth() {
        return Math.abs(getX2() - getX1());
    }

    /**
     * Gets is set rot.
     *
     * @return the is set rot
     */
    public boolean getIsSetRot() {
        return isSetRot;
    }
}
