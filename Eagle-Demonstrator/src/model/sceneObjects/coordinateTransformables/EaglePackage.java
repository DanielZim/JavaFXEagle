package model.sceneObjects.coordinateTransformables;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import model.BoundingBox;
import model.sceneObjects.ISceneObject;
import utils.CoordinateTransformer;

import java.util.*;

/**
 * The type Eagle package.
 */
public class EaglePackage implements ICoordinateTransformable {

    // required
    private String name;
    private String description;

    // optional
    private List<ILayerable> buildingBlocks;
    private Map<String, Pad> pads;
    private EagleText nameText;
    private EagleText valueText;

    private boolean isSetBuildingBlocks;
    private boolean isSetNameText;
    private boolean isSetValueText;

    /**
     * Instantiates a new Eagle package.
     *
     * @param name        the name
     * @param description the description
     */
    public EaglePackage(String name, String description) {
        this.name = name;
        this.description = description;

        this.buildingBlocks = new ArrayList<>();
        this.pads = new HashMap<>();
        this.nameText = null;
        this.valueText = null;

        this.isSetBuildingBlocks = false;
        this.isSetNameText = false;
        this.isSetValueText = false;
    }

    /**
     * Add building block.
     *
     * @param buildingBlock the building block
     */
    public void addBuildingBlock(ILayerable buildingBlock) {
        buildingBlocks.add(buildingBlock);
        this.isSetBuildingBlocks = true;
    }

    /**
     * Add pad.
     *
     * @param pad the pad
     */
    public void addPad(Pad pad) {
        pads.put(pad.getName(), pad);
    }

    /**
     * Gets pad by name.
     *
     * @param name the name
     * @return the pad
     */
    public Pad getPad(String name) {
        return pads.get(name);
    }

    /**
     * Add building blocks.
     *
     * @param buildingBlocks the building blocks
     */
    public void addBuildingBlocks(Collection<? extends ILayerable> buildingBlocks) {
        this.buildingBlocks.addAll(buildingBlocks);
        this.isSetBuildingBlocks = true;
    }

    @Override
    public Node getSceneNode() {
        return getSceneNode(0, 0);
    }

    @Override
    public Node getSceneNode(double referenceX, double referenceY) {
        return getSceneNode(referenceX, referenceY, null, null, 0);
    }

    public Node getSceneNode(double referenceX, double referenceY, String name, String value, int rotation) {
        // sort building blocks by layer, higher layers are drawn on top of lower ones
        List<ILayerable> sortedBuildingBlocks = getBuildingBlocks();
        sortedBuildingBlocks.sort(Comparator.comparingInt(ILayerable::getLayerNumber));

        Pane sceneNode = new Pane();

        for (ISceneObject buildingBlock : sortedBuildingBlocks) {
            sceneNode.getChildren().add(buildingBlock.getSceneNode(referenceX, referenceY));
        }

        if(getIsSetNameText() && name != null) {
            EagleText nameText = getNameText();
            nameText.setCenterRotation(rotation);
            nameText.setValue(name);
            sceneNode.getChildren().add(nameText.getSceneNode(referenceX, referenceY));
        }

        if(getIsSetValueText() && value != null) {
            EagleText valueText = getValueText();
            valueText.setCenterRotation(rotation);
            valueText.setValue(value);
            sceneNode.getChildren().add(valueText.getSceneNode(referenceX, referenceY));
        }

        return sceneNode;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return getBoundingBox(0,0);
    }

    @Override
    public BoundingBox getBoundingBox(double referenceX, double referenceY) {
        BoundingBox bbox = null;

        for (ISceneObject buildingBlock : getBuildingBlocks()) {
            if(!(buildingBlock instanceof EagleText)) {
                BoundingBox blockBbox = buildingBlock.getBoundingBox(referenceX, referenceY);

                if(blockBbox != null) {
                    bbox = blockBbox.merge(bbox);
                }
            }
        }

        return bbox;
    }

    /**
     * Gets pads.
     *
     * @return the pads
     */
    public Map<String, Pad> getPads() {
        return pads;
    }

    /**
     * Sets name text.
     *
     * @param nameText the name text
     */
    public void setNameText(EagleText nameText) {
        this.nameText = nameText;
        this.isSetNameText = true;
    }

    /**
     * Sets value text.
     *
     * @param valueText the value text
     */
    public void setValueText(EagleText valueText) {
        this.valueText = valueText;
        this.isSetValueText = true;
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
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
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
     * Gets name text.
     *
     * @return the name text
     */
    public EagleText getNameText() {
        return nameText;
    }

    /**
     * Gets value text.
     *
     * @return the value text
     */
    public EagleText getValueText() {
        return valueText;
    }

    /**
     * Gets is set building blocks.
     *
     * @return the is set building blocks
     */
    public boolean getIsSetBuildingBlocks() {
        return isSetBuildingBlocks;
    }

    /**
     * Gets is set name text.
     *
     * @return the is set name text
     */
    public boolean getIsSetNameText() {
        return isSetNameText;
    }

    /**
     * Gets is set value text.
     *
     * @return the is set value text
     */
    public boolean getIsSetValueText() {
        return isSetValueText;
    }

    @Override
    public void transformCoordinates(CoordinateTransformer ct, boolean signTransformation) {
        for (ICoordinateTransformable transformable : getBuildingBlocks()) {
            transformable.transformCoordinates(ct, signTransformation);
        }
        if(nameText != null) {
            nameText.transformCoordinates(ct, signTransformation);
        }

        if(valueText != null) {
            valueText.transformCoordinates(ct, signTransformation);
        }
    }
}
