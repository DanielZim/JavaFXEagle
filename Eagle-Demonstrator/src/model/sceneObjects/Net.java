package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Net.
 */
public class Net  implements ISceneObject {

    private String name;
    private int classNumber;

    private List<Segment> segments;

    /**
     * Instantiates a new Net.
     *
     * @param name the name
     */
    public Net(String name) {
        this.name = name;

        this.classNumber = 0;

        this.segments = new ArrayList<>();
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
     * Sets class number.
     *
     * @param classNumber the class number
     */
    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    /**
     * Sets segments.
     *
     * @param segments the segments
     */
    public void setSegments(List<Segment> segments) {
        this.segments = segments;
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
     * Gets class number.
     *
     * @return the class number
     */
    public int getClassNumber() {
        return classNumber;
    }

    /**
     * Gets segments.
     *
     * @return the segments
     */
    public List<Segment> getSegments() {
        return segments;
    }
}
