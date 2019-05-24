package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Instance.
 */
public class Instance implements ISceneObject {

    // required
    private EagleBoard board;
    private double x1;
    private double y1;
    private String part;
    private String gate;

    // optional
    private boolean smashed;
    private int rot;

    private Map<String, Attribute> attributes;

    private boolean isSetSmashed;
    private boolean isSetRot;
    private boolean isSetAttributes;

    /**
     * Instantiates a new Instance.
     *
     * @param board the board
     * @param x1    the x 1
     * @param y1    the y 1
     * @param part  the part
     * @param gate  the gate
     */
    public Instance(EagleBoard board, double x1, double y1, String part, String gate) {
        this.board = board;
        this.x1 = x1;
        this.y1 = y1;
        this.part = part;
        this.gate = gate;

        this.smashed = false;
        this.rot = 0;

        this.attributes = new HashMap<>();

        this.isSetSmashed = false;
        this.isSetRot = false;
        this.isSetAttributes = false;
    }

    /**
     * Add attribute.
     *
     * @param attr the attr
     */
    public void addAttribute(Attribute attr) {
        this.attributes.put(attr.getName(), attr);
        this.isSetAttributes = true;
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
     * Sets smashed.
     *
     * @param smashed the smashed
     */
    public void setSmashed(boolean smashed) {
        this.smashed = smashed;
        this.isSetSmashed = true;
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

    /**
     * Gets part.
     *
     * @return the part
     */
    public String getPart() {
        return part;
    }

    /**
     * Gets gate.
     *
     * @return the gate
     */
    public String getGate() {
        return gate;
    }

    /**
     * Gets smashed.
     *
     * @return the smashed
     */
    public boolean getSmashed() {
        return smashed;
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
     * Gets attributes.
     *
     * @return the attributes
     */
    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Gets is set smashed.
     *
     * @return the is set smashed
     */
    public boolean getIsSetSmashed() {
        return isSetSmashed;
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
     * Gets is set attributes.
     *
     * @return the is set attributes
     */
    public boolean getIsSetAttributes() {
        return isSetAttributes;
    }
}
