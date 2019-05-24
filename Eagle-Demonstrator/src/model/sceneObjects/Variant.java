package model.sceneObjects;

/**
 * The type Variant.
 */
public class Variant {

    // required
    private String name;

    // implied
    private String value;
    private String technology;

    // optional
    private boolean populate;

    /**
     * Instantiates a new Variant.
     *
     * @param name the name
     */
    public Variant(String name) {
        this.name = name;

        this.value = null;
        this.technology = null;

        this.populate = true;
    }

    /**
     * Sets populate.
     *
     * @param populate the populate
     */
    public void setPopulate(boolean populate) {
        this.populate = populate;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets technology.
     *
     * @param technology the technology
     */
    public void setTechnology(String technology) {
        this.technology = technology;
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
     * Gets populate.
     *
     * @return the populate
     */
    public boolean getPopulate() {
        return populate;
    }
}
