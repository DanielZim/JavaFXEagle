package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;

/**
 * The interface Scene object.
 */
public interface ISceneObject {

    /**
     * Gets scene node.
     *
     * @return the scene node
     */
    Node getSceneNode();

    /**
     * Gets scene node.
     *
     * @param referenceX the reference x
     * @param referenceY the reference y
     * @return the scene node
     */
    Node getSceneNode(double referenceX, double referenceY);

    /**
     * Gets bounding box.
     *
     * @return the bounding box
     */
    BoundingBox getBoundingBox();

    /**
     * Gets bounding box.
     *
     * @param referenceX the reference x
     * @param referenceY the reference y
     * @return the bounding box
     */
    BoundingBox getBoundingBox(double referenceX, double referenceY);
}
