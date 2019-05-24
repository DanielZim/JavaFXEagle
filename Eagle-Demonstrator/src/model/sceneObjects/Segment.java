package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;
import model.Junction;
import model.PinRef;
import model.sceneObjects.coordinateTransformables.Wire;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Segment.
 */
public class Segment implements ISceneObject {

    private List<PinRef> pinRefs;
    private List<Wire> wires;
    private List<Junction> junctions;
    private List<Label> labels;

    /**
     * Instantiates a new Segment.
     */
    public Segment() {
        this.pinRefs = new ArrayList<>();
        this.wires = new ArrayList<>();
        this.junctions = new ArrayList<>();
        this.labels = new ArrayList<>();
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
     * Sets pin refs.
     *
     * @param pinRefs the pin refs
     */
    public void setPinRefs(List<PinRef> pinRefs) {
        this.pinRefs = pinRefs;
    }

    /**
     * Sets wires.
     *
     * @param wires the wires
     */
    public void setWires(List<Wire> wires) {
        this.wires = wires;
    }

    /**
     * Sets junctions.
     *
     * @param junctions the junctions
     */
    public void setJunctions(List<Junction> junctions) {
        this.junctions = junctions;
    }

    /**
     * Sets labels.
     *
     * @param labels the labels
     */
    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    /**
     * Gets pin refs.
     *
     * @return the pin refs
     */
    public List<PinRef> getPinRefs() {
        return pinRefs;
    }

    /**
     * Gets wires.
     *
     * @return the wires
     */
    public List<Wire> getWires() {
        return wires;
    }

    /**
     * Gets junctions.
     *
     * @return the junctions
     */
    public List<Junction> getJunctions() {
        return junctions;
    }

    /**
     * Gets labels.
     *
     * @return the labels
     */
    public List<Label> getLabels() {
        return labels;
    }
}
