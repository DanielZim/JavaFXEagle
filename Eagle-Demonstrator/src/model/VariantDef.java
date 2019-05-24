package model;

/**
 * The type Variant def.
 */
public class VariantDef {

    // required
    private String name;

    // optional
    private boolean current;
    private boolean isSetCurrent;

    /**
     * Instantiates a new Variant def.
     *
     * @param name the name
     */
    public VariantDef(String name) {
        this.name = name;

        this.current = false;
        this.isSetCurrent = false;
    }

    /**
     * Sets current.
     *
     * @param current the current
     */
    public void setCurrent(boolean current) {
        this.current = current;
        this.isSetCurrent = true;
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
     * Gets current.
     *
     * @return the current
     */
    public boolean getCurrent() {
        return current;
    }

    /**
     * Gets is set current.
     *
     * @return the is set current
     */
    public boolean getIsSetCurrent() {
        return isSetCurrent;
    }
}
