package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Bus.
 */
public class Bus implements ISceneObject {

    // required
    private String name;

    // optional
    private List<Segment> segments;

    private boolean isSetSegments;

    /**
     * Instantiates a new Bus.
     *
     * @param name the name
     */
    public Bus(String name) {
        this.name = name;

        this.segments = new ArrayList<>();
        this.isSetSegments = false;
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
     * Sets segments.
     *
     * @param segments the segments
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
        this.isSetSegments = true;
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
     * Gets segments.
     *
     * @return the segments
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Gets is set segments.
     *
     * @return the is set segments
     */
    public boolean getIsSetSegments() {
        return isSetSegments;
    }
}
