package model.sceneObjects.coordinateTransformables;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.BoundingBox;
import model.sceneObjects.ISceneObject;
import utils.CoordinateTransformer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The type Plain.
 */
public class Plain implements ICoordinateTransformable {

    private List<ILayerable> buildingBlocks;

    private boolean isBoardShuffled;

    /**
     * Instantiates a new Plain.
     */
    public Plain() {
        this.buildingBlocks = new ArrayList<>();
    }

    /**
     * Instantiates a new Plain.
     *
     * @param buildingBlocks the building blocks
     */
    public Plain(List<ILayerable> buildingBlocks) {
        this.buildingBlocks = buildingBlocks;
    }

    @Override
    public Node getSceneNode() {
        // sort building blocks by layer, higher layers are drawn on top of lower ones
        List<ILayerable> sortedBuildingBlocks = getBuildingBlocks();
        sortedBuildingBlocks.sort(Comparator.comparingInt(ILayerable::getLayerNumber));

        Pane sceneNode = new Pane();

        for (ISceneObject block : sortedBuildingBlocks) {
            if(getIsBoardShuffled() && block instanceof EagleText) continue;
            sceneNode.getChildren().add(block.getSceneNode());
        }

        return sceneNode;
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        return getSceneNode();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0, 0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = null;

        for (ISceneObject block : getBuildingBlocks()) {
            BoundingBox blockBbox = block.getBoundingBox(referenceX, referenceY);

            if(blockBbox != null) {
                bbox = blockBbox.merge(bbox);
            }
        }

        return bbox;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        for (ICoordinateTransformable transformable : getBuildingBlocks()) {
            transformable.transformCoordinates(ct, signTransformation);
        }
    }

    /**
     * Gets platine bbox.
     *
     * @return the platine bbox
     */
// this method is used to get the space where elements can be randomly placed,
    // we dont need to consider dimensions and text
    public BoundingBox getPlatineBbox() {
        BoundingBox bbox = null;

        for (ISceneObject block : getBuildingBlocks()) {
            if(!(block instanceof Dimension) && !(block instanceof EagleText)) {
                BoundingBox blockBbox = block.getBoundingBox();

                if(blockBbox != null) {
                    bbox = blockBbox.merge(bbox);
                }
            }
        }

        return bbox;
    }

    /**
     * Add building block.
     *
     * @param obj the obj
     */
    public void addBuildingBlock(ILayerable obj) {
        buildingBlocks.add(obj);
    }

    /**
     * Sets is board shuffled.
     *
     * @param isBoardShuffled the is board shuffled
     */
    public void setIsBoardShuffled(boolean isBoardShuffled) {
        this.isBoardShuffled = isBoardShuffled;
    }

    /**
     * Gets building blocks.
     *
     * @return the building blocks
     */
    public List<ILayerable> getBuildingBlocks() {
        return buildingBlocks;
    }

    /**
     * Gets is board shuffled.
     *
     * @return the is board shuffled
     */
    public boolean getIsBoardShuffled() {
        return isBoardShuffled;
    }
}
