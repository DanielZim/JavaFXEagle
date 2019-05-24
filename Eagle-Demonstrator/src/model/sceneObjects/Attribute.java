package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;
import model.enums.AttributeDisplay;
import model.enums.TextFont;

/**
 * The type Attribute.
 */
public class Attribute implements ISceneObject {

    // required
    private EagleBoard board;
    private String name;

    // implied
    private double x1;
    private double y1;
    private String value;
    private double size;
    private int layerNumber;
    private TextFont font;
    private int ratio;

    private boolean isSetX1;
    private boolean isSetY1;
    private boolean isSetValue;
    private boolean isSetSize;
    private boolean isSetLayerNumber;
    private boolean isSetFont;
    private boolean isSetRatio;

    // optional
    private int rot;
    private AttributeDisplay display;
    private boolean constant;

    private boolean isSetRot;
    private boolean isSetDisplay;
    private boolean isSetConstant;

    /**
     * Instantiates a new Attribute.
     *
     * @param board the board
     * @param name  the name
     */
    public Attribute(EagleBoard board, String name) {
        this.board = board;
        this.name = name;

        this.x1 = 0.0;
        this.y1 = 0.0;
        this.value = null;
        this.size = 0.0;
        this.layerNumber = 0;
        this.font = null;
        this.ratio = 0;

        this.isSetX1 = false;
        this.isSetY1 = false;
        this.isSetValue = false;
        this.isSetSize = false;
        this.isSetLayerNumber = false;
        this.isSetFont = false;
        this.isSetRatio = false;

        this.rot = 0;
        this.display = AttributeDisplay.Value;
        this.constant = false;

        this.isSetRot = false;
        this.isSetDisplay = false;
        this.isSetConstant = false;
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
     * Sets x 1.
     *
     * @param x1 the x 1
     */
    public void setX1(double x1) {
        this.x1 = x1;
    }

    /**
     * Sets y 1.
     *
     * @param y1 the y 1
     */
    public void setY1(double y1) {
        this.y1 = y1;
        this.isSetY1 = true;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
        this.isSetValue = true;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(double size) {
        this.size = size;
        this.isSetSize = true;
    }

    /**
     * Sets layer number.
     *
     * @param layerNumber the layer number
     */
    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
        this.isSetLayerNumber = true;
    }

    /**
     * Sets font.
     *
     * @param font the font
     */
    public void setFont(TextFont font) {
        this.font = font;
        this.isSetFont = true;
    }

    /**
     * Sets ratio.
     *
     * @param ratio the ratio
     */
    public void setRatio(int ratio) {
        this.ratio = ratio;
        this.isSetRatio = true;
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
     * Sets display.
     *
     * @param display the display
     */
    public void setDisplay(AttributeDisplay display) {
        this.display = display;
        this.isSetDisplay = true;
    }

    /**
     * Sets constant.
     *
     * @param constant the constant
     */
    public void setConstant(boolean constant) {
        this.constant = constant;
        this.isSetConstant = true;
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
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
     * Gets layer number.
     *
     * @return the layer number
     */
    public int getLayerNumber() {
        return layerNumber;
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
     * Gets display.
     *
     * @return the display
     */
    public AttributeDisplay getDisplay() {
        return display;
    }

    /**
     * Gets constant.
     *
     * @return the constant
     */
    public boolean getConstant() {
        return constant;
    }

    /**
     * Gets is set x 1.
     *
     * @return the is set x 1
     */
    public boolean getIsSetX1() {
        return isSetX1;
    }

    /**
     * Gets is set y 1.
     *
     * @return the is set y 1
     */
    public boolean getIsSetY1() {
        return isSetY1;
    }

    /**
     * Gets is set value.
     *
     * @return the is set value
     */
    public boolean getIsSetValue() {
        return isSetValue;
    }

    /**
     * Gets is set size.
     *
     * @return the is set size
     */
    public boolean getIsSetSize() {
        return isSetSize;
    }

    /**
     * Gets is set layer number.
     *
     * @return the is set layer number
     */
    public boolean getIsSetLayerNumber() {
        return isSetLayerNumber;
    }

    /**
     * Gets is set font.
     *
     * @return the is set font
     */
    public boolean getIsSetFont() {
        return isSetFont;
    }

    /**
     * Gets is set ratio.
     *
     * @return the is set ratio
     */
    public boolean getIsSetRatio() {
        return isSetRatio;
    }

    /**
     * Gets is set rot.
     *
     * @return the is set rot
     */
    public boolean getIsSetRot() {
        return isSetRot;
    }

    /**
     * Gets is set display.
     *
     * @return the is set display
     */
    public boolean getIsSetDisplay() {
        return isSetDisplay;
    }

    /**
     * Gets is set constant.
     *
     * @return the is set constant
     */
    public boolean getIsSetConstant() {
        return isSetConstant;
    }
}
