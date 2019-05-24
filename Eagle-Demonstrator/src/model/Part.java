package model;

import model.sceneObjects.Attribute;
import model.sceneObjects.EagleBoard;
import model.sceneObjects.Variant;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Part.
 */
public class Part {

    // required
    private EagleBoard board;
    private String name;
    private String libraryName;
    private String deviceSet;
    private String device;

    // implied
    private String value;

    // optional
    private String technology;

    private Map<String, Attribute> attributes;
    private Map<String, Variant> variants;

    private boolean isSetValue;
    private boolean isSetTechnology;
    private boolean isSetAttributes;
    private boolean isSetVariants;

    /**
     * Instantiates a new Part.
     *
     * @param board       the board
     * @param name        the name
     * @param libraryName the library name
     * @param deviceSet   the device set
     * @param device      the device
     */
    public Part(EagleBoard board, String name, String libraryName, String deviceSet, String device) {
        this.board = board;
        this.name = name;
        this.libraryName = libraryName;
        this.deviceSet = deviceSet;
        this.device = device;

        this.value = null;

        this.technology = "";

        this.attributes = new HashMap<>();
        this.variants = new HashMap<>();

        this.isSetValue = false;
        this.isSetTechnology = false;
        this.isSetAttributes = false;
        this.isSetVariants = false;
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

    /**
     * Add variant.
     *
     * @param var the var
     */
    public void addVariant(Variant var) {
        this.variants.put(var.getName(), var);
        this.isSetVariants = true;
    }

    /**
     * Sets technology.
     *
     * @param technology the technology
     */
    public void setTechnology(String technology) {
        this.technology = technology;
        this.isSetTechnology = true;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
        this.isSetValue = true;
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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets library name.
     *
     * @return the library name
     */
    public String getLibraryName() {
        return libraryName;
    }

    /**
     * Gets device set.
     *
     * @return the device set
     */
    public String getDeviceSet() {
        return deviceSet;
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    public String getDevice() {
        return device;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets technology.
     *
     * @return the technology
     */
    public String getTechnology() {
        return technology;
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
     * Gets variants.
     *
     * @return the variants
     */
    public Map<String, Variant> getVariants() {
        return variants;
    }

    /**
     * Gets is set value.
     *
     * @return the is set value
     */
    public boolean getIsSetValue() {
        return isSetValue;
    }

    /**
     * Gets is set technology.
     *
     * @return the is set technology
     */
    public boolean getIsSetTechnology() {
        return isSetTechnology;
    }

    /**
     * Gets is set attributes.
     *
     * @return the is set attributes
     */
    public boolean getIsSetAttributes() {
        return isSetAttributes;
    }

    /**
     * Gets is set variants.
     *
     * @return the is set variants
     */
    public boolean getIsSetVariants() {
        return isSetVariants;
    }
}
