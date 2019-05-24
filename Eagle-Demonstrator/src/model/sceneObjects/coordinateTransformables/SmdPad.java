package model.sceneObjects.coordinateTransformables;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.BoundingBox;
import model.sceneObjects.EagleBoard;
import utils.ColorProvider;

/**
 * The type Smd pad.
 */
public class SmdPad extends Pad {

    // required
    private double dx;
    private double dy;
    private int layerNumber;

    // optional
    private int roundness;
    private boolean cream;

    private boolean isSetRoundness;
    private boolean isSetCream;

    /**
     * Instantiates a new Smd pad.
     *
     * @param board       the board
     * @param name        the name
     * @param x           the x
     * @param y           the y
     * @param dx          the dx
     * @param dy          the dy
     * @param layerNumber the layer number
     */
    public SmdPad(EagleBoard board, String name, double x, double y, double dx, double dy, int layerNumber) {
        super(board, x, y, name);

        this.dx = dx;
        this.dy = dy;
        this.layerNumber = layerNumber;

        this.roundness = 0;
        this.cream = true;

        this.isSetCream = false;
        this.isSetRoundness = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        // cream, thermals and stop ignored

        double x = getX1() + referenceX;
        double y = getY1() + referenceY;

        Color color = ColorProvider.getColorByLayer(getBoard(), getLayerNumber());

        Rectangle sceneNode = new Rectangle(x - getDx() / 2, y - getDy() / 2, getDx(), getDy());
        sceneNode.setFill(color);
        sceneNode.setRotate(getRot());

        if(getIsSetRoundness()) {
            sceneNode.setArcWidth(getRoundness());
            sceneNode.setArcHeight(getRoundness());
        }

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        double x = getX1() + referenceX;
        double y = getY1() + referenceY;

        BoundingBox bbox = new BoundingBox(x - getDx() / 2, y - getDy() / 2,
                x + getDx() / 2, y + getDy() / 2);

        return bbox.rotate(getRot());
    }

    /**
     * Sets roundness.
     *
     * @param roundness the roundness
     */
    public void setRoundness(int roundness) {
        this.roundness = roundness;
        this.isSetRoundness = true;
    }

    /**
     * Sets cream.
     *
     * @param cream the cream
     */
    public void setCream(boolean cream) {
        this.cream = cream;
        this.isSetCream = true;
    }

    /**
     * Gets dx.
     *
     * @return the dx
     */
    public double getDx() {
        return dx;
    }

    /**
     * Gets dy.
     *
     * @return the dy
     */
    public double getDy() {
        return dy;
    }

    public int getLayerNumber() {
        return layerNumber;
    }

    /**
     * Gets roundness.
     *
     * @return the roundness
     */
    public int getRoundness() {
        return roundness;
    }

    /**
     * Gets cream.
     *
     * @return the cream
     */
    public boolean getCream() {
        return cream;
    }

    /**
     * Gets is set roundness.
     *
     * @return the is set roundness
     */
    public boolean getIsSetRoundness() {
        return isSetRoundness;
    }

    /**
     * Gets is set cream.
     *
     * @return the is set cream
     */
    public boolean getIsSetCream() {
        return isSetCream;
    }
}
