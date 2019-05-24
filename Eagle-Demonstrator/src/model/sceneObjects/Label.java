package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;
import model.enums.TextFont;

/**
 * The type Label.
 */
public class Label implements ISceneObject {

    private EagleBoard board;
    private double x1;
    private double y1;
    private int layerNumber;
    private double size;

    private TextFont font;
    private int ratio;
    private int rot;
    private boolean xref;

    /**
     * Instantiates a new Label.
     *
     * @param board       the board
     * @param x1          the x 1
     * @param y1          the y 1
     * @param layerNumber the layer number
     * @param size        the size
     */
    public Label(EagleBoard board, double x1, double y1, int layerNumber, double size) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.layerNumber = layerNumber;
        this.size = size;

        this.font = TextFont.Proportional;
        this.ratio = 8;
        this.rot = 0;
        this.xref = false;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        return null;
    }

    /**
     * Sets font.
     *
     * @param font the font
     */
    public void setFont(TextFont font) {
        this.font = font;
    }

    /**
     * Sets ratio.
     *
     * @param ratio the ratio
     */
    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    /**
     * Sets rot.
     *
     * @param rot the rot
     */
    public void setRot(int rot) {
        this.rot = rot;
    }

    /**
     * Sets xref.
     *
     * @param xref the xref
     */
    public void setXref(boolean xref) {
        this.xref = xref;
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

    /**
     * Gets layer number.
     *
     * @return the layer number
     */
    public int getLayerNumber() {
        return layerNumber;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * Gets font.
     *
     * @return the font
     */
    public TextFont getFont() {
        return font;
    }

    /**
     * Gets ratio.
     *
     * @return the ratio
     */
    public int getRatio() {
        return ratio;
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
     * Gets xref.
     *
     * @return the xref
     */
    public boolean getXref() {
        return xref;
    }
}
