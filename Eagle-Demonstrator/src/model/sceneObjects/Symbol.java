package model.sceneObjects;

import javafx.scene.Node;
import model.BoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Symbol.
 */
public class Symbol implements ISceneObject {

    // required
    private String name;

    // optional
    private List<ISceneObject> buildingBlocks;
    private boolean isSetBuildingBlocks;

    /**
     * Instantiates a new Symbol.
     *
     * @param name the name
     */
    public Symbol(String name) {
        this.name = name;

        this.buildingBlocks = new ArrayList<>();
        this.isSetBuildingBlocks = false;
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
     * Add building block.
     *
     * @param block the block
     */
    public void addBuildingBlock(ISceneObject block) {
        this.buildingBlocks.add(block);
        this.isSetBuildingBlocks = true;
    }

    /**
     * Sets building blocks.
     *
     * @param buildingBlocks the building blocks
     */
    public void setBuildingBlocks(List<ISceneObject> buildingBlocks) {
        this.buildingBlocks = buildingBlocks;
        this.isSetBuildingBlocks = true;
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
     * Gets building blocks.
     *
     * @return the building blocks
     */
    public List<ISceneObject> getBuildingBlocks() {
        return buildingBlocks;
    }

    /**
     * Gets is set building blocks.
     *
     * @return the is set building blocks
     */
    public boolean getIsSetBuildingBlocks() {
        return isSetBuildingBlocks;
    }
}
