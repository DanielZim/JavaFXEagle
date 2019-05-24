package model.sceneObjects.coordinateTransformables;

import model.sceneObjects.ISceneObject;
import utils.CoordinateTransformer;

/**
 * The interface Coordinate transformable.
 */
public interface ICoordinateTransformable extends ISceneObject {

    /**
     * Transform coordinates.
     *
     * @param ct                 the coordinate transformer
     * @param signTransformation the sign transformation; true: sign transformation, false: origin transformation
     */
    void transformCoordinates(CoordinateTransformer ct, boolean signTransformation);
}
