package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;
import model.enums.PinDirection;
import model.enums.PinFunction;
import model.enums.PinLength;
import model.enums.PinVisible;

/**
 * The type Pin.
 */
public class Pin implements ISceneObject {

    // required
    private String name;
    private double x1;
    private double x2;

    // optional
    private PinVisible visible;
    private PinLength length;
    private PinDirection direction;
    private PinFunction function;
    private int swapLevel;
    private int rot;

    private boolean isSetVisible;
    private boolean isSetLength;
    private boolean isSetDirection;
    private boolean isSetFunction;
    private boolean isSetSwapLevel;
    private boolean isSetRot;

    /**
     * Instantiates a new Pin.
     *
     * @param name the name
     * @param x1   the x 1
     * @param x2   the x 2
     */
    public Pin(String name, double x1, double x2) {
        this.name = name;
        this.x1 = x1;
        this.x2 = x2;

        this.visible = PinVisible.Both;
        this.length = PinLength.Long;
        this.direction = PinDirection.Io;
        this.function = PinFunction.None;
        this.swapLevel = 0;
        this.rot = 0;

        this.isSetVisible = false;
        this.isSetLength = false;
        this.isSetDirection = false;
        this.isSetFunction = false;
        this.isSetSwapLevel = false;
        this.isSetRot = false;
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
     * Sets visible.
     *
     * @param visible the visible
     */
    public void setVisible(PinVisible visible) {
        this.visible = visible;
        this.isSetVisible = true;
    }

    /**
     * Sets length.
     *
     * @param length the length
     */
    public void setLength(PinLength length) {
        this.length = length;
        this.isSetLength = true;
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(PinDirection direction) {
        this.direction = direction;
        this.isSetDirection = true;
    }

    /**
     * Sets function.
     *
     * @param function the function
     */
    public void setFunction(PinFunction function) {
        this.function = function;
        this.isSetFunction = true;
    }

    /**
     * Sets swap level.
     *
     * @param swapLevel the swap level
     */
    public void setSwapLevel(int swapLevel) {
        this.swapLevel = swapLevel;
        this.isSetSwapLevel = true;
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
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
     * Gets x 2.
     *
     * @return the x 2
     */
    public double getX2() {
        return x2;
    }

    /**
     * Gets visible.
     *
     * @return the visible
     */
    public PinVisible getVisible() {
        return visible;
    }

    /**
     * Gets length.
     *
     * @return the length
     */
    public PinLength getLength() {
        return length;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public PinDirection getDirection() {
        return direction;
    }

    /**
     * Gets function.
     *
     * @return the function
     */
    public PinFunction getFunction() {
        return function;
    }

    /**
     * Gets swap level.
     *
     * @return the swap level
     */
    public int getSwapLevel() {
        return swapLevel;
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
     * Gets is set visible.
     *
     * @return the is set visible
     */
    public boolean getIsSetVisible() {
        return isSetVisible;
    }

    /**
     * Gets is set length.
     *
     * @return the is set length
     */
    public boolean getIsSetLength() {
        return isSetLength;
    }

    /**
     * Gets is set direction.
     *
     * @return the is set direction
     */
    public boolean getIsSetDirection() {
        return isSetDirection;
    }

    /**
     * Gets is set function.
     *
     * @return the is set function
     */
    public boolean getIsSetFunction() {
        return isSetFunction;
    }

    /**
     * Gets is set swap level.
     *
     * @return the is set swap level
     */
    public boolean getIsSetSwapLevel() {
        return isSetSwapLevel;
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
